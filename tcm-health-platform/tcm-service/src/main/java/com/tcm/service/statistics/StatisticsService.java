package com.tcm.service.statistics;

import com.tcm.model.vo.DashboardVO;
import com.tcm.model.vo.ReviewStatsVO;
import com.tcm.model.vo.SalesTrendVO;
import com.tcm.model.vo.UserStatsVO;

import java.util.List;

/**
 * 统计服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface StatisticsService {

    /**
     * 获取仪表盘数据
     */
    DashboardVO getDashboardData();

    /**
     * 获取销售趋势
     */
    List<SalesTrendVO> getSalesTrend(Integer days);

    /**
     * 获取评价统计
     */
    ReviewStatsVO getReviewStats(Integer days);

    /**
     * 获取用户统计
     */
    UserStatsVO getUserStats(Integer days);
}
