package com.tcm.api.user.controller;

import com.tcm.common.constant.CommonConstant;
import com.tcm.common.result.Result;
import com.tcm.model.dto.LoginDTO;
import com.tcm.model.dto.RegisterDTO;
import com.tcm.model.vo.LoginVO;
import com.tcm.service.user.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "用户认证", description = "用户注册、登录相关接口")
@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = userService.login(dto, CommonConstant.USER_TYPE_USER);
        return Result.success(vo);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT无状态，客户端清除Token即可
        return Result.success();
    }
}
