package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户健康档案实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@TableName("user_health_record")
public class UserHealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 档案ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身高(cm)
     */
    private BigDecimal height;

    /**
     * 体重(kg)
     */
    private BigDecimal weight;

    /**
     * 血型
     */
    private String bloodType;

    /**
     * 体质类型
     */
    private String constitutionType;

    /**
     * 病史
     */
    private String medicalHistory;

    /**
     * 过敏史
     */
    private String allergyHistory;

    /**
     * 家族病史
     */
    private String familyHistory;

    /**
     * 生活习惯描述
     */
    private String lifestyle;

    /**
     * 饮食偏好
     */
    private String dietPreference;

    /**
     * 睡眠质量
     */
    private String sleepQuality;

    /**
     * 运动频率
     */
    private String exerciseFrequency;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
