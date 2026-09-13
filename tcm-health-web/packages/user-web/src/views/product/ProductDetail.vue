<!--
  商品详情页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="product-detail-page" v-loading="loading">
    <!-- 面包屑 -->
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/products' }">药材商城</el-breadcrumb-item>
        <el-breadcrumb-item>{{ product.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="detail-main" v-if="product.id">
      <!-- 左侧图片 -->
      <div class="detail-images">
        <div class="main-image">
          <img :src="getImageUrl(product.mainImage)" :alt="product.name" />
        </div>
      </div>

      <!-- 右侧信息 -->
      <div class="detail-info">
        <h1 class="product-name">{{ product.name }}</h1>
        <p class="product-subtitle">{{ product.subTitle || product.description }}</p>

        <div class="price-box">
          <span class="current-price">{{ product.price }}</span>
          <span v-if="product.originalPrice" class="original-price">{{ product.originalPrice }}</span>
        </div>

        <div class="info-list">
          <div class="info-item">
            <span class="info-label">销量</span>
            <span class="info-value">{{ product.sales || 0 }}件</span>
          </div>
          <div class="info-item">
            <span class="info-label">库存</span>
            <span class="info-value" :class="{ 'low-stock': product.stock < 10 }">{{ product.stock || 0 }}件</span>
          </div>
          <div class="info-item" v-if="product.origin">
            <span class="info-label">产地</span>
            <span class="info-value">{{ product.origin }}</span>
          </div>
          <div class="info-item" v-if="product.spec">
            <span class="info-label">规格</span>
            <span class="info-value">{{ product.spec }}</span>
          </div>
        </div>

        <div class="quantity-box">
          <span class="quantity-label">数量</span>
          <el-input-number v-model="quantity" :min="1" :max="product.stock || 99" size="large" />
        </div>

        <div class="action-buttons">
          <el-button type="primary" size="large" @click="handleAddToCart" :loading="addingCart">
            <SvgIcon name="cart" :size="18" class="mr-8" />加入购物车
          </el-button>
          <el-button size="large" @click="handleCollect">
            <SvgIcon :name="product.collected ? 'heart' : 'heart-outline'" :size="18" class="mr-8" />
            {{ product.collected ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 商品详情 -->
    <div class="detail-tabs" v-if="product.id">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="商品详情" name="detail">
          <div class="tab-content">
            <div v-if="product.efficacy" class="detail-section">
              <h4>功效说明</h4>
              <p>{{ product.efficacy }}</p>
            </div>
            <div v-if="product.usageMethod" class="detail-section">
              <h4>使用方法</h4>
              <p>{{ product.usageMethod }}</p>
            </div>
            <div v-if="product.storageMethod" class="detail-section">
              <h4>储存方法</h4>
              <p>{{ product.storageMethod }}</p>
            </div>
            <div v-if="product.detail" class="detail-section" v-html="product.detail"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="`用户评价(${reviewTotal})`" name="reviews">
          <div class="tab-content">
            <!-- 评价统计 -->
            <div class="review-summary" v-if="reviewTotal > 0">
              <div class="summary-score">
                <span class="score-value">{{ averageRating }}</span>
                <span class="score-label">综合评分</span>
              </div>
              <div class="summary-stats">
                <span class="stat-item">好评率 {{ goodRate }}%</span>
              </div>
            </div>
            
            <!-- 评价列表 -->
            <div class="review-list" v-loading="reviewLoading">
              <div v-for="review in reviews" :key="review.id" class="review-item">
                <div class="review-header">
                  <div class="reviewer-info">
                    <img v-if="review.avatar" :src="getImageUrl(review.avatar)" class="reviewer-avatar" />
                    <div v-else class="reviewer-avatar default-avatar">
                      <SvgIcon name="user" :size="16" />
                    </div>
                    <span class="reviewer-name">{{ review.nickname || '匿名用户' }}</span>
                  </div>
                  <div class="review-rating">
                    <el-rate v-model="review.rating" disabled :colors="['#C9A86C', '#C9A86C', '#C9A86C']" />
                  </div>
                </div>
                <div class="review-content">{{ review.content }}</div>
                <div class="review-images" v-if="review.images && review.images.length">
                  <img v-for="(img, idx) in review.images" :key="idx" :src="getImageUrl(img)" class="review-image" />
                </div>
                <div class="review-footer">
                  <span class="review-time">{{ formatDateTime(review.createTime) }}</span>
                </div>
                <!-- 商家回复 -->
                <div class="review-reply" v-if="review.replyContent">
                  <span class="reply-label">商家回复：</span>
                  <span class="reply-content">{{ review.replyContent }}</span>
                </div>
              </div>
              <el-empty v-if="!reviewLoading && reviews.length === 0" description="暂无评价" />
            </div>
            
            <!-- 分页 -->
            <div class="review-pagination" v-if="reviewTotal > reviewPageSize">
              <el-pagination
                v-model:current-page="reviewPage"
                :page-size="reviewPageSize"
                :total="reviewTotal"
                layout="prev, pager, next"
                @current-change="fetchReviews"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail, addToCart, addCollection, deleteCollection, checkCollection, getProductReviews } from 'shared/api/user.js'
import { getToken, formatDateTime } from 'shared/utils/index.js'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const addingCart = ref(false)
const product = ref({})
const quantity = ref(1)
const activeTab = ref('detail')

// 评价相关
const reviewLoading = ref(false)
const reviews = ref([])
const reviewPage = ref(1)
const reviewPageSize = ref(10)
const reviewTotal = ref(0)

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

// 计算平均评分
const averageRating = computed(() => {
  if (reviews.value.length === 0) return '5.0'
  const sum = reviews.value.reduce((acc, r) => acc + r.rating, 0)
  return (sum / reviews.value.length).toFixed(1)
})

// 计算好评率
const goodRate = computed(() => {
  if (reviews.value.length === 0) return 100
  const goodCount = reviews.value.filter(r => r.rating >= 4).length
  return Math.round((goodCount / reviews.value.length) * 100)
})

async function fetchProduct() {
  loading.value = true
  try {
    const data = await getProductDetail(route.params.id)
    product.value = data || {}
    // 检查是否已收藏
    if (getToken() && product.value.id) {
      try {
        const collected = await checkCollection(product.value.id, 1)
        product.value.collected = collected
      } catch (e) {
        product.value.collected = false
      }
    }
  } catch (error) {
    product.value = {
      id: route.params.id,
      name: '野生灵芝',
      subTitle: '深山野生采摘，品质保证',
      price: 299.00,
      originalPrice: 399.00,
      stock: 50,
      sales: 128,
      origin: '云南',
      spec: '50g/盒',
      efficacy: '补气安神、止咳平喘、延年益寿',
      usageMethod: '可煲汤、泡水饮用，建议每日3-5克',
      storageMethod: '密封、阴凉、干燥处保存',
      collected: false
    }
  } finally {
    loading.value = false
  }
}

async function fetchReviews() {
  reviewLoading.value = true
  try {
    const data = await getProductReviews(route.params.id, {
      pageNum: reviewPage.value,
      pageSize: reviewPageSize.value
    })
    reviews.value = data?.records || []
    reviewTotal.value = data?.total || 0
  } catch (error) {
    reviews.value = []
    reviewTotal.value = 0
  } finally {
    reviewLoading.value = false
  }
}

async function handleAddToCart() {
  if (!getToken()) {
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  addingCart.value = true
  try {
    await addToCart({ productId: product.value.id, quantity: quantity.value })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    console.error(error)
  } finally {
    addingCart.value = false
  }
}

async function handleCollect() {
  if (!getToken()) {
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  try {
    if (product.value.collected) {
      await deleteCollection(product.value.id, 1)
      product.value.collected = false
      ElMessage.success('已取消收藏')
    } else {
      await addCollection(product.value.id, 1)
      product.value.collected = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error(error)
  }
}

// 切换到评价tab时加载评价
watch(activeTab, (newVal) => {
  if (newVal === 'reviews' && reviews.value.length === 0) {
    fetchReviews()
  }
})

onMounted(() => {
  fetchProduct()
  fetchReviews()
})
</script>

<style scoped>
.product-detail-page { min-height: 60vh; }
.detail-main { display: flex; gap: 40px; margin-bottom: 40px; }
.detail-images { width: 450px; flex-shrink: 0; }
.main-image { width: 100%; aspect-ratio: 1; background: var(--tcm-bg-paper-dark); border-radius: var(--tcm-radius-md); overflow: hidden; }
.main-image img { width: 100%; height: 100%; object-fit: cover; }
.detail-info { flex: 1; }
.product-name { font-family: var(--tcm-font-title); font-size: 26px; color: var(--tcm-text-primary); margin: 0 0 8px; }
.product-subtitle { font-size: 15px; color: var(--tcm-text-secondary); margin-bottom: 20px; }
.price-box { background: var(--tcm-ochre-50); padding: 16px 20px; border-radius: var(--tcm-radius-md); margin-bottom: 24px; }
.current-price { font-family: var(--tcm-font-title); font-size: 32px; color: var(--tcm-vermilion); font-weight: 600; }
.current-price::before { content: '¥'; font-size: 18px; }
.original-price { font-size: 16px; color: var(--tcm-text-placeholder); text-decoration: line-through; margin-left: 12px; }
.original-price::before { content: '¥'; }
.info-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; margin-bottom: 24px; }
.info-item { display: flex; gap: 12px; }
.info-label { font-size: 14px; color: var(--tcm-text-secondary); }
.info-value { font-size: 14px; color: var(--tcm-text-primary); }
.info-value.low-stock { color: var(--tcm-vermilion); }
.quantity-box { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.quantity-label { font-size: 14px; color: var(--tcm-text-secondary); }
.action-buttons { display: flex; gap: 16px; }
.action-buttons .el-button { height: 48px; padding: 0 32px; }
.detail-tabs { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 20px; }
.tab-content { min-height: 200px; padding: 16px 0; }
.detail-section { margin-bottom: 24px; }
.detail-section h4 { font-family: var(--tcm-font-title); font-size: 16px; color: var(--tcm-text-primary); margin: 0 0 12px; }
.detail-section p { font-size: 14px; color: var(--tcm-text-regular); line-height: 1.8; margin: 0; }

/* 评价样式 */
.review-summary { display: flex; align-items: center; gap: 40px; padding: 20px; background: var(--tcm-ochre-50); border-radius: var(--tcm-radius-md); margin-bottom: 20px; }
.summary-score { text-align: center; }
.score-value { font-size: 36px; font-weight: 600; color: var(--tcm-ochre); }
.score-label { display: block; font-size: 12px; color: var(--tcm-text-secondary); margin-top: 4px; }
.summary-stats { font-size: 14px; color: var(--tcm-text-secondary); }
.review-list { }
.review-item { padding: 20px 0; border-bottom: 1px solid var(--tcm-border-lighter); }
.review-item:last-child { border-bottom: none; }
.review-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; }
.reviewer-info { display: flex; align-items: center; gap: 10px; }
.reviewer-avatar { width: 36px; height: 36px; border-radius: 50%; object-fit: cover; }
.default-avatar { background: var(--tcm-ochre-100); display: flex; align-items: center; justify-content: center; color: var(--tcm-ochre); }
.reviewer-name { font-size: 14px; color: var(--tcm-text-primary); }
.review-rating { }
.review-content { font-size: 14px; color: var(--tcm-text-regular); line-height: 1.8; margin-bottom: 12px; }
.review-images { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 12px; }
.review-image { width: 80px; height: 80px; object-fit: cover; border-radius: 4px; cursor: pointer; }
.review-footer { font-size: 12px; color: var(--tcm-text-placeholder); }
.review-reply { margin-top: 12px; padding: 12px; background: var(--tcm-bg-paper-dark); border-radius: var(--tcm-radius-base); font-size: 13px; }
.reply-label { color: var(--tcm-ochre); font-weight: 500; }
.reply-content { color: var(--tcm-text-secondary); }
.review-pagination { margin-top: 20px; display: flex; justify-content: center; }

@media (max-width: 768px) { .detail-main { flex-direction: column; } .detail-images { width: 100%; } }
</style>
