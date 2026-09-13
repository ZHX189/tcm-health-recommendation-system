package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.PostComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子评论Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface PostCommentMapper extends BaseMapper<PostComment> {
}
