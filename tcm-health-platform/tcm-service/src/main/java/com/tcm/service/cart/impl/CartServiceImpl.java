package com.tcm.service.cart.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import com.tcm.mapper.ProductMapper;
import com.tcm.mapper.ShoppingCartMapper;
import com.tcm.model.dto.CartDTO;
import com.tcm.model.entity.Product;
import com.tcm.model.entity.ShoppingCart;
import com.tcm.model.vo.CartVO;
import com.tcm.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ShoppingCartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public List<CartVO> getCartList(Long userId) {
        return cartMapper.selectCartList(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Long userId, CartDTO dto) {
        // 检查商品是否存在
        Product product = productMapper.selectById(dto.getProductId());
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
        if (product.getStatus() != 1) {
            throw new BusinessException(ResultCode.PRODUCT_OFF_SHELF);
        }

        // 检查是否已在购物车
        ShoppingCart existCart = cartMapper.selectOne(
                new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getUserId, userId)
                        .eq(ShoppingCart::getProductId, dto.getProductId())
        );

        if (existCart != null) {
            // 已存在，增加数量
            existCart.setQuantity(existCart.getQuantity() + dto.getQuantity());
            existCart.setUpdateTime(LocalDateTime.now());
            cartMapper.updateById(existCart);
        } else {
            // 不存在，新增
            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setProductId(dto.getProductId());
            cart.setQuantity(dto.getQuantity());
            cart.setSelected(1);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cart);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        ShoppingCart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车商品不存在");
        }
        cart.setQuantity(quantity);
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.updateById(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSelected(Long userId, Long cartId, Integer selected) {
        ShoppingCart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车商品不存在");
        }
        cart.setSelected(selected);
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.updateById(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAll(Long userId, Integer selected) {
        cartMapper.update(null,
                new LambdaUpdateWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getUserId, userId)
                        .set(ShoppingCart::getSelected, selected)
                        .set(ShoppingCart::getUpdateTime, LocalDateTime.now())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCart(Long userId, Long cartId) {
        ShoppingCart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车商品不存在");
        }
        cartMapper.deleteById(cartId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long userId, List<Long> cartIds) {
        cartMapper.delete(
                new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getUserId, userId)
                        .in(ShoppingCart::getId, cartIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        cartMapper.delete(
                new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId)
        );
    }
}
