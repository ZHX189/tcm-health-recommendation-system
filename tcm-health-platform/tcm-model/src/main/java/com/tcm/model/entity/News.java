package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 资讯实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("news")
public class News extends BaseEntity {

    /**
     * 资讯标题
     */
    private String title;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 资讯分类
     */
    private String category;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容（富文本）
     */
    private String content;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 状态：0-草稿，1-已发布，2-待审核
     */
    private Integer status;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
