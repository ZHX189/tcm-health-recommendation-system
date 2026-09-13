package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.HealthArticle;
import org.apache.ibatis.annotations.Mapper;

/**
 * 养生文章Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface HealthArticleMapper extends BaseMapper<HealthArticle> {
}
