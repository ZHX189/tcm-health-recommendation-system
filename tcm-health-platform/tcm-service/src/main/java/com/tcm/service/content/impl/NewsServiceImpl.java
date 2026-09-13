package com.tcm.service.content.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.NewsMapper;
import com.tcm.model.dto.NewsDTO;
import com.tcm.model.entity.News;
import com.tcm.model.query.PageQuery;
import com.tcm.service.content.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 资讯服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public Page<News> pageNews(PageQuery query, String category) {
        Page<News> page = new Page<>(query.getPageNum(), query.getPageSize());
        return page(page, new LambdaQueryWrapper<News>()
                .eq(StrUtil.isNotBlank(category), News::getCategory, category)
                .eq(News::getStatus, 1)
                .orderByDesc(News::getIsTop)
                .orderByDesc(News::getPublishTime));
    }

    @Override
    public Page<News> pageNewsForAdmin(PageQuery query, String keyword) {
        Page<News> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 管理端查询所有资讯，不过滤状态，支持标题关键词搜索
        return page(page, new LambdaQueryWrapper<News>()
                .like(StrUtil.isNotBlank(keyword), News::getTitle, keyword)
                .orderByDesc(News::getCreateTime));
    }

    @Override
    public News getNewsDetail(Long id) {
        News news = getById(id);
        if (news == null) {
            throw new BusinessException("资讯不存在");
        }
        lambdaUpdate().eq(News::getId, id).setSql("view_count = view_count + 1").update();
        return news;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNews(NewsDTO dto) {
        News news = new News();
        BeanUtil.copyProperties(dto, news);
        news.setViewCount(0);
        news.setStatus(0);
        news.setDeleted(0);
        save(news);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNews(Long id, NewsDTO dto) {
        News news = getById(id);
        if (news == null) {
            throw new BusinessException("资讯不存在");
        }
        BeanUtil.copyProperties(dto, news, "id", "viewCount", "status", "createTime", "deleted");
        updateById(news);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNews(Long id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        News news = getById(id);
        if (news == null) {
            throw new BusinessException("资讯不存在");
        }
        news.setStatus(status);
        if (status == 1) {
            news.setPublishTime(LocalDateTime.now());
        }
        updateById(news);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewsAsDraft(NewsDTO dto) {
        News news = new News();
        BeanUtil.copyProperties(dto, news);
        news.setViewCount(0);
        news.setStatus(2); // 待审核
        news.setDeleted(0);
        save(news);
    }
}
