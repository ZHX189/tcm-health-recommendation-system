package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品销量排行VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品销量排行")
public class ProductRankVO {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "销量")
    private Integer sales;

    @Schema(description = "销售额")
    private BigDecimal salesAmount;
}
