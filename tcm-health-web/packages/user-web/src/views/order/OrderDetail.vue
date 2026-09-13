<!--
  订单详情页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="order-detail-page" v-loading="loading">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item :to="{ path: '/orders' }">我的订单</el-breadcrumb-item><el-breadcrumb-item>订单详情</el-breadcrumb-item></el-breadcrumb>
    </div>

    <div class="order-detail" v-if="order.id">
      <div class="detail-card">
        <div class="card-header"><h3>订单信息</h3><span class="order-status" :class="getStatusClass(order.status)">{{ getStatusLabel(order.status) }}</span></div>
        <div class="card-body">
          <div class="info-row"><span class="label">订单编号</span><span class="value">{{ order.orderNo }}</span></div>
          <div class="info-row"><span class="label">下单时间</span><span class="value">{{ formatDateTime(order.createTime) }}</span></div>
          <div class="info-row" v-if="order.payTime"><span class="label">支付时间</span><span class="value">{{ formatDateTime(order.payTime) }}</span></div>
          <div class="info-row" v-if="order.shipTime"><span class="label">发货时间</span><span class="value">{{ formatDateTime(order.shipTime) }}</span></div>
        </div>
      </div>

      <div class="detail-card">
        <div class="card-header"><h3>收货信息</h3></div>
        <div class="card-body">
          <div class="info-row"><span class="label">收货人</span><span class="value">{{ order.receiverName }}</span></div>
          <div class="info-row"><span class="label">联系电话</span><span class="value">{{ order.receiverPhone }}</span></div>
          <div class="info-row"><span class="label">收货地址</span><span class="value">{{ order.receiverAddress }}</span></div>
        </div>
      </div>

      <div class="detail-card">
        <div class="card-header"><h3>商品信息</h3></div>
        <div class="card-body">
          <div v-for="item in order.items" :key="item.id" class="product-item">
            <img :src="getImageUrl(item.productImage)" :alt="item.productName" class="product-image" />
            <div class="product-info">
              <div class="product-name">{{ item.productName }}</div>
              <div class="product-spec">x{{ item.quantity }}</div>
            </div>
            <div class="product-price">¥{{ formatMoney(item.productPrice) }}</div>
            <!-- 评价按钮 - 订单已完成且未评价时显示 -->
            <el-button 
              v-if="order.status === 3 && !item.isReviewed" 
              type="primary" 
              size="small" 
              @click="openReviewDialog(item)"
            >
              评价
            </el-button>
            <span v-else-if="item.isReviewed" class="reviewed-tag">已评价</span>
          </div>
        </div>
      </div>

      <div class="detail-card">
        <div class="card-header"><h3>费用信息</h3></div>
        <div class="card-body">
          <div class="info-row"><span class="label">商品总额</span><span class="value">¥{{ formatMoney(order.totalAmount) }}</span></div>
          <div class="info-row"><span class="label">运费</span><span class="value">¥{{ formatMoney(order.freightAmount || 0) }}</span></div>
          <div class="info-row total"><span class="label">实付金额</span><span class="value price">¥{{ formatMoney(order.payAmount || order.totalAmount) }}</span></div>
        </div>
      </div>
    </div>

    <!-- 评价对话框 -->
    <el-dialog v-model="reviewDialogVisible" title="发表评价" width="500px">
      <div class="review-product" v-if="currentItem">
        <img :src="getImageUrl(currentItem.productImage)" class="review-product-image" />
        <span class="review-product-name">{{ currentItem.productName }}</span>
      </div>
      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules" label-width="80px">
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="reviewForm.rating" :colors="['#C9A86C', '#C9A86C', '#C9A86C']" show-text :texts="['很差', '较差', '一般', '满意', '非常满意']" />
        </el-form-item>
        <el-form-item label="评价内容" prop="content">
          <el-input v-model="reviewForm.content" type="textarea" :rows="4" placeholder="请分享您的使用体验..." maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="匿名评价">
          <el-switch v-model="reviewForm.isAnonymous" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail, createReview } from 'shared/api/user.js'
import { formatDateTime, formatMoney } from 'shared/utils/index.js'
import { ORDER_STATUS_LABEL } from 'shared/constants/index.js'

const route = useRoute()
const loading = ref(false)
const order = ref({})

// 评价相关
const reviewDialogVisible = ref(false)
const reviewFormRef = ref(null)
const currentItem = ref(null)
const submitting = ref(false)
const reviewForm = reactive({
  orderItemId: null,
  rating: 5,
  content: '',
  isAnonymous: 0
})
const reviewRules = {
  rating: [{ required: true, message: '请选择评分', trigger: 'change' }],
  content: [{ max: 500, message: '评价内容最多500个字符', trigger: 'blur' }]
}

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

function getStatusLabel(status) { return ORDER_STATUS_LABEL[status] || '未知' }
function getStatusClass(status) { const classes = { 0: 'pending', 1: 'paid', 2: 'shipped', 3: 'completed', 4: 'cancelled' }; return classes[status] || '' }

async function fetchOrder() {
  loading.value = true
  try {
    const data = await getOrderDetail(route.params.id)
    order.value = data || {}
  } catch (error) {
    order.value = { id: route.params.id, orderNo: 'TCM202602030001', status: 1, totalAmount: 598.00, receiverName: '张三', receiverPhone: '13800138001', receiverAddress: '北京市朝阳区xxx街道', createTime: '2026-02-03 10:30:00', items: [{ id: 1, productName: '野生灵芝', productPrice: 299.00, quantity: 2 }] }
  } finally { loading.value = false }
}

function openReviewDialog(item) {
  currentItem.value = item
  reviewForm.orderItemId = item.id
  reviewForm.rating = 5
  reviewForm.content = ''
  reviewForm.isAnonymous = 0
  reviewDialogVisible.value = true
}

async function submitReview() {
  if (!reviewFormRef.value) return
  try {
    await reviewFormRef.value.validate()
    submitting.value = true
    await createReview({
      orderItemId: reviewForm.orderItemId,
      rating: reviewForm.rating,
      content: reviewForm.content,
      isAnonymous: reviewForm.isAnonymous
    })
    ElMessage.success('评价成功')
    reviewDialogVisible.value = false
    // 更新订单项的评价状态
    if (currentItem.value) {
      currentItem.value.isReviewed = 1
    }
  } catch (error) {
    console.error(error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => fetchOrder())
</script>

<style scoped>
.order-detail-page { min-height: 60vh; }
.detail-card { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); margin-bottom: 16px; }
.card-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; border-bottom: 1px solid var(--tcm-border-lighter); }
.card-header h3 { font-family: var(--tcm-font-title); font-size: 16px; margin: 0; color: var(--tcm-text-primary); }
.order-status { padding: 2px 10px; border-radius: 2px; font-size: 12px; }
.order-status.pending { background: rgba(230, 162, 60, 0.1); color: #E6A23C; }
.order-status.paid { background: rgba(64, 158, 255, 0.1); color: #409EFF; }
.order-status.shipped { background: rgba(103, 194, 58, 0.1); color: #67C23A; }
.order-status.completed { background: rgba(144, 147, 153, 0.1); color: #909399; }
.card-body { padding: 16px 20px; }
.info-row { display: flex; padding: 8px 0; }
.info-row .label { width: 100px; font-size: 14px; color: var(--tcm-text-secondary); }
.info-row .value { flex: 1; font-size: 14px; color: var(--tcm-text-primary); }
.info-row.total { border-top: 1px solid var(--tcm-border-lighter); margin-top: 8px; padding-top: 16px; }
.info-row .value.price { font-size: 20px; color: var(--tcm-vermilion); font-weight: 600; }
.product-item { display: flex; align-items: center; gap: 16px; padding: 12px 0; border-bottom: 1px solid var(--tcm-border-lighter); }
.product-item:last-child { border-bottom: none; }
.product-image { width: 64px; height: 64px; object-fit: cover; border-radius: 4px; }
.product-info { flex: 1; }
.product-name { font-size: 14px; color: var(--tcm-text-primary); }
.product-spec { font-size: 12px; color: var(--tcm-text-secondary); margin-top: 4px; }
.product-price { font-size: 14px; color: var(--tcm-text-primary); margin-right: 16px; }
.reviewed-tag { font-size: 12px; color: var(--tcm-text-placeholder); padding: 4px 8px; background: var(--tcm-bg-paper-dark); border-radius: 2px; }

/* 评价对话框样式 */
.review-product { display: flex; align-items: center; gap: 12px; padding: 12px; background: var(--tcm-bg-paper-dark); border-radius: var(--tcm-radius-base); margin-bottom: 20px; }
.review-product-image { width: 48px; height: 48px; object-fit: cover; border-radius: 4px; }
.review-product-name { font-size: 14px; color: var(--tcm-text-primary); }
</style>
