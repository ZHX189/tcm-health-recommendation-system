package com.tcm.common.enums;

import lombok.Getter;

/**
 * 收藏类型枚举
 *
 * @author Ti
 * @since 2026-02-03
 */
@Getter
public enum CollectionTypeEnum {

    /**
     * 商品
     */
    PRODUCT(1, "商品"),

    /**
     * 养生文章
     */
    ARTICLE(2, "养生文章"),

    /**
     * 养生方案
     */
    HEALTH_PLAN(3, "养生方案");

    private final Integer code;
    private final String desc;

    CollectionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据状态码获取枚举
     */
    public static CollectionTypeEnum getByCode(Integer code) {
        for (CollectionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
