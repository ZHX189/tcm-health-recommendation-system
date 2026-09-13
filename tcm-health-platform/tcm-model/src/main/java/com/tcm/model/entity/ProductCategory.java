package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_category")
public class ProductCategory extends BaseEntity {

    /**
     * 父分类ID，0表示一级分类
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}
