package com.tcm.service.content.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.AnnouncementMapper;
import com.tcm.model.dto.AnnouncementDTO;
import com.tcm.model.entity.Announcement;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 公告服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public Page<Announcement> pageAnnouncements(PageQuery query) {
        Page<Announcement> page = new Page<>(query.getPageNum(), query.getPageSize());
        LocalDateTime now = LocalDateTime.now();
        return page(page, new LambdaQueryWrapper<Announcement>()
                .eq(Announcement::getStatus, 1)
                .and(w -> w.isNull(Announcement::getStartTime).or().le(Announcement::getStartTime, now))
                .and(w -> w.isNull(Announcement::getEndTime).or().ge(Announcement::getEndTime, now))
                .orderByDesc(Announcement::getIsTop)
                .orderByDesc(Announcement::getPublishTime));
    }

    @Override
    public Page<Announcement> pageAnnouncementsForAdmin(PageQuery query) {
        Page<Announcement> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 管理端查询所有公告，不过滤状态
        return page(page, new LambdaQueryWrapper<Announcement>()
                .orderByDesc(Announcement::getCreateTime));
    }

    @Override
    public Announcement getAnnouncementDetail(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        return announcement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAnnouncement(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        BeanUtil.copyProperties(dto, announcement);
        announcement.setStatus(0);
        announcement.setDeleted(0);
        save(announcement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAnnouncement(Long id, AnnouncementDTO dto) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        BeanUtil.copyProperties(dto, announcement, "id", "status", "createTime", "deleted");
        updateById(announcement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAnnouncement(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        announcement.setStatus(status);
        if (status == 1) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        updateById(announcement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAnnouncementAsDraft(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        BeanUtil.copyProperties(dto, announcement);
        announcement.setStatus(0); // 草稿/待审核
        announcement.setDeleted(0);
        save(announcement);
    }
}
