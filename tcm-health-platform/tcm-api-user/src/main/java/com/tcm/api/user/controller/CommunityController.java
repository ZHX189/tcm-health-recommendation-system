package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.dto.PostDTO;
import com.tcm.model.query.PageQuery;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 社区控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "社区", description = "社区帖子相关接口")
@RestController
@RequestMapping("/api/user/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "帖子列表")
    @GetMapping("/posts")
    public Result<Page<Map<String, Object>>> list(PageQuery query,
                                                   @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Map<String, Object>> page = communityService.pagePosts(query, keyword);
        return Result.success(page);
    }

    @Operation(summary = "帖子详情")
    @GetMapping("/posts/{id}")
    public Result<Map<String, Object>> detail(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        Map<String, Object> detail = communityService.getPostDetail(id, userId);
        return Result.success(detail);
    }

    @Operation(summary = "发布帖子")
    @PostMapping("/posts")
    public Result<Void> create(@Valid @RequestBody PostDTO dto) {
        Long userId = UserContextHolder.getUserId();
        communityService.createPost(userId, dto);
        return Result.success();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/posts/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        communityService.deletePost(userId, id);
        return Result.success();
    }

    @Operation(summary = "点赞/取消点赞")
    @PostMapping("/posts/{id}/like")
    public Result<Void> toggleLike(@PathVariable("id") Long id) {
        Long userId = UserContextHolder.getUserId();
        communityService.toggleLike(userId, id);
        return Result.success();
    }

    @Operation(summary = "帖子评论列表")
    @GetMapping("/posts/{id}/comments")
    public Result<List<Map<String, Object>>> comments(@PathVariable("id") Long id) {
        List<Map<String, Object>> list = communityService.getComments(id);
        return Result.success(list);
    }

    @Operation(summary = "发表评论")
    @PostMapping("/posts/{id}/comments")
    public Result<Void> addComment(@PathVariable("id") Long id,
                                   @RequestParam("content") String content,
                                   @RequestParam(value = "parentId", required = false) Long parentId,
                                   @RequestParam(value = "replyUserId", required = false) Long replyUserId) {
        Long userId = UserContextHolder.getUserId();
        communityService.addComment(userId, id, parentId, replyUserId, content);
        return Result.success();
    }
}
