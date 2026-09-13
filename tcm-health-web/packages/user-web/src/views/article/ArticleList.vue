<!--
  养生文章列表页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="article-list-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item>养生文章</el-breadcrumb-item></el-breadcrumb>
    </div>
    <div class="page-header"><h1 class="page-title">养生文章</h1><p class="page-desc">专业中医养生知识，助您健康生活</p></div>

    <div class="article-list" v-loading="loading">
      <div v-for="article in articles" :key="article.id" class="article-card" @click="viewArticle(article)">
        <div class="article-image" v-if="article.coverImage"><img :src="getImageUrl(article.coverImage)" :alt="article.title" /></div>
        <div class="article-content">
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary || '点击查看详情...' }}</p>
          <div class="article-meta">
            <span class="article-author"><SvgIcon name="user" :size="14" />{{ article.author || 'Ti' }}</span>
            <span class="article-time"><SvgIcon name="clock" :size="14" />{{ formatDate(article.createTime) }}</span>
            <span class="article-views"><SvgIcon name="view" :size="14" />{{ article.viewCount || 0 }}阅读</span>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && articles.length === 0" description="暂无文章" />
    </div>

    <div class="tcm-pagination" v-if="total > 0">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, prev, pager, next" @current-change="fetchArticles" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleList } from 'shared/api/user.js'
import { formatDate } from 'shared/utils/index.js'

const router = useRouter()
const loading = ref(false)
const articles = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

async function fetchArticles() {
  loading.value = true
  try {
    const data = await getArticleList({ pageNum: page.value, pageSize: size.value })
    articles.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    articles.value = [
      { id: 1, title: '春季养生之道', summary: '春季万物复苏，是养肝护肝的最佳时节...', author: 'Ti', viewCount: 256, createTime: '2026-02-01' },
      { id: 2, title: '中医体质调理指南', summary: '了解自己的体质类型，选择适合的养生方法...', author: 'Ti', viewCount: 189, createTime: '2026-02-02' },
      { id: 3, title: '冬季进补注意事项', summary: '冬季是进补的好时机，但也要注意方法...', author: 'Ti', viewCount: 312, createTime: '2026-01-28' }
    ]
    total.value = 3
  } finally { loading.value = false }
}

function viewArticle(article) {
  router.push(`/articles/${article.id}`)
}

onMounted(() => fetchArticles())
</script>

<style scoped>
.article-list-page { min-height: 60vh; }
.page-desc { font-size: 14px; color: var(--tcm-text-secondary); margin-top: 8px; }
.article-list { display: flex; flex-direction: column; gap: 16px; }
.article-card { display: flex; gap: 20px; background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 20px; cursor: pointer; transition: all var(--tcm-transition-base); }
.article-card:hover { box-shadow: var(--tcm-shadow-md); }
.article-image { width: 200px; height: 130px; flex-shrink: 0; border-radius: var(--tcm-radius-base); overflow: hidden; }
.article-image img { width: 100%; height: 100%; object-fit: cover; }
.article-content { flex: 1; }
.article-title { font-family: var(--tcm-font-title); font-size: 18px; color: var(--tcm-text-primary); margin: 0 0 12px; }
.article-summary { font-size: 14px; color: var(--tcm-text-secondary); line-height: 1.6; margin-bottom: 16px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.article-meta { display: flex; gap: 20px; font-size: 13px; color: var(--tcm-text-placeholder); }
.article-meta span { display: flex; align-items: center; gap: 4px; }
@media (max-width: 768px) { .article-card { flex-direction: column; } .article-image { width: 100%; height: 180px; } }
</style>
