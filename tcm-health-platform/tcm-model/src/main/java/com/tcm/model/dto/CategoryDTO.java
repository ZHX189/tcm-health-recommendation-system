package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "分类请求")
public class CategoryDTO {

    @Schema(description = "父分类ID，0表示一级分类")
    private Long parentId;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称最多100个字符")
    private String name;

    @Schema(description = "分类图标")
    private String icon;

    @Schema(description = "排序值")
    private Integer sort;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
}
