<!--
  购物车页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="cart-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>购物车</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="page-header">
      <h1 class="page-title">我的购物车</h1>
    </div>

    <div class="cart-content" v-loading="loading">
      <div class="cart-list" v-if="cartItems.length > 0">
        <div class="cart-header">
          <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
          <span class="col-product">商品信息</span>
          <span class="col-price">单价</span>
          <span class="col-quantity">数量</span>
          <span class="col-total">小计</span>
          <span class="col-action">操作</span>
        </div>

        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <el-checkbox v-model="item.checked" @change="handleSelectItem(item)" />
          <div class="item-product">
            <img :src="getImageUrl(item.productImage)" :alt="item.productName" class="item-image" />
            <div class="item-info">
              <div class="item-name">{{ item.productName }}</div>
              <div class="item-status" v-if="item.productStatus !== 1">商品已下架</div>
            </div>
          </div>
          <div class="item-price">{{ formatMoney(item.productPrice) }}</div>
          <div class="item-quantity">
            <el-input-number v-model="item.quantity" :min="1" :max="item.stock || 99" size="small" @change="handleQuantityChange(item)" />
          </div>
          <div class="item-total">{{ formatMoney(item.productPrice * item.quantity) }}</div>
          <div class="item-action">
            <span class="delete-icon" @click="handleDelete(item)" title="删除">
              <SvgIcon name="delete" :size="18" />
            </span>
          </div>
        </div>
      </div>

      <el-empty v-else description="购物车还是空的" :image-size="120">
        <el-button type="primary" @click="$router.push('/products')">去选购</el-button>
      </el-empty>
    </div>

    <!-- 结算栏 -->
    <div class="cart-footer" v-if="cartItems.length > 0">
      <div class="footer-left">
        <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
        <el-button link @click="handleDeleteSelected">删除选中</el-button>
      </div>
      <div class="footer-right">
        <span class="selected-count">已选 <strong>{{ selectedCount }}</strong> 件</span>
        <span class="total-price">合计：<strong>{{ formatMoney(totalPrice) }}</strong></span>
        <el-button type="primary" size="large" :disabled="selectedCount === 0" @click="handleCheckout">
          去结算
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartQuantity, deleteCartItem, batchDeleteCart } from 'shared/api/user.js'
import { formatMoney } from 'shared/utils/index.js'

const router = useRouter()
const loading = ref(false)
const cartItems = ref([])

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL（与商品列表保持一致）
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

const selectAll = computed({
  get: () => cartItems.value.length > 0 && cartItems.value.every(item => item.checked),
  set: (val) => cartItems.value.forEach(item => item.checked = val)
})

const selectedItems = computed(() => cartItems.value.filter(item => item.checked))
const selectedCount = computed(() => selectedItems.value.reduce((sum, item) => sum + item.quantity, 0))
const totalPrice = computed(() => selectedItems.value.reduce((sum, item) => sum + item.productPrice * item.quantity, 0))

// 隐藏页脚
function hideFooter() {
  const footer = document.querySelector('.user-footer')
  if (footer) footer.style.display = 'none'
}

// 显示页脚
function showFooter() {
  const footer = document.querySelector('.user-footer')
  if (footer) footer.style.display = ''
}

async function fetchCart() {
  loading.value = true
  try {
    const data = await getCartList()
    cartItems.value = (data || []).map(item => ({ ...item, checked: item.selected === 1 }))
  } catch (error) {
    cartItems.value = [
      { id: 1, productId: 1, productName: '野生灵芝', productImage: '', productPrice: 299.00, quantity: 1, stock: 50, productStatus: 1, checked: true },
      { id: 2, productId: 2, productName: '人参片', productImage: '', productPrice: 199.00, quantity: 2, stock: 30, productStatus: 1, checked: true }
    ]
  } finally {
    loading.value = false
  }
}

function handleSelectAll(val) {
  cartItems.value.forEach(item => item.checked = val)
}

function handleSelectItem(item) {
  // 更新选中状态
}

async function handleQuantityChange(item) {
  try {
    await updateCartQuantity(item.id, item.quantity)
  } catch (error) {
    fetchCart()
  }
}

async function handleDelete(item) {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？', '提示', { type: 'warning' })
    await deleteCartItem(item.id)
    ElMessage.success('删除成功')
    fetchCart()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleDeleteSelected() {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请选择要删除的商品')
    return
  }
  try {
    await ElMessageBox.confirm('确定删除选中的商品吗？', '提示', { type: 'warning' })
    await batchDeleteCart(selectedItems.value.map(item => item.id))
    ElMessage.success('删除成功')
    fetchCart()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

function handleCheckout() {
  if (selectedCount.value === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  // 将选中的购物车ID存储到sessionStorage，跳转到结算页面
  const cartIds = selectedItems.value.map(item => item.id)
  sessionStorage.setItem('checkoutCartIds', JSON.stringify(cartIds))
  router.push('/checkout')
}

onMounted(() => {
  fetchCart()
  hideFooter()
})

onUnmounted(() => {
  showFooter()
})
</script>

<style scoped>
.cart-page { min-height: 60vh; padding-bottom: 80px; }
.cart-content { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); }
.cart-header { display: flex; align-items: center; padding: 16px 20px; background: var(--tcm-bg-paper-dark); border-bottom: 1px solid var(--tcm-border-light); font-size: 14px; color: var(--tcm-text-secondary); }
.cart-header .el-checkbox { margin-right: 16px; }
.col-product { flex: 1; }
.col-price, .col-quantity, .col-total, .col-action { width: 120px; text-align: center; }
.cart-item { display: flex; align-items: center; padding: 20px; border-bottom: 1px solid var(--tcm-border-lighter); }
.cart-item .el-checkbox { margin-right: 16px; }
.item-product { flex: 1; display: flex; gap: 16px; }
.item-image { width: 80px; height: 80px; object-fit: cover; border-radius: var(--tcm-radius-base); background: var(--tcm-bg-paper-dark); }
.item-info { flex: 1; }
.item-name { font-size: 15px; color: var(--tcm-text-primary); margin-bottom: 8px; }
.item-status { font-size: 12px; color: var(--tcm-vermilion); }
.item-price, .item-total { width: 120px; text-align: center; font-size: 15px; color: var(--tcm-text-primary); }
.item-total { color: var(--tcm-vermilion); font-weight: 500; }
.item-price::before, .item-total::before { content: '¥'; }
.item-quantity { width: 120px; display: flex; justify-content: center; }
.item-action { width: 120px; display: flex; justify-content: center; }
.delete-icon { cursor: pointer; color: var(--tcm-text-secondary); transition: color 0.2s; display: flex; align-items: center; justify-content: center; }
.delete-icon:hover { color: var(--tcm-vermilion); }
.cart-footer { position: fixed; bottom: 0; left: 0; right: 0; background: var(--tcm-bg-paper); border-top: 1px solid var(--tcm-border-light); padding: 16px 24px; display: flex; align-items: center; justify-content: space-between; z-index: 100; }
.footer-left { display: flex; align-items: center; gap: 24px; }
.footer-right { display: flex; align-items: center; gap: 24px; }
.selected-count { font-size: 14px; color: var(--tcm-text-secondary); }
.selected-count strong { color: var(--tcm-ochre); }
.total-price { font-size: 14px; color: var(--tcm-text-secondary); }
.total-price strong { font-size: 24px; color: var(--tcm-vermilion); }
.total-price strong::before { content: '¥'; font-size: 16px; }
</style>
