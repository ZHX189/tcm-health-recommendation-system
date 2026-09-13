package com.tcm.service.export;

import java.io.OutputStream;

/**
 * 数据导出服务接口
 *
 * @author Ti
 * @since 2026-02-06
 */
public interface ExportService {

    void exportOrders(OutputStream out, Integer status, String startDate, String endDate);

    void exportUsers(OutputStream out);

    void exportProducts(OutputStream out);

    void exportReviews(OutputStream out);
}
