package com.tcm.service.review.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.OrderInfoMapper;
import com.tcm.mapper.OrderItemMapper;
import com.tcm.mapper.ProductMapper;
import com.tcm.mapper.ProductReviewMapper;
import com.tcm.mapper.SysUserMapper;
import com.tcm.model.dto.ReviewDTO;
import com.tcm.model.entity.OrderInfo;
import com.tcm.model.entity.OrderItem;
import com.tcm.model.entity.Product;
import com.tcm.model.entity.ProductReview;
import com.tcm.model.entity.SysUser;
import com.tcm.model.query.PageQuery;
import com.tcm.model.query.ReviewQuery;
import com.tcm.model.vo.ReviewVO;
import com.tcm.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评价服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview> implements ReviewService {

    private final OrderItemMapper orderItemMapper;
    private final OrderInfoMapper orderInfoMapper;
    private final ProductMapper productMapper;
    private final SysUserMapper sysUserMapper;

    private static final String[] STATUS_DESC = {"待审核", "已通过", "已驳回"};

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReview(Long userId, ReviewDTO dto) {
        // 检查订单明细
        OrderItem orderItem = orderItemMapper.selectById(dto.getOrderItemId());
        if (orderItem == null) {
            throw new BusinessException("订单商品不存在");
        }
        if (orderItem.getIsReviewed() == 1) {
            throw new BusinessException("该商品已评价");
        }

        // 创建评价
        ProductReview review = new ProductReview();
        review.setUserId(userId);
        review.setProductId(orderItem.getProductId());
        review.setOrderId(orderItem.getOrderId());
        review.setOrderItemId(orderItem.getId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setStatus(1); // 直接通过
        review.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : 0);
        review.setDeleted(0);
        save(review);

        // 更新订单明细状态
        orderItem.setIsReviewed(1);
        orderItemMapper.updateById(orderItem);
    }

    @Override
    public Page<ReviewVO> getProductReviews(Long productId, PageQuery query) {
        Page<ProductReview> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<ProductReview> reviewPage = page(page, new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getProductId, productId)
                .eq(ProductReview::getStatus, 1)
                .eq(ProductReview::getDeleted, 0)
                .orderByDesc(ProductReview::getCreateTime));

        return convertToVOPage(reviewPage);
    }

    @Override
    public Page<ReviewVO> getReviewList(ReviewQuery query) {
        Page<ProductReview> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ProductReview> wrapper = new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getDeleted, 0)
                .eq(query.getProductId() != null, ProductReview::getProductId, query.getProductId())
                .eq(query.getUserId() != null, ProductReview::getUserId, query.getUserId())
                .eq(query.getOrderId() != null, ProductReview::getOrderId, query.getOrderId())
                .eq(query.getStatus() != null, ProductReview::getStatus, query.getStatus())
                .eq(query.getRating() != null, ProductReview::getRating, query.getRating())
                .orderByDesc(ProductReview::getCreateTime);

        Page<ProductReview> reviewPage = page(page, wrapper);
        return convertToVOPage(reviewPage);
    }

    @Override
    public ReviewVO getReviewDetail(Long id) {
        ProductReview review = getById(id);
        if (review == null || review.getDeleted() == 1) {
            throw new BusinessException("评价不存在");
        }
        return convertToVO(review);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditReview(Long id, Integer status) {
        ProductReview review = getById(id);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        review.setStatus(status);
        updateById(review);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyReview(Long id, String replyContent) {
        ProductReview review = getById(id);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        review.setReplyContent(replyContent);
        review.setReplyTime(LocalDateTime.now());
        updateById(review);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(Long id) {
        ProductReview review = getById(id);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        review.setDeleted(1);
        updateById(review);
    }

    /**
     * 转换为VO分页
     */
    private Page<ReviewVO> convertToVOPage(Page<ProductReview> reviewPage) {
        Page<ReviewVO> voPage = new Page<>(reviewPage.getCurrent(), reviewPage.getSize(), reviewPage.getTotal());
        
        List<ProductReview> reviews = reviewPage.getRecords();
        if (reviews.isEmpty()) {
            voPage.setRecords(new ArrayList<>());
            return voPage;
        }

        // 批量查询用户信息
        List<Long> userIds = reviews.stream().map(ProductReview::getUserId).distinct().collect(Collectors.toList());
        Map<Long, SysUser> userMap = sysUserMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(SysUser::getId, u -> u));

        // 批量查询商品信息
        List<Long> productIds = reviews.stream().map(ProductReview::getProductId).distinct().collect(Collectors.toList());
        Map<Long, Product> productMap = productMapper.selectBatchIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        // 批量查询订单信息
        List<Long> orderIds = reviews.stream()
                .map(ProductReview::getOrderId)
                .filter(orderId -> orderId != null && orderId > 0)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, OrderInfo> orderMap = orderIds.isEmpty() ? Map.of() :
                orderInfoMapper.selectBatchIds(orderIds).stream()
                        .collect(Collectors.toMap(OrderInfo::getId, o -> o));

        List<ReviewVO> voList = reviews.stream().map(review -> {
            ReviewVO vo = new ReviewVO();
            vo.setId(review.getId());
            vo.setUserId(review.getUserId());
            vo.setProductId(review.getProductId());
            vo.setOrderId(review.getOrderId());
            vo.setOrderItemId(review.getOrderItemId());
            vo.setRating(review.getRating());
            vo.setContent(review.getContent());
            vo.setStatus(review.getStatus());
            vo.setStatusDesc(review.getStatus() != null && review.getStatus() < STATUS_DESC.length 
                    ? STATUS_DESC[review.getStatus()] : "未知");
            vo.setIsAnonymous(review.getIsAnonymous());
            vo.setReplyContent(review.getReplyContent());
            vo.setReplyTime(review.getReplyTime());
            vo.setCreateTime(review.getCreateTime());

            // 设置用户信息
            SysUser user = userMap.get(review.getUserId());
            if (review.getIsAnonymous() != null && review.getIsAnonymous() == 1) {
                // 匿名评价
                vo.setNickname("匿名用户");
                vo.setAvatar(null);
            } else if (user != null) {
                // 非匿名评价，显示用户信息
                vo.setNickname(user.getNickname() != null ? user.getNickname() : "用户" + review.getUserId());
                vo.setAvatar(user.getAvatar());
            } else {
                // 用户不存在时的默认处理
                vo.setNickname("用户" + review.getUserId());
                vo.setAvatar(null);
            }

            // 设置商品信息
            Product product = productMap.get(review.getProductId());
            if (product != null) {
                vo.setProductName(product.getName());
                vo.setProductImage(product.getMainImage());
            }

            // 设置订单信息
            if (review.getOrderId() != null && review.getOrderId() > 0) {
                OrderInfo order = orderMap.get(review.getOrderId());
                if (order != null) {
                    vo.setOrderNo(order.getOrderNo());
                }
            }

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 转换为VO
     */
    private ReviewVO convertToVO(ProductReview review) {
        ReviewVO vo = new ReviewVO();
        vo.setId(review.getId());
        vo.setUserId(review.getUserId());
        vo.setProductId(review.getProductId());
        vo.setOrderId(review.getOrderId());
        vo.setOrderItemId(review.getOrderItemId());
        vo.setRating(review.getRating());
        vo.setContent(review.getContent());
        vo.setStatus(review.getStatus());
        vo.setStatusDesc(review.getStatus() != null && review.getStatus() < STATUS_DESC.length 
                ? STATUS_DESC[review.getStatus()] : "未知");
        vo.setIsAnonymous(review.getIsAnonymous());
        vo.setReplyContent(review.getReplyContent());
        vo.setReplyTime(review.getReplyTime());
        vo.setCreateTime(review.getCreateTime());

        // 查询用户信息
        SysUser user = sysUserMapper.selectById(review.getUserId());
        if (review.getIsAnonymous() != null && review.getIsAnonymous() == 1) {
            // 匿名评价
            vo.setNickname("匿名用户");
            vo.setAvatar(null);
        } else if (user != null) {
            // 非匿名评价，显示用户信息
            vo.setNickname(user.getNickname() != null ? user.getNickname() : "用户" + review.getUserId());
            vo.setAvatar(user.getAvatar());
        } else {
            // 用户不存在时的默认处理
            vo.setNickname("用户" + review.getUserId());
            vo.setAvatar(null);
        }

        // 查询商品信息
        Product product = productMapper.selectById(review.getProductId());
        if (product != null) {
            vo.setProductName(product.getName());
            vo.setProductImage(product.getMainImage());
        }

        // 查询订单信息
        if (review.getOrderId() != null && review.getOrderId() > 0) {
            OrderInfo order = orderInfoMapper.selectById(review.getOrderId());
            if (order != null) {
                vo.setOrderNo(order.getOrderNo());
            }
        }

        return vo;
    }
}
