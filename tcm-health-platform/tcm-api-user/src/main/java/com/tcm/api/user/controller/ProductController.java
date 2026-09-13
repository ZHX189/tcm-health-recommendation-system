package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.ProductQuery;
import com.tcm.model.vo.CategoryVO;
import com.tcm.model.vo.ProductVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "商品", description = "商品浏览相关接口")
@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "商品列表")
    @GetMapping
    public Result<Page<ProductVO>> list(ProductQuery query) {
        Long userId = UserContextHolder.getUserId();
        // 只查询上架商品
        query.setStatus(1);
        Page<ProductVO> page = productService.pageProducts(query, userId);
        return Result.success(page);
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> detail(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        // 增加浏览量
        productService.incrementViewCount(id);
        ProductVO vo = productService.getProductDetail(id, userId);
        return Result.success(vo);
    }

    @Operation(summary = "商品分类")
    @GetMapping("/categories")
    public Result<List<CategoryVO>> categories() {
        List<CategoryVO> list = productService.getCategoryTree();
        return Result.success(list);
    }
}
