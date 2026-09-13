package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 养生文章DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "养生文章请求")
public class ArticleDTO {

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200个字符")
    private String title;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "文章摘要")
    @Size(max = 500, message = "摘要最多500个字符")
    private String summary;

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "是否推荐")
    private Integer isRecommend;
}
