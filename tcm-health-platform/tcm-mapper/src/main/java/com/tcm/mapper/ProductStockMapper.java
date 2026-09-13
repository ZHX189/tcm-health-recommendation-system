package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.ProductStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品库存Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface ProductStockMapper extends BaseMapper<ProductStock> {

    /**
     * 扣减库存
     *
     * @param productId 商品ID
     * @param quantity  扣减数量
     * @return 影响行数
     */
    @Update("UPDATE product_stock SET stock = stock - #{quantity} WHERE product_id = #{productId} AND stock >= #{quantity}")
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 增加库存
     *
     * @param productId 商品ID
     * @param quantity  增加数量
     * @return 影响行数
     */
    @Update("UPDATE product_stock SET stock = stock + #{quantity} WHERE product_id = #{productId}")
    int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
