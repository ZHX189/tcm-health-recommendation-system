package com.tcm.api.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.result.Result;
import com.tcm.model.entity.Announcement;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公告控制器
 *
 * @author Ti
 * @since 2026-02-03
 */
@Tag(name = "公告", description = "公告相关接口")
@RestController
@RequestMapping("/api/user/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "公告列表")
    @GetMapping
    public Result<Page<Announcement>> list(PageQuery query) {
        Page<Announcement> page = announcementService.pageAnnouncements(query);
        return Result.success(page);
    }

    @Operation(summary = "公告详情")
    @GetMapping("/{id}")
    public Result<Announcement> detail(@PathVariable("id") Long id) {
        Announcement announcement = announcementService.getAnnouncementDetail(id);
        return Result.success(announcement);
    }
}
