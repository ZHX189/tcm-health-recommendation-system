package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "商品请求")
public class ProductDTO {

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称最多200个字符")
    private String name;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Schema(description = "销售价格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "主图URL")
    private String mainImage;

    @Schema(description = "副标题")
    @Size(max = 500, message = "副标题最多500个字符")
    private String subTitle;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "商品详情（富文本）")
    private String detail;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "产地")
    private String origin;

    @Schema(description = "功效说明")
    private String efficacy;

    @Schema(description = "使用方法")
    private String usageMethod;

    @Schema(description = "储存方法")
    private String storageMethod;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "预警库存")
    private Integer warningStock;

    @Schema(description = "是否推荐：0-否，1-是")
    private Integer isRecommend;

    @Schema(description = "商品图片列表")
    private List<String> images;
}
