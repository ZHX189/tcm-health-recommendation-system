package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.OrderQuery;
import com.tcm.model.vo.OrderVO;
import com.tcm.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端订单管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "订单管理", description = "管理员-订单管理相关接口")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "订单列表")
    @GetMapping
    public Result<Page<OrderVO>> list(OrderQuery query) {
        Page<OrderVO> page = orderService.pageOrders(query, null);
        return Result.success(page);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable("id") Long id) {
        OrderVO vo = orderService.getOrderDetail(id, null);
        return Result.success(vo);
    }

    @Operation(summary = "订单发货")
    @PostMapping("/{id}/ship")
    public Result<Void> ship(@PathVariable("id") Long id,
                             @RequestParam("expressCompany") String expressCompany,
                             @RequestParam("expressNo") String expressNo) {
        orderService.shipOrder(id, expressCompany, expressNo);
        return Result.success();
    }

    @Operation(summary = "修改订单状态")
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        orderService.updateOrderStatus(id, status);
        return Result.success();
    }
}
