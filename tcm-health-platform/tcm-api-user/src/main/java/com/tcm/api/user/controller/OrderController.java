package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.dto.OrderCreateDTO;
import com.tcm.model.query.OrderQuery;
import com.tcm.model.vo.OrderVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "订单", description = "订单管理相关接口")
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "订单列表")
    @GetMapping
    public Result<Page<OrderVO>> list(OrderQuery query) {
        Long userId = UserContextHolder.getUserId();
        Page<OrderVO> page = orderService.pageOrders(query, userId);
        return Result.success(page);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        OrderVO vo = orderService.getOrderDetail(id, userId);
        return Result.success(vo);
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<OrderVO> create(@Valid @RequestBody OrderCreateDTO dto) {
        Long userId = UserContextHolder.getUserId();
        OrderVO vo = orderService.createOrder(userId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "支付订单")
    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable("id") Long id, @RequestParam("payType") Integer payType) {
        Long userId = UserContextHolder.getUserId();
        orderService.payOrder(id, userId, payType);
        return Result.success();
    }

    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        Long userId = UserContextHolder.getUserId();
        orderService.cancelOrder(id, userId, reason);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @PostMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        orderService.confirmOrder(id, userId);
        return Result.success();
    }
}
