package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.News;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资讯Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
