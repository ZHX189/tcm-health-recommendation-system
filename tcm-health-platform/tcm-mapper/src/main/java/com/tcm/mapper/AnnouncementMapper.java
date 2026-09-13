package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
