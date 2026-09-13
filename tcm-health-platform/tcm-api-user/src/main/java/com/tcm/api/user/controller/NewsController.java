package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.entity.News;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 资讯控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "资讯", description = "资讯相关接口")
@RestController
@RequestMapping("/api/user/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "资讯列表")
    @GetMapping
    public Result<Page<News>> list(PageQuery query, @RequestParam(value = "category", required = false) String category) {
        Page<News> page = newsService.pageNews(query, category);
        return Result.success(page);
    }

    @Operation(summary = "资讯详情")
    @GetMapping("/{id}")
    public Result<News> detail(@PathVariable("id") Long id) {
        News news = newsService.getNewsDetail(id);
        return Result.success(news);
    }
}
