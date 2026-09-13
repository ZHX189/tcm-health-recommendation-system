package com.tcm.service.content.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.HealthPlanMapper;
import com.tcm.model.dto.HealthPlanDTO;
import com.tcm.model.entity.HealthPlan;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.HealthPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 养生方案服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class HealthPlanServiceImpl extends ServiceImpl<HealthPlanMapper, HealthPlan> implements HealthPlanService {

    @Override
    public Page<HealthPlan> pagePlans(PageQuery query, String constitutionType, String season) {
        Page<HealthPlan> page = new Page<>(query.getPageNum(), query.getPageSize());
        return page(page, new LambdaQueryWrapper<HealthPlan>()
                .eq(StrUtil.isNotBlank(constitutionType), HealthPlan::getConstitutionType, constitutionType)
                .eq(StrUtil.isNotBlank(season), HealthPlan::getSeason, season)
                .eq(HealthPlan::getStatus, 1)
                .orderByDesc(HealthPlan::getIsRecommend)
                .orderByDesc(HealthPlan::getPublishTime));
    }

    @Override
    public Page<HealthPlan> pagePlansForAdmin(PageQuery query, String keyword, String constitutionType, String season) {
        Page<HealthPlan> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 管理端查询所有状态的方案，支持标题关键词搜索
        return page(page, new LambdaQueryWrapper<HealthPlan>()
                .like(StrUtil.isNotBlank(keyword), HealthPlan::getTitle, keyword)
                .eq(StrUtil.isNotBlank(constitutionType), HealthPlan::getConstitutionType, constitutionType)
                .eq(StrUtil.isNotBlank(season), HealthPlan::getSeason, season)
                .orderByDesc(HealthPlan::getCreateTime));
    }

    @Override
    public HealthPlan getPlanDetail(Long id) {
        HealthPlan plan = getById(id);
        if (plan == null) {
            throw new BusinessException("养生方案不存在");
        }
        // 增加浏览量
        lambdaUpdate().eq(HealthPlan::getId, id).setSql("view_count = view_count + 1").update();
        return plan;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPlan(HealthPlanDTO dto) {
        HealthPlan plan = new HealthPlan();
        BeanUtil.copyProperties(dto, plan);
        plan.setViewCount(0);
        plan.setCollectCount(0);
        plan.setStatus(0);
        plan.setDeleted(0);
        save(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan(Long id, HealthPlanDTO dto) {
        HealthPlan plan = getById(id);
        if (plan == null) {
            throw new BusinessException("养生方案不存在");
        }
        BeanUtil.copyProperties(dto, plan, "id", "viewCount", "collectCount", "status", "createTime", "deleted");
        updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        HealthPlan plan = getById(id);
        if (plan == null) {
            throw new BusinessException("养生方案不存在");
        }
        plan.setStatus(status);
        if (status == 1) {
            plan.setPublishTime(LocalDateTime.now());
        }
        updateById(plan);
    }
}
