package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.result.Result;
import com.tcm.model.dto.StaffCreateDTO;
import com.tcm.model.query.UserQuery;
import com.tcm.model.vo.UserVO;
import com.tcm.service.user.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "员工管理", description = "管理员-员工管理相关接口")
@RestController
@RequestMapping("/api/admin/staffs")
@RequiredArgsConstructor
public class AdminStaffController {

    private final SysUserService userService;

    @Operation(summary = "员工列表")
    @GetMapping
    public Result<Page<UserVO>> list(UserQuery query) {
        query.setUserType(CommonConstant.USER_TYPE_STAFF);
        Page<UserVO> page = userService.pageUsers(query);
        return Result.success(page);
    }

    @Operation(summary = "创建员工")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody StaffCreateDTO dto) {
        userService.createStaff(dto);
        return Result.success();
    }

    @Operation(summary = "员工详情")
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable("id") Long id) {
        UserVO vo = userService.getCurrentUser(id);
        return Result.success(vo);
    }

    @Operation(summary = "禁用/启用员工")
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }
}
