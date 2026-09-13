package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘数据VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "仪表盘数据")
public class DashboardVO {

    @Schema(description = "今日销售额")
    private BigDecimal todaySales;

    @Schema(description = "今日订单数")
    private Integer todayOrders;

    @Schema(description = "今日新增用户")
    private Integer todayUsers;

    @Schema(description = "待发货订单数")
    private Integer pendingShipOrders;

    @Schema(description = "库存预警商品数")
    private Integer stockWarningCount;

    @Schema(description = "待审核帖子数")
    private Integer pendingPostCount;

    @Schema(description = "本周销售额")
    private BigDecimal weekSales;

    @Schema(description = "本月销售额")
    private BigDecimal monthSales;

    @Schema(description = "总用户数")
    private Integer totalUsers;

    @Schema(description = "总商品数")
    private Integer totalProducts;

    @Schema(description = "总订单数")
    private Integer totalOrders;

    @Schema(description = "近7天销售趋势")
    private List<SalesTrendVO> salesTrend;

    @Schema(description = "商品销量排行")
    private List<ProductRankVO> productRank;

    @Schema(description = "订单状态分布")
    private List<StatusCountVO> orderStatusDist;
}
