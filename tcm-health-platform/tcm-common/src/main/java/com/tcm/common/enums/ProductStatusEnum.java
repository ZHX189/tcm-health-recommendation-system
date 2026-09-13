package com.tcm.common.enums;

import lombok.Getter;

/**
 * 商品状态枚举
 *
 * @author Ti
 * @since 2026-02-03
 */
@Getter
public enum ProductStatusEnum {

    /**
     * 下架
     */
    OFF_SHELF(0, "下架"),

    /**
     * 上架
     */
    ON_SHELF(1, "上架"),

    /**
     * 待审核
     */
    PENDING(2, "待审核");

    private final Integer code;
    private final String desc;

    ProductStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据状态码获取枚举
     */
    public static ProductStatusEnum getByCode(Integer code) {
        for (ProductStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
