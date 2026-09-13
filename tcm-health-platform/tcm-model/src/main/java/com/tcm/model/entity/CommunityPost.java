package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社区帖子实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("community_post")
public class CommunityPost extends BaseEntity {

    /**
     * 发布用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 图片URLs（JSON数组）
     */
    private String images;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 分享数
     */
    private Integer shareCount;

    /**
     * 状态：0-待审核，1-已发布，2-已驳回，3-已删除
     */
    private Integer status;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否精华
     */
    private Integer isEssence;
}
