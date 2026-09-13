package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 订单创建DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "创建订单请求")
public class OrderCreateDTO {

    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @Schema(description = "订单备注")
    @Size(max = 500, message = "备注最多500个字符")
    private String remark;

    @Schema(description = "购物车商品ID列表（为空则下单全部选中商品）")
    private List<Long> cartIds;

    @Schema(description = "直接购买的商品ID（与cartIds二选一）")
    private Long productId;

    @Schema(description = "直接购买的商品数量")
    private Integer quantity;
}
