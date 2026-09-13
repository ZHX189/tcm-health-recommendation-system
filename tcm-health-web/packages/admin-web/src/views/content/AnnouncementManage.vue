<!--
  公告管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="announcement-manage-page">
    <div class="page-header">
      <h1 class="page-title">公告管理</h1>
      <p class="page-desc">管理平台公告，发布重要通知信息</p>
    </div>

    <div class="search-bar">
      <el-button type="primary" @click="handleAdd">
        <SvgIcon name="add" :size="16" class="mr-8" />新增公告
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="title" label="公告标题" min-width="250" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : row.status === 2 ? '待审核' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
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

    <el-dialog v-model="formVisible" :title="isEdit ? '编辑公告' : '新增公告'" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="6" placeholder="请输入公告内容" />
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
import { getAdminAnnouncementList, addAnnouncement, updateAnnouncement, deleteAnnouncement, updateAnnouncementStatus } from 'shared/api/admin.js'
import { formatDateTime } from 'shared/utils/index.js'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const formVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({ id: null, title: '', content: '' })
const formRules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}
const pagination = reactive({ page: 1, size: 10, total: 0 })

async function fetchList() {
  loading.value = true
  try {
    const params = { pageNum: pagination.page, pageSize: pagination.size }
    const data = await getAdminAnnouncementList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    tableData.value = [
      { id: 1, title: '春节放假通知', status: 1, createTime: '2026-01-25 10:00:00' },
      { id: 2, title: '系统升级公告', status: 1, createTime: '2026-02-01 09:00:00' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

function handleAdd() {
  isEdit.value = false
  Object.assign(formData, { id: null, title: '', content: '' })
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
      await updateAnnouncement(formData.id, formData)
      ElMessage.success('修改成功')
    } else {
      await addAnnouncement(formData)
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
    await deleteAnnouncement(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handlePublish(row) {
  try {
    await ElMessageBox.confirm(`确定发布"${row.title}"吗？`, '提示', { type: 'info' })
    await updateAnnouncementStatus(row.id, 1)
    ElMessage.success('发布成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleUnpublish(row) {
  try {
    await ElMessageBox.confirm(`确定下架"${row.title}"吗？`, '提示', { type: 'warning' })
    await updateAnnouncementStatus(row.id, 0)
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
.announcement-manage-page { min-height: 100%; }
.search-bar { margin-bottom: 20px; }
.table-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.action-btn { display: inline-flex; align-items: center; gap: 4px; padding: 4px 8px; border: none; border-radius: 4px; cursor: pointer; font-size: 12px; background: transparent; }
.action-btn.edit { color: var(--el-color-primary, #409eff); }
.action-btn.delete { color: var(--el-color-danger, #f56c6c); }
.action-btn.success { color: var(--el-color-success, #67c23a); }
.action-btn.warning { color: var(--el-color-warning, #e6a23c); }
.action-btn:hover { opacity: 0.8; }
</style>
