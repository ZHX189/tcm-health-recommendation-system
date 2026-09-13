package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "公告请求")
public class AnnouncementDTO {

    @Schema(description = "公告标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200个字符")
    private String title;

    @Schema(description = "公告内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "公告类型：1-系统公告，2-活动公告，3-其他")
    private Integer type;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "生效开始时间")
    private LocalDateTime startTime;

    @Schema(description = "生效结束时间")
    private LocalDateTime endTime;
}
