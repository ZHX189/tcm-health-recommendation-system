<!--
  资讯详情页面
  @author Ti
  @since 2026-02-06
-->
<template>
  <div class="news-detail-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/news' }">养生资讯</el-breadcrumb-item>
        <el-breadcrumb-item>资讯详情</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div v-if="news" class="news-detail-card">
      <div class="news-header">
        <el-tag v-if="news.category" size="small" type="warning">{{ news.category }}</el-tag>
        <h1 class="news-title">{{ news.title }}</h1>
        <div class="news-meta">
          <span><SvgIcon name="clock" :size="14" />{{ formatDate(news.publishTime || news.createTime) }}</span>
          <span><SvgIcon name="view" :size="14" />{{ news.viewCount || 0 }} 阅读</span>
        </div>
      </div>
      <el-divider />
      <div class="news-body" v-html="news.content"></div>
    </div>

    <el-empty v-if="!loading && !news" description="资讯不存在" />

    <div class="back-btn">
      <el-button @click="$router.push('/news')"><SvgIcon name="arrow-left" :size="16" /> 返回资讯列表</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getNewsDetail } from 'shared/api/user.js'
import { formatDate } from 'shared/utils/index.js'

const route = useRoute()
const loading = ref(false)
const news = ref(null)

async function fetchDetail() {
  loading.value = true
  try {
    news.value = await getNewsDetail(route.params.id)
  } catch (error) {
    console.error(error)
  } finally { loading.value = false }
}

onMounted(() => fetchDetail())
</script>

<style scoped>
.news-detail-page { min-height: 60vh; max-width: 800px; margin: 0 auto; }
.news-detail-card { background: #fff; border-radius: 12px; padding: 32px; border: 1px solid #eee; }
.news-header { text-align: center; }
.news-title { font-size: 24px; color: #333; margin: 12px 0 16px; font-weight: 600; line-height: 1.4; }
.news-meta { display: flex; justify-content: center; gap: 24px; font-size: 13px; color: #999; }
.news-meta span { display: flex; align-items: center; gap: 4px; }
.news-body { font-size: 15px; color: #555; line-height: 1.8; }
.news-body :deep(img) { max-width: 100%; border-radius: 8px; margin: 12px 0; }
.news-body :deep(p) { margin-bottom: 12px; }
.back-btn { margin-top: 24px; text-align: center; }
</style>
