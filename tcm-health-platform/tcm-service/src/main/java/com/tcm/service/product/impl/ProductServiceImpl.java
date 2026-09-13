package com.tcm.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.enums.CollectionTypeEnum;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import com.tcm.mapper.*;
import com.tcm.model.dto.ProductDTO;
import com.tcm.model.entity.*;
import com.tcm.model.query.ProductQuery;
import com.tcm.model.vo.CategoryVO;
import com.tcm.model.vo.ProductVO;
import com.tcm.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductCategoryMapper categoryMapper;
    private final ProductStockMapper stockMapper;
    private final UserCollectionMapper collectionMapper;

    @Override
    public Page<ProductVO> pageProducts(ProductQuery query, Long userId) {
        Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(query.getKeyword()), Product::getName, query.getKeyword())
                .eq(query.getStatus() != null, Product::getStatus, query.getStatus())
                .ge(query.getMinPrice() != null, Product::getPrice, query.getMinPrice())
                .le(query.getMaxPrice() != null, Product::getPrice, query.getMaxPrice())
                .eq(query.getIsRecommend() != null, Product::getIsRecommend, query.getIsRecommend());
        
        // 分类筛选：支持一级分类查询其下所有子分类的商品
        if (query.getCategoryId() != null) {
            List<Long> categoryIds = getAllChildCategoryIds(query.getCategoryId());
            categoryIds.add(query.getCategoryId()); // 包含自身
            wrapper.in(Product::getCategoryId, categoryIds);
        }

        // 排序
        if (StrUtil.isNotBlank(query.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getSortOrder());
            switch (query.getSortField()) {
                case "sales" -> wrapper.orderBy(true, isAsc, Product::getSales);
                case "price" -> wrapper.orderBy(true, isAsc, Product::getPrice);
                default -> wrapper.orderByDesc(Product::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Product::getCreateTime);
        }

        Page<Product> result = page(page, wrapper);

        // 转换为VO
        List<ProductVO> voList = result.getRecords().stream()
                .map(p -> convertToVO(p, userId))
                .toList();

        Page<ProductVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public ProductVO getProductDetail(Long id, Long userId) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
        return convertToVO(product, userId);
    }

    @Override
    public List<CategoryVO> getCategoryTree() {
        List<ProductCategory> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getStatus, CommonConstant.STATUS_ENABLE)
                        .orderByAsc(ProductCategory::getSort)
        );

        // 构建树形结构
        Map<Long, List<ProductCategory>> groupByParent = categories.stream()
                .collect(Collectors.groupingBy(ProductCategory::getParentId));

        return buildCategoryTree(groupByParent, 0L);
    }

    private List<CategoryVO> buildCategoryTree(Map<Long, List<ProductCategory>> groupByParent, Long parentId) {
        List<ProductCategory> children = groupByParent.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }

        return children.stream().map(c -> {
            CategoryVO vo = new CategoryVO();
            BeanUtil.copyProperties(c, vo);
            vo.setChildren(buildCategoryTree(groupByParent, c.getId()));
            return vo;
        }).toList();
    }
    
    /**
     * 获取某分类下所有子分类ID（递归）
     */
    private List<Long> getAllChildCategoryIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        List<ProductCategory> children = categoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getParentId, parentId)
        );
        for (ProductCategory child : children) {
            result.add(child.getId());
            result.addAll(getAllChildCategoryIds(child.getId()));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(ProductDTO dto) {
        Product product = new Product();
        BeanUtil.copyProperties(dto, product);
        product.setStatus(0); // 默认下架
        product.setSales(0);
        product.setViewCount(0);
        product.setDeleted(CommonConstant.DELETED_NO);
        save(product);

        // 创建库存记录
        ProductStock stock = new ProductStock();
        stock.setProductId(product.getId());
        stock.setStock(dto.getStock() != null ? dto.getStock() : 0);
        stock.setWarningStock(dto.getWarningStock() != null ? dto.getWarningStock() : 10);
        stockMapper.insert(stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Long id, ProductDTO dto) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }

        BeanUtil.copyProperties(dto, product, "id", "status", "sales", "viewCount", "createTime", "deleted");
        updateById(product);

        // 更新库存
        if (dto.getStock() != null || dto.getWarningStock() != null) {
            ProductStock stock = stockMapper.selectOne(
                    new LambdaQueryWrapper<ProductStock>().eq(ProductStock::getProductId, id)
            );
            if (stock != null) {
                if (dto.getStock() != null) {
                    stock.setStock(dto.getStock());
                }
                if (dto.getWarningStock() != null) {
                    stock.setWarningStock(dto.getWarningStock());
                }
                stockMapper.updateById(stock);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_EXISTS);
        }
        product.setStatus(status);
        updateById(product);
    }

    @Override
    public void incrementViewCount(Long id) {
        lambdaUpdate()
                .eq(Product::getId, id)
                .setSql("view_count = view_count + 1")
                .update();
    }

    /**
     * 转换为VO
     */
    private ProductVO convertToVO(Product product, Long userId) {
        ProductVO vo = new ProductVO();
        BeanUtil.copyProperties(product, vo);

        // 获取分类名称
        ProductCategory category = categoryMapper.selectById(product.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        // 获取库存
        ProductStock stock = stockMapper.selectOne(
                new LambdaQueryWrapper<ProductStock>().eq(ProductStock::getProductId, product.getId())
        );
        vo.setStock(stock != null ? stock.getStock() : 0);

        // 判断是否收藏
        if (userId != null) {
            long count = collectionMapper.selectCount(
                    new LambdaQueryWrapper<UserCollection>()
                            .eq(UserCollection::getUserId, userId)
                            .eq(UserCollection::getTargetId, product.getId())
                            .eq(UserCollection::getTargetType, CollectionTypeEnum.PRODUCT.getCode())
            );
            vo.setCollected(count > 0);
        } else {
            vo.setCollected(false);
        }

        return vo;
    }
}
