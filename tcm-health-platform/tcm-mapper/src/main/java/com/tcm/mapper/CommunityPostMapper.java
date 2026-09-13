package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.CommunityPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 社区帖子Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface CommunityPostMapper extends BaseMapper<CommunityPost> {
}
