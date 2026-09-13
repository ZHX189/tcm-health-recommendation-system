package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.entity.HealthPlan;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.HealthPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 养生方案控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "养生方案", description = "养生方案相关接口")
@RestController
@RequestMapping("/api/user/health-plans")
@RequiredArgsConstructor
public class HealthPlanController {

    private final HealthPlanService healthPlanService;

    @Operation(summary = "养生方案列表")
    @GetMapping
    public Result<Page<HealthPlan>> list(PageQuery query,
                                         @RequestParam(value = "constitutionType", required = false) String constitutionType,
                                         @RequestParam(value = "season", required = false) String season) {
        Page<HealthPlan> page = healthPlanService.pagePlans(query, constitutionType, season);
        return Result.success(page);
    }

    @Operation(summary = "养生方案详情")
    @GetMapping("/{id}")
    public Result<HealthPlan> detail(@PathVariable("id") Long id) {
        HealthPlan plan = healthPlanService.getPlanDetail(id);
        return Result.success(plan);
    }
}
