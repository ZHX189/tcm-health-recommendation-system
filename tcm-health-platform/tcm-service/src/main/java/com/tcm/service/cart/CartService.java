package com.tcm.service.cart;

import com.tcm.model.dto.CartDTO;
import com.tcm.model.vo.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface CartService {

    /**
     * 获取购物车列表
     */
    List<CartVO> getCartList(Long userId);

    /**
     * 添加到购物车
     */
    void addToCart(Long userId, CartDTO dto);

    /**
     * 更新购物车数量
     */
    void updateQuantity(Long userId, Long cartId, Integer quantity);

    /**
     * 更新选中状态
     */
    void updateSelected(Long userId, Long cartId, Integer selected);

    /**
     * 全选/取消全选
     */
    void selectAll(Long userId, Integer selected);

    /**
     * 删除购物车商品
     */
    void deleteCart(Long userId, Long cartId);

    /**
     * 批量删除
     */
    void batchDelete(Long userId, List<Long> cartIds);

    /**
     * 清空购物车
     */
    void clearCart(Long userId);
}
