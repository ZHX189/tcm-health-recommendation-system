package com.tcm.service.content.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.HealthArticleMapper;
import com.tcm.model.dto.ArticleDTO;
import com.tcm.model.entity.HealthArticle;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 养生文章服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<HealthArticleMapper, HealthArticle> implements ArticleService {

    @Override
    public Page<HealthArticle> pageArticles(PageQuery query, Long categoryId, String keyword) {
        Page<HealthArticle> page = new Page<>(query.getPageNum(), query.getPageSize());
        return page(page, new LambdaQueryWrapper<HealthArticle>()
                .eq(categoryId != null, HealthArticle::getCategoryId, categoryId)
                .like(StrUtil.isNotBlank(keyword), HealthArticle::getTitle, keyword)
                .eq(HealthArticle::getStatus, 1)
                .orderByDesc(HealthArticle::getIsRecommend)
                .orderByDesc(HealthArticle::getPublishTime));
    }

    @Override
    public Page<HealthArticle> pageArticlesForAdmin(PageQuery query, Long categoryId, String keyword) {
        Page<HealthArticle> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 管理端查询所有状态的文章
        return page(page, new LambdaQueryWrapper<HealthArticle>()
                .eq(categoryId != null, HealthArticle::getCategoryId, categoryId)
                .like(StrUtil.isNotBlank(keyword), HealthArticle::getTitle, keyword)
                .orderByDesc(HealthArticle::getCreateTime));
    }

    @Override
    public HealthArticle getArticleDetail(Long id) {
        HealthArticle article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        // 增加浏览量
        lambdaUpdate().eq(HealthArticle::getId, id).setSql("view_count = view_count + 1").update();
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleDTO dto) {
        HealthArticle article = new HealthArticle();
        BeanUtil.copyProperties(dto, article);
        // 如果没有指定分类，设置默认分类ID为1
        if (article.getCategoryId() == null) {
            article.setCategoryId(1L);
        }
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCollectCount(0);
        article.setStatus(0);
        article.setDeleted(0);
        save(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Long id, ArticleDTO dto) {
        HealthArticle article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        BeanUtil.copyProperties(dto, article, "id", "viewCount", "likeCount", "collectCount", "status", "createTime", "deleted");
        updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        HealthArticle article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        article.setStatus(status);
        if (status == 1) {
            article.setPublishTime(LocalDateTime.now());
        }
        updateById(article);
    }
}
