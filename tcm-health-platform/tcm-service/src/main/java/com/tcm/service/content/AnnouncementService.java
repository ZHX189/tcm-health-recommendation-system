package com.tcm.service.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.AnnouncementDTO;
import com.tcm.model.entity.Announcement;
import com.tcm.model.query.PageQuery;

/**
 * 公告服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 用户端公告分页查询（只查已发布且在有效期内的）
     */
    Page<Announcement> pageAnnouncements(PageQuery query);

    /**
     * 管理端公告分页查询（查询所有状态）
     */
    Page<Announcement> pageAnnouncementsForAdmin(PageQuery query);

    Announcement getAnnouncementDetail(Long id);

    void addAnnouncement(AnnouncementDTO dto);

    void updateAnnouncement(Long id, AnnouncementDTO dto);

    void deleteAnnouncement(Long id);

    void updateStatus(Long id, Integer status);

    /**
     * 员工提交公告草稿（待审核状态）
     */
    void addAnnouncementAsDraft(AnnouncementDTO dto);
}
