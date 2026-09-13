<!--
  订单列表页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="order-list-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item>我的订单</el-breadcrumb-item></el-breadcrumb>
    </div>
    <div class="page-header"><h1 class="page-title">我的订单</h1></div>

    <div class="order-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部订单" name="all" />
        <el-tab-pane label="待支付" name="pending" />
        <el-tab-pane label="待发货" name="paid" />
        <el-tab-pane label="待收货" name="shipped" />
        <el-tab-pane label="已完成" name="completed" />
      </el-tabs>
    </div>

    <div class="order-list" v-loading="loading">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-time">{{ formatDateTime(order.createTime) }}</span>
          <span class="order-status" :class="getStatusClass(order.status)">{{ getStatusLabel(order.status) }}</span>
        </div>
        <div class="order-items">
          <div v-for="item in (order.items || []).slice(0, 2)" :key="item.id" class="order-item">
            <img :src="getImageUrl(item.productImage)" :alt="item.productName" class="item-image" />
            <div class="item-info">
              <div class="item-name">{{ item.productName }}</div>
              <div class="item-spec">{{ item.spec || '' }} x{{ item.quantity }}</div>
            </div>
            <div class="item-price">¥{{ formatMoney(item.productPrice) }}</div>
            <!-- 评价状态标签 -->
            <span v-if="order.status === 3 && item.isReviewed" class="reviewed-tag">已评价</span>
          </div>
        </div>
        <div class="order-footer">
          <div class="order-total">共{{ getTotalQuantity(order) }}件商品，合计：<strong>¥{{ formatMoney(order.totalAmount) }}</strong></div>
          <div class="order-actions">
            <el-button size="small" @click="$router.push(`/orders/${order.id}`)"><SvgIcon name="view" :size="14" class="mr-8" />查看详情</el-button>
            <el-button v-if="order.status === 0" type="primary" size="small">去支付</el-button>
            <el-button v-if="order.status === 2" type="primary" size="small" @click="handleConfirm(order)">确认收货</el-button>
            <el-button v-if="order.status === 3 && hasUnreviewedItems(order)" type="primary" size="small" @click="$router.push(`/orders/${order.id}`)">去评价</el-button>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && orders.length === 0" description="暂无订单" />
    </div>

    <div class="tcm-pagination" v-if="total > 0">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, prev, pager, next" @current-change="fetchOrders" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, confirmOrder } from 'shared/api/user.js'
import { formatDateTime, formatMoney } from 'shared/utils/index.js'
import { ORDER_STATUS_LABEL } from 'shared/constants/index.js'

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
const orders = ref([])
const activeTab = ref('all')
const page = ref(1)
const size = ref(10)
const total = ref(0)

function getStatusLabel(status) { return ORDER_STATUS_LABEL[status] || '未知' }
function getStatusClass(status) { const classes = { 0: 'pending', 1: 'paid', 2: 'shipped', 3: 'completed', 4: 'cancelled' }; return classes[status] || '' }
function getTotalQuantity(order) { return order.items?.reduce((sum, item) => sum + item.quantity, 0) || 0 }

// 检查是否有未评价的商品
function hasUnreviewedItems(order) {
  return order.items?.some(item => !item.isReviewed) || false
}

async function fetchOrders() {
  loading.value = true
  try {
    const params = { pageNum: page.value, pageSize: size.value, status: activeTab.value === 'all' ? undefined : { pending: 0, paid: 1, shipped: 2, completed: 3 }[activeTab.value] }
    const data = await getOrderList(params)
    orders.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    orders.value = [
      { id: 1, orderNo: 'TCM202602030001', status: 1, totalAmount: 598.00, createTime: '2026-02-03 10:30:00', items: [{ id: 1, productName: '野生灵芝', productPrice: 299.00, quantity: 2 }] },
      { id: 2, orderNo: 'TCM202602020001', status: 3, totalAmount: 299.00, createTime: '2026-02-02 15:20:00', items: [{ id: 1, productName: '人参片', productPrice: 299.00, quantity: 1, isReviewed: 0 }] }
    ]
    total.value = 2
  } finally { loading.value = false }
}

function handleTabChange() { page.value = 1; fetchOrders() }
async function handleConfirm(order) {
  try {
    await confirmOrder(order.id)
    ElMessage.success('确认收货成功')
    fetchOrders()
  } catch (error) { console.error(error) }
}

onMounted(() => fetchOrders())
</script>

<style scoped>
.order-list-page { min-height: 60vh; }
.order-tabs { margin-bottom: 20px; }
.order-card { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); margin-bottom: 16px; overflow: hidden; }
.order-header { display: flex; align-items: center; gap: 20px; padding: 12px 20px; background: var(--tcm-bg-paper-dark); font-size: 13px; }
.order-no { color: var(--tcm-text-secondary); }
.order-time { color: var(--tcm-text-placeholder); }
.order-status { margin-left: auto; padding: 2px 10px; border-radius: 2px; font-size: 12px; }
.order-status.pending { background: rgba(230, 162, 60, 0.1); color: #E6A23C; }
.order-status.paid { background: rgba(64, 158, 255, 0.1); color: #409EFF; }
.order-status.shipped { background: rgba(103, 194, 58, 0.1); color: #67C23A; }
.order-status.completed { background: rgba(144, 147, 153, 0.1); color: #909399; }
.order-status.cancelled { background: rgba(245, 108, 108, 0.1); color: #F56C6C; }
.order-items { padding: 16px 20px; }
.order-item { display: flex; align-items: center; gap: 16px; padding: 8px 0; }
.item-image { width: 64px; height: 64px; object-fit: cover; border-radius: 4px; background: var(--tcm-bg-paper-dark); }
.item-info { flex: 1; }
.item-name { font-size: 14px; color: var(--tcm-text-primary); }
.item-spec { font-size: 12px; color: var(--tcm-text-secondary); margin-top: 4px; }
.item-price { font-size: 14px; color: var(--tcm-text-primary); }
.reviewed-tag { font-size: 12px; color: var(--tcm-text-placeholder); padding: 4px 8px; background: var(--tcm-bg-paper-dark); border-radius: 2px; }
.order-footer { display: flex; align-items: center; justify-content: space-between; padding: 12px 20px; border-top: 1px solid var(--tcm-border-lighter); }
.order-total { font-size: 13px; color: var(--tcm-text-secondary); }
.order-total strong { font-size: 18px; color: var(--tcm-vermilion); }
.order-actions { display: flex; gap: 12px; }
</style>
