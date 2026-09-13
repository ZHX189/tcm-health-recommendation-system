package com.tcm.service.health;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.HealthRecordDTO;
import com.tcm.model.entity.UserHealthRecord;
import com.tcm.model.query.HealthRecordQuery;

/**
 * 健康档案服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface HealthRecordService extends IService<UserHealthRecord> {

    /**
     * 获取用户健康档案
     */
    UserHealthRecord getByUserId(Long userId);

    /**
     * 创建或更新健康档案
     */
    void saveOrUpdate(Long userId, HealthRecordDTO dto);

    /**
     * 分页查询健康档案
     */
    Page<UserHealthRecord> pageRecords(HealthRecordQuery query);

    /**
     * 删除健康档案
     */
    void deleteRecord(Long id);
}
