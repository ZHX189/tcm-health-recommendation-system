<template>
  <div class="comment-manage">
    <h3>评论审核</h3>
    <div class="filter-bar">
      <el-select v-model="statusFilter" placeholder="状态筛选" clearable style="width:140px" @change="loadComments">
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
    </div>

    <el-table :data="comments" border stripe>
      <el-table-column prop="nickname" label="用户" width="120" />
      <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'warning' : 'danger'">
            {{ row.status === 0 ? '待审核' : row.status === 1 ? '已通过' : '已驳回' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="170" />
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
                     layout="prev, pager, next" @current-change="loadComments" />
    </div>

    <!-- 评论详情弹窗 -->
    <el-dialog v-model="detailVisible" title="评论详情" width="500px">
      <div v-if="currentComment" class="comment-detail-dialog">
        <div class="detail-row">
          <span class="label">用户：</span>
          <span>{{ currentComment.nickname }}</span>
          <el-tag :type="currentComment.status === 1 ? 'success' : currentComment.status === 0 ? 'warning' : 'danger'" style="margin-left:12px">
            {{ currentComment.status === 0 ? '待审核' : currentComment.status === 1 ? '已通过' : '已驳回' }}
          </el-tag>
        </div>
        <div class="detail-row">
          <span class="label">所属帖子：</span>
          <span>{{ currentComment.postTitle || '未知帖子' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">评论时间：</span>
          <span>{{ currentComment.createTime }}</span>
        </div>
        <div class="detail-row">
          <span class="label">评论内容：</span>
        </div>
        <div class="detail-content">{{ currentComment.content }}</div>
      </div>
      <template #footer>
        <template v-if="currentComment && currentComment.status === 0">
          <el-button type="success" @click="audit(currentComment.id, 1); detailVisible = false">审核通过</el-button>
          <el-button type="warning" @click="detailVisible = false; showReject(currentComment.id)">驳回</el-button>
        </template>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 驳回原因弹窗 -->
    <el-dialog v-model="rejectVisible" title="驳回评论" width="420px">
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
import { getAdminCommentList, auditComment, deleteComment } from 'shared/api/admin.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const comments = ref([])
const statusFilter = ref(null)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const detailVisible = ref(false)
const currentComment = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const rejectId = ref(null)

const loadComments = async () => {
  try {
    const res = await getAdminCommentList({ pageNum: pageNum.value, pageSize: pageSize.value, status: statusFilter.value })
    comments.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { console.error(e) }
}

const showDetail = (row) => {
  currentComment.value = row
  detailVisible.value = true
}

const audit = async (id, status) => {
  try {
    await auditComment(id, status)
    ElMessage.success('审核通过')
    loadComments()
  } catch (e) { ElMessage.error('操作失败') }
}

const showReject = (id) => {
  rejectId.value = id
  rejectReason.value = ''
  rejectVisible.value = true
}

const confirmReject = async () => {
  try {
    await auditComment(rejectId.value, 2)
    ElMessage.success('已驳回')
    rejectVisible.value = false
    loadComments()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该评论？', '提示', { type: 'warning' })
    await deleteComment(id)
    ElMessage.success('删除成功')
    loadComments()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => loadComments())
</script>

<style scoped>
.comment-manage { padding: 20px; }
.comment-manage h3 { margin: 0 0 16px; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 16px; }
.comment-detail-dialog .detail-row { margin-bottom: 10px; font-size: 14px; }
.comment-detail-dialog .label { color: #999; margin-right: 4px; }
.detail-content { background: #fafafa; border-radius: 8px; padding: 16px; font-size: 14px; line-height: 1.8; color: #555; }
</style>
