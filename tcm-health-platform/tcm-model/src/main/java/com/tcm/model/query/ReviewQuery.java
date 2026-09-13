package com.tcm.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评价查询参数
 *
 * @author Ti
 * @since 2026-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "评价查询参数")
public class ReviewQuery extends PageQuery {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "状态：0-待审核，1-已通过，2-已驳回")
    private Integer status;

    @Schema(description = "评分")
    private Integer rating;

    @Schema(description = "关键词（商品名称/用户昵称/评价内容）")
    private String keyword;
}
