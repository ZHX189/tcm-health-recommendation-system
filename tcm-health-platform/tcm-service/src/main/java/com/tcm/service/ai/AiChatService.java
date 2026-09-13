package com.tcm.service.ai;

import com.tcm.model.dto.ChatDTO;
import reactor.core.publisher.Flux;

/**
 * AI聊天服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface AiChatService {

    /**
     * 发送消息（流式响应）
     *
     * @param userId 用户ID
     * @param dto    聊天请求
     * @return SSE流
     */
    Flux<String> chat(Long userId, ChatDTO dto);

    /**
     * 发送消息（普通响应）
     */
    String chatSync(Long userId, ChatDTO dto);

    /**
     * 清除会话上下文
     */
    void clearSession(Long userId, String sessionId);
}
