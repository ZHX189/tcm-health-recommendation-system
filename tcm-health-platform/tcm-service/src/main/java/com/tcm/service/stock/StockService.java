package com.tcm.service.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.model.entity.ProductStock;
import com.tcm.model.query.PageQuery;

import java.util.List;
import java.util.Map;

/**
 * 库存服务接口
 *
 * @author Ti
 * @since 2026-02-06
 */
public interface StockService {

    /**
     * 库存列表（带商品信息）
     */
    Page<Map<String, Object>> pageStocks(PageQuery query, String keyword, Boolean warningOnly);

    /**
     * 更新库存
     */
    void updateStock(Long productId, Integer stock, Long operatorId, String remark);

    /**
     * 批量更新库存
     */
    void batchUpdateStock(List<Map<String, Object>> items, Long operatorId);

    /**
     * 库存变更记录
     */
    Page<Map<String, Object>> pageStockLogs(PageQuery query, Long productId);

    /**
     * 库存预警列表
     */
    List<Map<String, Object>> getWarningStocks();
}
