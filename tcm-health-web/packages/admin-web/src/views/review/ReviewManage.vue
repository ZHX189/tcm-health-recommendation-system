<!--
  评价管理页面
  @author Ti
  @since 2026-02-06
-->
<template>
  <div class="review-manage">
    <!-- 搜索区域 -->
    <div class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.keyword" placeholder="商品名称/用户昵称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="评分">
          <el-select v-model="searchForm.rating" placeholder="全部" clearable style="width: 120px">
            <el-option label="5星" :value="5" />
            <el-option label="4星" :value="4" />
            <el-option label="3星" :value="3" />
            <el-option label="2星" :value="2" />
            <el-option label="1星" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已驳回" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><SvgIcon name="search" :size="16" class="mr-8" />搜索</el-button>
          <el-button @click="handleReset"><SvgIcon name="refresh" :size="16" class="mr-8" />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-card">
      <el-table :data="reviews" v-loading="loading" stripe>
        <el-table-column label="商品信息" min-width="200">
          <template #default="{ row }">
            <div class="product-cell">
              <img :src="getImageUrl(row.productImage)" class="product-image" />
              <span class="product-name">{{ row.productName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="用户" width="120">
          <template #default="{ row }">
            <div class="user-cell">
              <img v-if="row.avatar" :src="getImageUrl(row.avatar)" class="user-avatar" />
              <div v-else class="user-avatar default-avatar"><SvgIcon name="user" :size="14" /></div>
              <span>{{ row.nickname || '匿名用户' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评分" width="140">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled :colors="['#C9A86C', '#C9A86C', '#C9A86C']" />
          </template>
        </el-table-column>
        <el-table-column label="评价内容" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.content || '用户未填写评价内容' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">{{ row.statusDesc }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评价时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 0" type="success" link size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" type="warning" link size="small" @click="handleAudit(row, 2)">驳回</el-button>
            <el-button type="primary" link size="small" @click="handleReply(row)">回复</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchReviews"
          @current-change="fetchReviews"
        />
      </div>
    </div>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="detailVisible" title="评价详情" width="600px">
      <div class="review-detail" v-if="currentReview">
        <div class="detail-section">
          <h4>商品信息</h4>
          <div class="product-info">
            <img :src="getImageUrl(currentReview.productImage)" class="detail-product-image" />
            <div>
              <div class="detail-product-name">{{ currentReview.productName }}</div>
              <div class="detail-order-no" v-if="currentReview.orderNo">订单号：{{ currentReview.orderNo }}</div>
            </div>
          </div>
        </div>
        <div class="detail-section">
          <h4>用户信息</h4>
          <div class="user-info">
            <img v-if="currentReview.avatar" :src="getImageUrl(currentReview.avatar)" class="detail-user-avatar" />
            <div v-else class="detail-user-avatar default-avatar"><SvgIcon name="user" :size="20" /></div>
            <span>{{ currentReview.nickname || '匿名用户' }}</span>
            <el-tag v-if="currentReview.isAnonymous" size="small" type="info">匿名评价</el-tag>
          </div>
        </div>
        <div class="detail-section">
          <h4>评价内容</h4>
          <div class="rating-row">
            <span>评分：</span>
            <el-rate v-model="currentReview.rating" disabled :colors="['#C9A86C', '#C9A86C', '#C9A86C']" />
          </div>
          <div class="content-text">{{ currentReview.content || '用户未填写评价内容' }}</div>
          <div class="review-images" v-if="currentReview.images && currentReview.images.length">
            <img v-for="(img, idx) in currentReview.images" :key="idx" :src="getImageUrl(img)" class="review-image" />
          </div>
          <div class="review-time">评价时间：{{ formatDateTime(currentReview.createTime) }}</div>
        </div>
        <div class="detail-section" v-if="currentReview.replyContent">
          <h4>商家回复</h4>
          <div class="reply-content">{{ currentReview.replyContent }}</div>
          <div class="reply-time">回复时间：{{ formatDateTime(currentReview.replyTime) }}</div>
        </div>
      </div>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog v-model="replyVisible" title="回复评价" width="500px">
      <el-form ref="replyFormRef" :model="replyForm" :rules="replyRules" label-width="80px">
        <el-form-item label="回复内容" prop="replyContent">
          <el-input v-model="replyForm.replyContent" type="textarea" :rows="4" placeholder="请输入回复内容..." maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyVisible = false">取消</el-button>
        <el-button type="primary" :loading="replying" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminReviewList, auditReview, replyReview, deleteReview } from 'shared/api/admin.js'
import { formatDateTime } from 'shared/utils/index.js'

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

const loading = ref(false)
const reviews = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const searchForm = reactive({
  keyword: '',
  rating: null,
  status: null
})

// 详情对话框
const detailVisible = ref(false)
const currentReview = ref(null)

// 回复对话框
const replyVisible = ref(false)
const replyFormRef = ref(null)
const replying = ref(false)
const replyForm = reactive({
  id: null,
  replyContent: ''
})
const replyRules = {
  replyContent: [
    { required: true, message: '请输入回复内容', trigger: 'blur' },
    { max: 500, message: '回复内容最多500个字符', trigger: 'blur' }
  ]
}

function getStatusType(status) {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

async function fetchReviews() {
  loading.value = true
  try {
    const params = {
      pageNum: page.value,
      pageSize: size.value,
      keyword: searchForm.keyword || undefined,
      rating: searchForm.rating,
      status: searchForm.status
    }
    const data = await getAdminReviewList(params)
    reviews.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    reviews.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  fetchReviews()
}

function handleReset() {
  searchForm.keyword = ''
  searchForm.rating = null
  searchForm.status = null
  page.value = 1
  fetchReviews()
}

function handleView(row) {
  currentReview.value = row
  detailVisible.value = true
}

async function handleAudit(row, status) {
  const statusText = status === 1 ? '通过' : '驳回'
  try {
    await ElMessageBox.confirm(`确定要${statusText}该评价吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await auditReview(row.id, status)
    ElMessage.success(`已${statusText}`)
    fetchReviews()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

function handleReply(row) {
  replyForm.id = row.id
  replyForm.replyContent = row.replyContent || ''
  replyVisible.value = true
}

async function submitReply() {
  if (!replyFormRef.value) return
  try {
    await replyFormRef.value.validate()
    replying.value = true
    await replyReview(replyForm.id, replyForm.replyContent)
    ElMessage.success('回复成功')
    replyVisible.value = false
    fetchReviews()
  } catch (error) {
    console.error(error)
  } finally {
    replying.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定要删除该评价吗？删除后不可恢复', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteReview(row.id)
    ElMessage.success('删除成功')
    fetchReviews()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => fetchReviews())
</script>

<style scoped>
.review-manage { }
.search-card { background: var(--tcm-bg-paper); padding: 20px; border-radius: var(--tcm-radius-md); margin-bottom: 16px; }
.table-card { background: var(--tcm-bg-paper); padding: 20px; border-radius: var(--tcm-radius-md); }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: flex-end; }

.product-cell { display: flex; align-items: center; gap: 10px; }
.product-image { width: 48px; height: 48px; object-fit: cover; border-radius: 4px; }
.product-name { font-size: 14px; color: var(--tcm-text-primary); }

.user-cell { display: flex; align-items: center; gap: 8px; }
.user-avatar { width: 28px; height: 28px; border-radius: 50%; object-fit: cover; }
.default-avatar { background: var(--tcm-ochre-100); display: flex; align-items: center; justify-content: center; color: var(--tcm-ochre); }

/* 详情对话框样式 */
.review-detail { }
.detail-section { margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid var(--tcm-border-lighter); }
.detail-section:last-child { border-bottom: none; margin-bottom: 0; }
.detail-section h4 { font-size: 14px; color: var(--tcm-text-primary); margin: 0 0 12px; font-weight: 500; }
.product-info { display: flex; align-items: center; gap: 12px; }
.detail-product-image { width: 64px; height: 64px; object-fit: cover; border-radius: 4px; }
.detail-product-name { font-size: 14px; color: var(--tcm-text-primary); margin-bottom: 4px; }
.detail-order-no { font-size: 12px; color: var(--tcm-text-secondary); }
.user-info { display: flex; align-items: center; gap: 10px; }
.detail-user-avatar { width: 40px; height: 40px; border-radius: 50%; object-fit: cover; }
.detail-user-avatar.default-avatar { background: var(--tcm-ochre-100); display: flex; align-items: center; justify-content: center; color: var(--tcm-ochre); }
.rating-row { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; font-size: 14px; color: var(--tcm-text-secondary); }
.content-text { font-size: 14px; color: var(--tcm-text-regular); line-height: 1.8; margin-bottom: 12px; }
.review-images { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 12px; }
.review-image { width: 80px; height: 80px; object-fit: cover; border-radius: 4px; }
.review-time { font-size: 12px; color: var(--tcm-text-placeholder); }
.reply-content { font-size: 14px; color: var(--tcm-text-regular); line-height: 1.8; margin-bottom: 8px; padding: 12px; background: var(--tcm-ochre-50); border-radius: var(--tcm-radius-base); }
.reply-time { font-size: 12px; color: var(--tcm-text-placeholder); }
</style>
