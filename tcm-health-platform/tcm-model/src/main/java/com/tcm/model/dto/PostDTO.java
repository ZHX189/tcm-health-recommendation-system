package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 帖子DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "发布帖子请求")
public class PostDTO {

    @Schema(description = "帖子标题")
    @Size(max = 200, message = "标题最多200个字符")
    private String title;

    @Schema(description = "帖子内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    @Schema(description = "图片列表")
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;
}
