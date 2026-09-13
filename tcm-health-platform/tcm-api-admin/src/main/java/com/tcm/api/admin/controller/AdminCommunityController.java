package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.PageQuery;
import com.tcm.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端社区审核控制器
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "社区审核", description = "管理员-社区内容审核接口")
@RestController
@RequestMapping("/api/admin/community")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final CommunityService communityService;

    @Operation(summary = "帖子列表")
    @GetMapping("/posts")
    public Result<Page<Map<String, Object>>> postList(PageQuery query,
                                                       @RequestParam(value = "status", required = false) Integer status,
                                                       @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Map<String, Object>> page = communityService.pagePostsForAdmin(query, status, keyword);
        return Result.success(page);
    }

    @Operation(summary = "帖子详情")
    @GetMapping("/posts/{id}")
    public Result<Map<String, Object>> postDetail(@PathVariable("id") Long id) {
        Map<String, Object> detail = communityService.getPostDetail(id, null);
        return Result.success(detail);
    }

    @Operation(summary = "审核帖子")
    @PatchMapping("/posts/{id}/audit")
    public Result<Void> auditPost(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        communityService.auditPost(id, status);
        return Result.success();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/posts/{id}")
    public Result<Void> deletePost(@PathVariable("id") Long id) {
        communityService.deletePostByAdmin(id);
        return Result.success();
    }

    @Operation(summary = "评论列表")
    @GetMapping("/comments")
    public Result<Page<Map<String, Object>>> commentList(PageQuery query,
                                                          @RequestParam(value = "status", required = false) Integer status) {
        Page<Map<String, Object>> page = communityService.pageCommentsForAdmin(query, status);
        return Result.success(page);
    }

    @Operation(summary = "审核评论")
    @PatchMapping("/comments/{id}/audit")
    public Result<Void> auditComment(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        communityService.auditComment(id, status);
        return Result.success();
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable("id") Long id) {
        communityService.deleteCommentByAdmin(id);
        return Result.success();
    }
}
