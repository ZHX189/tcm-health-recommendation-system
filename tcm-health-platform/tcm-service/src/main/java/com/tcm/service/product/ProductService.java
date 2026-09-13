package com.tcm.service.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.ProductDTO;
import com.tcm.model.entity.Product;
import com.tcm.model.query.ProductQuery;
import com.tcm.model.vo.CategoryVO;
import com.tcm.model.vo.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品
     */
    Page<ProductVO> pageProducts(ProductQuery query, Long userId);

    /**
     * 获取商品详情
     */
    ProductVO getProductDetail(Long id, Long userId);

    /**
     * 获取分类树
     */
    List<CategoryVO> getCategoryTree();

    /**
     * 新增商品
     */
    void addProduct(ProductDTO dto);

    /**
     * 更新商品
     */
    void updateProduct(Long id, ProductDTO dto);

    /**
     * 删除商品
     */
    void deleteProduct(Long id);

    /**
     * 上下架商品
     */
    void updateStatus(Long id, Integer status);

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);
}
