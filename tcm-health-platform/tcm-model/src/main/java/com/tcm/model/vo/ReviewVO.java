package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价VO
 *
 * @author Ti
 * @since 2026-02-06
 */
@Data
@Schema(description = "评价信息")
public class ReviewVO {

    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单明细ID")
    private Long orderItemId;

    @Schema(description = "评分：1-5星")
    private Integer rating;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价图片列表")
    private List<String> images;

    @Schema(description = "状态：0-待审核，1-已通过，2-已驳回")
    private Integer status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "是否匿名：0-否，1-是")
    private Integer isAnonymous;

    @Schema(description = "商家回复")
    private String replyContent;

    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
