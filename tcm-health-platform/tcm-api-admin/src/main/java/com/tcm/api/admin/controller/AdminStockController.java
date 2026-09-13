package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.PageQuery;
import com.tcm.security.util.SecurityUtil;
import com.tcm.service.stock.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理端库存管理控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "库存管理", description = "管理员-库存管理相关接口")
@RestController
@RequestMapping("/api/admin/stocks")
@RequiredArgsConstructor
public class AdminStockController {

    private final StockService stockService;

    @Operation(summary = "库存列表")
    @GetMapping
    public Result<Page<Map<String, Object>>> list(PageQuery query,
                                                   @RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "warningOnly", required = false) Boolean warningOnly) {
        Page<Map<String, Object>> page = stockService.pageStocks(query, keyword, warningOnly);
        return Result.success(page);
    }

    @Operation(summary = "更新库存")
    @PutMapping("/{productId}")
    public Result<Void> update(@PathVariable("productId") Long productId,
                               @RequestParam("stock") Integer stock,
                               @RequestParam(value = "remark", required = false) String remark) {
        Long operatorId = SecurityUtil.getCurrentUserId();
        stockService.updateStock(productId, stock, operatorId, remark);
        return Result.success();
    }

    @Operation(summary = "批量更新库存")
    @PutMapping("/batch")
    public Result<Void> batchUpdate(@RequestBody List<Map<String, Object>> items) {
        Long operatorId = SecurityUtil.getCurrentUserId();
        stockService.batchUpdateStock(items, operatorId);
        return Result.success();
    }

    @Operation(summary = "库存变更记录")
    @GetMapping("/logs")
    public Result<Page<Map<String, Object>>> logs(PageQuery query,
                                                   @RequestParam(value = "productId", required = false) Long productId) {
        Page<Map<String, Object>> page = stockService.pageStockLogs(query, productId);
        return Result.success(page);
    }

    @Operation(summary = "库存预警列表")
    @GetMapping("/warnings")
    public Result<List<Map<String, Object>>> warnings() {
        List<Map<String, Object>> list = stockService.getWarningStocks();
        return Result.success(list);
    }
}
