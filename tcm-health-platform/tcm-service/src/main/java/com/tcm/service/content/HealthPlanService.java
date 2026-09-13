package com.tcm.service.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.HealthPlanDTO;
import com.tcm.model.entity.HealthPlan;
import com.tcm.model.query.PageQuery;

/**
 * 养生方案服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface HealthPlanService extends IService<HealthPlan> {

    /**
     * 用户端分页查询养生方案（只查已发布的）
     */
    Page<HealthPlan> pagePlans(PageQuery query, String constitutionType, String season);

    /**
     * 管理端分页查询养生方案（查询所有状态，支持关键词搜索）
     */
    Page<HealthPlan> pagePlansForAdmin(PageQuery query, String keyword, String constitutionType, String season);

    /**
     * 获取方案详情
     */
    HealthPlan getPlanDetail(Long id);

    /**
     * 新增方案
     */
    void addPlan(HealthPlanDTO dto);

    /**
     * 更新方案
     */
    void updatePlan(Long id, HealthPlanDTO dto);

    /**
     * 删除方案
     */
    void deletePlan(Long id);

    /**
     * 发布/下架方案
     */
    void updateStatus(Long id, Integer status);
}
