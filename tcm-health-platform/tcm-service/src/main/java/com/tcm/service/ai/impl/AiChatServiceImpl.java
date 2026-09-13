package com.tcm.service.ai.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import com.tcm.mapper.AiChatRecordMapper;
import com.tcm.mapper.HealthArticleMapper;
import com.tcm.mapper.HealthPlanMapper;
import com.tcm.mapper.ProductMapper;
import com.tcm.model.dto.ChatDTO;
import com.tcm.model.entity.AiChatRecord;
import com.tcm.model.entity.HealthArticle;
import com.tcm.model.entity.HealthPlan;
import com.tcm.model.entity.Product;
import com.tcm.service.ai.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AI聊天服务实现（集成阿里云百炼平台 + RAG检索增强 + Redis缓存优化）
 *
 * @author Ti
 * @since 2026-02-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final AiChatRecordMapper chatRecordMapper;
    private final ProductMapper productMapper;
    private final HealthArticleMapper healthArticleMapper;
    private final HealthPlanMapper healthPlanMapper;
    private final StringRedisTemplate redisTemplate;

    private static final String SYSTEM_PROMPT = """
            你是中医养生平台的智能养生助手，名叫"养生小助手"。你的主要职责是：
            1. 解答用户关于中医药材的功效、用法、禁忌等问题
            2. 提供个性化的养生建议和健康咨询
            3. 根据用户需求推荐平台上的相关商品、文章和养生方案
            4. 解答订单、支付、物流等常见问题
            
            注意事项：
            - 回答要专业、准确、友善，体现中医养生的专业性
            - 涉及疾病诊断治疗的问题，建议用户就医咨询专业医生
            - 不要推荐具体药品用于治疗疾病，但可以推荐养生保健类产品
            - 回答要简洁明了，使用markdown格式让内容更易读
            - 当推荐商品时，可以提及商品名称、功效和价格
            - 使用中文回答，语气亲切自然
            """;

    private static final int MAX_CONTEXT_ROUNDS = 10;

    private static final String CHAT_CONTEXT_KEY = "chat:context:";
    private static final String RAG_CACHE_KEY = "rag:cache:";
    private static final long CHAT_CONTEXT_TTL_MINUTES = 30;
    private static final long RAG_CACHE_TTL_MINUTES = 5;

    @Override
    public Flux<String> chat(Long userId, ChatDTO dto) {
        try {
            String sessionId = getOrCreateSessionId(dto.getSessionId());

            String ragContext = retrieveRelevantKnowledgeWithCache(dto.getMessage());

            List<Message> messages = buildMessagesWithCache(userId, sessionId, dto.getMessage(), ragContext);

            // 用户消息写入Redis缓存 + 异步持久化MySQL
            cacheMessage(userId, sessionId, "user", dto.getMessage());
            asyncPersistMessage(userId, sessionId, "user", dto.getMessage());

            ChatClient chatClient = chatClientBuilder.build();
            StringBuilder fullResponse = new StringBuilder();

            return chatClient.prompt(new Prompt(messages))
                    .stream()
                    .content()
                    .doOnNext(fullResponse::append)
                    .doOnComplete(() -> {
                        String response = fullResponse.toString();
                        cacheMessage(userId, sessionId, "assistant", response);
                        asyncPersistMessage(userId, sessionId, "assistant", response);
                    })
                    .doOnError(e -> log.error("AI聊天出错", e));
        } catch (Exception e) {
            log.error("AI聊天出错", e);
            throw new BusinessException(ResultCode.AI_SERVICE_ERROR);
        }
    }

    @Override
    public String chatSync(Long userId, ChatDTO dto) {
        try {
            String sessionId = getOrCreateSessionId(dto.getSessionId());

            String ragContext = retrieveRelevantKnowledgeWithCache(dto.getMessage());
            List<Message> messages = buildMessagesWithCache(userId, sessionId, dto.getMessage(), ragContext);

            cacheMessage(userId, sessionId, "user", dto.getMessage());
            asyncPersistMessage(userId, sessionId, "user", dto.getMessage());

            ChatClient chatClient = chatClientBuilder.build();
            String response = chatClient.prompt(new Prompt(messages)).call().content();

            cacheMessage(userId, sessionId, "assistant", response);
            asyncPersistMessage(userId, sessionId, "assistant", response);

            return response;
        } catch (Exception e) {
            log.error("AI聊天出错", e);
            throw new BusinessException(ResultCode.AI_SERVICE_ERROR);
        }
    }

    @Override
    public void clearSession(Long userId, String sessionId) {
        String cacheKey = CHAT_CONTEXT_KEY + userId + ":" + sessionId;
        redisTemplate.delete(cacheKey);

        chatRecordMapper.delete(
                new LambdaQueryWrapper<AiChatRecord>()
                        .eq(AiChatRecord::getUserId, userId)
                        .eq(AiChatRecord::getSessionId, sessionId)
        );
    }

    // ==================== Redis缓存操作 ====================

    /**
     * 追加消息到Redis List缓存，自动裁剪保留最近N轮对话
     */
    private void cacheMessage(Long userId, String sessionId, String role, String content) {
        try {
            String key = CHAT_CONTEXT_KEY + userId + ":" + sessionId;
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", role);
            msgObj.put("content", content);
            redisTemplate.opsForList().rightPush(key, msgObj.toJSONString());
            Long size = redisTemplate.opsForList().size(key);
            if (size != null && size > MAX_CONTEXT_ROUNDS * 2) {
                redisTemplate.opsForList().trim(key, size - MAX_CONTEXT_ROUNDS * 2, -1);
            }
            redisTemplate.expire(key, CHAT_CONTEXT_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis缓存消息失败，不影响主流程", e);
        }
    }

    /**
     * 从Redis缓存构建消息列表，缓存未命中时降级查MySQL并回填缓存
     */
    private List<Message> buildMessagesWithCache(Long userId, String sessionId,
                                                  String currentMessage, String ragContext) {
        List<Message> messages = new ArrayList<>();

        String enhancedSystemPrompt = SYSTEM_PROMPT;
        if (StrUtil.isNotBlank(ragContext)) {
            enhancedSystemPrompt += "\n\n以下是与用户问题相关的平台知识库内容，请参考这些信息来回答用户问题：\n" + ragContext;
        }
        messages.add(new SystemMessage(enhancedSystemPrompt));

        // 优先从Redis获取历史上下文
        String key = CHAT_CONTEXT_KEY + userId + ":" + sessionId;
        List<String> cachedMessages = null;
        try {
            cachedMessages = redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            log.warn("Redis读取历史消息失败，降级查询MySQL", e);
        }

        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            for (String msgJson : cachedMessages) {
                JSONObject obj = JSON.parseObject(msgJson);
                String role = obj.getString("role");
                String msgContent = obj.getString("content");
                if ("user".equals(role)) {
                    messages.add(new UserMessage(msgContent));
                } else {
                    messages.add(new AssistantMessage(msgContent));
                }
            }
        } else {
            // 缓存未命中，降级查MySQL并回填缓存
            List<AiChatRecord> historyRecords = chatRecordMapper.selectList(
                    new LambdaQueryWrapper<AiChatRecord>()
                            .eq(AiChatRecord::getUserId, userId)
                            .eq(AiChatRecord::getSessionId, sessionId)
                            .orderByAsc(AiChatRecord::getCreateTime)
                            .last("LIMIT " + (MAX_CONTEXT_ROUNDS * 2))
            );
            for (AiChatRecord record : historyRecords) {
                if ("user".equals(record.getRole())) {
                    messages.add(new UserMessage(record.getContent()));
                } else {
                    messages.add(new AssistantMessage(record.getContent()));
                }
                cacheMessage(userId, sessionId, record.getRole(), record.getContent());
            }
        }

        messages.add(new UserMessage(currentMessage));
        return messages;
    }

    /**
     * 异步持久化消息到MySQL（不阻塞主线程）
     */
    private void asyncPersistMessage(Long userId, String sessionId, String role, String content) {
        CompletableFuture.runAsync(() -> {
            try {
                AiChatRecord record = new AiChatRecord();
                record.setUserId(userId);
                record.setSessionId(sessionId);
                record.setRole(role);
                record.setContent(content);
                record.setCreateTime(LocalDateTime.now());
                chatRecordMapper.insert(record);
            } catch (Exception e) {
                log.error("异步保存聊天记录失败", e);
            }
        });
    }

    // ==================== RAG检索（带缓存） ====================

    /**
     * RAG检索，优先从Redis读取缓存结果，未命中则查数据库并回填
     */
    private String retrieveRelevantKnowledgeWithCache(String query) {
        List<String> keywords = extractKeywords(query);
        if (keywords.isEmpty()) {
            return "";
        }

        String sortedKeywords = keywords.stream().sorted().collect(Collectors.joining(","));
        String cacheKey = RAG_CACHE_KEY + sortedKeywords;

        try {
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.debug("RAG缓存命中: {}", sortedKeywords);
                return cached;
            }
        } catch (Exception e) {
            log.warn("Redis读取RAG缓存失败，降级查询MySQL", e);
        }

        String result = doRetrieveRelevantKnowledge(keywords);

        if (StrUtil.isNotBlank(result)) {
            try {
                redisTemplate.opsForValue().set(cacheKey, result, RAG_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.warn("Redis写入RAG缓存失败", e);
            }
        }

        return result;
    }

    private String doRetrieveRelevantKnowledge(List<String> keywords) {
        StringBuilder context = new StringBuilder();

        List<Product> products = searchProducts(keywords);
        if (!products.isEmpty()) {
            context.append("\n【相关商品信息】\n");
            for (Product p : products) {
                context.append(String.format("- %s：%s，价格：%.2f元，功效：%s\n",
                        p.getName(),
                        StrUtil.isNotBlank(p.getSubTitle()) ? p.getSubTitle() : "",
                        p.getPrice(),
                        StrUtil.isNotBlank(p.getEfficacy()) ? p.getEfficacy() : "暂无"));
            }
        }

        List<HealthArticle> articles = searchArticles(keywords);
        if (!articles.isEmpty()) {
            context.append("\n【相关养生文章】\n");
            for (HealthArticle a : articles) {
                context.append(String.format("- 《%s》：%s\n",
                        a.getTitle(),
                        StrUtil.isNotBlank(a.getSummary()) ? a.getSummary() : ""));
            }
        }

        List<HealthPlan> plans = searchHealthPlans(keywords);
        if (!plans.isEmpty()) {
            context.append("\n【相关养生方案】\n");
            for (HealthPlan p : plans) {
                context.append(String.format("- %s（适用体质：%s，适用季节：%s）：%s\n",
                        p.getTitle(),
                        StrUtil.isNotBlank(p.getConstitutionType()) ? p.getConstitutionType() : "通用",
                        StrUtil.isNotBlank(p.getSeason()) ? p.getSeason() : "四季",
                        StrUtil.isNotBlank(p.getSummary()) ? p.getSummary() : ""));
                if (StrUtil.isNotBlank(p.getDietAdvice())) {
                    context.append(String.format("  饮食建议：%s\n", p.getDietAdvice()));
                }
                if (StrUtil.isNotBlank(p.getMedicineAdvice())) {
                    context.append(String.format("  药材建议：%s\n", p.getMedicineAdvice()));
                }
            }
        }

        return context.toString();
    }

    // ==================== 辅助方法 ====================

    private String getOrCreateSessionId(String sessionId) {
        if (StrUtil.isBlank(sessionId)) {
            return "session_" + IdUtil.simpleUUID();
        }
        return sessionId;
    }

    private List<String> extractKeywords(String query) {
        List<String> keywords = new ArrayList<>();

        String[] tcmKeywords = {
                "黄芪", "人参", "党参", "西洋参", "当归", "阿胶", "枸杞", "红枣", "桂圆",
                "菊花", "金银花", "板蓝根", "茯苓", "薏仁", "陈皮", "三七", "丹参", "红花",
                "玫瑰", "决明子", "燕窝", "虫草", "灵芝", "艾灸", "刮痧", "拔罐", "按摩",
                "补气", "补血", "养血", "滋阴", "壮阳", "清热", "解毒", "祛湿", "健脾",
                "养肝", "护肝", "养心", "安神", "明目", "润肺", "止咳", "活血", "化瘀",
                "气虚", "阳虚", "阴虚", "血虚", "痰湿", "湿热", "血瘀", "气郁",
                "春季", "夏季", "秋季", "冬季", "四季",
                "失眠", "疲劳", "手脚冰凉", "上火", "便秘", "感冒", "咳嗽",
                "八段锦", "太极", "养生茶", "食疗", "药膳"
        };

        for (String keyword : tcmKeywords) {
            if (query.contains(keyword)) {
                keywords.add(keyword);
            }
        }

        return keywords;
    }

    private List<Product> searchProducts(List<String> keywords) {
        if (keywords.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
               .eq(Product::getDeleted, 0);

        wrapper.and(w -> {
            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i);
                if (i == 0) {
                    w.like(Product::getName, keyword)
                     .or().like(Product::getEfficacy, keyword)
                     .or().like(Product::getDescription, keyword);
                } else {
                    w.or().like(Product::getName, keyword)
                     .or().like(Product::getEfficacy, keyword)
                     .or().like(Product::getDescription, keyword);
                }
            }
        });

        wrapper.last("LIMIT 5");
        return productMapper.selectList(wrapper);
    }

    private List<HealthArticle> searchArticles(List<String> keywords) {
        if (keywords.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<HealthArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthArticle::getStatus, 1)
               .eq(HealthArticle::getDeleted, 0);

        wrapper.and(w -> {
            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i);
                if (i == 0) {
                    w.like(HealthArticle::getTitle, keyword)
                     .or().like(HealthArticle::getSummary, keyword)
                     .or().like(HealthArticle::getContent, keyword);
                } else {
                    w.or().like(HealthArticle::getTitle, keyword)
                     .or().like(HealthArticle::getSummary, keyword)
                     .or().like(HealthArticle::getContent, keyword);
                }
            }
        });

        wrapper.last("LIMIT 3");
        return healthArticleMapper.selectList(wrapper);
    }

    private List<HealthPlan> searchHealthPlans(List<String> keywords) {
        if (keywords.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<HealthPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthPlan::getStatus, 1)
               .eq(HealthPlan::getDeleted, 0);

        wrapper.and(w -> {
            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i);
                if (i == 0) {
                    w.like(HealthPlan::getTitle, keyword)
                     .or().like(HealthPlan::getSummary, keyword)
                     .or().like(HealthPlan::getConstitutionType, keyword)
                     .or().like(HealthPlan::getDietAdvice, keyword)
                     .or().like(HealthPlan::getMedicineAdvice, keyword);
                } else {
                    w.or().like(HealthPlan::getTitle, keyword)
                     .or().like(HealthPlan::getSummary, keyword)
                     .or().like(HealthPlan::getConstitutionType, keyword)
                     .or().like(HealthPlan::getDietAdvice, keyword)
                     .or().like(HealthPlan::getMedicineAdvice, keyword);
                }
            }
        });

        wrapper.last("LIMIT 3");
        return healthPlanMapper.selectList(wrapper);
    }
}
