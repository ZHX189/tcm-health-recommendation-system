package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "购物车信息")
public class CartVO {

    @Schema(description = "购物车ID")
    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "商品价格")
    private BigDecimal productPrice;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "是否选中")
    private Integer selected;

    @Schema(description = "商品状态：0-下架，1-上架")
    private Integer productStatus;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "小计金额")
    private BigDecimal totalPrice;
}
