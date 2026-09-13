package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.service.config.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点配置公开接口（无需登录）
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "站点配置", description = "公开的站点配置接口")
@RestController
@RequestMapping("/api/user/site-config")
@RequiredArgsConstructor
public class SiteConfigController {

    private final SysConfigService configService;

    @Operation(summary = "获取站点配置")
    @GetMapping
    public Result<Map<String, String>> getSiteConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("site_name", configService.getConfigValue("site_name"));
        config.put("site_logo", configService.getConfigValue("site_logo"));
        config.put("contact_phone", configService.getConfigValue("contact_phone"));
        config.put("contact_email", configService.getConfigValue("contact_email"));
        return Result.success(config);
    }
}
