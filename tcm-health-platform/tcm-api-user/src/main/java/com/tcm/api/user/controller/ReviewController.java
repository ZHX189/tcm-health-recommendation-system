package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.dto.ReviewDTO;
import com.tcm.model.query.PageQuery;
import com.tcm.model.vo.ReviewVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 评价控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "商品评价", description = "商品评价相关接口")
@RestController
@RequestMapping("/api/user/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "商品评价列表")
    @GetMapping("/product/{productId}")
    public Result<Page<ReviewVO>> productReviews(@PathVariable("productId") Long productId, PageQuery query) {
        Page<ReviewVO> page = reviewService.getProductReviews(productId, query);
        return Result.success(page);
    }

    @Operation(summary = "发表评价")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ReviewDTO dto) {
        Long userId = UserContextHolder.getUserId();
        reviewService.addReview(userId, dto);
        return Result.success();
    }
}
