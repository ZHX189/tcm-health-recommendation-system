package com.tcm.api.staff.controller;

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
 * 员工-健康档案查看控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "健康档案查看", description = "员工端健康档案查看接口")
@RestController
@RequestMapping("/api/staff/health-records")
@RequiredArgsConstructor
public class StaffHealthRecordController {

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
}
