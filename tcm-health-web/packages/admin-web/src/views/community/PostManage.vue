<template>
  <div class="post-manage">
    <h3>社区帖子审核</h3>
    <div class="filter-bar">
      <el-select v-model="statusFilter" placeholder="状态筛选" clearable style="width:140px" @change="loadPosts">
        <el-option label="待审核" :value="0" />
        <el-option label="已发布" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
      <el-input v-model="keyword" placeholder="搜索帖子..." clearable style="width:200px" @clear="loadPosts" @keyup.enter="loadPosts" />
      <el-button type="primary" @click="loadPosts">搜索</el-button>
    </div>

    <el-table :data="posts" border stripe>
      <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
      <el-table-column prop="nickname" label="作者" width="120" />
      <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
      <el-table-column prop="likeCount" label="点赞" width="80" align="center" />
      <el-table-column prop="commentCount" label="评论" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'warning' : 'danger'">
            {{ row.status === 0 ? '待审核' : row.status === 1 ? '已发布' : '已驳回' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="170" />
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showDetail(row)">查看</el-button>
          <el-button v-if="row.status === 0" type="success" size="small" @click="audit(row.id, 1)">通过</el-button>
          <el-button v-if="row.status === 0" type="warning" size="small" @click="showReject(row.id)">驳回</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:16px">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total"
                     layout="prev, pager, next" @current-change="loadPosts" />
    </div>

    <!-- 帖子详情弹窗 -->
    <el-dialog v-model="detailVisible" title="帖子详情" width="640px" top="5vh">
      <div v-if="currentPost" class="post-detail-dialog">
        <div class="detail-row">
          <span class="label">作者：</span>
          <span>{{ currentPost.nickname }}</span>
          <el-tag :type="currentPost.status === 1 ? 'success' : currentPost.status === 0 ? 'warning' : 'danger'" style="margin-left:12px">
            {{ currentPost.status === 0 ? '待审核' : currentPost.status === 1 ? '已发布' : '已驳回' }}
          </el-tag>
        </div>
        <div class="detail-row">
          <span class="label">发布时间：</span>
          <span>{{ currentPost.createTime }}</span>
        </div>
        <div class="detail-row">
          <span class="label">标题：</span>
          <span style="font-weight:600">{{ currentPost.title }}</span>
        </div>
        <div class="detail-row">
          <span class="label">内容：</span>
        </div>
        <div class="detail-content" v-html="currentPost.content?.replace(/\n/g, '<br/>')"></div>
        <div v-if="currentPost.images && currentPost.images.length > 0" class="detail-images">
          <el-image v-for="(img, idx) in currentPost.images" :key="idx" :src="img" fit="cover"
                    style="width:120px;height:120px;border-radius:8px" :preview-src-list="currentPost.images" />
        </div>
      </div>
      <template #footer>
        <template v-if="currentPost && currentPost.status === 0">
          <el-button type="success" @click="audit(currentPost.id, 1); detailVisible = false">审核通过</el-button>
          <el-button type="warning" @click="detailVisible = false; showReject(currentPost.id)">驳回</el-button>
        </template>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 驳回原因弹窗 -->
    <el-dialog v-model="rejectVisible" title="驳回帖子" width="420px">
      <el-form>
        <el-form-item label="驳回原因">
          <el-input v-model="rejectReason" type="textarea" :rows="3" placeholder="请输入驳回原因（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="warning" @click="confirmReject">确认驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminPostList, getAdminPostDetail, auditPost, deletePost } from 'shared/api/admin.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const posts = ref([])
const statusFilter = ref(null)
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const detailVisible = ref(false)
const currentPost = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const rejectId = ref(null)

const loadPosts = async () => {
  try {
    const res = await getAdminPostList({ pageNum: pageNum.value, pageSize: pageSize.value, status: statusFilter.value, keyword: keyword.value })
    posts.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { console.error(e) }
}

const showDetail = async (row) => {
  try {
    const res = await getAdminPostDetail(row.id)
    currentPost.value = res
    detailVisible.value = true
  } catch (e) {
    currentPost.value = row
    detailVisible.value = true
  }
}

const audit = async (id, status) => {
  try {
    await auditPost(id, status)
    ElMessage.success('审核通过')
    loadPosts()
  } catch (e) { ElMessage.error('操作失败') }
}

const showReject = (id) => {
  rejectId.value = id
  rejectReason.value = ''
  rejectVisible.value = true
}

const confirmReject = async () => {
  try {
    await auditPost(rejectId.value, 2)
    ElMessage.success('已驳回')
    rejectVisible.value = false
    loadPosts()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该帖子？', '提示', { type: 'warning' })
    await deletePost(id)
    ElMessage.success('删除成功')
    loadPosts()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => loadPosts())
</script>

<style scoped>
.post-manage { padding: 20px; }
.post-manage h3 { margin: 0 0 16px; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 16px; }
.post-detail-dialog .detail-row { margin-bottom: 10px; font-size: 14px; }
.post-detail-dialog .label { color: #999; margin-right: 4px; }
.detail-content { background: #fafafa; border-radius: 8px; padding: 16px; margin-bottom: 12px; font-size: 14px; line-height: 1.8; color: #555; max-height: 300px; overflow-y: auto; }
.detail-images { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
