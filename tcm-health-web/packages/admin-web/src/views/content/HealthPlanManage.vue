<!--
  养生方案管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="health-plan-manage-page">
    <div class="page-header">
      <h1 class="page-title">养生方案管理</h1>
      <p class="page-desc">管理养生方案，根据体质和季节提供专业调理建议</p>
    </div>

    <div class="search-bar">
      <el-input v-model="searchForm.keyword" placeholder="搜索方案名称" clearable @keyup.enter="handleSearch">
        <template #prefix><SvgIcon name="search" :size="16" /></template>
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
        <SvgIcon name="search" :size="16" class="mr-8" />搜索
      </el-button>
      <el-button type="primary" @click="handleAdd">
        <SvgIcon name="add" :size="16" class="mr-8" />新增方案
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="title" label="方案名称" min-width="250">
          <template #default="{ row }">
            <span class="plan-title">{{ row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="constitutionType" label="体质类型" width="120" align="center" />
        <el-table-column prop="season" label="适用季节" width="120" align="center">
          <template #default="{ row }">
            {{ row.season || '四季通用' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : row.status === 2 ? '待审核' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button v-if="row.status !== 1" class="action-btn success" @click="handlePublish(row)">
                <SvgIcon name="check" :size="14" /><span>发布</span>
              </button>
              <button v-if="row.status === 1" class="action-btn warning" @click="handleUnpublish(row)">
                <SvgIcon name="disable" :size="14" /><span>下架</span>
              </button>
              <button class="action-btn edit" @click="handleEdit(row)">
                <SvgIcon name="edit" :size="14" /><span>编辑</span>
              </button>
              <button class="action-btn delete" @click="handleDelete(row)">
                <SvgIcon name="delete" :size="14" /><span>删除</span>
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="tcm-pagination">
        <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange" @current-change="handlePageChange" />
      </div>
    </div>

    <el-dialog v-model="formVisible" :title="isEdit ? '编辑方案' : '新增方案'" width="700px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="方案名称" prop="title">
              <el-input v-model="formData.title" placeholder="请输入方案名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体质类型" prop="constitutionType">
              <el-select v-model="formData.constitutionType" placeholder="请选择体质类型" style="width: 100%">
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
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="适用季节">
          <el-select v-model="formData.season" placeholder="请选择季节" style="width: 200px">
            <el-option label="春季" value="春季" />
            <el-option label="夏季" value="夏季" />
            <el-option label="秋季" value="秋季" />
            <el-option label="冬季" value="冬季" />
            <el-option label="四季通用" value="四季通用" />
          </el-select>
        </el-form-item>
        <el-form-item label="方案内容" prop="content">
          <RichTextEditor 
            v-model="formData.content" 
            placeholder="请输入养生方案详细内容..."
            height="300px"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          <SvgIcon name="check" :size="16" class="mr-8" />确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminHealthPlanList, addHealthPlan, updateHealthPlan, deleteHealthPlan, updateHealthPlanStatus } from 'shared/api/admin.js'
import RichTextEditor from '@/components/RichTextEditor.vue'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const formVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({ keyword: '', constitutionType: '' })
const formData = reactive({ id: null, title: '', constitutionType: '', season: '', content: '' })
const formRules = {
  title: [{ required: true, message: '请输入方案名称', trigger: 'blur' }],
  constitutionType: [{ required: true, message: '请选择体质类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入方案内容', trigger: 'blur' }]
}
const pagination = reactive({ page: 1, size: 10, total: 0 })

async function fetchList() {
  loading.value = true
  try {
    const params = { pageNum: pagination.page, pageSize: pagination.size, ...searchForm }
    const data = await getAdminHealthPlanList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    tableData.value = [
      { id: 1, title: '气虚体质春季调养方案', constitutionType: '气虚质', season: '春季', status: 1 },
      { id: 2, title: '阳虚体质冬季温补方案', constitutionType: '阳虚质', season: '冬季', status: 1 }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

function handleSearch() { pagination.page = 1; fetchList() }
function handleAdd() {
  isEdit.value = false
  Object.assign(formData, { id: null, title: '', constitutionType: '', season: '', content: '' })
  formVisible.value = true
}
function handleEdit(row) {
  isEdit.value = true
  Object.assign(formData, row)
  formVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    if (isEdit.value) {
      await updateHealthPlan(formData.id, formData)
      ElMessage.success('修改成功')
    } else {
      await addHealthPlan(formData)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    fetchList()
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除"${row.title}"吗？`, '提示', { type: 'warning' })
    await deleteHealthPlan(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handlePublish(row) {
  try {
    await ElMessageBox.confirm(`确定发布"${row.title}"吗？`, '提示', { type: 'info' })
    await updateHealthPlanStatus(row.id, 1)
    ElMessage.success('发布成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleUnpublish(row) {
  try {
    await ElMessageBox.confirm(`确定下架"${row.title}"吗？`, '提示', { type: 'warning' })
    await updateHealthPlanStatus(row.id, 0)
    ElMessage.success('已下架')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

function handleSizeChange() { pagination.page = 1; fetchList() }
function handlePageChange() { fetchList() }

onMounted(() => fetchList())
</script>

<style scoped>
.health-plan-manage-page { min-height: 100%; }
.search-bar { display: flex; gap: 16px; margin-bottom: 20px; flex-wrap: wrap; }
.search-bar .el-input { width: 200px; }
.search-bar .el-select { width: 150px; }
.plan-title { font-weight: 500; color: var(--tcm-text-primary); }
.table-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.action-btn { display: inline-flex; align-items: center; gap: 4px; padding: 4px 8px; border: none; border-radius: 4px; cursor: pointer; font-size: 12px; background: transparent; }
.action-btn.edit { color: var(--el-color-primary, #409eff); }
.action-btn.delete { color: var(--el-color-danger, #f56c6c); }
.action-btn.success { color: var(--el-color-success, #67c23a); }
.action-btn.warning { color: var(--el-color-warning, #e6a23c); }
.action-btn:hover { opacity: 0.8; }
</style>
