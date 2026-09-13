package com.tcm.security.util;

import com.tcm.security.context.UserContext;
import com.tcm.security.context.UserContextHolder;

/**
 * 安全工具类
 *
 * @author Ti
 * @since 2026-02-03
 */
public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        return UserContextHolder.getUserId();
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        return UserContextHolder.getUsername();
    }

    /**
     * 获取当前用户类型
     */
    public static Integer getCurrentUserType() {
        return UserContextHolder.getUserType();
    }

    /**
     * 获取当前用户上下文
     */
    public static UserContext getCurrentUser() {
        return UserContextHolder.getContext();
    }

    /**
     * 判断是否已登录
     */
    public static boolean isAuthenticated() {
        return UserContextHolder.getContext() != null;
    }
}
