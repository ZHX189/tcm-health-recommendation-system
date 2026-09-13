package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.model.dto.AddressDTO;
import com.tcm.model.entity.UserAddress;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "收货地址", description = "收货地址管理相关接口")
@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "地址列表")
    @GetMapping
    public Result<List<UserAddress>> list() {
        Long userId = UserContextHolder.getUserId();
        List<UserAddress> list = addressService.getAddressList(userId);
        return Result.success(list);
    }

    @Operation(summary = "新增地址")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody AddressDTO dto) {
        Long userId = UserContextHolder.getUserId();
        addressService.addAddress(userId, dto);
        return Result.success();
    }

    @Operation(summary = "修改地址")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable("id") Long id, @Valid @RequestBody AddressDTO dto) {
        Long userId = UserContextHolder.getUserId();
        addressService.updateAddress(userId, id, dto);
        return Result.success();
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        addressService.deleteAddress(userId, id);
        return Result.success();
    }

    @Operation(summary = "设置默认地址")
    @PatchMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        addressService.setDefault(userId, id);
        return Result.success();
    }
}
