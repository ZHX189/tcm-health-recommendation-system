<template>
  <div class="community-page">
    <div class="community-header">
      <h2>养生社区</h2>
      <div class="header-actions">
        <el-input v-model="keyword" placeholder="搜索帖子..." clearable style="width: 240px" @clear="loadPosts" @keyup.enter="loadPosts">
          <template #append>
            <el-button @click="loadPosts"><SvgIcon name="search" :size="16" /></el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateDialog = true">发布帖子</el-button>
      </div>
    </div>

    <div class="post-list">
      <div v-for="post in posts" :key="post.id" class="post-card" @click="goDetail(post.id)">
        <div class="post-author">
          <el-avatar :src="getImageUrl(post.avatar)" :size="40" />
          <div class="author-info">
            <span class="nickname">{{ post.nickname }}</span>
            <span class="time">{{ formatTime(post.createTime) }}</span>
          </div>
          <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
          <el-tag v-if="post.isEssence" type="warning" size="small">精华</el-tag>
        </div>
        <h3 class="post-title">{{ post.title }}</h3>
        <p class="post-content">{{ post.content?.substring(0, 150) }}{{ post.content?.length > 150 ? '...' : '' }}</p>
        <div v-if="post.images && post.images.length > 0" class="post-images">
          <el-image v-for="(img, idx) in post.images.slice(0, 3)" :key="idx" :src="img" fit="cover" class="post-img" />
        </div>
        <div class="post-stats">
          <span><SvgIcon name="view" :size="14" /> {{ post.viewCount }}</span>
          <span><SvgIcon name="comment" :size="14" /> {{ post.commentCount }}</span>
          <span><SvgIcon name="star" :size="14" /> {{ post.likeCount }}</span>
        </div>
      </div>
      <el-empty v-if="posts.length === 0" description="暂无帖子" />
    </div>

    <div class="pagination">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total"
                     layout="prev, pager, next" @current-change="loadPosts" />
    </div>

    <!-- 发布帖子对话框 -->
    <el-dialog v-model="showCreateDialog" title="发布帖子" width="600px">
      <el-form :model="postForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="postForm.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="postForm.content" type="textarea" :rows="6" placeholder="分享你的养生心得..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPostList, createPost } from 'shared/api/user.js'
import { ElMessage } from 'element-plus'

const API_BASE_URL = ''

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return `${API_BASE_URL}${url.startsWith('/') ? '' : '/'}${url}`
}

const router = useRouter()
const posts = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showCreateDialog = ref(false)
const submitting = ref(false)
const postForm = ref({ title: '', content: '', images: [] })

const loadPosts = async () => {
  try {
    const res = await getPostList({ pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value })
    posts.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { console.error(e) }
}

const goDetail = (id) => {
  router.push(`/community/${id}`)
}

const submitPost = async () => {
  if (!postForm.value.content) {
    ElMessage.warning('请输入帖子内容')
    return
  }
  submitting.value = true
  try {
    await createPost(postForm.value)
    ElMessage.success('发布成功，等待审核')
    showCreateDialog.value = false
    postForm.value = { title: '', content: '', images: [] }
    loadPosts()
  } catch (e) {
    ElMessage.error('发布失败')
  } finally {
    submitting.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => loadPosts())
</script>

<style scoped>
.community-page { max-width: 800px; margin: 0 auto; padding: 20px; }
.community-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.community-header h2 { margin: 0; color: #2c5530; }
.header-actions { display: flex; gap: 12px; align-items: center; }
.post-card { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 16px; cursor: pointer; transition: box-shadow 0.3s; border: 1px solid #eee; }
.post-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.post-author { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.author-info { display: flex; flex-direction: column; flex: 1; }
.nickname { font-weight: 600; font-size: 14px; }
.time { font-size: 12px; color: #999; }
.post-title { margin: 0 0 8px; font-size: 16px; color: #333; }
.post-content { color: #666; font-size: 14px; line-height: 1.6; margin: 0 0 12px; }
.post-images { display: flex; gap: 8px; margin-bottom: 12px; }
.post-img { width: 120px; height: 120px; border-radius: 8px; }
.post-stats { display: flex; gap: 20px; color: #999; font-size: 13px; }
.post-stats span { display: flex; align-items: center; gap: 4px; }
.pagination { display: flex; justify-content: center; margin-top: 20px; }
</style>
