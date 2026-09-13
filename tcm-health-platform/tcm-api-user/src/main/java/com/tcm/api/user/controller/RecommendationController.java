package com.tcm.api.user.controller;

import com.tcm.common.result.Result;
import com.tcm.model.vo.RecommendationVO;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.recommendation.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个性化推荐控制器
 *
 * @author Ti
 * @since 2026-03-27
 */
@Tag(name = "个性化推荐", description = "基于体质、行为、季节的个性化推荐接口")
@RestController
@RequestMapping("/api/user/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "获取首页个性化推荐")
    @GetMapping("/home")
    public Result<RecommendationVO> getHomeRecommendations() {
        Long userId = UserContextHolder.getUserId();
        RecommendationVO vo = recommendationService.getHomeRecommendations(userId);
        return Result.success(vo);
    }
}
