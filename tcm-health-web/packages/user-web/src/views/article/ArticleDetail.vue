<!--
  文章详情页面
  @author Ti
  @since 2026-02-05
-->
<template>
  <div class="article-detail-page" v-loading="loading">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/articles' }">养生文章</el-breadcrumb-item>
        <el-breadcrumb-item>{{ article.title || '文章详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="detail-container" v-if="article.id">
      <!-- 文章头部 -->
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span class="meta-item"><SvgIcon name="user" :size="14" />{{ article.author || 'Ti' }}</span>
          <span class="meta-item"><SvgIcon name="clock" :size="14" />{{ formatDate(article.createTime) }}</span>
          <span class="meta-item"><SvgIcon name="view" :size="14" />{{ article.viewCount || 0 }}阅读</span>
        </div>
        <div class="article-cover" v-if="article.coverImage">
          <img :src="getImageUrl(article.coverImage)" :alt="article.title" />
        </div>
      </div>

      <!-- 文章摘要 -->
      <div class="article-summary" v-if="article.summary">
        <SvgIcon name="quote" :size="20" />
        <p>{{ article.summary }}</p>
      </div>

      <!-- 文章内容 -->
      <div class="article-content" v-html="article.content || formatContent(article.body)"></div>

      <!-- 文章标签 -->
      <div class="article-tags" v-if="article.tags">
        <span class="tag-label">标签：</span>
        <el-tag v-for="tag in article.tags.split(',')" :key="tag" size="small" type="info">{{ tag }}</el-tag>
      </div>

      <!-- 操作栏 -->
      <div class="article-actions">
        <el-button @click="$router.push('/articles')">
          <SvgIcon name="arrow-left" :size="16" class="mr-8" />返回文章列表
        </el-button>
        <el-button type="primary" @click="handleCollect">
          <SvgIcon :name="article.collected ? 'heart' : 'heart-outline'" :size="16" class="mr-8" />
          {{ article.collected ? '已收藏' : '收藏文章' }}
        </el-button>
      </div>
    </div>

    <el-empty v-if="!loading && !article.id" description="文章不存在或已下架" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getArticleDetail, addCollection, deleteCollection, checkCollection } from 'shared/api/user.js'
import { formatDate, getToken } from 'shared/utils/index.js'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const article = ref({})

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

// 格式化内容
function formatContent(text) {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

async function fetchArticle() {
  loading.value = true
  try {
    const data = await getArticleDetail(route.params.id)
    article.value = data || {}
    // 检查是否已收藏
    if (getToken() && article.value.id) {
      try {
        const collected = await checkCollection(article.value.id, 2) // 2表示文章类型
        article.value.collected = collected
      } catch (e) {
        article.value.collected = false
      }
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    // 模拟数据
    article.value = {
      id: route.params.id,
      title: '春季养生之道',
      author: 'Ti',
      summary: '春季万物复苏，是养肝护肝的最佳时节。本文将为您详细介绍春季养生的要点和方法。',
      content: `
        <h2>一、春季养生的重要性</h2>
        <p>春季是一年之始，万物复苏，阳气升发。中医认为，春季与肝相应，是养肝护肝的最佳时节。顺应春季的特点进行养生，可以为全年的健康打下良好基础。</p>
        
        <h2>二、饮食调养</h2>
        <p>春季饮食应以清淡为主，多吃新鲜蔬菜，如菠菜、芹菜、韭菜等。这些蔬菜富含维生素和矿物质，有助于肝气的疏泄。</p>
        <p>同时，可以适当食用一些养肝的食物，如枸杞、菊花、决明子等，可以泡茶饮用。</p>
        
        <h2>三、起居养生</h2>
        <p>春季应早睡早起，顺应自然规律。早晨可以到户外散步，呼吸新鲜空气，有助于阳气的升发。</p>
        <p>穿衣方面，春季气温变化大，应注意"春捂"，不要过早减少衣物，以免受寒。</p>
        
        <h2>四、情志调摄</h2>
        <p>春季肝气旺盛，容易出现情绪波动。应保持心情舒畅，避免过度愤怒或抑郁。可以通过听音乐、赏花、郊游等方式调节情绪。</p>
        
        <h2>五、运动锻炼</h2>
        <p>春季适合进行户外运动，如散步、慢跑、太极拳、八段锦等。运动强度不宜过大，以微微出汗为宜。</p>
      `,
      viewCount: 256,
      createTime: '2026-02-01',
      tags: '春季养生,养肝,中医养生',
      collected: false
    }
  } finally {
    loading.value = false
  }
}

async function handleCollect() {
  if (!getToken()) {
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  try {
    if (article.value.collected) {
      await deleteCollection(article.value.id, 2)
      article.value.collected = false
      ElMessage.success('已取消收藏')
    } else {
      await addCollection(article.value.id, 2)
      article.value.collected = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => fetchArticle())
</script>

<style scoped>
.article-detail-page { min-height: 60vh; }

.detail-container { max-width: 900px; margin: 0 auto; }

.article-header {
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 32px;
  margin-bottom: 24px;
}

.article-title {
  font-family: var(--tcm-font-title);
  font-size: 28px;
  color: var(--tcm-text-primary);
  margin: 0 0 16px;
  line-height: 1.4;
}

.article-meta {
  display: flex;
  gap: 24px;
  font-size: 13px;
  color: var(--tcm-text-placeholder);
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-cover {
  width: 100%;
  max-height: 400px;
  border-radius: var(--tcm-radius-md);
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-summary {
  display: flex;
  gap: 12px;
  background: var(--tcm-ochre-50);
  border-left: 4px solid var(--tcm-ochre);
  padding: 20px;
  margin-bottom: 24px;
  border-radius: 0 var(--tcm-radius-md) var(--tcm-radius-md) 0;
}

.article-summary p {
  margin: 0;
  font-size: 14px;
  color: var(--tcm-text-secondary);
  line-height: 1.8;
  font-style: italic;
}

.article-content {
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 32px;
  margin-bottom: 24px;
  font-size: 15px;
  color: var(--tcm-text-regular);
  line-height: 2;
}

.article-content :deep(h2) {
  font-family: var(--tcm-font-title);
  font-size: 20px;
  color: var(--tcm-text-primary);
  margin: 32px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--tcm-border-lighter);
}

.article-content :deep(h2:first-child) { margin-top: 0; }

.article-content :deep(p) { margin-bottom: 16px; }

.article-content :deep(img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 16px 0;
}

.article-tags {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.tag-label {
  font-size: 14px;
  color: var(--tcm-text-secondary);
}

.article-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}
</style>
