<template>
  <div class="post-detail-page">
    <el-button text @click="$router.back()" style="margin-bottom: 16px">
      <SvgIcon name="arrow-left" :size="16" /> 返回社区
    </el-button>

    <div v-if="post" class="post-detail">
      <div class="post-author">
        <el-avatar :src="getImageUrl(post.avatar)" :size="48" />
        <div class="author-info">
          <span class="nickname">{{ post.nickname }}</span>
          <span class="time">{{ formatTime(post.createTime) }}</span>
        </div>
        <el-tag v-if="post.isEssence" type="warning" size="small">精华</el-tag>
      </div>
      <h2 class="post-title">{{ post.title }}</h2>
      <div class="post-content" v-html="post.content?.replace(/\n/g, '<br/>')"></div>
      <div v-if="post.images && post.images.length > 0" class="post-images">
        <el-image v-for="(img, idx) in post.images" :key="idx" :src="img" fit="cover" class="post-img"
                  :preview-src-list="post.images" />
      </div>
      <div class="post-actions">
        <el-button :type="post.isLiked ? 'primary' : 'default'" @click="handleLike">
          <SvgIcon name="star" :size="16" /> {{ post.isLiked ? '已点赞' : '点赞' }} ({{ post.likeCount }})
        </el-button>
        <span class="stat"><SvgIcon name="view" :size="16" /> {{ post.viewCount }} 浏览</span>
      </div>

      <!-- 评论区 -->
      <div class="comment-section">
        <h3>评论 ({{ comments.length }})</h3>
        <div class="comment-input">
          <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..." />
          <el-button type="primary" style="margin-top: 8px" :loading="commentSubmitting" @click="submitComment">发表评论</el-button>
        </div>
        <div class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <el-avatar :src="getImageUrl(comment.avatar)" :size="32" />
            <div class="comment-body">
              <div class="comment-header">
                <span class="comment-nickname">{{ comment.nickname }}</span>
                <span v-if="comment.replyNickname" class="reply-to">回复 {{ comment.replyNickname }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              <p class="comment-text">{{ comment.content }}</p>
              <el-button text size="small" @click="replyTo(comment)">回复</el-button>
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发吧" :image-size="80" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPostDetail, togglePostLike, getPostComments, addPostComment } from 'shared/api/user.js'
import { ElMessage } from 'element-plus'

const API_BASE_URL = ''

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return `${API_BASE_URL}${url.startsWith('/') ? '' : '/'}${url}`
}

const route = useRoute()
const post = ref(null)
const comments = ref([])
const commentContent = ref('')
const commentSubmitting = ref(false)
const replyInfo = ref({ parentId: null, replyUserId: null })

const loadPost = async () => {
  try {
    const res = await getPostDetail(route.params.id)
    post.value = res
  } catch (e) { console.error(e) }
}

const loadComments = async () => {
  try {
    const res = await getPostComments(route.params.id)
    comments.value = res || []
  } catch (e) { console.error(e) }
}

const handleLike = async () => {
  try {
    await togglePostLike(route.params.id)
    post.value.isLiked = !post.value.isLiked
    post.value.likeCount += post.value.isLiked ? 1 : -1
  } catch (e) { ElMessage.error('操作失败') }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  commentSubmitting.value = true
  try {
    await addPostComment(route.params.id, {
      content: commentContent.value,
      parentId: replyInfo.value.parentId,
      replyUserId: replyInfo.value.replyUserId
    })
    ElMessage.success('评论成功，等待管理员审核')
    commentContent.value = ''
    replyInfo.value = { parentId: null, replyUserId: null }
    loadComments()
  } catch (e) {
    ElMessage.error('评论失败')
  } finally {
    commentSubmitting.value = false
  }
}

const replyTo = (comment) => {
  replyInfo.value = { parentId: comment.id, replyUserId: comment.userId }
  commentContent.value = `@${comment.nickname} `
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => { loadPost(); loadComments() })
</script>

<style scoped>
.post-detail-page { max-width: 800px; margin: 0 auto; padding: 20px; }
.post-detail { background: #fff; border-radius: 12px; padding: 24px; border: 1px solid #eee; }
.post-author { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; }
.author-info { display: flex; flex-direction: column; flex: 1; }
.nickname { font-weight: 600; font-size: 15px; }
.time { font-size: 12px; color: #999; }
.post-title { margin: 0 0 16px; font-size: 20px; color: #333; }
.post-content { color: #555; font-size: 15px; line-height: 1.8; margin-bottom: 16px; }
.post-images { display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 16px; }
.post-img { width: 200px; height: 200px; border-radius: 8px; }
.post-actions { display: flex; align-items: center; gap: 20px; padding: 16px 0; border-top: 1px solid #f0f0f0; }
.stat { display: flex; align-items: center; gap: 4px; color: #999; font-size: 14px; }
.comment-section { margin-top: 24px; }
.comment-section h3 { margin-bottom: 16px; color: #333; }
.comment-input { margin-bottom: 20px; }
.comment-item { display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid #f5f5f5; }
.comment-body { flex: 1; }
.comment-header { display: flex; align-items: center; gap: 8px; margin-bottom: 4px; }
.comment-nickname { font-weight: 600; font-size: 13px; }
.reply-to { color: #999; font-size: 12px; }
.comment-time { color: #ccc; font-size: 12px; margin-left: auto; }
.comment-text { margin: 4px 0; font-size: 14px; color: #555; }
</style>
