package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.entity.HealthArticle;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 养生文章控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "养生文章", description = "养生文章相关接口")
@RestController
@RequestMapping("/api/user/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "文章列表")
    @GetMapping
    public Result<Page<HealthArticle>> list(PageQuery query,
                                            @RequestParam(value = "categoryId", required = false) Long categoryId,
                                            @RequestParam(value = "keyword", required = false) String keyword) {
        Page<HealthArticle> page = articleService.pageArticles(query, categoryId, keyword);
        return Result.success(page);
    }

    @Operation(summary = "文章详情")
    @GetMapping("/{id}")
    public Result<HealthArticle> detail(@PathVariable("id") Long id) {
        HealthArticle article = articleService.getArticleDetail(id);
        return Result.success(article);
    }
}
