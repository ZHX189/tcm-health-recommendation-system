package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子评论实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("post_comment")
public class PostComment extends BaseEntity {

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 父评论ID，0表示一级评论
     */
    private Long parentId;

    /**
     * 回复的用户ID
     */
    private Long replyUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态：0-待审核，1-已通过，2-已驳回
     */
    private Integer status;
}
