package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.entity.UserHealthRecord;
import com.tcm.model.query.HealthRecordQuery;
import com.tcm.service.health.HealthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员-健康档案管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "健康档案管理", description = "管理员-健康档案管理相关接口")
@RestController
@RequestMapping("/api/admin/health-records")
@RequiredArgsConstructor
public class AdminHealthRecordController {

    private final HealthRecordService healthRecordService;

    @Operation(summary = "健康档案列表")
    @GetMapping
    public Result<Page<UserHealthRecord>> list(HealthRecordQuery query) {
        Page<UserHealthRecord> page = healthRecordService.pageRecords(query);
        return Result.success(page);
    }

    @Operation(summary = "健康档案详情")
    @GetMapping("/{id}")
    public Result<UserHealthRecord> detail(@PathVariable("id") Long id) {
        UserHealthRecord record = healthRecordService.getById(id);
        return Result.success(record);
    }

    @Operation(summary = "删除健康档案")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        healthRecordService.deleteRecord(id);
        return Result.success();
    }
}
