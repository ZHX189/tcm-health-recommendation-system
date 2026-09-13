package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "订单明细")
public class OrderItemVO {

    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "商品单价")
    private BigDecimal productPrice;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "小计金额")
    private BigDecimal totalPrice;

    @Schema(description = "是否已评价")
    private Integer isReviewed;
}
