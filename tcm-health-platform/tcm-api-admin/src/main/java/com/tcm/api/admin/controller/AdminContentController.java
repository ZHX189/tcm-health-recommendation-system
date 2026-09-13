package com.tcm.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.*;
import com.tcm.model.entity.*;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 内容管理控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "内容管理", description = "管理员-内容管理相关接口")
@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final ArticleService articleService;
    private final HealthPlanService healthPlanService;
    private final NewsService newsService;
    private final AnnouncementService announcementService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    // ==================== 养生文章管理 ====================

    @Operation(summary = "文章列表")
    @GetMapping("/articles")
    public Result<Page<HealthArticle>> articleList(PageQuery query,
                                                   @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                   @RequestParam(value = "keyword", required = false) String keyword) {
        Page<HealthArticle> page = articleService.pageArticlesForAdmin(query, categoryId, keyword);
        return Result.success(page);
    }

    @Operation(summary = "新增文章")
    @PostMapping("/articles")
    public Result<Void> addArticle(@Valid @RequestBody ArticleDTO dto) {
        articleService.addArticle(dto);
        return Result.success();
    }

    @Operation(summary = "修改文章")
    @PutMapping("/articles/{id}")
    public Result<Void> updateArticle(@PathVariable("id") Long id, @Valid @RequestBody ArticleDTO dto) {
        articleService.updateArticle(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/articles/{id}")
    public Result<Void> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @Operation(summary = "发布/下架文章")
    @PatchMapping("/articles/{id}/status")
    public Result<Void> updateArticleStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        articleService.updateStatus(id, status);
        return Result.success();
    }

    // ==================== 养生方案管理 ====================

    @Operation(summary = "养生方案列表")
    @GetMapping("/health-plans")
    public Result<Page<HealthPlan>> planList(PageQuery query,
                                             @RequestParam(value = "keyword", required = false) String keyword,
                                             @RequestParam(value = "constitutionType", required = false) String constitutionType,
                                             @RequestParam(value = "season", required = false) String season) {
        Page<HealthPlan> page = healthPlanService.pagePlansForAdmin(query, keyword, constitutionType, season);
        return Result.success(page);
    }

    @Operation(summary = "新增养生方案")
    @PostMapping("/health-plans")
    public Result<Void> addPlan(@Valid @RequestBody HealthPlanDTO dto) {
        healthPlanService.addPlan(dto);
        return Result.success();
    }

    @Operation(summary = "修改养生方案")
    @PutMapping("/health-plans/{id}")
    public Result<Void> updatePlan(@PathVariable("id") Long id, @Valid @RequestBody HealthPlanDTO dto) {
        healthPlanService.updatePlan(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除养生方案")
    @DeleteMapping("/health-plans/{id}")
    public Result<Void> deletePlan(@PathVariable("id") Long id) {
        healthPlanService.deletePlan(id);
        return Result.success();
    }

    @Operation(summary = "发布/下架养生方案")
    @PatchMapping("/health-plans/{id}/status")
    public Result<Void> updatePlanStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        healthPlanService.updateStatus(id, status);
        return Result.success();
    }

    // ==================== 资讯管理 ====================

    @Operation(summary = "资讯列表")
    @GetMapping("/news")
    public Result<Page<News>> newsList(PageQuery query, @RequestParam(value = "keyword", required = false) String keyword) {
        Page<News> page = newsService.pageNewsForAdmin(query, keyword);
        return Result.success(page);
    }

    @Operation(summary = "新增资讯")
    @PostMapping("/news")
    public Result<Void> addNews(@Valid @RequestBody NewsDTO dto) {
        newsService.addNews(dto);
        return Result.success();
    }

    @Operation(summary = "修改资讯")
    @PutMapping("/news/{id}")
    public Result<Void> updateNews(@PathVariable("id") Long id, @Valid @RequestBody NewsDTO dto) {
        newsService.updateNews(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除资讯")
    @DeleteMapping("/news/{id}")
    public Result<Void> deleteNews(@PathVariable("id") Long id) {
        newsService.deleteNews(id);
        return Result.success();
    }

    @Operation(summary = "发布/下架资讯")
    @PatchMapping("/news/{id}/status")
    public Result<Void> updateNewsStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        newsService.updateStatus(id, status);
        return Result.success();
    }

    // ==================== 公告管理 ====================

    @Operation(summary = "公告列表")
    @GetMapping("/announcements")
    public Result<Page<Announcement>> announcementList(PageQuery query) {
        Page<Announcement> page = announcementService.pageAnnouncementsForAdmin(query);
        return Result.success(page);
    }

    @Operation(summary = "新增公告")
    @PostMapping("/announcements")
    public Result<Void> addAnnouncement(@Valid @RequestBody AnnouncementDTO dto) {
        announcementService.addAnnouncement(dto);
        return Result.success();
    }

    @Operation(summary = "修改公告")
    @PutMapping("/announcements/{id}")
    public Result<Void> updateAnnouncement(@PathVariable("id") Long id, @Valid @RequestBody AnnouncementDTO dto) {
        announcementService.updateAnnouncement(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/announcements/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.success();
    }

    @Operation(summary = "发布/下架公告")
    @PatchMapping("/announcements/{id}/status")
    public Result<Void> updateAnnouncementStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        announcementService.updateStatus(id, status);
        return Result.success();
    }

    // ==================== 图片上传 ====================

    @Operation(summary = "上传图片")
    @PostMapping("/images")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = FileUploadUtil.uploadImage(file, uploadPath);
        return Result.success(imageUrl);
    }
}
