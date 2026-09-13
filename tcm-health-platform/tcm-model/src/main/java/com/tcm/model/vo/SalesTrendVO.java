package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 销售趋势VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "销售趋势")
public class SalesTrendVO {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "销售额")
    private BigDecimal amount;

    @Schema(description = "订单数")
    private Integer orderCount;
}
