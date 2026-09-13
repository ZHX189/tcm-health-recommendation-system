<!--
  健康档案查看页面（员工端）
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="health-record-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">健康档案查看</h1>
      <p class="page-desc">查看用户健康档案信息，辅助养生方案推荐</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索用户姓名"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <SvgIcon name="search" :size="16" />
        </template>
      </el-input>
      <el-select v-model="searchForm.constitutionType" placeholder="体质类型" clearable @change="handleSearch">
        <el-option label="平和质" value="平和质" />
        <el-option label="气虚质" value="气虚质" />
        <el-option label="阳虚质" value="阳虚质" />
        <el-option label="阴虚质" value="阴虚质" />
        <el-option label="痰湿质" value="痰湿质" />
        <el-option label="湿热质" value="湿热质" />
        <el-option label="血瘀质" value="血瘀质" />
        <el-option label="气郁质" value="气郁质" />
        <el-option label="特禀质" value="特禀质" />
      </el-select>
      <el-button type="primary" @click="handleSearch">
        <SvgIcon name="search" :size="16" class="mr-8" />
        搜索
      </el-button>
      <el-button @click="handleReset">
        <SvgIcon name="refresh" :size="16" class="mr-8" />
        重置
      </el-button>
    </div>

    <!-- 表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="realName" label="姓名" min-width="100">
          <template #default="{ row }">
            {{ row.realName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="70" align="center">
          <template #default="{ row }">
            {{ row.age || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="身高/体重" width="120" align="center">
          <template #default="{ row }">
            <span v-if="row.height || row.weight">{{ row.height || '-' }}cm / {{ row.weight || '-' }}kg</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="bloodType" label="血型" width="70" align="center">
          <template #default="{ row }">
            {{ row.bloodType || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="constitutionType" label="体质类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.constitutionType" size="small" type="primary">{{ row.constitutionType }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="medicalHistory" label="病史" min-width="120" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.medicalHistory || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="allergyHistory" label="过敏史" min-width="120" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.allergyHistory || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn view" @click="handleView(row)">
                <SvgIcon name="view" :size="14" />
                <span>查看</span>
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="健康档案详情"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="档案ID">{{ currentRecord.id }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentRecord.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ currentRecord.age || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ currentRecord.height ? currentRecord.height + ' cm' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="体重">{{ currentRecord.weight ? currentRecord.weight + ' kg' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="血型">{{ currentRecord.bloodType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="体质类型">
          <el-tag v-if="currentRecord.constitutionType" size="small" type="primary">{{ currentRecord.constitutionType }}</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="病史" :span="2">{{ currentRecord.medicalHistory || '-' }}</el-descriptions-item>
        <el-descriptions-item label="过敏史" :span="2">{{ currentRecord.allergyHistory || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家族病史" :span="2">{{ currentRecord.familyHistory || '-' }}</el-descriptions-item>
        <el-descriptions-item label="生活习惯" :span="2">{{ currentRecord.lifestyle || '-' }}</el-descriptions-item>
        <el-descriptions-item label="饮食偏好">{{ currentRecord.dietPreference || '-' }}</el-descriptions-item>
        <el-descriptions-item label="睡眠质量">{{ currentRecord.sleepQuality || '-' }}</el-descriptions-item>
        <el-descriptions-item label="运动频率">{{ currentRecord.exerciseFrequency || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentRecord.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(currentRecord.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDateTime(currentRecord.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getStaffHealthRecordList } from 'shared/api/staff.js'
import { formatDateTime } from 'shared/utils/index.js'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentRecord = ref({})

const searchForm = reactive({
  keyword: '',
  constitutionType: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取列表
async function fetchList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword || undefined,
      constitutionType: searchForm.constitutionType || undefined
    }
    const data = await getStaffHealthRecordList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('获取健康档案列表失败:', error)
    tableData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  fetchList()
}

// 重置
function handleReset() {
  searchForm.keyword = ''
  searchForm.constitutionType = ''
  pagination.page = 1
  fetchList()
}

// 查看详情
function handleView(row) {
  currentRecord.value = row
  detailVisible.value = true
}

// 分页
function handleSizeChange() {
  pagination.page = 1
  fetchList()
}

function handlePageChange() {
  fetchList()
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.health-record-page {
  min-height: 100%;
}

.search-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-bar .el-input {
  width: 240px;
}

.search-bar .el-select {
  width: 150px;
}
</style>
