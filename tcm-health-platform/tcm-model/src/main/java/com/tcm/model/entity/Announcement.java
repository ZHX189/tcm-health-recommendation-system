package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公告实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("announcement")
public class Announcement extends BaseEntity {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告类型：1-系统公告，2-活动公告，3-其他
     */
    private Integer type;

    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 生效开始时间
     */
    private LocalDateTime startTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime endTime;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
