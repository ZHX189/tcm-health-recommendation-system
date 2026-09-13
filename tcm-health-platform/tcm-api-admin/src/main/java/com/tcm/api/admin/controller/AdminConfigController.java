package com.tcm.api.admin.controller;

import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.service.config.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 系统配置控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "系统配置", description = "管理员-系统配置相关接口")
@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SysConfigService configService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Operation(summary = "获取所有配置")
    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        List<Map<String, Object>> list = configService.getAllConfigs();
        return Result.success(list);
    }

    @Operation(summary = "更新配置")
    @PutMapping("/{configKey}")
    public Result<Void> update(@PathVariable("configKey") String configKey,
                               @RequestParam("configValue") String configValue) {
        configService.updateConfig(configKey, configValue);
        return Result.success();
    }

    @Operation(summary = "批量更新配置")
    @PutMapping("/batch")
    public Result<Void> batchUpdate(@RequestBody List<Map<String, String>> configs) {
        configService.batchUpdateConfigs(configs);
        return Result.success();
    }

    @Operation(summary = "上传配置图片（如Logo）")
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = FileUploadUtil.uploadImage(file, uploadPath);
        return Result.success(imageUrl);
    }
}
