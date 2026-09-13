package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.model.dto.ChatDTO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.ai.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI智能客服控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "AI智能客服", description = "AI智能客服相关接口")
@RestController
@RequestMapping("/api/user/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @Operation(summary = "AI对话（SSE流式）")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@Valid @RequestBody ChatDTO dto) {
        Long userId = UserContextHolder.getUserId();
        return aiChatService.chat(userId, dto);
    }

    @Operation(summary = "AI对话（普通）")
    @PostMapping
    public Result<String> chat(@Valid @RequestBody ChatDTO dto) {
        Long userId = UserContextHolder.getUserId();
        String response = aiChatService.chatSync(userId, dto);
        return Result.success(response);
    }

    @Operation(summary = "清除会话上下文")
    @DeleteMapping("/session/{sessionId}")
    public Result<Void> clearSession(@PathVariable("sessionId") String sessionId) {
        Long userId = UserContextHolder.getUserId();
        aiChatService.clearSession(userId, sessionId);
        return Result.success();
    }
}
