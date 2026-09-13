<!--
  订单结算页面
  @author Ti
  @since 2026-02-05
-->
<template>
  <div class="checkout-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/cart' }">购物车</el-breadcrumb-item>
        <el-breadcrumb-item>订单结算</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="page-header">
      <h1 class="page-title">订单结算</h1>
    </div>

    <div class="checkout-content" v-loading="loading">
      <!-- 收货地址 -->
      <div class="checkout-section">
        <div class="section-header">
          <h3 class="section-title">收货地址</h3>
          <el-button link type="primary" @click="showAddressDialog = true">
            <SvgIcon name="add" :size="14" />新增地址
          </el-button>
        </div>
        <div class="address-list" v-if="addresses.length > 0">
          <div 
            v-for="addr in addresses" 
            :key="addr.id" 
            class="address-item"
            :class="{ active: selectedAddressId === addr.id }"
            @click="selectedAddressId = addr.id"
          >
            <div class="address-info">
              <span class="address-name">{{ addr.receiverName }}</span>
              <span class="address-phone">{{ addr.receiverPhone }}</span>
              <el-tag v-if="addr.isDefault === 1" size="small" type="warning">默认</el-tag>
            </div>
            <div class="address-detail">
              {{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无收货地址，请先添加" :image-size="60" />
      </div>

      <!-- 商品清单 -->
      <div class="checkout-section">
        <div class="section-header">
          <h3 class="section-title">商品清单</h3>
        </div>
        <div class="product-list">
          <div v-for="item in orderItems" :key="item.id" class="product-item">
            <img :src="getImageUrl(item.productImage)" :alt="item.productName" class="product-image" />
            <div class="product-info">
              <div class="product-name">{{ item.productName }}</div>
              <div class="product-spec" v-if="item.spec">{{ item.spec }}</div>
            </div>
            <div class="product-price">¥{{ item.productPrice.toFixed(2) }}</div>
            <div class="product-quantity">x{{ item.quantity }}</div>
            <div class="product-total">¥{{ (item.productPrice * item.quantity).toFixed(2) }}</div>
          </div>
        </div>
      </div>

      <!-- 订单备注 -->
      <div class="checkout-section">
        <div class="section-header">
          <h3 class="section-title">订单备注</h3>
        </div>
        <el-input 
          v-model="remark" 
          type="textarea" 
          :rows="3" 
          placeholder="选填，可以告诉卖家您的特殊需求"
          maxlength="200"
          show-word-limit
        />
      </div>
    </div>

    <!-- 结算栏 -->
    <div class="checkout-footer">
      <div class="footer-info">
        <div class="info-row">
          <span>商品金额：</span>
          <span>¥{{ productAmount.toFixed(2) }}</span>
        </div>
        <div class="info-row">
          <span>运费：</span>
          <span>¥{{ shippingFee.toFixed(2) }}</span>
        </div>
      </div>
      <div class="footer-total">
        <span class="total-label">应付金额：</span>
        <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <el-button 
        type="primary" 
        size="large" 
        :loading="submitting"
        :disabled="!selectedAddressId || orderItems.length === 0"
        @click="handleSubmit"
      >
        提交订单
      </el-button>
    </div>

    <!-- 新增地址弹窗 -->
    <el-dialog v-model="showAddressDialog" title="新增收货地址" width="500px">
      <el-form :model="addressForm" :rules="addressRules" ref="addressFormRef" label-width="80px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="所在地区" prop="province">
          <div class="region-select">
            <el-input v-model="addressForm.province" placeholder="省" style="width: 100px" />
            <el-input v-model="addressForm.city" placeholder="市" style="width: 100px" />
            <el-input v-model="addressForm.district" placeholder="区/县" style="width: 100px" />
          </div>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input v-model="addressForm.detailAddress" type="textarea" :rows="2" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" :loading="addressSaving" @click="handleSaveAddress">保存</el-button>
      </template>
    </el-dialog>

    <!-- 支付弹窗 -->
    <el-dialog v-model="showPayDialog" title="订单支付" width="400px" :close-on-click-modal="false">
      <div class="pay-dialog-content">
        <div class="pay-amount">
          <span>支付金额：</span>
          <span class="amount">¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <div class="pay-methods">
          <div 
            class="pay-method" 
            :class="{ active: payType === 1 }"
            @click="payType = 1"
          >
            <SvgIcon name="wechat" :size="24" />
            <span>微信支付</span>
          </div>
          <div 
            class="pay-method" 
            :class="{ active: payType === 2 }"
            @click="payType = 2"
          >
            <SvgIcon name="alipay" :size="24" />
            <span>支付宝</span>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="handleCancelPay">取消支付</el-button>
        <el-button type="primary" :loading="paying" @click="handlePay">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, getAddressList, addAddress, createOrder, payOrder } from 'shared/api/user.js'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const paying = ref(false)
const addressSaving = ref(false)

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

// 数据
const addresses = ref([])
const selectedAddressId = ref(null)
const orderItems = ref([])
const remark = ref('')
const cartIds = ref([])
const createdOrderId = ref(null)

// 支付相关
const showPayDialog = ref(false)
const payType = ref(1)

// 地址弹窗
const showAddressDialog = ref(false)
const addressFormRef = ref(null)
const addressForm = ref({
  receiverName: '',
  receiverPhone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: 0
})
const addressRules = {
  receiverName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  detailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

// 计算属性
const productAmount = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.productPrice * item.quantity, 0)
})

const shippingFee = computed(() => {
  // 满99免运费
  return productAmount.value >= 99 ? 0 : 10
})

const totalAmount = computed(() => {
  return productAmount.value + shippingFee.value
})

// 隐藏页脚
function hideFooter() {
  const footer = document.querySelector('.user-footer')
  if (footer) footer.style.display = 'none'
}

function showFooter() {
  const footer = document.querySelector('.user-footer')
  if (footer) footer.style.display = ''
}

// 获取收货地址
async function fetchAddresses() {
  try {
    const data = await getAddressList()
    addresses.value = data || []
    // 默认选中默认地址
    const defaultAddr = addresses.value.find(a => a.isDefault === 1)
    if (defaultAddr) {
      selectedAddressId.value = defaultAddr.id
    } else if (addresses.value.length > 0) {
      selectedAddressId.value = addresses.value[0].id
    }
  } catch (error) {
    addresses.value = []
  }
}

// 获取结算商品
async function fetchOrderItems() {
  loading.value = true
  try {
    // 从sessionStorage获取选中的购物车ID
    const storedCartIds = sessionStorage.getItem('checkoutCartIds')
    if (!storedCartIds) {
      ElMessage.warning('请先选择要结算的商品')
      router.push('/cart')
      return
    }
    cartIds.value = JSON.parse(storedCartIds)
    
    // 获取购物车列表
    const cartList = await getCartList()
    // 筛选出选中的商品
    orderItems.value = (cartList || []).filter(item => cartIds.value.includes(item.id))
    
    if (orderItems.value.length === 0) {
      ElMessage.warning('未找到结算商品')
      router.push('/cart')
    }
  } catch (error) {
    ElMessage.error('获取商品信息失败')
    router.push('/cart')
  } finally {
    loading.value = false
  }
}

// 保存地址
async function handleSaveAddress() {
  try {
    await addressFormRef.value.validate()
    addressSaving.value = true
    await addAddress(addressForm.value)
    ElMessage.success('地址添加成功')
    showAddressDialog.value = false
    // 重置表单
    addressForm.value = {
      receiverName: '',
      receiverPhone: '',
      province: '',
      city: '',
      district: '',
      detailAddress: '',
      isDefault: 0
    }
    // 刷新地址列表
    await fetchAddresses()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('添加地址失败')
    }
  } finally {
    addressSaving.value = false
  }
}

// 提交订单
async function handleSubmit() {
  if (!selectedAddressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  if (orderItems.value.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }

  submitting.value = true
  try {
    const orderData = {
      addressId: selectedAddressId.value,
      remark: remark.value || undefined,
      cartIds: cartIds.value
    }
    const result = await createOrder(orderData)
    createdOrderId.value = result.id
    ElMessage.success('订单创建成功')
    // 清除sessionStorage
    sessionStorage.removeItem('checkoutCartIds')
    // 显示支付弹窗
    showPayDialog.value = true
  } catch (error) {
    ElMessage.error(error.message || '订单创建失败')
  } finally {
    submitting.value = false
  }
}

// 确认支付
async function handlePay() {
  if (!createdOrderId.value) return
  
  paying.value = true
  try {
    await payOrder(createdOrderId.value, payType.value)
    ElMessage.success('支付成功')
    showPayDialog.value = false
    // 跳转到订单详情
    router.push(`/orders/${createdOrderId.value}`)
  } catch (error) {
    ElMessage.error(error.message || '支付失败')
  } finally {
    paying.value = false
  }
}

// 取消支付
function handleCancelPay() {
  ElMessageBox.confirm('确定取消支付吗？订单将保留，您可以稍后在订单列表中继续支付', '提示', {
    confirmButtonText: '继续支付',
    cancelButtonText: '取消支付',
    type: 'warning'
  }).then(() => {
    // 继续支付，不做任何操作
  }).catch(() => {
    showPayDialog.value = false
    // 跳转到订单列表
    router.push('/orders')
  })
}

onMounted(() => {
  hideFooter()
  fetchAddresses()
  fetchOrderItems()
})

onUnmounted(() => {
  showFooter()
})
</script>


<style scoped>
.checkout-page { min-height: 60vh; padding-bottom: 100px; }
.checkout-content { display: flex; flex-direction: column; gap: 20px; }
.checkout-section { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 20px; }
.section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.section-title { font-family: var(--tcm-font-title); font-size: 16px; margin: 0; color: var(--tcm-text-primary); }

/* 地址列表 */
.address-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; }
.address-item { padding: 16px; border: 2px solid var(--tcm-border-light); border-radius: var(--tcm-radius-base); cursor: pointer; transition: all 0.2s; }
.address-item:hover { border-color: var(--tcm-primary-light); }
.address-item.active { border-color: var(--tcm-primary); background: rgba(139, 90, 43, 0.05); }
.address-info { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.address-name { font-size: 15px; font-weight: 500; color: var(--tcm-text-primary); }
.address-phone { font-size: 14px; color: var(--tcm-text-secondary); }
.address-detail { font-size: 13px; color: var(--tcm-text-secondary); line-height: 1.5; }

/* 商品列表 */
.product-list { border: 1px solid var(--tcm-border-lighter); border-radius: var(--tcm-radius-base); }
.product-item { display: flex; align-items: center; gap: 16px; padding: 16px; border-bottom: 1px solid var(--tcm-border-lighter); }
.product-item:last-child { border-bottom: none; }
.product-image { width: 80px; height: 80px; object-fit: cover; border-radius: var(--tcm-radius-base); background: var(--tcm-bg-paper-dark); }
.product-info { flex: 1; }
.product-name { font-size: 14px; color: var(--tcm-text-primary); margin-bottom: 4px; }
.product-spec { font-size: 12px; color: var(--tcm-text-secondary); }
.product-price { width: 100px; text-align: right; font-size: 14px; color: var(--tcm-text-primary); }
.product-quantity { width: 60px; text-align: center; font-size: 14px; color: var(--tcm-text-secondary); }
.product-total { width: 100px; text-align: right; font-size: 15px; font-weight: 500; color: var(--tcm-vermilion); }

/* 结算栏 */
.checkout-footer { position: fixed; bottom: 0; left: 0; right: 0; background: var(--tcm-bg-paper); border-top: 1px solid var(--tcm-border-light); padding: 16px 24px; display: flex; align-items: center; justify-content: flex-end; gap: 32px; z-index: 100; box-shadow: 0 -2px 10px rgba(0,0,0,0.05); }
.footer-info { display: flex; gap: 24px; }
.info-row { font-size: 14px; color: var(--tcm-text-secondary); }
.footer-total { display: flex; align-items: baseline; gap: 8px; }
.total-label { font-size: 14px; color: var(--tcm-text-secondary); }
.total-price { font-size: 24px; font-weight: 600; color: var(--tcm-vermilion); }

/* 地址表单 */
.region-select { display: flex; gap: 8px; }

/* 支付弹窗 */
.pay-dialog-content { padding: 20px 0; }
.pay-amount { text-align: center; margin-bottom: 24px; font-size: 16px; }
.pay-amount .amount { font-size: 32px; font-weight: 600; color: var(--tcm-vermilion); }
.pay-methods { display: flex; gap: 16px; justify-content: center; }
.pay-method { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 20px 32px; border: 2px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); cursor: pointer; transition: all 0.2s; }
.pay-method:hover { border-color: var(--tcm-primary-light); }
.pay-method.active { border-color: var(--tcm-primary); background: rgba(139, 90, 43, 0.05); }
.pay-method span { font-size: 14px; color: var(--tcm-text-primary); }

@media (max-width: 768px) {
  .address-list { grid-template-columns: 1fr; }
  .checkout-footer { flex-wrap: wrap; gap: 16px; }
  .footer-info { width: 100%; justify-content: space-between; }
}
</style>
