package com.tcm.service.review;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.ReviewDTO;
import com.tcm.model.entity.ProductReview;
import com.tcm.model.query.PageQuery;
import com.tcm.model.query.ReviewQuery;
import com.tcm.model.vo.ReviewVO;

/**
 * 评价服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface ReviewService extends IService<ProductReview> {

    /**
     * 发表评价
     */
    void addReview(Long userId, ReviewDTO dto);

    /**
     * 商品评价列表（用户端）
     */
    Page<ReviewVO> getProductReviews(Long productId, PageQuery query);

    /**
     * 评价列表（管理端）
     */
    Page<ReviewVO> getReviewList(ReviewQuery query);

    /**
     * 评价详情
     */
    ReviewVO getReviewDetail(Long id);

    /**
     * 审核评价
     */
    void auditReview(Long id, Integer status);

    /**
     * 回复评价
     */
    void replyReview(Long id, String replyContent);

    /**
     * 删除评价
     */
    void deleteReview(Long id);
}
