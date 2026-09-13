package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.UserHealthRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 健康档案Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface UserHealthRecordMapper extends BaseMapper<UserHealthRecord> {
}
