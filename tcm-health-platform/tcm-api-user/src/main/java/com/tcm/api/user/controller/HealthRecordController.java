package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.model.dto.HealthRecordDTO;
import com.tcm.model.entity.UserHealthRecord;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.health.HealthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 健康档案控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "健康档案", description = "健康档案管理相关接口")
@RestController
@RequestMapping("/api/user/health-record")
@RequiredArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @Operation(summary = "获取健康档案")
    @GetMapping
    public Result<UserHealthRecord> get() {
        Long userId = UserContextHolder.getUserId();
        UserHealthRecord record = healthRecordService.getByUserId(userId);
        return Result.success(record);
    }

    @Operation(summary = "创建健康档案")
    @PostMapping
    public Result<Void> save(@Valid @RequestBody HealthRecordDTO dto) {
        Long userId = UserContextHolder.getUserId();
        healthRecordService.saveOrUpdate(userId, dto);
        return Result.success();
    }

    @Operation(summary = "更新健康档案")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody HealthRecordDTO dto) {
        Long userId = UserContextHolder.getUserId();
        healthRecordService.saveOrUpdate(userId, dto);
        return Result.success();
    }
}
