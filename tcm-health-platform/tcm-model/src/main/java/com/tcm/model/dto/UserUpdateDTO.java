package com.tcm.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户信息更新DTO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Schema(description = "用户信息更新请求")
public class UserUpdateDTO {

    @Schema(description = "昵称")
    @Size(max = 50, message = "昵称最多50个字符")
    private String nickname;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", message = "邮箱格式不正确")
    private String email;

    @Schema(description = "性别：0-未知，1-男，2-女")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthday;
}
