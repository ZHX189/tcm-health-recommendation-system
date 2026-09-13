package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.ReviewQuery;
import com.tcm.model.vo.ReviewVO;
import com.tcm.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端评价管理控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "评价管理", description = "管理员端评价管理接口")
@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "评价列表")
    @GetMapping
    public Result<Page<ReviewVO>> list(ReviewQuery query) {
        Page<ReviewVO> page = reviewService.getReviewList(query);
        return Result.success(page);
    }

    @Operation(summary = "评价详情")
    @GetMapping("/{id}")
    public Result<ReviewVO> detail(@PathVariable("id") Long id) {
        ReviewVO vo = reviewService.getReviewDetail(id);
        return Result.success(vo);
    }

    @Operation(summary = "审核评价")
    @PatchMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        reviewService.auditReview(id, status);
        return Result.success();
    }

    @Operation(summary = "回复评价")
    @PostMapping("/{id}/reply")
    public Result<Void> reply(@PathVariable("id") Long id, @RequestParam("replyContent") String replyContent) {
        reviewService.replyReview(id, replyContent);
        return Result.success();
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return Result.success();
    }
}
