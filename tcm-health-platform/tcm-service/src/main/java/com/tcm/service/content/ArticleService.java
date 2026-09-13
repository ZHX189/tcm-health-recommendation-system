package com.tcm.service.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.ArticleDTO;
import com.tcm.model.entity.HealthArticle;
import com.tcm.model.query.PageQuery;

/**
 * 养生文章服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface ArticleService extends IService<HealthArticle> {

    /**
     * 用户端分页查询文章（只查已发布的）
     */
    Page<HealthArticle> pageArticles(PageQuery query, Long categoryId, String keyword);

    /**
     * 管理端分页查询文章（查询所有状态）
     */
    Page<HealthArticle> pageArticlesForAdmin(PageQuery query, Long categoryId, String keyword);

    /**
     * 获取文章详情
     */
    HealthArticle getArticleDetail(Long id);

    /**
     * 新增文章
     */
    void addArticle(ArticleDTO dto);

    /**
     * 更新文章
     */
    void updateArticle(Long id, ArticleDTO dto);

    /**
     * 删除文章
     */
    void deleteArticle(Long id);

    /**
     * 发布/下架文章
     */
    void updateStatus(Long id, Integer status);
}
