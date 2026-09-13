<!--
  用户管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="user-manage-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <p class="page-desc">管理平台注册用户，查看用户信息，控制用户状态</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索用户名/昵称/手机号"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <SvgIcon name="search" :size="16" />
        </template>
      </el-input>
      <el-select v-model="searchForm.status" placeholder="用户状态" clearable @change="handleSearch">
        <el-option label="已启用" :value="1" />
        <el-option label="已禁用" :value="0" />
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
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="nickname" label="昵称" min-width="120">
          <template #default="{ row }">
            {{ row.nickname || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130">
          <template #default="{ row }">
            {{ row.phone || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180">
          <template #default="{ row }">
            {{ row.email || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '已启用' : '已禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn view" @click="handleView(row)">
                <SvgIcon name="view" :size="14" />
                <span>查看</span>
              </button>
              <button 
                class="action-btn" 
                :class="row.status === 1 ? 'delete' : 'edit'"
                @click="handleToggleStatus(row)"
              >
                <SvgIcon :name="row.status === 1 ? 'disable' : 'enable'" :size="14" />
                <span>{{ row.status === 1 ? '禁用' : '启用' }}</span>
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

    <!-- 用户详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="用户详情"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户ID">{{ currentUser.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentUser.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          {{ currentUser.gender === 1 ? '男' : currentUser.gender === 2 ? '女' : '未知' }}
        </el-descriptions-item>
        <el-descriptions-item label="生日">{{ currentUser.birthday || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'" size="small">
            {{ currentUser.status === 1 ? '已启用' : '已禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">
          {{ formatDateTime(currentUser.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="最后登录">
          {{ formatDateTime(currentUser.lastLoginTime) || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, updateUserStatus } from 'shared/api/admin.js'
import { formatDateTime } from 'shared/utils/index.js'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentUser = ref({})

const searchForm = reactive({
  keyword: '',
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取用户列表
async function fetchUserList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status ?? undefined
    }
    const data = await getUserList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('获取用户列表失败:', error)
    // 使用模拟数据
    tableData.value = [
      { id: 1, username: 'user001', nickname: '养生达人', phone: '13800138001', email: 'user1@example.com', status: 1, createTime: '2026-01-15 10:30:00' },
      { id: 2, username: 'user002', nickname: '健康生活', phone: '13800138002', email: 'user2@example.com', status: 1, createTime: '2026-01-20 14:20:00' },
      { id: 3, username: 'user003', nickname: '中医爱好者', phone: '13800138003', email: 'user3@example.com', status: 0, createTime: '2026-01-25 09:15:00' }
    ]
    pagination.total = 3
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  fetchUserList()
}

// 重置
function handleReset() {
  searchForm.keyword = ''
  searchForm.status = null
  pagination.page = 1
  fetchUserList()
}

// 查看详情
function handleView(row) {
  currentUser.value = row
  detailVisible.value = true
}

// 切换状态
async function handleToggleStatus(row) {
  const action = row.status === 1 ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(`确定要${action}用户"${row.username}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const newStatus = row.status === 1 ? 0 : 1
    await updateUserStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

// 分页
function handleSizeChange() {
  pagination.page = 1
  fetchUserList()
}

function handlePageChange() {
  fetchUserList()
}

onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.user-manage-page {
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
