package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.OrderInfo;
import com.tcm.model.vo.ProductRankVO;
import com.tcm.model.vo.SalesTrendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 统计销售额
     */
    @Select("SELECT COALESCE(SUM(pay_amount), 0) FROM order_info WHERE status IN (1,2,3) AND create_time >= #{startTime} AND create_time < #{endTime} AND deleted = 0")
    BigDecimal sumSalesAmount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计订单数
     */
    @Select("SELECT COUNT(*) FROM order_info WHERE create_time >= #{startTime} AND create_time < #{endTime} AND deleted = 0")
    Integer countOrders(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询销售趋势
     */
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COALESCE(SUM(pay_amount), 0) as amount, COUNT(*) as orderCount " +
            "FROM order_info WHERE status IN (1,2,3) AND create_time >= #{startTime} AND deleted = 0 " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date")
    List<SalesTrendVO> selectSalesTrend(@Param("startTime") LocalDateTime startTime);

    /**
     * 查询商品销量排行
     */
    @Select("SELECT oi.product_id as productId, p.name as productName, p.main_image as productImage, " +
            "SUM(oi.quantity) as sales, SUM(oi.total_price) as salesAmount " +
            "FROM order_item oi " +
            "LEFT JOIN product p ON oi.product_id = p.id " +
            "LEFT JOIN order_info o ON oi.order_id = o.id " +
            "WHERE o.status IN (1,2,3) AND o.deleted = 0 " +
            "GROUP BY oi.product_id, p.name, p.main_image " +
            "ORDER BY sales DESC LIMIT #{limit}")
    List<ProductRankVO> selectProductRank(@Param("limit") Integer limit);
}
