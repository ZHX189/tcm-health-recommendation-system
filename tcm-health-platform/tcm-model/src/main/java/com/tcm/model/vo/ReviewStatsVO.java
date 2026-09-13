package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 评价统计VO
 *
 * @author Ti
 * @since 2026-02-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评价统计")
public class ReviewStatsVO {

    @Schema(description = "总评价数")
    private Integer totalCount;

    @Schema(description = "平均评分")
    private Double avgRating;

    @Schema(description = "好评数(4-5星)")
    private Integer goodCount;

    @Schema(description = "中评数(3星)")
    private Integer midCount;

    @Schema(description = "差评数(1-2星)")
    private Integer badCount;

    @Schema(description = "好评率(%)")
    private Double goodRate;

    @Schema(description = "各星级分布")
    private List<StatusCountVO> ratingDist;

    @Schema(description = "近期评价趋势")
    private List<SalesTrendVO> trend;
}
