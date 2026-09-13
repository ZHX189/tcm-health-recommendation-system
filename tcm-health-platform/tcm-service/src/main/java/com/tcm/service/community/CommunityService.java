package com.tcm.service.community;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.model.dto.PostDTO;
import com.tcm.model.entity.CommunityPost;
import com.tcm.model.query.PageQuery;

import java.util.List;
import java.util.Map;

/**
 * 社区服务接口
 *
 * @author Ti
 * @since 2026-02-06
 */
public interface CommunityService {

    /**
     * 发布帖子
     */
    void createPost(Long userId, PostDTO dto);

    /**
     * 帖子列表（用户端，只查已发布的）
     */
    Page<Map<String, Object>> pagePosts(PageQuery query, String keyword);

    /**
     * 帖子详情
     */
    Map<String, Object> getPostDetail(Long id, Long userId);

    /**
     * 点赞/取消点赞
     */
    void toggleLike(Long userId, Long postId);

    /**
     * 判断是否已点赞
     */
    boolean isLiked(Long userId, Long postId);

    /**
     * 发表评论
     */
    void addComment(Long userId, Long postId, Long parentId, Long replyUserId, String content);

    /**
     * 帖子评论列表
     */
    List<Map<String, Object>> getComments(Long postId);

    /**
     * 删除自己的帖子
     */
    void deletePost(Long userId, Long postId);

    /**
     * 管理端帖子列表（所有状态）
     */
    Page<Map<String, Object>> pagePostsForAdmin(PageQuery query, Integer status, String keyword);

    /**
     * 审核帖子
     */
    void auditPost(Long id, Integer status);

    /**
     * 删除帖子（管理端）
     */
    void deletePostByAdmin(Long id);

    /**
     * 管理端评论列表
     */
    Page<Map<String, Object>> pageCommentsForAdmin(PageQuery query, Integer status);

    /**
     * 审核评论
     */
    void auditComment(Long id, Integer status);

    /**
     * 删除评论（管理端）
     */
    void deleteCommentByAdmin(Long id);
}
