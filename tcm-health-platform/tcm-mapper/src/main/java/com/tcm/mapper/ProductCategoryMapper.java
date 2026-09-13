package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}
