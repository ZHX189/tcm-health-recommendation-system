package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 主图URL
     */
    private String mainImage;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情（富文本）
     */
    private String detail;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String spec;

    /**
     * 产地
     */
    private String origin;

    /**
     * 功效说明
     */
    private String efficacy;

    /**
     * 使用方法
     */
    private String usageMethod;

    /**
     * 储存方法
     */
    private String storageMethod;

    /**
     * 状态：0-下架，1-上架，2-待审核
     */
    private Integer status;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 是否推荐：0-否，1-是
     */
    private Integer isRecommend;
}
