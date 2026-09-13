<!--
  资讯列表页面
  @author Ti
  @since 2026-02-06
-->
<template>
  <div class="news-list-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item>养生资讯</el-breadcrumb-item></el-breadcrumb>
    </div>
    <div class="page-header"><h1 class="page-title">养生资讯</h1><p class="page-desc">了解最新养生动态，掌握健康资讯</p></div>

    <div class="filter-bar">
      <el-radio-group v-model="category" @change="handleFilter">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="养生资讯">养生资讯</el-radio-button>
        <el-radio-button label="行业动态">行业动态</el-radio-button>
        <el-radio-button label="健康知识">健康知识</el-radio-button>
      </el-radio-group>
    </div>

    <div class="news-list" v-loading="loading">
      <div v-for="item in newsList" :key="item.id" class="news-card" @click="viewNews(item)">
        <div class="news-content">
          <div class="news-tag" v-if="item.category">
            <el-tag size="small" type="warning">{{ item.category }}</el-tag>
          </div>
          <h3 class="news-title">{{ item.title }}</h3>
          <p class="news-summary">{{ item.summary || stripHtml(item.content) }}</p>
          <div class="news-meta">
            <span><SvgIcon name="clock" :size="14" />{{ formatDate(item.publishTime || item.createTime) }}</span>
            <span><SvgIcon name="view" :size="14" />{{ item.viewCount || 0 }} 阅读</span>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && newsList.length === 0" description="暂无资讯" />
    </div>

    <div class="tcm-pagination" v-if="total > 0">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, prev, pager, next" @current-change="fetchNews" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getNewsList } from 'shared/api/user.js'
import { formatDate } from 'shared/utils/index.js'

const router = useRouter()
const loading = ref(false)
const newsList = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const category = ref('')

function stripHtml(html) {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 120) + '...'
}

async function fetchNews() {
  loading.value = true
  try {
    const data = await getNewsList({ pageNum: page.value, pageSize: size.value, category: category.value || undefined })
    newsList.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    console.error(error)
  } finally { loading.value = false }
}

function handleFilter() { page.value = 1; fetchNews() }

function viewNews(item) {
  router.push(`/news/${item.id}`)
}

onMounted(() => fetchNews())
</script>

<style scoped>
.news-list-page { min-height: 60vh; }
.page-desc { font-size: 14px; color: var(--tcm-text-secondary); margin-top: 8px; }
.filter-bar { margin-bottom: 20px; }
.news-list { display: flex; flex-direction: column; gap: 16px; }
.news-card { background: var(--tcm-bg-paper, #fff); border: 1px solid var(--tcm-border-light, #eee); border-radius: 12px; padding: 20px; cursor: pointer; transition: all 0.3s; }
.news-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); transform: translateY(-2px); }
.news-content { flex: 1; }
.news-tag { margin-bottom: 8px; }
.news-title { font-size: 18px; color: #333; margin: 0 0 10px; font-weight: 600; }
.news-summary { font-size: 14px; color: #666; line-height: 1.6; margin-bottom: 12px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.news-meta { display: flex; gap: 20px; font-size: 13px; color: #999; }
.news-meta span { display: flex; align-items: center; gap: 4px; }
</style>
