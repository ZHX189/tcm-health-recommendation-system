package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.result.Result;
import com.tcm.model.query.UserQuery;
import com.tcm.model.vo.UserVO;
import com.tcm.service.user.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "用户管理", description = "管理员-用户管理相关接口")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final SysUserService userService;

    @Operation(summary = "用户列表")
    @GetMapping
    public Result<Page<UserVO>> list(UserQuery query) {
        query.setUserType(CommonConstant.USER_TYPE_USER);
        Page<UserVO> page = userService.pageUsers(query);
        return Result.success(page);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable("id") Long id) {
        UserVO vo = userService.getCurrentUser(id);
        return Result.success(vo);
    }

    @Operation(summary = "禁用/启用用户")
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }
}
