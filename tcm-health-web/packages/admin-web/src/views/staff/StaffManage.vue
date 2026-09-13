<!--
  员工管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="staff-manage-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">员工管理</h1>
      <p class="page-desc">管理平台员工账号，创建新员工，控制员工状态</p>
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
      <el-select v-model="searchForm.status" placeholder="员工状态" clearable @change="handleSearch">
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
      <el-button type="primary" @click="handleAdd">
        <SvgIcon name="add" :size="16" class="mr-8" />
        新增员工
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
        <el-table-column prop="createTime" label="创建时间" width="170">
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

    <!-- 新增员工对话框 -->
    <el-dialog
      v-model="addVisible"
      title="新增员工"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        :rules="addRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" placeholder="请输入用户名">
            <template #prefix>
              <SvgIcon name="user" :size="16" />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password" show-password placeholder="请输入密码">
            <template #prefix>
              <SvgIcon name="lock" :size="16" />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="addForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addForm.phone" placeholder="请输入手机号">
            <template #prefix>
              <SvgIcon name="phone" :size="16" />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" placeholder="请输入邮箱">
            <template #prefix>
              <SvgIcon name="email" :size="16" />
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitAdd">
          <SvgIcon name="check" :size="16" class="mr-8" />
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 员工详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="员工详情"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="员工ID">{{ currentStaff.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentStaff.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentStaff.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentStaff.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentStaff.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentStaff.status === 1 ? 'success' : 'danger'" size="small">
            {{ currentStaff.status === 1 ? '已启用' : '已禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(currentStaff.createTime) }}
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
import { getStaffList, createStaff, updateStaffStatus } from 'shared/api/admin.js'
import { formatDateTime } from 'shared/utils/index.js'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const addVisible = ref(false)
const detailVisible = ref(false)
const currentStaff = ref({})
const addFormRef = ref(null)

const searchForm = reactive({
  keyword: '',
  status: null
})

const addForm = reactive({
  username: '',
  password: '',
  nickname: '',
  phone: '',
  email: ''
})

const addRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取员工列表
async function fetchStaffList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status ?? undefined
    }
    const data = await getStaffList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('获取员工列表失败:', error)
    // 使用模拟数据
    tableData.value = [
      { id: 1, username: 'staff001', nickname: '张三', phone: '13900139001', email: 'staff1@example.com', status: 1, createTime: '2026-01-10 10:00:00' },
      { id: 2, username: 'staff002', nickname: '李四', phone: '13900139002', email: 'staff2@example.com', status: 1, createTime: '2026-01-12 14:30:00' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  fetchStaffList()
}

// 重置
function handleReset() {
  searchForm.keyword = ''
  searchForm.status = null
  pagination.page = 1
  fetchStaffList()
}

// 新增
function handleAdd() {
  Object.assign(addForm, {
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: ''
  })
  addVisible.value = true
}

// 提交新增
async function handleSubmitAdd() {
  if (!addFormRef.value) return
  
  try {
    await addFormRef.value.validate()
    submitLoading.value = true
    
    await createStaff(addForm)
    ElMessage.success('创建成功')
    addVisible.value = false
    fetchStaffList()
  } catch (error) {
    console.error('创建失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 查看详情
function handleView(row) {
  currentStaff.value = row
  detailVisible.value = true
}

// 切换状态
async function handleToggleStatus(row) {
  const action = row.status === 1 ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(`确定要${action}员工"${row.username}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const newStatus = row.status === 1 ? 0 : 1
    await updateStaffStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    fetchStaffList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

// 分页
function handleSizeChange() {
  pagination.page = 1
  fetchStaffList()
}

function handlePageChange() {
  fetchStaffList()
}

onMounted(() => {
  fetchStaffList()
})
</script>

<style scoped>
.staff-manage-page {
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
