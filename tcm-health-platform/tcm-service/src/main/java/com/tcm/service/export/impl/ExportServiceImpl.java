package com.tcm.service.export.impl;

import com.tcm.service.export.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 数据导出服务实现（CSV格式）
 *
 * @author Ti
 * @since 2026-02-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void exportOrders(OutputStream out, Integer status, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_no, o.total_amount, o.pay_amount, o.status, " +
                "o.pay_type, o.receiver_name, o.receiver_phone, o.receiver_address, " +
                "o.create_time, o.pay_time, u.username, u.nickname " +
                "FROM order_info o LEFT JOIN sys_user u ON o.user_id = u.id " +
                "WHERE o.deleted = 0");
        if (status != null) {
            sql.append(" AND o.status = ").append(status);
        }
        if (StringUtils.hasText(startDate)) {
            sql.append(" AND o.create_time >= '").append(startDate).append(" 00:00:00'");
        }
        if (StringUtils.hasText(endDate)) {
            sql.append(" AND o.create_time <= '").append(endDate).append(" 23:59:59'");
        }
        sql.append(" ORDER BY o.create_time DESC");

        String[] headers = {"订单号", "用户名", "昵称", "总金额", "实付金额", "状态",
                "支付方式", "收货人", "联系电话", "收货地址", "下单时间", "支付时间"};
        String[] fields = {"order_no", "username", "nickname", "total_amount", "pay_amount", "status",
                "pay_type", "receiver_name", "receiver_phone", "receiver_address", "create_time", "pay_time"};

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql.toString());
        for (Map<String, Object> row : data) {
            Object s = row.get("status");
            if (s != null) {
                row.put("status", switch (((Number) s).intValue()) {
                    case 0 -> "待支付"; case 1 -> "已支付"; case 2 -> "已发货";
                    case 3 -> "已完成"; case 4 -> "已取消"; case 5 -> "已退款"; default -> "未知";
                });
            }
            Object pt = row.get("pay_type");
            if (pt != null) {
                row.put("pay_type", switch (((Number) pt).intValue()) {
                    case 1 -> "支付宝"; case 2 -> "微信"; default -> "其他";
                });
            }
        }
        writeCsv(out, headers, fields, data);
    }

    @Override
    public void exportUsers(OutputStream out) {
        String sql = "SELECT username, nickname, phone, email, gender, status, " +
                "create_time, last_login_time FROM sys_user WHERE user_type = 1 ORDER BY create_time DESC";

        String[] headers = {"用户名", "昵称", "手机号", "邮箱", "性别", "状态", "注册时间", "最后登录"};
        String[] fields = {"username", "nickname", "phone", "email", "gender", "status", "create_time", "last_login_time"};

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : data) {
            Object g = row.get("gender");
            if (g != null) {
                row.put("gender", switch (((Number) g).intValue()) {
                    case 1 -> "男"; case 2 -> "女"; default -> "未知";
                });
            }
            Object s = row.get("status");
            if (s != null) {
                row.put("status", ((Number) s).intValue() == 1 ? "正常" : "禁用");
            }
        }
        writeCsv(out, headers, fields, data);
    }

    @Override
    public void exportProducts(OutputStream out) {
        String sql = "SELECT p.name, pc.name as category_name, p.price, p.original_price, " +
                "p.sales, p.status, ps.stock, ps.warning_stock, p.create_time " +
                "FROM product p " +
                "LEFT JOIN product_category pc ON p.category_id = pc.id " +
                "LEFT JOIN product_stock ps ON p.id = ps.product_id " +
                "WHERE p.deleted = 0 ORDER BY p.create_time DESC";

        String[] headers = {"商品名称", "分类", "售价", "原价", "销量", "状态", "库存", "预警值", "创建时间"};
        String[] fields = {"name", "category_name", "price", "original_price", "sales", "status", "stock", "warning_stock", "create_time"};

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : data) {
            Object s = row.get("status");
            if (s != null) {
                row.put("status", ((Number) s).intValue() == 1 ? "上架" : "下架");
            }
        }
        writeCsv(out, headers, fields, data);
    }

    @Override
    public void exportReviews(OutputStream out) {
        String sql = "SELECT p.name as product_name, u.nickname, r.rating, r.content, " +
                "r.status, r.reply_content, r.create_time, r.reply_time " +
                "FROM product_review r " +
                "LEFT JOIN product p ON r.product_id = p.id " +
                "LEFT JOIN sys_user u ON r.user_id = u.id " +
                "WHERE r.deleted = 0 ORDER BY r.create_time DESC";

        String[] headers = {"商品名称", "用户昵称", "评分", "评价内容", "状态", "商家回复", "评价时间", "回复时间"};
        String[] fields = {"product_name", "nickname", "rating", "content", "status", "reply_content", "create_time", "reply_time"};

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : data) {
            Object s = row.get("status");
            if (s != null) {
                row.put("status", switch (((Number) s).intValue()) {
                    case 0 -> "待审核"; case 1 -> "已通过"; case 2 -> "已驳回"; default -> "未知";
                });
            }
        }
        writeCsv(out, headers, fields, data);
    }

    private void writeCsv(OutputStream out, String[] headers, String[] fields, List<Map<String, Object>> data) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.write('\uFEFF'); // BOM for Excel
            writer.println(String.join(",", headers));
            for (Map<String, Object> row : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    if (i > 0) line.append(",");
                    Object val = row.get(fields[i]);
                    String str = val != null ? val.toString() : "";
                    if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
                        str = "\"" + str.replace("\"", "\"\"") + "\"";
                    }
                    line.append(str);
                }
                writer.println(line);
            }
            writer.flush();
        }
    }
}
