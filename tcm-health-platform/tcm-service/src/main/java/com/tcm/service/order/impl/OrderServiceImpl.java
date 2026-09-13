package com.tcm.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.enums.OrderStatusEnum;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import com.tcm.mapper.*;
import com.tcm.model.dto.OrderCreateDTO;
import com.tcm.model.entity.*;
import com.tcm.model.query.OrderQuery;
import com.tcm.model.vo.OrderItemVO;
import com.tcm.model.vo.OrderVO;
import com.tcm.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartMapper cartMapper;
    private final ProductMapper productMapper;
    private final ProductStockMapper stockMapper;
    private final UserAddressMapper addressMapper;
    private final SysUserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {
        // 获取收货地址
        UserAddress address = addressMapper.selectById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("收货地址不存在");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 直接购买
        if (dto.getProductId() != null) {
            Product product = productMapper.selectById(dto.getProductId());
            if (product == null || product.getStatus() != 1) {
                throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
            }

            int quantity = dto.getQuantity() != null ? dto.getQuantity() : 1;
            
            // 检查库存
            checkAndDeductStock(product.getId(), quantity);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getMainImage());
            item.setProductPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            item.setIsReviewed(0);
            orderItems.add(item);

            totalAmount = item.getTotalPrice();
        } else {
            // 从购物车下单
            LambdaQueryWrapper<ShoppingCart> cartWrapper = new LambdaQueryWrapper<>();
            cartWrapper.eq(ShoppingCart::getUserId, userId)
                    .eq(ShoppingCart::getSelected, 1);
            if (CollUtil.isNotEmpty(dto.getCartIds())) {
                cartWrapper.in(ShoppingCart::getId, dto.getCartIds());
            }

            List<ShoppingCart> cartList = cartMapper.selectList(cartWrapper);
            if (CollUtil.isEmpty(cartList)) {
                throw new BusinessException(ResultCode.CART_EMPTY);
            }

            for (ShoppingCart cart : cartList) {
                Product product = productMapper.selectById(cart.getProductId());
                if (product == null || product.getStatus() != 1) {
                    throw new BusinessException("商品[" + cart.getProductId() + "]已下架");
                }

                // 检查并扣减库存
                checkAndDeductStock(product.getId(), cart.getQuantity());

                OrderItem item = new OrderItem();
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setProductImage(product.getMainImage());
                item.setProductPrice(product.getPrice());
                item.setQuantity(cart.getQuantity());
                item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                item.setIsReviewed(0);
                orderItems.add(item);

                totalAmount = totalAmount.add(item.getTotalPrice());

                // 删除购物车
                cartMapper.deleteById(cart.getId());
            }
        }

        // 创建订单
        OrderInfo order = new OrderInfo();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatusEnum.UNPAID.getCode());
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        order.setRemark(dto.getRemark());
        order.setDeleted(0);
        save(order);

        // 保存订单明细
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setOrderNo(order.getOrderNo());
            orderItemMapper.insert(item);
        }

        return getOrderDetail(order.getId(), userId);
    }

    /**
     * 检查并扣减库存
     */
    private void checkAndDeductStock(Long productId, Integer quantity) {
        int rows = stockMapper.decreaseStock(productId, quantity);
        if (rows == 0) {
            throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = IdUtil.simpleUUID().substring(0, 6).toUpperCase();
        return "ORD" + date + random;
    }

    @Override
    public Page<OrderVO> pageOrders(OrderQuery query, Long userId) {
        Page<OrderInfo> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, OrderInfo::getUserId, userId)
                .eq(query.getUserId() != null, OrderInfo::getUserId, query.getUserId())
                .eq(StrUtil.isNotBlank(query.getOrderNo()), OrderInfo::getOrderNo, query.getOrderNo())
                .eq(query.getStatus() != null, OrderInfo::getStatus, query.getStatus())
                .ge(query.getStartTime() != null, OrderInfo::getCreateTime, query.getStartTime())
                .le(query.getEndTime() != null, OrderInfo::getCreateTime, query.getEndTime())
                .like(StrUtil.isNotBlank(query.getReceiverName()), OrderInfo::getReceiverName, query.getReceiverName())
                .like(StrUtil.isNotBlank(query.getReceiverPhone()), OrderInfo::getReceiverPhone, query.getReceiverPhone())
                .orderByDesc(OrderInfo::getCreateTime);

        Page<OrderInfo> result = page(page, wrapper);

        List<OrderVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        Page<OrderVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OrderVO getOrderDetail(Long id, Long userId) {
        OrderInfo order = getById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        if (userId != null && !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long id, Long userId, String reason) {
        OrderInfo order = getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        if (!OrderStatusEnum.UNPAID.getCode().equals(order.getStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)
        );
        for (OrderItem item : items) {
            stockMapper.increaseStock(item.getProductId(), item.getQuantity());
        }

        order.setStatus(OrderStatusEnum.CANCELLED.getCode());
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(reason);
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long id, Long userId, Integer payType) {
        OrderInfo order = getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        if (!OrderStatusEnum.UNPAID.getCode().equals(order.getStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPayType(payType);
        order.setPayTime(LocalDateTime.now());
        updateById(order);

        // 增加商品销量
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)
        );
        for (OrderItem item : items) {
            productMapper.update(null,
                    new LambdaUpdateWrapper<Product>()
                            .eq(Product::getId, item.getProductId())
                            .setSql("sales = sales + " + item.getQuantity())
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long id, Long userId) {
        OrderInfo order = getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        if (!OrderStatusEnum.SHIPPED.getCode().equals(order.getStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.COMPLETED.getCode());
        order.setCompleteTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shipOrder(Long id, String expressCompany, String expressNo) {
        OrderInfo order = getById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        if (!OrderStatusEnum.PAID.getCode().equals(order.getStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.SHIPPED.getCode());
        order.setExpressCompany(expressCompany);
        order.setExpressNo(expressNo);
        order.setShipTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(Long id, Integer status) {
        OrderInfo order = getById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_EXISTS);
        }
        order.setStatus(status);
        updateById(order);
    }

    /**
     * 转换为VO
     */
    private OrderVO convertToVO(OrderInfo order) {
        OrderVO vo = new OrderVO();
        BeanUtil.copyProperties(order, vo);

        // 设置状态描述
        OrderStatusEnum statusEnum = OrderStatusEnum.getByCode(order.getStatus());
        vo.setStatusDesc(statusEnum != null ? statusEnum.getDesc() : "未知");

        // 获取用户名
        SysUser user = userMapper.selectById(order.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }

        // 获取订单明细
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId())
        );
        List<OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtil.copyProperties(item, itemVO);
            return itemVO;
        }).toList();
        vo.setItems(itemVOs);

        return vo;
    }
}
