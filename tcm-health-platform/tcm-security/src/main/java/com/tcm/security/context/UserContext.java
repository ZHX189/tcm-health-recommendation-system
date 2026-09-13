package com.tcm.security.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户上下文
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    private Integer userType;
}
