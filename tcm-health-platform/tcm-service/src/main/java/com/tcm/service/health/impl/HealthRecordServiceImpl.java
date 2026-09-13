package com.tcm.service.health.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.mapper.UserHealthRecordMapper;
import com.tcm.model.dto.HealthRecordDTO;
import com.tcm.model.entity.UserHealthRecord;
import com.tcm.model.query.HealthRecordQuery;
import com.tcm.service.health.HealthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 健康档案服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class HealthRecordServiceImpl extends ServiceImpl<UserHealthRecordMapper, UserHealthRecord> implements HealthRecordService {

    @Override
    public UserHealthRecord getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<UserHealthRecord>().eq(UserHealthRecord::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, HealthRecordDTO dto) {
        UserHealthRecord record = getByUserId(userId);
        if (record == null) {
            record = new UserHealthRecord();
            record.setUserId(userId);
            record.setCreateTime(LocalDateTime.now());
        }
        BeanUtil.copyProperties(dto, record, "id", "userId", "createTime");
        record.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(record);
    }

    @Override
    public Page<UserHealthRecord> pageRecords(HealthRecordQuery query) {
        Page<UserHealthRecord> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<UserHealthRecord> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(UserHealthRecord::getRealName, query.getKeyword());
        }
        if (StrUtil.isNotBlank(query.getConstitutionType())) {
            wrapper.eq(UserHealthRecord::getConstitutionType, query.getConstitutionType());
        }
        if (query.getUserId() != null) {
            wrapper.eq(UserHealthRecord::getUserId, query.getUserId());
        }
        wrapper.orderByDesc(UserHealthRecord::getUpdateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(Long id) {
        removeById(id);
    }
}
