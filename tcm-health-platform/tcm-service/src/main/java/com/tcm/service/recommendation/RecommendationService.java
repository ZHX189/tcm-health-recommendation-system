package com.tcm.service.recommendation;

import com.tcm.model.vo.RecommendationVO;

/**
 * 个性化推荐服务接口
 *
 * @author Ti
 * @since 2026-03-27
 */
public interface RecommendationService {

    /**
     * 获取首页个性化推荐数据
     *
     * @param userId 用户ID
     * @return 综合推荐结果
     */
    RecommendationVO getHomeRecommendations(Long userId);
}
