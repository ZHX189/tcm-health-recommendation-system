package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 健康档案DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "健康档案请求")
public class HealthRecordDTO {

    @Schema(description = "真实姓名")
    @Size(max = 50, message = "真实姓名最多50个字符")
    private String realName;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "身高(cm)")
    private BigDecimal height;

    @Schema(description = "体重(kg)")
    private BigDecimal weight;

    @Schema(description = "血型")
    private String bloodType;

    @Schema(description = "体质类型")
    private String constitutionType;

    @Schema(description = "病史")
    private String medicalHistory;

    @Schema(description = "过敏史")
    private String allergyHistory;

    @Schema(description = "家族病史")
    private String familyHistory;

    @Schema(description = "生活习惯描述")
    private String lifestyle;

    @Schema(description = "饮食偏好")
    private String dietPreference;

    @Schema(description = "睡眠质量")
    private String sleepQuality;

    @Schema(description = "运动频率")
    private String exerciseFrequency;

    @Schema(description = "备注")
    private String remark;
}
