package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品评价实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_review")
public class ProductReview extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long orderItemId;

    /**
     * 评分：1-5星
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 状态：0-待审核，1-已通过，2-已驳回
     */
    private Integer status;

    /**
     * 是否匿名：0-否，1-是
     */
    private Integer isAnonymous;

    /**
     * 商家回复
     */
    private String replyContent;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;
}
