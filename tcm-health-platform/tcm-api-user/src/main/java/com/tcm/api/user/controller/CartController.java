package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.model.dto.CartDTO;
import com.tcm.model.vo.CartVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "购物车", description = "购物车管理相关接口")
@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "购物车列表")
    @GetMapping
    public Result<List<CartVO>> list() {
        Long userId = UserContextHolder.getUserId();
        List<CartVO> list = cartService.getCartList(userId);
        return Result.success(list);
    }

    @Operation(summary = "添加到购物车")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody CartDTO dto) {
        Long userId = UserContextHolder.getUserId();
        cartService.addToCart(userId, dto);
        return Result.success();
    }

    @Operation(summary = "修改数量")
    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity) {
        Long userId = UserContextHolder.getUserId();
        cartService.updateQuantity(userId, id, quantity);
        return Result.success();
    }

    @Operation(summary = "更新选中状态")
    @PatchMapping("/{id}/selected")
    public Result<Void> updateSelected(@PathVariable("id") Long id, @RequestParam("selected") Integer selected) {
        Long userId = UserContextHolder.getUserId();
        cartService.updateSelected(userId, id, selected);
        return Result.success();
    }

    @Operation(summary = "全选/取消全选")
    @PatchMapping("/select-all")
    public Result<Void> selectAll(@RequestParam("selected") Integer selected) {
        Long userId = UserContextHolder.getUserId();
        cartService.selectAll(userId, selected);
        return Result.success();
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        cartService.deleteCart(userId, id);
        return Result.success();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        Long userId = UserContextHolder.getUserId();
        cartService.batchDelete(userId, ids);
        return Result.success();
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear")
    public Result<Void> clear() {
        Long userId = UserContextHolder.getUserId();
        cartService.clearCart(userId);
        return Result.success();
    }
}
