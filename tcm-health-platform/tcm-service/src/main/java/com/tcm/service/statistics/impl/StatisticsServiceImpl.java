package com.tcm.service.statistics.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.enums.OrderStatusEnum;
import com.tcm.mapper.*;
import com.tcm.model.entity.*;
import com.tcm.model.vo.*;
import com.tcm.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderInfoMapper orderMapper;
    private final SysUserMapper userMapper;
    private final ProductMapper productMapper;
    private final ProductStockMapper stockMapper;
    private final CommunityPostMapper postMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public DashboardVO getDashboardData() {
        DashboardVO vo = new DashboardVO();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime weekStart = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);

        // 今日销售额
        vo.setTodaySales(orderMapper.sumSalesAmount(todayStart, todayEnd));
        if (vo.getTodaySales() == null) {
            vo.setTodaySales(BigDecimal.ZERO);
        }

        // 今日订单数
        vo.setTodayOrders(orderMapper.countOrders(todayStart, todayEnd));

        // 今日新增用户
        vo.setTodayUsers(Math.toIntExact(userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER)
                        .ge(SysUser::getCreateTime, todayStart)
                        .le(SysUser::getCreateTime, todayEnd)
        )));

        // 待发货订单数
        vo.setPendingShipOrders(Math.toIntExact(orderMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getStatus, OrderStatusEnum.PAID.getCode())
        )));

        // 库存预警商品数
        vo.setStockWarningCount(Math.toIntExact(stockMapper.selectCount(
                new LambdaQueryWrapper<ProductStock>()
                        .apply("stock <= warning_stock")
        )));

        // 待审核帖子数
        vo.setPendingPostCount(Math.toIntExact(postMapper.selectCount(
                new LambdaQueryWrapper<CommunityPost>()
                        .eq(CommunityPost::getStatus, 0)
        )));

        // 本周销售额
        vo.setWeekSales(orderMapper.sumSalesAmount(weekStart, todayEnd));
        if (vo.getWeekSales() == null) {
            vo.setWeekSales(BigDecimal.ZERO);
        }

        // 本月销售额
        vo.setMonthSales(orderMapper.sumSalesAmount(monthStart, todayEnd));
        if (vo.getMonthSales() == null) {
            vo.setMonthSales(BigDecimal.ZERO);
        }

        // 总用户数
        vo.setTotalUsers(Math.toIntExact(userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER)
        )));

        // 总商品数
        vo.setTotalProducts(Math.toIntExact(productMapper.selectCount(null)));

        // 总订单数
        vo.setTotalOrders(Math.toIntExact(orderMapper.selectCount(null)));

        // 近7天销售趋势
        vo.setSalesTrend(getSalesTrend(7));

        // 商品销量排行（Top 10）
        vo.setProductRank(orderMapper.selectProductRank(10));

        // 订单状态分布
        vo.setOrderStatusDist(getOrderStatusDist());

        return vo;
    }

    @Override
    public List<SalesTrendVO> getSalesTrend(Integer days) {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(days - 1), LocalTime.MIN);
        return orderMapper.selectSalesTrend(startTime);
    }

    @Override
    public ReviewStatsVO getReviewStats(Integer days) {
        ReviewStatsVO vo = new ReviewStatsVO();

        // 总评价数
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM product_review WHERE deleted = 0", Long.class);
        vo.setTotalCount(total != null ? total.intValue() : 0);

        // 平均评分
        Double avg = jdbcTemplate.queryForObject(
                "SELECT COALESCE(AVG(rating), 0) FROM product_review WHERE deleted = 0 AND status = 1", Double.class);
        vo.setAvgRating(avg != null ? Math.round(avg * 10) / 10.0 : 0.0);

        // 好中差评
        Long good = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM product_review WHERE deleted = 0 AND status = 1 AND rating >= 4", Long.class);
        Long mid = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM product_review WHERE deleted = 0 AND status = 1 AND rating = 3", Long.class);
        Long bad = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM product_review WHERE deleted = 0 AND status = 1 AND rating <= 2", Long.class);
        vo.setGoodCount(good != null ? good.intValue() : 0);
        vo.setMidCount(mid != null ? mid.intValue() : 0);
        vo.setBadCount(bad != null ? bad.intValue() : 0);

        int passedTotal = vo.getGoodCount() + vo.getMidCount() + vo.getBadCount();
        vo.setGoodRate(passedTotal > 0 ? Math.round(vo.getGoodCount() * 1000.0 / passedTotal) / 10.0 : 0.0);

        // 各星级分布
        List<StatusCountVO> ratingDist = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            Long cnt = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM product_review WHERE deleted = 0 AND status = 1 AND rating = ?",
                    Long.class, i);
            ratingDist.add(new StatusCountVO(i, i + "星", cnt != null ? cnt.intValue() : 0));
        }
        vo.setRatingDist(ratingDist);

        // 近N天评价趋势
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(days - 1), LocalTime.MIN);
        List<Map<String, Object>> trendRows = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COUNT(*) as orderCount " +
                "FROM product_review WHERE deleted = 0 AND create_time >= ? " +
                "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date", startTime);
        List<SalesTrendVO> trend = new ArrayList<>();
        for (Map<String, Object> row : trendRows) {
            SalesTrendVO t = new SalesTrendVO();
            t.setDate(row.get("date").toString());
            t.setOrderCount(((Number) row.get("orderCount")).intValue());
            t.setAmount(BigDecimal.ZERO);
            trend.add(t);
        }
        vo.setTrend(trend);

        return vo;
    }

    @Override
    public UserStatsVO getUserStats(Integer days) {
        UserStatsVO vo = new UserStatsVO();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime weekStart = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);

        // 总用户数
        Long total = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER));
        vo.setTotalUsers(total.intValue());

        // 今日新增
        Long todayNew = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER)
                        .ge(SysUser::getCreateTime, todayStart).le(SysUser::getCreateTime, todayEnd));
        vo.setTodayNew(todayNew.intValue());

        // 本周新增
        Long weekNew = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER)
                        .ge(SysUser::getCreateTime, weekStart));
        vo.setWeekNew(weekNew.intValue());

        // 本月新增
        Long monthNew = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserType, CommonConstant.USER_TYPE_USER)
                        .ge(SysUser::getCreateTime, monthStart));
        vo.setMonthNew(monthNew.intValue());

        // 活跃用户(近7天有登录)
        Long active = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE user_type = 1 AND last_login_time >= ?",
                Long.class, weekStart);
        vo.setActiveUsers(active != null ? active.intValue() : 0);
        vo.setActiveRate(vo.getTotalUsers() > 0
                ? Math.round(vo.getActiveUsers() * 1000.0 / vo.getTotalUsers()) / 10.0 : 0.0);

        // 用户增长趋势
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(days - 1), LocalTime.MIN);
        List<Map<String, Object>> trendRows = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COUNT(*) as orderCount " +
                "FROM sys_user WHERE user_type = 1 AND create_time >= ? " +
                "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date", startTime);
        List<SalesTrendVO> growthTrend = new ArrayList<>();
        for (Map<String, Object> row : trendRows) {
            SalesTrendVO t = new SalesTrendVO();
            t.setDate(row.get("date").toString());
            t.setOrderCount(((Number) row.get("orderCount")).intValue());
            t.setAmount(BigDecimal.ZERO);
            growthTrend.add(t);
        }
        vo.setGrowthTrend(growthTrend);

        return vo;
    }

    /**
     * 获取订单状态分布
     */
    private List<StatusCountVO> getOrderStatusDist() {
        List<StatusCountVO> list = new ArrayList<>();
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            long count = orderMapper.selectCount(
                    new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, status.getCode())
            );
            list.add(new StatusCountVO(status.getCode(), status.getDesc(), (int) count));
        }
        return list;
    }
}
