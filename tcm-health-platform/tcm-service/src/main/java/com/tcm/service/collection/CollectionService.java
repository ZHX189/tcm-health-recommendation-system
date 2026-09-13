package com.tcm.service.collection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.model.query.PageQuery;

/**
 * 收藏服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface CollectionService {

    /**
     * 添加收藏
     */
    void addCollection(Long userId, Long targetId, Integer targetType);

    /**
     * 取消收藏
     */
    void removeCollection(Long userId, Long targetId, Integer targetType);

    /**
     * 判断是否已收藏
     */
    boolean isCollected(Long userId, Long targetId, Integer targetType);

    /**
     * 获取收藏列表
     */
    Page<?> getCollectionList(Long userId, Integer targetType, PageQuery query);
}
