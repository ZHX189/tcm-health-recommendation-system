package com.tcm.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询")
public class UserQuery extends PageQuery {

    @Schema(description = "关键词搜索（用户名/昵称/手机号）")
    private String keyword;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "用户类型")
    private Integer userType;
}
