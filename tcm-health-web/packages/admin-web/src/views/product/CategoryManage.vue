<!--
  分类管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="category-manage-page">
    <div class="page-header">
      <h1 class="page-title">分类管理</h1>
      <p class="page-desc">管理商品分类，支持新增、编辑、删除分类</p>
    </div>

    <div class="search-bar">
      <el-button type="primary" @click="handleAdd">
        <SvgIcon name="add" :size="16" class="mr-8" />
        新增分类
      </el-button>
    </div>

    <div class="table-container">
      <el-table 
        :data="treeData" 
        v-loading="loading" 
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
      >
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="分类名称" min-width="250">
          <template #default="{ row }">
            <div class="category-name-cell" :class="{ 'is-child': row.parentId && row.parentId !== 0 }">
              <span v-if="row.parentId && row.parentId !== 0" class="child-indicator">└─</span>
              <span class="category-name-text">{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="icon" label="图标" width="100" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.icon"
              :src="getImageUrl(row.icon)"
              fit="cover"
              class="category-icon"
              @click="handlePreviewIcon(row.icon)"
            >
              <template #error>
                <div class="icon-placeholder">-</div>
              </template>
            </el-image>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn edit" @click="handleEdit(row)">
                <SvgIcon name="edit" :size="14" />
                <span>编辑</span>
              </button>
              <button class="action-btn delete" @click="handleDelete(row)">
                <SvgIcon name="delete" :size="14" />
                <span>删除</span>
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑分类对话框 -->
    <el-dialog v-model="formVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="500px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="父分类">
          <el-select 
            v-model="formData.parentId" 
            placeholder="请选择父分类（不选为一级分类）" 
            clearable 
            :disabled="isEdit"
            style="width: 100%"
          >
            <el-option 
              v-for="item in parentCategoryOptions" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类图标">
          <div class="icon-upload-wrapper">
            <el-upload
              class="icon-uploader"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleIconSuccess"
              :before-upload="beforeIconUpload"
              accept="image/*"
            >
              <el-image v-if="formData.icon" :src="getImageUrl(formData.icon)" fit="cover" class="uploaded-icon" />
              <div v-else class="icon-upload-placeholder">
                <SvgIcon name="add" :size="24" />
                <span>上传图标</span>
              </div>
            </el-upload>
            <el-button v-if="formData.icon" type="danger" size="small" @click="formData.icon = ''">
              删除
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          <SvgIcon name="check" :size="16" class="mr-8" />
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图标预览" width="400px" class="preview-dialog">
      <div class="preview-content">
        <img :src="previewUrl" alt="图标预览" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminCategoryList, addCategory, updateCategory, deleteCategory } from 'shared/api/admin.js'
import { getToken } from 'shared/utils/index.js'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const treeData = ref([])
const formVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const previewVisible = ref(false)
const previewUrl = ref('')

// 后端服务器地址
const API_BASE_URL = ''

// 上传配置
const uploadUrl = '/api/admin/content/images'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${getToken()}`
}))

// 获取图片URL（处理相对路径）
function getImageUrl(url) {
  if (!url) return ''
  // 如果是完整URL，直接返回
  if (url.startsWith('http')) {
    return url
  }
  // 如果是相对路径（如 /uploads/...），拼接后端服务器地址
  if (url.startsWith('/uploads')) {
    return `${API_BASE_URL}${url}`
  }
  return url
}

const formData = reactive({
  id: null,
  parentId: 0,
  name: '',
  icon: '',
  sort: 0,
  status: 1
})

// 父分类选项（只能选一级分类作为父分类）
const parentCategoryOptions = computed(() => {
  // 从树形数据中获取一级分类（parentId为0或不存在的）
  return treeData.value.map(item => ({
    id: item.id,
    name: item.name
  }))
})

const formRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

async function fetchCategoryList() {
  loading.value = true
  try {
    const data = await getAdminCategoryList()
    // 后端返回的是树形结构（包含children），直接使用
    // 如果后端返回的是树形结构，直接赋值；如果是扁平结构，则构建树
    const isTreeData = data && data.length > 0 && Array.isArray(data[0].children)
    if (isTreeData) {
      treeData.value = data || []
      // 将树形结构展平为扁平列表（用于其他操作）
      tableData.value = flattenTree(data || [])
    } else {
      tableData.value = data || []
      treeData.value = buildCategoryTree(data || [])
    }
  } catch (error) {
    const mockData = [
      { id: 1, parentId: 0, name: '中药材', icon: 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', sort: 1, status: 1 },
      { id: 2, parentId: 0, name: '养生茶饮', icon: 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', sort: 2, status: 1 },
      { id: 3, parentId: 0, name: '滋补品', icon: 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', sort: 3, status: 1 },
      { id: 6, parentId: 1, name: '补气类', icon: 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', sort: 1, status: 1 },
      { id: 7, parentId: 1, name: '补血类', icon: 'https://img95.699pic.com/photo/40164/0837.jpg_wh860.jpg', sort: 2, status: 1 }
    ]
    tableData.value = mockData
    treeData.value = buildCategoryTree(mockData)
  } finally {
    loading.value = false
  }
}

// 将树形结构展平为扁平列表
function flattenTree(tree) {
  const result = []
  function traverse(nodes) {
    for (const node of nodes) {
      const { children, ...rest } = node
      result.push(rest)
      if (children && children.length > 0) {
        traverse(children)
      }
    }
  }
  traverse(tree)
  return result
}

// 构建树形分类结构
function buildCategoryTree(list) {
  const map = {}
  const tree = []
  
  // 先建立映射
  list.forEach(item => {
    map[item.id] = { ...item, children: [] }
  })
  
  // 构建树
  list.forEach(item => {
    const node = map[item.id]
    if (item.parentId === 0 || !item.parentId) {
      tree.push(node)
    } else if (map[item.parentId]) {
      map[item.parentId].children.push(node)
    }
  })
  
  // 移除空的 children 数组
  const removeEmptyChildren = (nodes) => {
    nodes.forEach(node => {
      if (node.children && node.children.length === 0) {
        delete node.children
      } else if (node.children) {
        removeEmptyChildren(node.children)
      }
    })
  }
  removeEmptyChildren(tree)
  
  return tree
}

function handleAdd() {
  isEdit.value = false
  Object.assign(formData, { id: null, parentId: 0, name: '', icon: '', sort: 0, status: 1 })
  formVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  Object.assign(formData, row)
  formVisible.value = true
}

// 图标上传成功回调
function handleIconSuccess(response) {
  if (response.code === 200) {
    formData.icon = response.data
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传前校验
function beforeIconUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  return true
}

// 预览图标
function handlePreviewIcon(url) {
  previewUrl.value = getImageUrl(url)
  previewVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    if (isEdit.value) {
      await updateCategory(formData.id, formData)
      ElMessage.success('修改成功')
    } else {
      await addCategory(formData)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    fetchCategoryList()
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除分类"${row.name}"吗？`, '提示', {
      type: 'warning'
    })
    await deleteCategory(row.id)
    ElMessage.success('删除成功')
    fetchCategoryList()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => fetchCategoryList())
</script>

<style scoped>
.category-manage-page { min-height: 100%; }
.search-bar { margin-bottom: 20px; }

/* 树形表格层级样式 */
.table-container :deep(.el-table__row--level-1) {
  background-color: var(--tcm-bg-paper-dark, #fafafa);
}

/* 增强展开图标样式 */
.table-container :deep(.el-table__expand-icon) {
  color: var(--tcm-primary, #a87c4f);
  font-size: 16px;
}

/* 分类名称样式 */
.category-name-cell {
  display: flex;
  align-items: center;
  gap: 4px;
}

.category-name-cell.is-child {
  padding-left: 20px;
}

.child-indicator {
  color: var(--tcm-text-secondary, #999);
  font-family: monospace;
  margin-right: 4px;
}

.category-name-text {
  font-weight: 500;
}

.category-name-cell.is-child .category-name-text {
  font-weight: 400;
  color: var(--tcm-text-secondary, #666);
}

.category-icon {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.2s;
}

.category-icon:hover {
  transform: scale(1.1);
}

.icon-placeholder {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--tcm-bg-paper-dark);
  border-radius: 4px;
  color: var(--tcm-text-placeholder);
}

.icon-upload-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 12px;
}

.icon-uploader {
  width: 100px;
  height: 100px;
}

.icon-uploader :deep(.el-upload) {
  width: 100%;
  height: 100%;
  border: 1px dashed var(--tcm-border-color);
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s;
}

.icon-uploader :deep(.el-upload:hover) {
  border-color: var(--tcm-primary);
}

.uploaded-icon {
  width: 100%;
  height: 100%;
}

.icon-upload-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--tcm-text-secondary);
  font-size: 12px;
}

.preview-dialog .preview-content {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.preview-dialog .preview-content img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
}
</style>
