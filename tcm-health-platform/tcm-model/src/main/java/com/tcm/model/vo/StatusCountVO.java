package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态统计VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "状态统计")
public class StatusCountVO {

    @Schema(description = "状态值")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "数量")
    private Integer count;
}
