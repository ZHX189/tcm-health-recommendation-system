package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "商品信息")
public class ProductVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "销售价格")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "主图URL")
    private String mainImage;

    @Schema(description = "副标题")
    private String subTitle;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "商品详情")
    private String detail;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "产地")
    private String origin;

    @Schema(description = "功效说明")
    private String efficacy;

    @Schema(description = "使用方法")
    private String usageMethod;

    @Schema(description = "储存方法")
    private String storageMethod;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "销量")
    private Integer sales;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "是否推荐")
    private Integer isRecommend;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "商品图片列表")
    private List<String> images;

    @Schema(description = "是否收藏")
    private Boolean collected;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
