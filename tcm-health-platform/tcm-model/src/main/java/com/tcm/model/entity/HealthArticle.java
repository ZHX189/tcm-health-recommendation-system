package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 养生文章实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_article")
public class HealthArticle extends BaseEntity {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容（富文本）
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
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 状态：0-草稿，1-已发布，2-待审核
     */
    private Integer status;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
