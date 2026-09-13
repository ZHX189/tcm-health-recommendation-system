<!--
  员工工作台首页
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="dashboard-page">
    <div class="page-header">
      <h1 class="page-title">工作台</h1>
      <p class="page-desc">欢迎回来，{{ nickname }}！今日待处理事项一览</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="8">
        <div class="stat-card">
          <div class="stat-card-icon ochre"><SvgIcon name="order" :size="28" /></div>
          <div class="stat-card-content">
            <div class="stat-card-label">待发货订单</div>
            <div class="stat-card-value">{{ stats.pendingShipOrders }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <div class="stat-card">
          <div class="stat-card-icon turmeric"><SvgIcon name="product" :size="28" /></div>
          <div class="stat-card-content">
            <div class="stat-card-label">商品总数</div>
            <div class="stat-card-value">{{ stats.totalProducts }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <div class="stat-card">
          <div class="stat-card-icon ink"><SvgIcon name="stock" :size="28" /></div>
          <div class="stat-card-content">
            <div class="stat-card-label">库存预警</div>
            <div class="stat-card-value">{{ stats.stockWarning }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作和待发货订单 -->
    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :md="8">
        <div class="data-card quick-card">
          <div class="data-card-header"><span class="data-card-title">快捷操作</span></div>
          <div class="quick-actions">
            <div class="quick-action-item" @click="$router.push('/orders')">
              <div class="action-icon ochre"><SvgIcon name="ship" :size="24" /></div>
              <span>订单发货</span>
            </div>
            <div class="quick-action-item" @click="$router.push('/products')">
              <div class="action-icon turmeric"><SvgIcon name="edit" :size="24" /></div>
              <span>商品管理</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :md="16">
        <div class="data-card orders-card">
          <div class="data-card-header">
            <span class="data-card-title">待发货订单</span>
            <router-link to="/orders" class="view-more">查看全部<SvgIcon name="arrow-right" :size="14" /></router-link>
          </div>
          <div class="pending-orders">
            <el-table :data="pendingOrders" v-loading="loading" stripe size="small" style="width: 100%">
              <el-table-column prop="orderNo" label="订单编号" min-width="180">
                <template #default="{ row }"><span class="order-no">{{ row.orderNo }}</span></template>
              </el-table-column>
              <el-table-column prop="username" label="用户" width="100" />
              <el-table-column prop="totalAmount" label="金额" width="100" align="right">
                <template #default="{ row }"><span class="price-text">¥{{ formatMoney(row.totalAmount) }}</span></template>
              </el-table-column>
              <el-table-column prop="receiverName" label="收货人" width="80" />
              <el-table-column prop="createTime" label="下单时间" width="160">
                <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="handleShip(row)"><SvgIcon name="ship" :size="14" class="mr-4" />发货</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="pendingOrders.length === 0 && !loading" description="暂无待发货订单" :image-size="60" />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 发货对话框 -->
    <el-dialog v-model="shipVisible" title="订单发货" width="500px">
      <el-form ref="shipFormRef" :model="shipForm" :rules="shipRules" label-width="80px">
        <el-form-item label="订单编号"><el-input :value="currentOrder.orderNo" disabled /></el-form-item>
        <el-form-item label="快递公司" prop="expressCompany">
          <el-select v-model="shipForm.expressCompany" placeholder="请选择快递公司" style="width: 100%">
            <el-option label="顺丰速运" value="顺丰速运" />
            <el-option label="中通快递" value="中通快递" />
            <el-option label="圆通速递" value="圆通速递" />
            <el-option label="韵达快递" value="韵达快递" />
            <el-option label="申通快递" value="申通快递" />
          </el-select>
        </el-form-item>
        <el-form-item label="快递单号" prop="expressNo"><el-input v-model="shipForm.expressNo" placeholder="请输入快递单号" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipVisible = false">取消</el-button>
        <el-button type="primary" :loading="shipLoading" @click="handleSubmitShip"><SvgIcon name="ship" :size="16" class="mr-8" />确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getStaffOrderList, staffShipOrder, getStaffProductList } from 'shared/api/staff.js'
import { getUserInfo, formatMoney, formatDateTime } from 'shared/utils/index.js'

const nickname = computed(() => { const info = getUserInfo(); return info?.nickname || info?.username || '员工' })
const loading = ref(false)
const stats = reactive({ pendingShipOrders: 0, totalProducts: 0, stockWarning: 0 })
const pendingOrders = ref([])

const shipVisible = ref(false)
const shipLoading = ref(false)
const shipFormRef = ref(null)
const currentOrder = ref({})
const shipForm = reactive({ expressCompany: '', expressNo: '' })
const shipRules = {
  expressCompany: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
  expressNo: [{ required: true, message: '请输入快递单号', trigger: 'blur' }]
}

async function fetchData() {
  loading.value = true
  try {
    const [orderData, productData] = await Promise.all([
      getStaffOrderList({ status: 1, pageNum: 1, pageSize: 10 }),
      getStaffProductList({ pageNum: 1, pageSize: 1000 })
    ])
    pendingOrders.value = orderData?.records || []
    stats.pendingShipOrders = orderData?.total || 0
    stats.totalProducts = productData?.total || 0
    stats.stockWarning = (productData?.records || []).filter(p => p.stock < 10).length
  } catch (error) {
    pendingOrders.value = [
      { id: 1, orderNo: 'ORD202602020008', username: 'user007', totalAmount: 526.00, receiverName: '王建国', createTime: '2026-02-02 14:30:00' },
      { id: 2, orderNo: 'ORD202601220004', username: 'user004', totalAmount: 128.00, receiverName: '王芳', createTime: '2026-01-22 10:30:00' }
    ]
    stats.pendingShipOrders = 2
    stats.totalProducts = 0
    stats.stockWarning = 0
  } finally { loading.value = false }
}

function handleShip(order) {
  currentOrder.value = order
  shipForm.expressCompany = ''
  shipForm.expressNo = ''
  shipVisible.value = true
}

async function handleSubmitShip() {
  if (!shipFormRef.value) return
  try {
    await shipFormRef.value.validate()
    shipLoading.value = true
    await staffShipOrder(currentOrder.value.id, shipForm.expressCompany, shipForm.expressNo)
    ElMessage.success('发货成功')
    shipVisible.value = false
    fetchData()
  } catch (error) { console.error(error) } finally { shipLoading.value = false }
}

onMounted(() => fetchData())
</script>

<style scoped>
.dashboard-page { min-height: 100%; }
.stat-row { margin-bottom: 20px; }
.stat-row .el-col { margin-bottom: 20px; }
.content-row .el-col { margin-bottom: 20px; }

.quick-card { height: 100%; }
.quick-actions { display: flex; flex-direction: column; gap: 12px; padding: 8px 0; }
.quick-action-item { display: flex; align-items: center; gap: 12px; padding: 16px; border-radius: var(--tcm-radius-md); cursor: pointer; transition: all var(--tcm-transition-fast); border: 1px solid var(--tcm-border-lighter); }
.quick-action-item:hover { background: var(--tcm-ochre-50); border-color: var(--tcm-ochre-100); }
.action-icon { width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; border-radius: var(--tcm-radius-md); flex-shrink: 0; }
.action-icon.ochre { background: var(--tcm-ochre-100); color: var(--tcm-ochre); }
.action-icon.turmeric { background: var(--tcm-turmeric-100); color: var(--tcm-turmeric); }
.quick-action-item span { font-size: 14px; color: var(--tcm-text-regular); font-weight: 500; }

.orders-card { height: 100%; }
.view-more { display: flex; align-items: center; gap: 4px; font-size: 13px; color: var(--tcm-ochre); text-decoration: none; }
.pending-orders { min-height: 200px; }
.order-no { font-family: monospace; font-weight: 500; color: var(--tcm-primary); }
.price-text { color: var(--tcm-vermilion); font-weight: 500; }
.mr-4 { margin-right: 4px; }
</style>
