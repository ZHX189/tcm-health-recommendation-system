package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户统计VO
 *
 * @author Ti
 * @since 2026-02-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户统计")
public class UserStatsVO {

    @Schema(description = "总用户数")
    private Integer totalUsers;

    @Schema(description = "今日新增")
    private Integer todayNew;

    @Schema(description = "本周新增")
    private Integer weekNew;

    @Schema(description = "本月新增")
    private Integer monthNew;

    @Schema(description = "活跃用户数(近7天有登录)")
    private Integer activeUsers;

    @Schema(description = "活跃率(%)")
    private Double activeRate;

    @Schema(description = "用户增长趋势")
    private List<SalesTrendVO> growthTrend;
}
