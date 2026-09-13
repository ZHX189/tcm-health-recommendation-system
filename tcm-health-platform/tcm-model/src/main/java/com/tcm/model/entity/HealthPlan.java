package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 养生方案实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_plan")
public class HealthPlan extends BaseEntity {

    /**
     * 方案标题
     */
    private String title;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 适用体质类型
     */
    private String constitutionType;

    /**
     * 适用季节
     */
    private String season;

    /**
     * 方案摘要
     */
    private String summary;

    /**
     * 方案详情（富文本）
     */
    private String content;

    /**
     * 饮食建议
     */
    private String dietAdvice;

    /**
     * 运动建议
     */
    private String exerciseAdvice;

    /**
     * 睡眠建议
     */
    private String sleepAdvice;

    /**
     * 药材建议
     */
    private String medicineAdvice;

    /**
     * 禁忌事项
     */
    private String taboo;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 状态：0-草稿，1-已发布，2-已下架
     */
    private Integer status;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
