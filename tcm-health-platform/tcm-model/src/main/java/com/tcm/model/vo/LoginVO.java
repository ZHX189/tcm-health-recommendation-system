package com.tcm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 登录响应VO
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@Builder
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户类型")
    private Integer userType;
}
