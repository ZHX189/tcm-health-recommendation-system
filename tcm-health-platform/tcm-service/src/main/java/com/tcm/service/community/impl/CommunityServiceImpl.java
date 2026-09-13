package com.tcm.service.community.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.CommunityPostMapper;
import com.tcm.mapper.PostCommentMapper;
import com.tcm.mapper.SysUserMapper;
import com.tcm.model.dto.PostDTO;
import com.tcm.model.entity.CommunityPost;
import com.tcm.model.entity.PostComment;
import com.tcm.model.entity.SysUser;
import com.tcm.model.query.PageQuery;
import com.tcm.service.community.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 社区服务实现
 *
 * @author Ti
 * @since 2026-02-06
 */
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityPostMapper postMapper;
    private final PostCommentMapper commentMapper;
    private final SysUserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createPost(Long userId, PostDTO dto) {
        CommunityPost post = new CommunityPost();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            post.setImages("[\"" + String.join("\",\"", dto.getImages()) + "\"]");
        }
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setShareCount(0);
        post.setStatus(0); // 待审核
        post.setIsTop(0);
        post.setIsEssence(0);
        postMapper.insert(post);
    }

    @Override
    public Page<Map<String, Object>> pagePosts(PageQuery query, String keyword) {
        Page<CommunityPost> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<CommunityPost>()
                .eq(CommunityPost::getStatus, 1) // 只查已发布
                .like(StringUtils.hasText(keyword), CommunityPost::getTitle, keyword)
                .orderByDesc(CommunityPost::getIsTop)
                .orderByDesc(CommunityPost::getCreateTime);
        Page<CommunityPost> postPage = postMapper.selectPage(page, wrapper);
        return convertPostPage(postPage);
    }

    @Override
    public Map<String, Object> getPostDetail(Long id, Long userId) {
        CommunityPost post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        // 增加浏览量
        postMapper.update(null, new LambdaUpdateWrapper<CommunityPost>()
                .eq(CommunityPost::getId, id)
                .setSql("view_count = view_count + 1"));

        Map<String, Object> result = postToMap(post);
        // 查询是否已点赞
        if (userId != null) {
            result.put("isLiked", isLiked(userId, id));
        }
        return result;
    }

    @Override
    @Transactional
    public void toggleLike(Long userId, Long postId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_like WHERE post_id = ? AND user_id = ?",
                Integer.class, postId, userId);
        if (count != null && count > 0) {
            jdbcTemplate.update("DELETE FROM post_like WHERE post_id = ? AND user_id = ?", postId, userId);
            postMapper.update(null, new LambdaUpdateWrapper<CommunityPost>()
                    .eq(CommunityPost::getId, postId)
                    .setSql("like_count = like_count - 1"));
        } else {
            jdbcTemplate.update("INSERT INTO post_like (post_id, user_id) VALUES (?, ?)", postId, userId);
            postMapper.update(null, new LambdaUpdateWrapper<CommunityPost>()
                    .eq(CommunityPost::getId, postId)
                    .setSql("like_count = like_count + 1"));
        }
    }

    @Override
    public boolean isLiked(Long userId, Long postId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_like WHERE post_id = ? AND user_id = ?",
                Integer.class, postId, userId);
        return count != null && count > 0;
    }

    @Override
    @Transactional
    public void addComment(Long userId, Long postId, Long parentId, Long replyUserId, String content) {
        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId != null ? parentId : 0L);
        comment.setReplyUserId(replyUserId);
        comment.setContent(content);
        comment.setLikeCount(0);
        comment.setStatus(0); // 待审核
        commentMapper.insert(comment);
        // 更新帖子评论数
        postMapper.update(null, new LambdaUpdateWrapper<CommunityPost>()
                .eq(CommunityPost::getId, postId)
                .setSql("comment_count = comment_count + 1"));
    }

    @Override
    public List<Map<String, Object>> getComments(Long postId) {
        List<PostComment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<PostComment>()
                        .eq(PostComment::getPostId, postId)
                        .eq(PostComment::getStatus, 1)
                        .orderByAsc(PostComment::getCreateTime));
        Set<Long> userIds = comments.stream().map(PostComment::getUserId).collect(Collectors.toSet());
        comments.stream().filter(c -> c.getReplyUserId() != null).forEach(c -> userIds.add(c.getReplyUserId()));
        Map<Long, SysUser> userMap = getUserMap(userIds);

        return comments.stream().map(c -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", c.getId());
            map.put("postId", c.getPostId());
            map.put("userId", c.getUserId());
            map.put("parentId", c.getParentId());
            map.put("replyUserId", c.getReplyUserId());
            map.put("content", c.getContent());
            map.put("likeCount", c.getLikeCount());
            map.put("createTime", c.getCreateTime());
            SysUser user = userMap.get(c.getUserId());
            map.put("nickname", user != null ? user.getNickname() : "未知用户");
            map.put("avatar", user != null ? user.getAvatar() : null);
            if (c.getReplyUserId() != null && c.getReplyUserId() > 0) {
                SysUser replyUser = userMap.get(c.getReplyUserId());
                map.put("replyNickname", replyUser != null ? replyUser.getNickname() : "未知用户");
            }
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        CommunityPost post = postMapper.selectById(postId);
        if (post == null || !post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该帖子");
        }
        postMapper.deleteById(postId);
    }

    @Override
    public Page<Map<String, Object>> pagePostsForAdmin(PageQuery query, Integer status, String keyword) {
        Page<CommunityPost> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<CommunityPost>()
                .eq(status != null, CommunityPost::getStatus, status)
                .like(StringUtils.hasText(keyword), CommunityPost::getTitle, keyword)
                .orderByDesc(CommunityPost::getCreateTime);
        Page<CommunityPost> postPage = postMapper.selectPage(page, wrapper);
        return convertPostPage(postPage);
    }

    @Override
    public void auditPost(Long id, Integer status) {
        postMapper.update(null, new LambdaUpdateWrapper<CommunityPost>()
                .eq(CommunityPost::getId, id)
                .set(CommunityPost::getStatus, status));
    }

    @Override
    public void deletePostByAdmin(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public Page<Map<String, Object>> pageCommentsForAdmin(PageQuery query, Integer status) {
        Page<PostComment> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<PostComment> wrapper = new LambdaQueryWrapper<PostComment>()
                .eq(status != null, PostComment::getStatus, status)
                .orderByDesc(PostComment::getCreateTime);
        Page<PostComment> commentPage = commentMapper.selectPage(page, wrapper);

        Set<Long> userIds = commentPage.getRecords().stream().map(PostComment::getUserId).collect(Collectors.toSet());
        Map<Long, SysUser> userMap = getUserMap(userIds);

        // 批量查询评论关联的帖子标题
        Set<Long> postIds = commentPage.getRecords().stream().map(PostComment::getPostId).collect(Collectors.toSet());
        Map<Long, String> postTitleMap = new HashMap<>();
        if (!postIds.isEmpty()) {
            List<CommunityPost> posts = postMapper.selectBatchIds(postIds);
            posts.forEach(p -> postTitleMap.put(p.getId(), p.getTitle()));
        }

        Page<Map<String, Object>> result = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        result.setRecords(commentPage.getRecords().stream().map(c -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", c.getId());
            map.put("postId", c.getPostId());
            map.put("postTitle", postTitleMap.getOrDefault(c.getPostId(), "未知帖子"));
            map.put("userId", c.getUserId());
            map.put("content", c.getContent());
            map.put("status", c.getStatus());
            map.put("createTime", c.getCreateTime());
            SysUser user = userMap.get(c.getUserId());
            map.put("nickname", user != null ? user.getNickname() : "未知用户");
            return map;
        }).collect(Collectors.toList()));
        return result;
    }

    @Override
    public void auditComment(Long id, Integer status) {
        commentMapper.update(null, new LambdaUpdateWrapper<PostComment>()
                .eq(PostComment::getId, id)
                .set(PostComment::getStatus, status));
    }

    @Override
    public void deleteCommentByAdmin(Long id) {
        commentMapper.deleteById(id);
    }

    // ==================== 私有方法 ====================

    private Page<Map<String, Object>> convertPostPage(Page<CommunityPost> postPage) {
        Set<Long> userIds = postPage.getRecords().stream().map(CommunityPost::getUserId).collect(Collectors.toSet());
        Map<Long, SysUser> userMap = getUserMap(userIds);

        Page<Map<String, Object>> result = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        result.setRecords(postPage.getRecords().stream().map(p -> {
            Map<String, Object> map = postToMap(p);
            SysUser user = userMap.get(p.getUserId());
            map.put("nickname", user != null ? user.getNickname() : "未知用户");
            map.put("avatar", user != null ? user.getAvatar() : null);
            return map;
        }).collect(Collectors.toList()));
        return result;
    }

    private Map<String, Object> postToMap(CommunityPost p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", p.getId());
        map.put("userId", p.getUserId());
        map.put("title", p.getTitle());
        map.put("content", p.getContent());
        map.put("images", parseImages(p.getImages()));
        map.put("viewCount", p.getViewCount());
        map.put("likeCount", p.getLikeCount());
        map.put("commentCount", p.getCommentCount());
        map.put("shareCount", p.getShareCount());
        map.put("status", p.getStatus());
        map.put("isTop", p.getIsTop());
        map.put("isEssence", p.getIsEssence());
        map.put("createTime", p.getCreateTime());
        // 查用户信息
        SysUser user = userMapper.selectById(p.getUserId());
        map.put("nickname", user != null ? user.getNickname() : "未知用户");
        map.put("avatar", user != null ? user.getAvatar() : null);
        return map;
    }

    private Map<Long, SysUser> getUserMap(Set<Long> userIds) {
        if (userIds.isEmpty()) return Collections.emptyMap();
        List<SysUser> users = userMapper.selectBatchIds(userIds);
        return users.stream().collect(Collectors.toMap(SysUser::getId, u -> u, (a, b) -> a));
    }

    private List<String> parseImages(String images) {
        if (images == null || images.isEmpty()) return Collections.emptyList();
        // 处理JSON数组格式 ["url1","url2"] 或逗号分隔格式
        String cleaned = images.replaceAll("[\\[\\]\"\\s]", "");
        if (cleaned.isEmpty()) return Collections.emptyList();
        return Arrays.asList(cleaned.split(","));
    }
}
