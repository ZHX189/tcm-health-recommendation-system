<template>
  <div class="data-export">
    <h3>数据导出</h3>
    <p class="desc">导出平台数据为CSV文件，可用Excel打开查看</p>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="hover" class="export-card">
          <div class="export-item">
            <div class="export-icon ochre"><SvgIcon name="order" :size="32" /></div>
            <div class="export-info">
              <h4>订单数据</h4>
              <p>导出所有订单信息，包含订单号、金额、状态、收货信息等</p>
              <div class="export-filter">
                <el-select v-model="orderStatus" placeholder="订单状态" clearable size="small" style="width:120px">
                  <el-option label="待支付" :value="0" />
                  <el-option label="已支付" :value="1" />
                  <el-option label="已发货" :value="2" />
                  <el-option label="已完成" :value="3" />
                  <el-option label="已取消" :value="4" />
                </el-select>
                <el-date-picker v-model="orderDateRange" type="daterange" range-separator="至"
                  start-placeholder="开始日期" end-placeholder="结束日期" size="small"
                  value-format="YYYY-MM-DD" style="width:240px" />
              </div>
            </div>
            <el-button type="primary" :loading="exporting.orders" @click="handleExport('orders')">
              <SvgIcon name="download" :size="16" /> 导出
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover" class="export-card">
          <div class="export-item">
            <div class="export-icon jade"><SvgIcon name="users" :size="32" /></div>
            <div class="export-info">
              <h4>用户数据</h4>
              <p>导出所有注册用户信息，包含用户名、联系方式、注册时间等</p>
            </div>
            <el-button type="primary" :loading="exporting.users" @click="handleExport('users')">
              <SvgIcon name="download" :size="16" /> 导出
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover" class="export-card">
          <div class="export-item">
            <div class="export-icon turmeric"><SvgIcon name="product" :size="32" /></div>
            <div class="export-info">
              <h4>商品数据</h4>
              <p>导出所有商品信息，包含名称、价格、库存、销量等</p>
            </div>
            <el-button type="primary" :loading="exporting.products" @click="handleExport('products')">
              <SvgIcon name="download" :size="16" /> 导出
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover" class="export-card">
          <div class="export-item">
            <div class="export-icon ink"><SvgIcon name="comment" :size="32" /></div>
            <div class="export-info">
              <h4>评价数据</h4>
              <p>导出所有商品评价信息，包含评分、内容、商家回复等</p>
            </div>
            <el-button type="primary" :loading="exporting.reviews" @click="handleExport('reviews')">
              <SvgIcon name="download" :size="16" /> 导出
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getToken } from 'shared/utils/index.js'

const API_BASE = ''
const orderStatus = ref(null)
const orderDateRange = ref(null)
const exporting = reactive({ orders: false, users: false, products: false, reviews: false })

function buildUrl(path, params = {}) {
  const url = new URL(path, window.location.origin)
  Object.entries(params).forEach(([k, v]) => {
    if (v !== null && v !== undefined && v !== '') url.searchParams.append(k, v)
  })
  return url.toString()
}

async function handleExport(type) {
  exporting[type] = true
  try {
    let url
    const now = new Date().toISOString().slice(0, 10)
    let filename

    switch (type) {
      case 'orders':
        url = buildUrl('/api/admin/export/orders', {
          status: orderStatus.value,
          startDate: orderDateRange.value?.[0],
          endDate: orderDateRange.value?.[1]
        })
        filename = `orders_${now}.csv`
        break
      case 'users':
        url = buildUrl('/api/admin/export/users')
        filename = `users_${now}.csv`
        break
      case 'products':
        url = buildUrl('/api/admin/export/products')
        filename = `products_${now}.csv`
        break
      case 'reviews':
        url = buildUrl('/api/admin/export/reviews')
        filename = `reviews_${now}.csv`
        break
    }

    const token = getToken()
    const res = await fetch(url, {
      headers: { 'Authorization': token ? `Bearer ${token}` : '' }
    })

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`)
    }

    const blob = await res.blob()
    const a = document.createElement('a')
    a.href = window.URL.createObjectURL(blob)
    a.download = filename
    a.click()
    window.URL.revokeObjectURL(a.href)
    ElMessage.success('导出成功')
  } catch (e) {
    ElMessage.error('导出失败')
    console.error(e)
  } finally {
    exporting[type] = false
  }
}
</script>

<style scoped>
.data-export { padding: 20px; }
.data-export h3 { margin: 0 0 4px; }
.desc { color: #999; font-size: 14px; margin: 0 0 20px; }
.export-card { margin-bottom: 16px; }
.export-item { display: flex; align-items: flex-start; gap: 16px; }
.export-icon { width: 56px; height: 56px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.export-icon.ochre { background: rgba(139,69,19,0.1); color: #8B4513; }
.export-icon.jade { background: rgba(46,139,87,0.1); color: #2E8B57; }
.export-icon.turmeric { background: rgba(201,168,108,0.15); color: #C9A86C; }
.export-icon.ink { background: rgba(47,79,79,0.1); color: #2F4F4F; }
.export-info { flex: 1; }
.export-info h4 { margin: 0 0 4px; font-size: 15px; color: #4A4036; }
.export-info p { margin: 0 0 8px; font-size: 13px; color: #999; }
.export-filter { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
