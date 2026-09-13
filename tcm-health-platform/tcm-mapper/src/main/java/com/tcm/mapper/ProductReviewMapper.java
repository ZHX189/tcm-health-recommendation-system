package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.ProductReview;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface ProductReviewMapper extends BaseMapper<ProductReview> {
}
