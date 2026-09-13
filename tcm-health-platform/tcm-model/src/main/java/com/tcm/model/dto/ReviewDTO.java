package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 评价DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "商品评价请求")
public class ReviewDTO {

    @Schema(description = "订单明细ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单明细ID不能为空")
    private Long orderItemId;

    @Schema(description = "评分（1-5星）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    @Schema(description = "评价内容")
    @Size(max = 500, message = "评价内容最多500个字符")
    private String content;

    @Schema(description = "评价图片列表")
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;

    @Schema(description = "是否匿名：0-否，1-是")
    private Integer isAnonymous;
}
