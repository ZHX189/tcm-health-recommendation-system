package com.tcm.api.staff.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.ProductDTO;
import com.tcm.model.query.ProductQuery;
import com.tcm.model.vo.ProductVO;
import com.tcm.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 员工商品管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "商品管理", description = "员工商品管理相关接口")
@RestController
@RequestMapping("/api/staff/products")
@RequiredArgsConstructor
public class StaffProductController {

    private final ProductService productService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Operation(summary = "商品列表")
    @GetMapping
    public Result<Page<ProductVO>> list(ProductQuery query) {
        Page<ProductVO> page = productService.pageProducts(query, null);
        return Result.success(page);
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> detail(@PathVariable("id") Long id) {
        ProductVO vo = productService.getProductDetail(id, null);
        return Result.success(vo);
    }

    @Operation(summary = "修改商品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO dto) {
        productService.updateProduct(id, dto);
        return Result.success();
    }

    @Operation(summary = "上传商品图片")
    @PostMapping("/{id}/images")
    public Result<String> uploadImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        String imageUrl = FileUploadUtil.uploadImage(file, uploadPath);
        return Result.success(imageUrl);
    }

    @Operation(summary = "商品上下架（需审核）")
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        // 员工操作的状态变更需要审核，设为待审核状态
        productService.updateStatus(id, 2);
        return Result.success();
    }
}
