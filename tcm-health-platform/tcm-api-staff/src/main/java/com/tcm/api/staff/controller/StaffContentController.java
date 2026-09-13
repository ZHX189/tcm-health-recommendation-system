package com.tcm.api.staff.controller;

import com.tcm.common.result.Result;
import com.tcm.common.utils.FileUploadUtil;
import com.tcm.model.dto.AnnouncementDTO;
import com.tcm.model.dto.NewsDTO;
import com.tcm.service.content.AnnouncementService;
import com.tcm.service.content.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 员工内容协助控制器（草稿提交，待管理端审核）
 *
 * @author Ti
 * @since 2026-02-06
 */
@Tag(name = "内容协助", description = "员工端资讯/公告草稿提交接口")
@RestController
@RequestMapping("/api/staff/content")
@RequiredArgsConstructor
public class StaffContentController {

    private final NewsService newsService;
    private final AnnouncementService announcementService;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Operation(summary = "提交资讯草稿")
    @PostMapping("/news/draft")
    public Result<Void> submitNewsDraft(@Valid @RequestBody NewsDTO dto) {
        // 员工提交的资讯状态为待审核(2)
        newsService.addNewsAsDraft(dto);
        return Result.success();
    }

    @Operation(summary = "提交公告草稿")
    @PostMapping("/announcements/draft")
    public Result<Void> submitAnnouncementDraft(@Valid @RequestBody AnnouncementDTO dto) {
        announcementService.addAnnouncementAsDraft(dto);
        return Result.success();
    }

    @Operation(summary = "上传图片")
    @PostMapping("/images")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = FileUploadUtil.uploadImage(file, uploadPath);
        return Result.success(imageUrl);
    }
}
