package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI聊天DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "AI聊天请求")
public class ChatDTO {

    @Schema(description = "会话ID（首次为空，后续传入）")
    private String sessionId;

    @Schema(description = "用户消息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "消息内容不能为空")
    private String message;
}
