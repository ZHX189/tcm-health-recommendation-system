package com.tcm.service.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.NewsDTO;
import com.tcm.model.entity.News;
import com.tcm.model.query.PageQuery;

/**
 * 资讯服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface NewsService extends IService<News> {

    /**
     * 用户端资讯分页查询（只查已发布的）
     */
    Page<News> pageNews(PageQuery query, String category);

    /**
     * 管理端资讯分页查询（查询所有状态，支持关键词搜索）
     */
    Page<News> pageNewsForAdmin(PageQuery query, String keyword);

    News getNewsDetail(Long id);

    void addNews(NewsDTO dto);

    void updateNews(Long id, NewsDTO dto);

    void deleteNews(Long id);

    void updateStatus(Long id, Integer status);

    /**
     * 员工提交资讯草稿（待审核状态）
     */
    void addNewsAsDraft(NewsDTO dto);
}
