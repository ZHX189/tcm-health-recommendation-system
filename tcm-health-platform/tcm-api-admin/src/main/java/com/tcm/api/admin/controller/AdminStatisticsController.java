package com.tcm.api.admin.controller;

import com.tcm.common.result.Result;
import com.tcm.model.vo.*;
import com.tcm.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据统计控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "数据统计", description = "管理员-数据统计相关接口")
@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "仪表盘数据")
    @GetMapping("/dashboard")
    public Result<DashboardVO> dashboard() {
        DashboardVO vo = statisticsService.getDashboardData();
        return Result.success(vo);
    }

    @Operation(summary = "销售趋势")
    @GetMapping("/sales-trend")
    public Result<List<SalesTrendVO>> salesTrend(@RequestParam(value = "days", defaultValue = "7") Integer days) {
        List<SalesTrendVO> list = statisticsService.getSalesTrend(days);
        return Result.success(list);
    }

    @Operation(summary = "评价统计")
    @GetMapping("/review-stats")
    public Result<ReviewStatsVO> reviewStats(@RequestParam(value = "days", defaultValue = "30") Integer days) {
        ReviewStatsVO vo = statisticsService.getReviewStats(days);
        return Result.success(vo);
    }

    @Operation(summary = "用户统计")
    @GetMapping("/user-stats")
    public Result<UserStatsVO> userStats(@RequestParam(value = "days", defaultValue = "30") Integer days) {
        UserStatsVO vo = statisticsService.getUserStats(days);
        return Result.success(vo);
    }
}
