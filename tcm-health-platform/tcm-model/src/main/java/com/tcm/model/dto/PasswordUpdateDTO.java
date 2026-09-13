package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码修改DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "密码修改请求")
public class PasswordUpdateDTO {

    @Schema(description = "原密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度6-20位")
    private String newPassword;

    @Schema(description = "确认新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
}
