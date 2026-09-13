package com.tcm.api.staff.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.PageQuery;
import com.tcm.model.query.ReviewQuery;
import com.tcm.model.vo.ReviewVO;
import com.tcm.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 员工评价查看控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "评价查看", description = "员工端评价查看接口")
@RestController
@RequestMapping("/api/staff/reviews")
@RequiredArgsConstructor
public class StaffReviewController {

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
}
