package com.tcm.service.stock.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.common.exception.BusinessException;
import com.tcm.model.entity.ProductStock;
import com.tcm.model.query.PageQuery;
import com.tcm.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 库存服务实现
 *
 * @author Ti
 * @since 2026-02-06
 */
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Page<Map<String, Object>> pageStocks(PageQuery query, String keyword, Boolean warningOnly) {
        StringBuilder sql = new StringBuilder(
                "SELECT ps.id, ps.product_id, p.name AS product_name, p.main_image, " +
                "ps.stock, ps.warning_stock, p.sales, ps.update_time " +
                "FROM product_stock ps LEFT JOIN product p ON ps.product_id = p.id " +
                "WHERE p.deleted = 0");
        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(*) FROM product_stock ps LEFT JOIN product p ON ps.product_id = p.id " +
                "WHERE p.deleted = 0");

        if (StringUtils.hasText(keyword)) {
            sql.append(" AND p.name LIKE '%").append(keyword).append("%'");
            countSql.append(" AND p.name LIKE '%").append(keyword).append("%'");
        }
        if (Boolean.TRUE.equals(warningOnly)) {
            sql.append(" AND ps.stock <= ps.warning_stock");
            countSql.append(" AND ps.stock <= ps.warning_stock");
        }
        Long total = jdbcTemplate.queryForObject(countSql.toString(), Long.class);
        sql.append(" ORDER BY ps.update_time DESC LIMIT ").append(query.getOffset()).append(",").append(query.getPageSize());

        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql.toString());
        Page<Map<String, Object>> page = new Page<>(query.getPageNum(), query.getPageSize(), total != null ? total : 0);
        page.setRecords(records);
        return page;
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Integer stock, Long operatorId, String remark) {
        Map<String, Object> current = jdbcTemplate.queryForMap(
                "SELECT stock FROM product_stock WHERE product_id = ?", productId);
        int beforeStock = ((Number) current.get("stock")).intValue();
        int changeQuantity = stock - beforeStock;

        jdbcTemplate.update("UPDATE product_stock SET stock = ?, update_time = NOW() WHERE product_id = ?",
                stock, productId);

        // 记录库存变更日志
        jdbcTemplate.update(
                "INSERT INTO product_stock_log (product_id, change_type, change_quantity, before_stock, after_stock, operator_id, remark) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                productId, 3, changeQuantity, beforeStock, stock, operatorId, remark != null ? remark : "手动调整");
    }

    @Override
    @Transactional
    public void batchUpdateStock(List<Map<String, Object>> items, Long operatorId) {
        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            Integer stock = Integer.valueOf(item.get("stock").toString());
            String remark = item.get("remark") != null ? item.get("remark").toString() : "批量调整";
            updateStock(productId, stock, operatorId, remark);
        }
    }

    @Override
    public Page<Map<String, Object>> pageStockLogs(PageQuery query, Long productId) {
        StringBuilder sql = new StringBuilder(
                "SELECT l.*, p.name AS product_name FROM product_stock_log l " +
                "LEFT JOIN product p ON l.product_id = p.id WHERE 1=1");
        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(*) FROM product_stock_log l WHERE 1=1");

        if (productId != null) {
            sql.append(" AND l.product_id = ").append(productId);
            countSql.append(" AND l.product_id = ").append(productId);
        }
        Long total = jdbcTemplate.queryForObject(countSql.toString(), Long.class);
        sql.append(" ORDER BY l.create_time DESC LIMIT ").append(query.getOffset()).append(",").append(query.getPageSize());

        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql.toString());
        Page<Map<String, Object>> page = new Page<>(query.getPageNum(), query.getPageSize(), total != null ? total : 0);
        page.setRecords(records);
        return page;
    }

    @Override
    public List<Map<String, Object>> getWarningStocks() {
        return jdbcTemplate.queryForList(
                "SELECT ps.product_id, p.name AS product_name, ps.stock, ps.warning_stock " +
                "FROM product_stock ps LEFT JOIN product p ON ps.product_id = p.id " +
                "WHERE p.deleted = 0 AND ps.stock <= ps.warning_stock ORDER BY ps.stock ASC");
    }
}
