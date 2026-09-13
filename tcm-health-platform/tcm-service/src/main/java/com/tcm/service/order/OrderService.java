package com.tcm.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.OrderCreateDTO;
import com.tcm.model.entity.OrderInfo;
import com.tcm.model.query.OrderQuery;
import com.tcm.model.vo.OrderVO;

/**
 * 订单服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface OrderService extends IService<OrderInfo> {

    /**
     * 创建订单
     */
    OrderVO createOrder(Long userId, OrderCreateDTO dto);

    /**
     * 分页查询订单
     */
    Page<OrderVO> pageOrders(OrderQuery query, Long userId);

    /**
     * 获取订单详情
     */
    OrderVO getOrderDetail(Long id, Long userId);

    /**
     * 取消订单
     */
    void cancelOrder(Long id, Long userId, String reason);

    /**
     * 支付订单
     */
    void payOrder(Long id, Long userId, Integer payType);

    /**
     * 确认收货
     */
    void confirmOrder(Long id, Long userId);

    /**
     * 发货
     */
    void shipOrder(Long id, String expressCompany, String expressNo);

    /**
     * 修改订单状态（管理端）
     */
    void updateOrderStatus(Long id, Integer status);
}
