package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.PasswordUpdateDTO;
import com.tcm.model.dto.UserUpdateDTO;
import com.tcm.model.vo.UserVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.user.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "个人信息", description = "个人信息管理相关接口")
@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final SysUserService userService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Operation(summary = "获取个人信息")
    @GetMapping
    public Result<UserVO> getProfile() {
        Long userId = UserContextHolder.getUserId();
        UserVO vo = userService.getCurrentUser(userId);
        return Result.success(vo);
    }

    @Operation(summary = "修改个人信息")
    @PutMapping
    public Result<Void> updateProfile(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = UserContextHolder.getUserId();
        userService.updateUser(userId, dto);
        return Result.success();
    }

    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = UserContextHolder.getUserId();
        String avatarUrl = FileUploadUtil.uploadImage(file, uploadPath);
        userService.updateAvatar(userId, avatarUrl);
        return Result.success(avatarUrl);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        Long userId = UserContextHolder.getUserId();
        userService.updatePassword(userId, dto);
        return Result.success();
    }
}
