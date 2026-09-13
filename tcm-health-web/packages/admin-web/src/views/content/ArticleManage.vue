<!--
  文章管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="article-manage-page">
    <div class="page-header">
      <h1 class="page-title">文章管理</h1>
      <p class="page-desc">管理养生文章，支持新增、编辑、发布等操作</p>
    </div>

    <div class="search-bar">
      <el-input v-model="searchForm.keyword" placeholder="搜索文章标题" clearable @keyup.enter="handleSearch">
        <template #prefix><SvgIcon name="search" :size="16" /></template>
      </el-input>
      <el-button type="primary" @click="handleSearch">
        <SvgIcon name="search" :size="16" class="mr-8" />搜索
      </el-button>
      <el-button type="primary" @click="handleAdd">
        <SvgIcon name="add" :size="16" class="mr-8" />新增文章
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="title" label="文章标题" min-width="250" />
        <el-table-column prop="author" label="作者" width="100" />
        <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn edit" @click="handleEdit(row)">
                <SvgIcon name="edit" :size="14" /><span>编辑</span>
              </button>
              <button class="action-btn" :class="row.status === 1 ? 'view' : 'edit'" @click="handleToggleStatus(row)">
                <SvgIcon :name="row.status === 1 ? 'arrow-down' : 'arrow-up'" :size="14" />
                <span>{{ row.status === 1 ? '下架' : '发布' }}</span>
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

    <el-dialog v-model="formVisible" :title="isEdit ? '编辑文章' : '新增文章'" width="700px" top="5vh">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="formData.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="封面图">
          <div class="cover-upload-container">
            <el-upload
              class="cover-uploader"
              :show-file-list="false"
              :auto-upload="false"
              accept="image/*"
              @change="handleCoverUpload"
            >
              <div v-if="formData.coverImage" class="cover-preview">
                <img :src="getImageUrl(formData.coverImage)" alt="封面图" />
                <div class="cover-actions">
                  <el-button type="danger" size="small" circle @click.stop="handleRemoveCover">
                    <SvgIcon name="delete" :size="14" />
                  </el-button>
                </div>
              </div>
              <div v-else class="cover-placeholder" v-loading="uploadLoading">
                <SvgIcon name="add" :size="28" class="upload-icon" />
                <span>上传封面图</span>
              </div>
            </el-upload>
            <div class="upload-tip">建议尺寸：800x400，支持 JPG、PNG、GIF、WEBP，不超过 5MB</div>
          </div>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="formData.summary" type="textarea" :rows="2" placeholder="请输入文章摘要" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <RichTextEditor 
            v-model="formData.content" 
            placeholder="请输入文章内容..."
            height="350px"
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
import { getAdminArticleList, addArticle, updateArticle, deleteArticle, updateArticleStatus, uploadContentImage } from 'shared/api/admin.js'
import { formatDateTime } from 'shared/utils/index.js'
import RichTextEditor from '@/components/RichTextEditor.vue'

// 后端服务器地址
const API_BASE_URL = ''

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const formVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({ keyword: '' })
const formData = reactive({ id: null, title: '', author: 'Ti', summary: '', content: '', coverImage: '' })
const formRules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
}
const pagination = reactive({ page: 1, size: 10, total: 0 })
const uploadLoading = ref(false)

// 获取图片URL
function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

// 上传封面图
async function handleCoverUpload(uploadFile) {
  if (!uploadFile.raw) return
  
  // 验证文件类型
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
  if (!allowedTypes.includes(uploadFile.raw.type)) {
    ElMessage.error('只支持 JPG、PNG、GIF、WEBP 格式的图片')
    return
  }
  
  // 验证文件大小（5MB）
  if (uploadFile.raw.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return
  }
  
  uploadLoading.value = true
  try {
    const url = await uploadContentImage(uploadFile.raw)
    if (url) {
      formData.coverImage = url
      ElMessage.success('封面图上传成功')
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
  } finally {
    uploadLoading.value = false
  }
}

// 移除封面图
function handleRemoveCover() {
  formData.coverImage = ''
}

async function fetchArticleList() {
  loading.value = true
  try {
    const params = { pageNum: pagination.page, pageSize: pagination.size, keyword: searchForm.keyword || undefined }
    const data = await getAdminArticleList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    tableData.value = [
      { id: 1, title: '春季养生之道', author: 'Ti', viewCount: 256, status: 1, createTime: '2026-02-01 10:00:00' },
      { id: 2, title: '中医体质调理指南', author: 'Ti', viewCount: 189, status: 1, createTime: '2026-02-02 14:30:00' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

function handleSearch() { pagination.page = 1; fetchArticleList() }
function handleAdd() {
  isEdit.value = false
  Object.assign(formData, { id: null, title: '', author: 'Ti', summary: '', content: '', coverImage: '' })
  formVisible.value = true
}
function handleEdit(row) {
  isEdit.value = true
  Object.assign(formData, { ...row, coverImage: row.coverImage || '' })
  formVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    if (isEdit.value) {
      await updateArticle(formData.id, formData)
      ElMessage.success('修改成功')
    } else {
      await addArticle(formData)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    fetchArticleList()
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除"${row.title}"吗？`, '提示', { type: 'warning' })
    await deleteArticle(row.id)
    ElMessage.success('删除成功')
    fetchArticleList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await updateArticleStatus(row.id, newStatus)
    ElMessage.success(row.status === 1 ? '已下架' : '已发布')
    fetchArticleList()
  } catch (error) {
    console.error(error)
  }
}

function handleSizeChange() { pagination.page = 1; fetchArticleList() }
function handlePageChange() { fetchArticleList() }

onMounted(() => fetchArticleList())
</script>

<style scoped>
.article-manage-page { min-height: 100%; }
.search-bar { display: flex; gap: 16px; margin-bottom: 20px; flex-wrap: wrap; }
.search-bar .el-input { width: 240px; }

/* 封面图上传样式 */
.cover-upload-container { width: 100%; }

.cover-uploader {
  width: 200px;
  height: 120px;
}

.cover-uploader :deep(.el-upload) {
  width: 200px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.3s;
}

.cover-uploader :deep(.el-upload:hover) {
  border-color: var(--tcm-primary, #8B4513);
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-actions {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.cover-preview:hover .cover-actions {
  opacity: 1;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  background: #fafafa;
}

.cover-placeholder .upload-icon {
  color: #999;
  margin-bottom: 8px;
}

.cover-placeholder span {
  font-size: 12px;
}

.upload-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}
</style>
