package com.tcm.security.context;

/**
 * 用户上下文持有者
 *
 * @author Ti
 * @since 2026-02-03
 */
public class UserContextHolder {

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    /**
     * 设置上下文
     */
    public static void setContext(UserContext context) {
        CONTEXT.set(context);
    }

    /**
     * 获取上下文
     */
    public static UserContext getContext() {
        return CONTEXT.get();
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        UserContext context = getContext();
        return context != null ? context.getUserId() : null;
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        UserContext context = getContext();
        return context != null ? context.getUsername() : null;
    }

    /**
     * 获取用户类型
     */
    public static Integer getUserType() {
        UserContext context = getContext();
        return context != null ? context.getUserType() : null;
    }

    /**
     * 清除上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
