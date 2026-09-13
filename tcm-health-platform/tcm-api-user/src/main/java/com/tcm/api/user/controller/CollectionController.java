package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.query.PageQuery;
import com.tcm.security.context.UserContextHolder;
import com.tcm.service.collection.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "收藏", description = "收藏管理相关接口")
@RestController
@RequestMapping("/api/user/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @Operation(summary = "收藏列表")
    @GetMapping
    public Result<Page<?>> list(@RequestParam(value = "type", required = false) Integer type, PageQuery query) {
        Long userId = UserContextHolder.getUserId();
        Page<?> page = collectionService.getCollectionList(userId, type, query);
        return Result.success(page);
    }

    @Operation(summary = "添加收藏")
    @PostMapping
    public Result<Void> add(@RequestParam("targetId") Long targetId, @RequestParam("targetType") Integer targetType) {
        Long userId = UserContextHolder.getUserId();
        collectionService.addCollection(userId, targetId, targetType);
        return Result.success();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping
    public Result<Void> remove(@RequestParam("targetId") Long targetId, @RequestParam("targetType") Integer targetType) {
        Long userId = UserContextHolder.getUserId();
        collectionService.removeCollection(userId, targetId, targetType);
        return Result.success();
    }

    @Operation(summary = "判断是否已收藏")
    @GetMapping("/check")
    public Result<Boolean> check(@RequestParam("targetId") Long targetId, @RequestParam("targetType") Integer targetType) {
        Long userId = UserContextHolder.getUserId();
        boolean collected = collectionService.isCollected(userId, targetId, targetType);
        return Result.success(collected);
    }
}
