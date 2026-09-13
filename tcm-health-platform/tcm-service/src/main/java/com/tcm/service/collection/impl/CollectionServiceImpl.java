package com.tcm.service.collection.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.enums.CollectionTypeEnum;
import com.tcm.mapper.*;
import com.tcm.model.entity.*;
import com.tcm.model.query.PageQuery;
import com.tcm.model.vo.ProductVO;
import com.tcm.service.collection.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final UserCollectionMapper collectionMapper;
    private final ProductMapper productMapper;
    private final HealthArticleMapper articleMapper;
    private final HealthPlanMapper planMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCollection(Long userId, Long targetId, Integer targetType) {
        // 检查是否已收藏
        if (isCollected(userId, targetId, targetType)) {
            return;
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(userId);
        collection.setTargetId(targetId);
        collection.setTargetType(targetType);
        collection.setCreateTime(LocalDateTime.now());
        collectionMapper.insert(collection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCollection(Long userId, Long targetId, Integer targetType) {
        collectionMapper.delete(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, userId)
                        .eq(UserCollection::getTargetId, targetId)
                        .eq(UserCollection::getTargetType, targetType)
        );
    }

    @Override
    public boolean isCollected(Long userId, Long targetId, Integer targetType) {
        return collectionMapper.selectCount(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, userId)
                        .eq(UserCollection::getTargetId, targetId)
                        .eq(UserCollection::getTargetType, targetType)
        ) > 0;
    }

    @Override
    public Page<?> getCollectionList(Long userId, Integer targetType, PageQuery query) {
        // 查询收藏记录
        Page<UserCollection> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<UserCollection> collectionPage = collectionMapper.selectPage(page,
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, userId)
                        .eq(targetType != null, UserCollection::getTargetType, targetType)
                        .orderByDesc(UserCollection::getCreateTime)
        );

        List<Long> targetIds = collectionPage.getRecords().stream()
                .map(UserCollection::getTargetId)
                .toList();

        if (targetIds.isEmpty()) {
            return new Page<>(query.getPageNum(), query.getPageSize(), 0);
        }

        // 根据类型查询具体内容
        if (CollectionTypeEnum.PRODUCT.getCode().equals(targetType)) {
            List<Product> products = productMapper.selectBatchIds(targetIds);
            List<ProductVO> voList = products.stream().map(p -> {
                ProductVO vo = new ProductVO();
                BeanUtil.copyProperties(p, vo);
                vo.setCollected(true);
                return vo;
            }).toList();

            Page<ProductVO> voPage = new Page<>(collectionPage.getCurrent(), collectionPage.getSize(), collectionPage.getTotal());
            voPage.setRecords(voList);
            return voPage;
        }

        // 其他类型类似处理...
        return collectionPage;
    }
}
