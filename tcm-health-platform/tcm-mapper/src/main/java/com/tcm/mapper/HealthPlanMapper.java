package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.HealthPlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 养生方案Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface HealthPlanMapper extends BaseMapper<HealthPlan> {
}
