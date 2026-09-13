package com.tcm.common.constant;

/**
 * 公共常量
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface CommonConstant {

    /**
     * 用户类型：普通用户
     */
    Integer USER_TYPE_USER = 1;

    /**
     * 用户类型：员工
     */
    Integer USER_TYPE_STAFF = 2;

    /**
     * 用户类型：管理员
     */
    Integer USER_TYPE_ADMIN = 3;

    /**
     * 状态：禁用
     */
    Integer STATUS_DISABLE = 0;

    /**
     * 状态：启用
     */
    Integer STATUS_ENABLE = 1;

    /**
     * 逻辑删除：未删除
     */
    Integer DELETED_NO = 0;

    /**
     * 逻辑删除：已删除
     */
    Integer DELETED_YES = 1;

    /**
     * 默认页码
     */
    Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页大小
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    Integer MAX_PAGE_SIZE = 100;

    /**
     * JWT Token前缀
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * JWT Token请求头
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * 默认头像
     */
    String DEFAULT_AVATAR = "https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg";

    /**
     * 上传文件最大大小（10MB）
     */
    long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 允许上传的图片类型
     */
    String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
}
