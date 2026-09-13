<!--
  商品管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="product-manage-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">商品管理</h1>
      <p class="page-desc">管理中医药材商品，包括新增、编辑、上下架等操作</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索商品名称"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <SvgIcon name="search" :size="16" />
        </template>
      </el-input>
      <el-cascader
        v-model="searchForm.categoryId"
        :options="categoryTree"
        :props="{ value: 'id', label: 'name', children: 'children', emitPath: false, checkStrictly: true }"
        placeholder="商品分类"
        clearable
        @change="handleSearch"
        style="width: 180px"
      />
      <el-select v-model="searchForm.status" placeholder="商品状态" clearable @change="handleSearch">
        <el-option label="已上架" :value="1" />
        <el-option label="已下架" :value="0" />
        <el-option label="待审核" :value="2" />
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
        新增商品
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
        <el-table-column label="商品图片" width="100" align="center">
          <template #default="{ row }">
            <el-image
              :src="getImageUrl(row.mainImage)"
              fit="cover"
              class="product-image"
              @click="handlePreviewImage(row.mainImage)"
            >
              <template #error>
                <div class="image-placeholder">
                  <SvgIcon name="image" :size="24" />
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150">
          <template #default="{ row }">
            <div class="product-name">{{ row.name }}</div>
            <div class="product-subtitle">{{ row.subTitle || '' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="price" label="价格" width="100" align="right">
          <template #default="{ row }">
            <span class="price-text">{{ formatMoney(row.price) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" align="center">
          <template #default="{ row }">
            <span :class="{ 'stock-warning': row.stock < 10 }">{{ row.stock || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" label="销量" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'" 
              size="small"
            >
              {{ row.status === 1 ? '已上架' : row.status === 2 ? '待审核' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn edit" @click="handleEdit(row)">
                <SvgIcon name="edit" :size="14" />
                <span>编辑</span>
              </button>
              <button 
                class="action-btn" 
                :class="row.status === 1 ? 'view' : 'edit'"
                @click="handleToggleStatus(row)"
              >
                <SvgIcon :name="row.status === 1 ? 'arrow-down' : 'arrow-up'" :size="14" />
                <span>{{ row.status === 1 ? '下架' : '上架' }}</span>
              </button>
              <button class="action-btn delete" @click="handleDelete(row)">
                <SvgIcon name="delete" :size="14" />
                <span>删除</span>
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

    <!-- 新增/编辑商品对话框 -->
    <el-dialog
      v-model="formVisible"
      :title="isEdit ? '编辑商品' : '新增商品'"
      width="750px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <!-- 商品图片上传 -->
        <el-form-item label="商品图片">
          <div class="image-upload-wrapper">
            <el-upload
              class="image-uploader"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleImageSuccess"
              :before-upload="beforeImageUpload"
              accept="image/*"
            >
              <el-image v-if="formData.mainImage" :src="getImageUrl(formData.mainImage)" fit="cover" class="uploaded-image" />
              <div v-else class="image-upload-placeholder">
                <SvgIcon name="add" :size="32" />
                <span>上传商品图片</span>
              </div>
            </el-upload>
            <el-button v-if="formData.mainImage" type="danger" size="small" @click="formData.mainImage = ''">
              删除图片
            </el-button>
          </div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品分类" prop="categoryId">
              <el-cascader
                v-model="formData.categoryId"
                :options="categoryTree"
                :props="{ value: 'id', label: 'name', children: 'children', emitPath: false, checkStrictly: true }"
                placeholder="请选择分类"
                clearable
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售价格" prop="price">
              <el-input-number 
                v-model="formData.price" 
                :min="0" 
                :precision="2" 
                :controls="false"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原价">
              <el-input-number 
                v-model="formData.originalPrice" 
                :min="0" 
                :precision="2" 
                :controls="false"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="副标题">
          <el-input v-model="formData.subTitle" placeholder="请输入副标题" />
        </el-form-item>
        <el-form-item label="商品简介">
          <el-input 
            v-model="formData.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入商品简介" 
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="单位">
              <el-input v-model="formData.unit" placeholder="如：克、盒" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规格">
              <el-input v-model="formData.spec" placeholder="如：500g/盒" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产地">
              <el-input v-model="formData.origin" placeholder="如：云南" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="功效说明">
          <el-input 
            v-model="formData.efficacy" 
            type="textarea" 
            :rows="2"
            placeholder="请输入功效说明" 
          />
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
    <el-dialog v-model="previewVisible" title="商品图片预览" width="450px" class="preview-dialog">
      <div class="preview-content">
        <img :src="previewUrl" alt="商品图片" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getAdminProductList, 
  getAdminCategoryList,
  addProduct, 
  updateProduct, 
  deleteProduct,
  updateProductStatus 
} from 'shared/api/admin.js'
import { formatMoney, getToken } from 'shared/utils/index.js'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const categoryList = ref([])
const categoryTree = ref([])
const formVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const previewVisible = ref(false)
const previewUrl = ref('')
const imageRefreshKey = ref(Date.now())

// 上传配置
const uploadUrl = '/api/admin/products/images'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${getToken()}`
}))

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL（添加时间戳破坏缓存）
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  // 如果是完整URL，添加时间戳
  if (url.startsWith('http')) {
    const separator = url.includes('?') ? '&' : '?'
    return `${url}${separator}t=${imageRefreshKey.value}`
  }
  // 如果是相对路径（如 /uploads/...），拼接后端服务器地址
  if (url.startsWith('/uploads')) {
    const separator = url.includes('?') ? '&' : '?'
    return `${API_BASE_URL}${url}${separator}t=${imageRefreshKey.value}`
  }
  return url
}

const searchForm = reactive({
  keyword: '',
  categoryId: null,
  status: null
})

const formData = reactive({
  id: null,
  name: '',
  categoryId: null,
  price: null,
  originalPrice: null,
  mainImage: '',
  subTitle: '',
  description: '',
  unit: '',
  spec: '',
  origin: '',
  efficacy: ''
})

const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入销售价格', trigger: 'blur' }
  ]
}

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取商品列表
async function fetchProductList() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword || undefined,
      categoryId: searchForm.categoryId || undefined,
      status: searchForm.status ?? undefined
    }
    const data = await getAdminProductList(params)
    tableData.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('获取商品列表失败:', error)
    // 使用模拟数据
    tableData.value = [
      { id: 1, name: '野生灵芝', subTitle: '深山野生', categoryName: '滋补药材', price: 299.00, stock: 50, sales: 128, status: 1, mainImage: '' },
      { id: 2, name: '人参片', subTitle: '长白山正品', categoryName: '滋补药材', price: 199.00, stock: 8, sales: 256, status: 1, mainImage: '' },
      { id: 3, name: '枸杞子', subTitle: '宁夏特级', categoryName: '养生茶饮', price: 58.00, stock: 200, sales: 512, status: 1, mainImage: '' }
    ]
    pagination.total = 3
  } finally {
    loading.value = false
  }
}

// 获取分类列表并构建树形结构
async function fetchCategoryList() {
  try {
    const data = await getAdminCategoryList()
    // 后端返回的是树形结构（包含children），直接使用
    const isTreeData = data && data.length > 0 && Array.isArray(data[0].children)
    if (isTreeData) {
      categoryTree.value = data || []
      // 将树形结构展平为扁平列表
      categoryList.value = flattenTree(data || [])
    } else {
      categoryList.value = data || []
      categoryTree.value = buildCategoryTree(data || [])
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    // 模拟数据（带树形结构）
    categoryList.value = [
      { id: 1, parentId: 0, name: '中药材' },
      { id: 2, parentId: 0, name: '养生茶饮' },
      { id: 3, parentId: 0, name: '滋补品' },
      { id: 4, parentId: 0, name: '养生器具' },
      { id: 5, parentId: 0, name: '中医书籍' },
      { id: 6, parentId: 1, name: '补气类' },
      { id: 7, parentId: 1, name: '补血类' },
      { id: 8, parentId: 1, name: '清热类' },
      { id: 9, parentId: 1, name: '祛湿类' },
      { id: 10, parentId: 1, name: '活血化瘀类' },
      { id: 11, parentId: 2, name: '花草茶' },
      { id: 12, parentId: 2, name: '养生茶包' },
      { id: 13, parentId: 2, name: '代用茶' },
      { id: 14, parentId: 3, name: '燕窝' },
      { id: 15, parentId: 3, name: '人参' },
      { id: 16, parentId: 3, name: '阿胶' },
      { id: 17, parentId: 3, name: '冬虫夏草' },
      { id: 18, parentId: 4, name: '艾灸器具' },
      { id: 19, parentId: 4, name: '刮痧工具' },
      { id: 20, parentId: 4, name: '拔罐器具' },
      { id: 21, parentId: 4, name: '按摩器械' }
    ]
    categoryTree.value = buildCategoryTree(categoryList.value)
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

// 搜索
function handleSearch() {
  pagination.page = 1
  fetchProductList()
}

// 重置
function handleReset() {
  searchForm.keyword = ''
  searchForm.categoryId = null
  searchForm.status = null
  pagination.page = 1
  fetchProductList()
}

// 新增
function handleAdd() {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    name: '',
    categoryId: null,
    price: null,
    originalPrice: null,
    mainImage: '',
    subTitle: '',
    description: '',
    unit: '',
    spec: '',
    origin: '',
    efficacy: ''
  })
  formVisible.value = true
}

// 图片上传成功回调
function handleImageSuccess(response) {
  if (response.code === 200) {
    formData.mainImage = response.data
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传前校验
function beforeImageUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!')
    return false
  }
  return true
}

// 预览图片
function handlePreviewImage(url) {
  if (!url) {
    ElMessage.warning('暂无图片')
    return
  }
  previewUrl.value = getImageUrl(url)
  previewVisible.value = true
}

// 编辑
function handleEdit(row) {
  isEdit.value = true
  Object.assign(formData, row)
  formVisible.value = true
}

// 提交
async function handleSubmit() {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateProduct(formData.id, formData)
      ElMessage.success('修改成功')
    } else {
      await addProduct(formData)
      ElMessage.success('新增成功')
    }
    
    formVisible.value = false
    // 更新图片刷新key，强制重新加载图片
    imageRefreshKey.value = Date.now()
    fetchProductList()
  } catch (error) {
    console.error('操作失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 删除
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除商品"${row.name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteProduct(row.id)
    ElMessage.success('删除成功')
    fetchProductList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 切换状态
async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const action = row.status === 1 ? '下架' : '上架'
  
  try {
    await ElMessageBox.confirm(`确定要${action}商品"${row.name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateProductStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    fetchProductList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

// 分页
function handleSizeChange() {
  pagination.page = 1
  fetchProductList()
}

function handlePageChange() {
  fetchProductList()
}

onMounted(() => {
  fetchCategoryList()
  fetchProductList()
})
</script>

<style scoped>
.product-manage-page {
  min-height: 100%;
}

.search-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-bar .el-input {
  width: 200px;
}

.search-bar .el-select {
  width: 150px;
}

.product-name {
  font-weight: 500;
  color: var(--tcm-text-primary);
}

.product-subtitle {
  font-size: 12px;
  color: var(--tcm-text-secondary);
  margin-top: 2px;
}

.price-text {
  font-weight: 500;
  color: var(--tcm-vermilion);
}

.price-text::before {
  content: '¥';
  font-size: 12px;
}

.stock-warning {
  color: var(--tcm-vermilion);
  font-weight: 500;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.2s;
}

.product-image:hover {
  transform: scale(1.1);
}

.image-placeholder {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--tcm-bg-paper-dark);
  border-radius: 4px;
  color: var(--tcm-text-placeholder);
}

.table-actions {
  display: flex;
  flex-wrap: nowrap;
  gap: 4px;
}

/* 图片上传样式 */
.image-upload-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 12px;
}

.image-uploader {
  width: 120px;
  height: 120px;
}

.image-uploader :deep(.el-upload) {
  width: 100%;
  height: 100%;
  border: 1px dashed var(--tcm-border-color);
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s;
}

.image-uploader :deep(.el-upload:hover) {
  border-color: var(--tcm-primary);
}

.uploaded-image {
  width: 100%;
  height: 100%;
}

.image-upload-placeholder {
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

/* 图片预览弹窗 */
.preview-dialog .preview-content {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.preview-dialog .preview-content img {
  max-width: 100%;
  max-height: 350px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>
