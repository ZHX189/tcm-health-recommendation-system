package com.tcm.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.ProductCategoryMapper;
import com.tcm.model.dto.CategoryDTO;
import com.tcm.model.entity.ProductCategory;
import com.tcm.service.product.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品分类服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryDTO dto) {
        ProductCategory category = new ProductCategory();
        BeanUtil.copyProperties(dto, category);
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(CommonConstant.STATUS_ENABLE);
        }
        category.setDeleted(CommonConstant.DELETED_NO);
        save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Long id, CategoryDTO dto) {
        ProductCategory category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        BeanUtil.copyProperties(dto, category, "id", "createTime", "deleted");
        updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        long childCount = count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException("该分类下有子分类，无法删除");
        }
        removeById(id);
    }
}
