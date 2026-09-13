<!--
  订单管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="order-manage-page">
    <div class="page-header">
      <h1 class="page-title">订单管理</h1>
      <p class="page-desc">管理平台订单，查看订单详情，处理发货等操作</p>
    </div>

    <div class="search-bar">
      <el-input v-model="searchForm.orderNo" placeholder="订单编号" clearable @keyup.enter="handleSearch">
        <template #prefix><SvgIcon name="search" :size="16" /></template>
      </el-input>
      <el-select v-model="searchForm.status" placeholder="订单状态" clearable @change="handleSearch">
        <el-option label="待支付" :value="0" />
        <el-option label="已支付" :value="1" />
        <el-option label="已发货" :value="2" />
        <el-option label="已完成" :value="3" />
        <el-option label="已取消" :value="4" />
      </el-select>
      <el-button type="primary" @click="handleSearch">
        <SvgIcon name="search" :size="16" class="mr-8" />搜索
      </el-button>
      <el-button @click="handleReset">
        <SvgIcon name="refresh" :size="16" class="mr-8" />重置
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="orderNo" label="订单编号" min-width="200">
          <template #default="{ row }">
            <span class="order-no">{{ row.orderNo }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户" min-width="120">
          <template #default="{ row }">
            {{ row.username || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120" align="right">
          <template #default="{ row }">
            <span class="price-text">{{ formatMoney(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" min-width="100">
          <template #default="{ row }">
            {{ row.receiverName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="receiverPhone" label="联系电话" width="130">
          <template #default="{ row }">
            {{ row.receiverPhone || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="receiverAddress" label="收货地址" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.receiverAddress || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn view" @click="handleView(row)">
                <SvgIcon name="view" :size="14" /><span>详情</span>
              </button>
              <button v-if="row.status === 1" class="action-btn edit" @click="handleShip(row)">
                <SvgIcon name="ship" :size="14" /><span>发货</span>
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="tcm-pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 订单详情 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)" size="small">
            {{ getStatusLabel(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下单用户">{{ currentOrder.username }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">
          <span class="price-text">{{ formatMoney(currentOrder.totalAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrder.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ formatDateTime(currentOrder.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ formatDateTime(currentOrder.payTime) || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div class="order-items" v-if="currentOrder.items?.length">
        <h4 class="items-title">订单商品</h4>
        <el-table :data="currentOrder.items" border size="small">
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="productPrice" label="单价" width="100" align="right">
            <template #default="{ row }">{{ formatMoney(row.productPrice) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" align="center" />
          <el-table-column prop="totalPrice" label="小计" width="100" align="right">
            <template #default="{ row }">{{ formatMoney(row.totalPrice) }}</template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog v-model="shipVisible" title="订单发货" width="500px">
      <el-form ref="shipFormRef" :model="shipForm" :rules="shipRules" label-width="80px">
        <el-form-item label="快递公司" prop="expressCompany">
          <el-select v-model="shipForm.expressCompany" placeholder="请选择快递公司" style="width: 100%">
            <el-option label="顺丰速运" value="顺丰速运" />
            <el-option label="中通快递" value="中通快递" />
            <el-option label="圆通速递" value="圆通速递" />
            <el-option label="韵达快递" value="韵达快递" />
            <el-option label="申通快递" value="申通快递" />
          </el-select>
        </el-form-item>
        <el-form-item label="快递单号" prop="expressNo">
          <el-input v-model="shipForm.expressNo" placeholder="请输入快递单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipVisible = false">取消</el-button>
        <el-button type="primary" :loading="shipLoading" @click="handleSubmitShip">
          <SvgIcon name="ship" :size="16" class="mr-8" />确认发货
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminOrderList, getAdminOrderDetail, shipOrder } from 'shared/api/admin.js'
import { formatDateTime, formatMoney } from 'shared/utils/index.js'
import { ORDER_STATUS_LABEL } from 'shared/constants/index.js'

const loading = ref(false)
const shipLoading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const shipVisible = ref(false)
const currentOrder = ref({})
const shipFormRef = ref(null)

const searchForm = reactive({ orderNo: '', status: null })
const shipForm = reactive({ orderId: null, expressCompany: '', expressNo: '' })
const shipRules = {
  expressCompany: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
  expressNo: [{ required: true, message: '请输入快递单号', trigger: 'blur' }]
}
const pagination = reactive({ page: 1, size: 10, total: 0 })

function getStatusLabel(status) {
  return ORDER_STATUS_LABEL[status] || '未知'
}

function getStatusType(status) {
  const types = { 0: 'warning', 1: '', 2: 'success', 3: 'info', 4: 'danger' }
  return types[status] || 'info'
}

async function fetchOrderList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      orderNo: searchForm.orderNo || undefined,
      status: searchForm.status ?? undefined
    }
    const data = await getAdminOrderList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    tableData.value = [
      { id: 1, orderNo: 'TCM202602030001', username: '养生达人', totalAmount: 598.00, status: 1, receiverName: '张三', receiverPhone: '13800138001', receiverAddress: '北京市朝阳区xxx街道', createTime: '2026-02-03 10:30:00' },
      { id: 2, orderNo: 'TCM202602030002', username: '健康生活', totalAmount: 299.00, status: 2, receiverName: '李四', receiverPhone: '13800138002', receiverAddress: '上海市浦东新区xxx路', createTime: '2026-02-03 11:20:00' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  fetchOrderList()
}

function handleReset() {
  searchForm.orderNo = ''
  searchForm.status = null
  handleSearch()
}

async function handleView(row) {
  try {
    const data = await getAdminOrderDetail(row.id)
    currentOrder.value = data || row
  } catch (error) {
    currentOrder.value = row
  }
  detailVisible.value = true
}

function handleShip(row) {
  shipForm.orderId = row.id
  shipForm.expressCompany = ''
  shipForm.expressNo = ''
  shipVisible.value = true
}

async function handleSubmitShip() {
  if (!shipFormRef.value) return
  try {
    await shipFormRef.value.validate()
    shipLoading.value = true
    await shipOrder(shipForm.orderId, shipForm.expressCompany, shipForm.expressNo)
    ElMessage.success('发货成功')
    shipVisible.value = false
    fetchOrderList()
  } catch (error) {
    console.error(error)
  } finally {
    shipLoading.value = false
  }
}

function handleSizeChange() { pagination.page = 1; fetchOrderList() }
function handlePageChange() { fetchOrderList() }

onMounted(() => fetchOrderList())
</script>

<style scoped>
.order-manage-page { min-height: 100%; }
.search-bar { display: flex; gap: 16px; margin-bottom: 20px; flex-wrap: wrap; }
.search-bar .el-input { width: 200px; }
.search-bar .el-select { width: 150px; }

.order-no {
  font-family: monospace;
  font-weight: 500;
  color: var(--tcm-primary);
}

.price-text { color: var(--tcm-vermilion); font-weight: 500; }
.price-text::before { content: '¥'; font-size: 12px; }
.order-items { margin-top: 20px; }
.items-title { font-family: var(--tcm-font-title); font-size: 14px; margin-bottom: 12px; color: var(--tcm-text-primary); }
</style>
