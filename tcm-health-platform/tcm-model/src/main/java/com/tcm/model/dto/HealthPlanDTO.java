package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 养生方案DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "养生方案请求")
public class HealthPlanDTO {

    @Schema(description = "方案标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200个字符")
    private String title;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "适用体质类型")
    private String constitutionType;

    @Schema(description = "适用季节")
    private String season;

    @Schema(description = "方案摘要")
    @Size(max = 500, message = "摘要最多500个字符")
    private String summary;

    @Schema(description = "方案详情", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "饮食建议")
    private String dietAdvice;

    @Schema(description = "运动建议")
    private String exerciseAdvice;

    @Schema(description = "睡眠建议")
    private String sleepAdvice;

    @Schema(description = "药材建议")
    private String medicineAdvice;

    @Schema(description = "禁忌事项")
    private String taboo;

    @Schema(description = "是否推荐")
    private Integer isRecommend;
}
