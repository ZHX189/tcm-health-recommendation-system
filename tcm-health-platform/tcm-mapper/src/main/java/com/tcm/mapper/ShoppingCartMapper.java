package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.ShoppingCart;
import com.tcm.model.vo.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 查询购物车列表（包含商品信息）
     */
    List<CartVO> selectCartList(@Param("userId") Long userId);
}
