package com.tcm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收藏实体
 *
 * @author Ti
 * @since 2026-02-03
 */
@Data
@TableName("user_collection")
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收藏目标ID
     */
    private Long targetId;

    /**
     * 收藏类型：1-商品，2-养生文章，3-养生方案
     */
    private Integer targetType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
