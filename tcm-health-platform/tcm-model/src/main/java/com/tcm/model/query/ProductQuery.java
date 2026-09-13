package com.tcm.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品查询
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品查询")
public class ProductQuery extends PageQuery {

    @Schema(description = "关键词")
    private String keyword;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "最低价格")
    private BigDecimal minPrice;

    @Schema(description = "最高价格")
    private BigDecimal maxPrice;

    @Schema(description = "是否推荐")
    private Integer isRecommend;

    @Schema(description = "排序字段：sales-销量，price-价格，createTime-时间")
    private String sortField;

    @Schema(description = "排序方式：asc-升序，desc-降序")
    private String sortOrder;
}
