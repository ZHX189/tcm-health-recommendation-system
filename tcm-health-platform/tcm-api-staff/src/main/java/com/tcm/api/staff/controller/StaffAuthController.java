package com.tcm.api.staff.controller;

import com.tcm.common.constant.CommonConstant;
import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.LoginDTO;
import com.tcm.model.dto.PasswordUpdateDTO;
import com.tcm.model.dto.UserUpdateDTO;
import com.tcm.model.vo.LoginVO;
import com.tcm.model.vo.UserVO;
import com.tcm.security.util.SecurityUtil;
import com.tcm.service.user.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 员工认证控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "员工认证", description = "员工登录相关接口")
@RestController
@RequestMapping("/api/staff/auth")
@RequiredArgsConstructor
public class StaffAuthController {

    private final SysUserService userService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Operation(summary = "员工登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = userService.login(dto, CommonConstant.USER_TYPE_STAFF);
        return Result.success(vo);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/profile")
    public Result<UserVO> getProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserVO vo = userService.getCurrentUser(userId);
        return Result.success(vo);
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateUser(userId, dto);
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updatePassword(userId, dto);
        return Result.success();
    }

    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = SecurityUtil.getCurrentUserId();
        String avatarUrl = FileUploadUtil.uploadImage(file, uploadPath);
        userService.updateAvatar(userId, avatarUrl);
        return Result.success(avatarUrl);
    }
}
