package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.CategoryDTO;
import com.tcm.model.dto.ProductDTO;
import com.tcm.model.entity.ProductCategory;
import com.tcm.model.query.ProductQuery;
import com.tcm.model.vo.CategoryVO;
import com.tcm.model.vo.ProductVO;
import com.tcm.service.product.ProductService;
import com.tcm.service.product.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 管理端商品管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "商品管理", description = "管理员-商品管理相关接口")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductCategoryService categoryService;

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

    @Operation(summary = "新增商品")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ProductDTO dto) {
        productService.addProduct(dto);
        return Result.success();
    }

    @Operation(summary = "修改商品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO dto) {
        productService.updateProduct(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    @Operation(summary = "商品上下架")
    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        productService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "上传商品图片")
    @PostMapping("/images")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = FileUploadUtil.uploadImage(file, uploadPath);
        return Result.success(imageUrl);
    }

    @Operation(summary = "分类列表")
    @GetMapping("/categories")
    public Result<List<CategoryVO>> categoryList() {
        List<CategoryVO> list = productService.getCategoryTree();
        return Result.success(list);
    }

    @Operation(summary = "新增分类")
    @PostMapping("/categories")
    public Result<Void> addCategory(@Valid @RequestBody CategoryDTO dto) {
        categoryService.addCategory(dto);
        return Result.success();
    }

    @Operation(summary = "修改分类")
    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO dto) {
        categoryService.updateCategory(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
