package com.tcm.service.recommendation.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tcm.mapper.*;
import com.tcm.model.entity.*;
import com.tcm.model.vo.RecommendationVO;
import com.tcm.service.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 个性化推荐服务实现
 * 融合体质推荐、行为协同推荐、季节性推荐三种策略
 *
 * @author Ti
 * @since 2026-03-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserHealthRecordMapper healthRecordMapper;
    private final ProductMapper productMapper;
    private final HealthPlanMapper healthPlanMapper;
    private final HealthArticleMapper healthArticleMapper;
    private final UserCollectionMapper collectionMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderInfoMapper orderInfoMapper;
    private final StringRedisTemplate redisTemplate;

    private static final String RECOMMEND_CACHE_KEY = "recommend:home:";
    private static final long RECOMMEND_CACHE_TTL = 10;

    /**
     * 体质类型 → 中医关键词映射
     */
    private static final Map<String, List<String>> CONSTITUTION_KEYWORDS = new HashMap<>();

    /**
     * 季节 → 养生关键词映射
     */
    private static final Map<String, List<String>> SEASON_KEYWORDS = new HashMap<>();

    static {
        CONSTITUTION_KEYWORDS.put("气虚质", List.of("补气", "益气", "黄芪", "人参", "党参", "西洋参", "白术", "山药"));
        CONSTITUTION_KEYWORDS.put("阳虚质", List.of("温补", "补阳", "温经", "散寒", "肉桂", "鹿茸", "杜仲", "巴戟天", "干姜", "附子", "艾灸"));
        CONSTITUTION_KEYWORDS.put("阴虚质", List.of("滋阴", "养阴", "枸杞", "麦冬", "百合", "石斛", "玉竹", "燕窝", "阿胶"));
        CONSTITUTION_KEYWORDS.put("痰湿质", List.of("祛湿", "健脾", "利水", "薏仁", "薏苡", "茯苓", "陈皮", "山药"));
        CONSTITUTION_KEYWORDS.put("湿热质", List.of("清热", "祛湿", "解毒", "菊花", "金银花", "薏仁", "荷叶", "板蓝根"));
        CONSTITUTION_KEYWORDS.put("血瘀质", List.of("活血", "化瘀", "散瘀", "三七", "丹参", "红花", "桃仁", "当归"));
        CONSTITUTION_KEYWORDS.put("气郁质", List.of("疏肝", "理气", "解郁", "玫瑰", "佛手", "陈皮", "薄荷", "合欢花"));
        CONSTITUTION_KEYWORDS.put("特禀质", List.of("固表", "增强免疫", "灵芝", "黄芪", "防风", "白术", "虫草"));
        CONSTITUTION_KEYWORDS.put("平和质", List.of("养生", "保健", "枸杞", "红枣", "桂圆", "灵芝", "燕窝"));

        SEASON_KEYWORDS.put("春季", List.of("养肝", "疏风", "春季", "菊花", "枸杞", "决明子"));
        SEASON_KEYWORDS.put("夏季", List.of("清热", "解暑", "夏季", "菊花", "金银花", "薏仁"));
        SEASON_KEYWORDS.put("秋季", List.of("润肺", "滋阴", "秋季", "百合", "麦冬", "银耳"));
        SEASON_KEYWORDS.put("冬季", List.of("温补", "养肾", "冬季", "人参", "鹿茸", "肉桂"));
    }

    @Override
    public RecommendationVO getHomeRecommendations(Long userId) {
        // 优先读缓存
        String cacheKey = RECOMMEND_CACHE_KEY + userId;
        try {
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return JSON.parseObject(cached, RecommendationVO.class);
            }
        } catch (Exception e) {
            log.warn("Redis读取推荐缓存失败", e);
        }

        RecommendationVO vo = new RecommendationVO();
        String currentSeason = getCurrentSeason();
        vo.setCurrentSeason(currentSeason);

        // ========== 1. 体质推荐 ==========
        UserHealthRecord record = healthRecordMapper.selectOne(
                new LambdaQueryWrapper<UserHealthRecord>()
                        .eq(UserHealthRecord::getUserId, userId)
        );

        if (record != null && StrUtil.isNotBlank(record.getConstitutionType())) {
            vo.setHasHealthRecord(true);
            vo.setConstitutionType(record.getConstitutionType());

            List<String> keywords = CONSTITUTION_KEYWORDS.getOrDefault(
                    record.getConstitutionType(), List.of());

            vo.setConstitutionProducts(searchProductsByKeywords(keywords, 8));
            vo.setConstitutionPlans(searchPlansByConstitution(record.getConstitutionType(), 4));
            vo.setConstitutionArticles(searchArticlesByKeywords(keywords, 4));
        } else {
            vo.setHasHealthRecord(false);
            vo.setConstitutionProducts(List.of());
            vo.setConstitutionPlans(List.of());
            vo.setConstitutionArticles(List.of());
        }

        // ========== 2. 行为协同推荐 ==========
        vo.setBehaviorProducts(getBehaviorRecommendations(userId, 8));

        // ========== 3. 季节性推荐 ==========
        List<String> seasonKeywords = SEASON_KEYWORDS.getOrDefault(currentSeason, List.of());
        vo.setSeasonalProducts(searchProductsByKeywords(seasonKeywords, 8));
        vo.setSeasonalPlans(searchPlansBySeason(currentSeason, 4));

        // 写入缓存
        try {
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(vo),
                    RECOMMEND_CACHE_TTL, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis写入推荐缓存失败", e);
        }

        return vo;
    }

    /**
     * 获取当前季节
     */
    private String getCurrentSeason() {
        int month = LocalDateTime.now().getMonthValue();
        if (month >= 3 && month <= 5) return "春季";
        if (month >= 6 && month <= 8) return "夏季";
        if (month >= 9 && month <= 11) return "秋季";
        return "冬季";
    }

    /**
     * 根据关键词搜索商品
     */
    private List<Product> searchProductsByKeywords(List<String> keywords, int limit) {
        if (keywords.isEmpty()) return List.of();

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.and(w -> {
            for (int i = 0; i < keywords.size(); i++) {
                String kw = keywords.get(i);
                if (i == 0) {
                    w.like(Product::getName, kw)
                            .or().like(Product::getEfficacy, kw)
                            .or().like(Product::getDescription, kw);
                } else {
                    w.or().like(Product::getName, kw)
                            .or().like(Product::getEfficacy, kw)
                            .or().like(Product::getDescription, kw);
                }
            }
        });
        wrapper.orderByDesc(Product::getSales);
        wrapper.last("LIMIT " + limit);
        return productMapper.selectList(wrapper);
    }

    /**
     * 按体质类型搜索养生方案
     */
    private List<HealthPlan> searchPlansByConstitution(String constitutionType, int limit) {
        return healthPlanMapper.selectList(
                new LambdaQueryWrapper<HealthPlan>()
                        .eq(HealthPlan::getStatus, 1)
                        .eq(HealthPlan::getConstitutionType, constitutionType)
                        .orderByDesc(HealthPlan::getViewCount)
                        .last("LIMIT " + limit)
        );
    }

    /**
     * 按季节搜索养生方案
     */
    private List<HealthPlan> searchPlansBySeason(String season, int limit) {
        return healthPlanMapper.selectList(
                new LambdaQueryWrapper<HealthPlan>()
                        .eq(HealthPlan::getStatus, 1)
                        .and(w -> w.eq(HealthPlan::getSeason, season)
                                .or().eq(HealthPlan::getSeason, "四季通用")
                                .or().eq(HealthPlan::getSeason, "四季"))
                        .orderByDesc(HealthPlan::getViewCount)
                        .last("LIMIT " + limit)
        );
    }

    /**
     * 根据关键词搜索养生文章
     */
    private List<HealthArticle> searchArticlesByKeywords(List<String> keywords, int limit) {
        if (keywords.isEmpty()) return List.of();

        LambdaQueryWrapper<HealthArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthArticle::getStatus, 1);
        wrapper.and(w -> {
            for (int i = 0; i < keywords.size(); i++) {
                String kw = keywords.get(i);
                if (i == 0) {
                    w.like(HealthArticle::getTitle, kw)
                            .or().like(HealthArticle::getSummary, kw);
                } else {
                    w.or().like(HealthArticle::getTitle, kw)
                            .or().like(HealthArticle::getSummary, kw);
                }
            }
        });
        wrapper.orderByDesc(HealthArticle::getViewCount);
        wrapper.last("LIMIT " + limit);
        return healthArticleMapper.selectList(wrapper);
    }

    /**
     * 基于用户行为的协同推荐
     * 策略：收集用户购买/收藏的商品 → 提取分类偏好 → 推荐同类热门商品
     */
    private List<Product> getBehaviorRecommendations(Long userId, int limit) {
        Set<Long> interactedProductIds = new HashSet<>();

        // 收集已收藏的商品ID
        List<UserCollection> collections = collectionMapper.selectList(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, userId)
                        .eq(UserCollection::getTargetType, 1)
        );
        collections.forEach(c -> interactedProductIds.add(c.getTargetId()));

        // 收集已购买的商品ID
        List<OrderInfo> orders = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .select(OrderInfo::getId)
                        .eq(OrderInfo::getUserId, userId)
        );
        if (!orders.isEmpty()) {
            List<Long> orderIds = orders.stream().map(OrderInfo::getId).toList();
            List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>()
                            .select(OrderItem::getProductId)
                            .in(OrderItem::getOrderId, orderIds)
            );
            items.forEach(i -> interactedProductIds.add(i.getProductId()));
        }

        if (interactedProductIds.isEmpty()) return List.of();

        // 提取用户偏好的分类
        List<Product> interactedProducts = productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .select(Product::getCategoryId)
                        .in(Product::getId, interactedProductIds)
        );
        Set<Long> categoryIds = interactedProducts.stream()
                .map(Product::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (categoryIds.isEmpty()) return List.of();

        // 推荐同分类下用户未购买/收藏过的热门商品
        return productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getStatus, 1)
                        .in(Product::getCategoryId, categoryIds)
                        .notIn(Product::getId, interactedProductIds)
                        .orderByDesc(Product::getSales)
                        .last("LIMIT " + limit)
        );
    }
}
