package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分类VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "分类信息")
public class CategoryVO {

    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类图标")
    private String icon;

    @Schema(description = "排序值")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "子分类列表")
    private List<CategoryVO> children;
}
