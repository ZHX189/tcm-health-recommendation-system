package com.tcm.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.CategoryDTO;
import com.tcm.model.entity.ProductCategory;

/**
 * 商品分类服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 新增分类
     */
    void addCategory(CategoryDTO dto);

    /**
     * 修改分类
     */
    void updateCategory(Long id, CategoryDTO dto);

    /**
     * 删除分类
     */
    void deleteCategory(Long id);
}
