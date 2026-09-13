package com.tcm.common.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author Ti
 * @since 2026-02-03
 */
@Getter
public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未认证
     */
    UNAUTHORIZED(401, "未认证，请先登录"),

    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),

    /**
     * 用户已存在
     */
    USER_EXISTS(1002, "用户名已存在"),

    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(1003, "用户不存在"),

    /**
     * 用户被禁用
     */
    USER_DISABLED(1004, "用户已被禁用"),

    /**
     * 原密码错误
     */
    OLD_PASSWORD_ERROR(1005, "原密码错误"),

    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(1006, "验证码错误"),

    /**
     * Token无效
     */
    TOKEN_INVALID(1007, "Token无效或已过期"),

    /**
     * 商品不存在
     */
    PRODUCT_NOT_EXISTS(2001, "商品不存在"),

    /**
     * 商品已下架
     */
    PRODUCT_OFF_SHELF(2002, "商品已下架"),

    /**
     * 库存不足
     */
    STOCK_NOT_ENOUGH(2003, "库存不足"),

    /**
     * 订单不存在
     */
    ORDER_NOT_EXISTS(3001, "订单不存在"),

    /**
     * 订单状态异常
     */
    ORDER_STATUS_ERROR(3002, "订单状态异常"),

    /**
     * 订单已支付
     */
    ORDER_PAID(3003, "订单已支付"),

    /**
     * 订单已取消
     */
    ORDER_CANCELLED(3004, "订单已取消"),

    /**
     * 购物车为空
     */
    CART_EMPTY(4001, "购物车为空"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(5001, "文件上传失败"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_ERROR(5002, "文件类型不支持"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_ERROR(5003, "文件大小超过限制"),

    /**
     * AI服务异常
     */
    AI_SERVICE_ERROR(6001, "AI服务异常，请稍后重试");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
