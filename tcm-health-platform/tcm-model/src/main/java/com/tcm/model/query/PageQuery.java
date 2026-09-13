package com.tcm.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询基类
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "分页查询")
public class PageQuery {

    @Schema(description = "当前页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;

    /**
     * 获取偏移量
     */
    public long getOffset() {
        return (long) (pageNum - 1) * pageSize;
    }
}
