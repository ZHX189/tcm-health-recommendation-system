package com.tcm.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 健康档案查询
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "健康档案查询")
public class HealthRecordQuery extends PageQuery {

    @Schema(description = "关键词搜索（姓名）")
    private String keyword;

    @Schema(description = "体质类型")
    private String constitutionType;

    @Schema(description = "用户ID")
    private Long userId;
}
