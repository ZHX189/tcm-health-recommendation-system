package com.tcm.api.admin.controller;

import com.tcm.service.export.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 数据导出控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Slf4j
@Tag(name = "数据导出", description = "管理员-数据导出接口")
@RestController
@RequestMapping("/api/admin/export")
@RequiredArgsConstructor
public class AdminExportController {

    private final ExportService exportService;

    @Operation(summary = "导出订单数据")
    @GetMapping("/orders")
    public void exportOrders(HttpServletResponse response,
                             @RequestParam(value = "status", required = false) Integer status,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate) throws IOException {
        setCsvResponse(response, "orders.csv");
        exportService.exportOrders(response.getOutputStream(), status, startDate, endDate);
    }

    @Operation(summary = "导出用户数据")
    @GetMapping("/users")
    public void exportUsers(HttpServletResponse response) throws IOException {
        setCsvResponse(response, "users.csv");
        exportService.exportUsers(response.getOutputStream());
    }

    @Operation(summary = "导出商品数据")
    @GetMapping("/products")
    public void exportProducts(HttpServletResponse response) throws IOException {
        setCsvResponse(response, "products.csv");
        exportService.exportProducts(response.getOutputStream());
    }

    @Operation(summary = "导出评价数据")
    @GetMapping("/reviews")
    public void exportReviews(HttpServletResponse response) throws IOException {
        setCsvResponse(response, "reviews.csv");
        exportService.exportReviews(response.getOutputStream());
    }

    private void setCsvResponse(HttpServletResponse response, String filename) {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setCharacterEncoding("UTF-8");
    }
}
