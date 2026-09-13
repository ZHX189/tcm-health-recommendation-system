<template>
  <div class="stock-manage">
    <div class="page-header">
      <h3>库存管理</h3>
      <div class="header-actions">
        <el-input v-model="keyword" placeholder="搜索商品..." clearable style="width: 200px" @clear="loadStocks" @keyup.enter="loadStocks" />
        <el-checkbox v-model="warningOnly" @change="loadStocks">仅显示预警</el-checkbox>
      </div>
    </div>

    <el-table :data="stocks" border stripe>
      <el-table-column prop="product_name" label="商品名称" min-width="180">
        <template #default="{ row }">
          <div style="display:flex;align-items:center;gap:8px">
            <el-image :src="getImageUrl(row.main_image)" style="width:40px;height:40px;border-radius:4px" fit="cover" />
            <span>{{ row.product_name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="当前库存" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="row.stock <= row.warning_stock ? 'danger' : 'success'">{{ row.stock }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="warning_stock" label="预警值" width="100" align="center" />
      <el-table-column prop="sales" label="销量" width="100" align="center" />
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">调整</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:center;margin-top:16px">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total"
                     layout="prev, pager, next" @current-change="loadStocks" />
    </div>

    <!-- 库存变更记录 -->
    <div style="margin-top:32px">
      <h3>库存变更记录</h3>
      <el-table :data="logs" border stripe>
        <el-table-column prop="product_name" label="商品" min-width="150" />
        <el-table-column prop="change_type" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.change_type === 1 ? 'success' : row.change_type === 2 ? 'danger' : 'warning'">
              {{ row.change_type === 1 ? '入库' : row.change_type === 2 ? '出库' : '调整' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="change_quantity" label="变更数量" width="100" align="center" />
        <el-table-column prop="before_stock" label="变更前" width="90" align="center" />
        <el-table-column prop="after_stock" label="变更后" width="90" align="center" />
        <el-table-column prop="remark" label="备注" min-width="120" />
        <el-table-column prop="create_time" label="时间" width="170" />
      </el-table>
      <div style="display:flex;justify-content:center;margin-top:16px">
        <el-pagination v-model:current-page="logPageNum" v-model:page-size="logPageSize" :total="logTotal"
                       layout="prev, pager, next" @current-change="loadLogs" />
      </div>
    </div>

    <!-- 调整库存对话框 -->
    <el-dialog v-model="editVisible" title="调整库存" width="400px">
      <el-form label-width="80px">
        <el-form-item label="商品">{{ editRow?.product_name }}</el-form-item>
        <el-form-item label="当前库存">{{ editRow?.stock }}</el-form-item>
        <el-form-item label="新库存">
          <el-input-number v-model="newStock" :min="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editRemark" placeholder="调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStaffStockList, updateStaffStock, getStaffStockLogs } from 'shared/api/staff.js'
import { ElMessage } from 'element-plus'

const API_BASE_URL = ''

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return `${API_BASE_URL}${url.startsWith('/') ? '' : '/'}${url}`
}

const stocks = ref([])
const keyword = ref('')
const warningOnly = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const logs = ref([])
const logPageNum = ref(1)
const logPageSize = ref(10)
const logTotal = ref(0)
const editVisible = ref(false)
const editRow = ref(null)
const newStock = ref(0)
const editRemark = ref('')

const loadStocks = async () => {
  try {
    const res = await getStaffStockList({ pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value, warningOnly: warningOnly.value || undefined })
    stocks.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { console.error(e) }
}

const loadLogs = async () => {
  try {
    const res = await getStaffStockLogs({ pageNum: logPageNum.value, pageSize: logPageSize.value })
    logs.value = res?.records || []
    logTotal.value = res?.total || 0
  } catch (e) { console.error(e) }
}

const openEdit = (row) => {
  editRow.value = row
  newStock.value = row.stock
  editRemark.value = ''
  editVisible.value = true
}

const submitEdit = async () => {
  try {
    await updateStaffStock(editRow.value.product_id, newStock.value, editRemark.value)
    ElMessage.success('库存调整成功')
    editVisible.value = false
    loadStocks()
    loadLogs()
  } catch (e) { ElMessage.error('调整失败') }
}

onMounted(() => { loadStocks(); loadLogs() })
</script>

<style scoped>
.stock-manage { padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { margin: 0; }
.header-actions { display: flex; gap: 12px; align-items: center; }
</style>
