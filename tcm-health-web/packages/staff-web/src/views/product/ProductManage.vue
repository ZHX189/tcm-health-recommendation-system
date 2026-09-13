<!--
  员工商品管理页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="product-manage-page">
    <div class="page-header">
      <h1 class="page-title">商品管理</h1>
      <p class="page-desc">查看和编辑商品信息</p>
    </div>

    <div class="search-bar">
      <el-input v-model="searchForm.keyword" placeholder="搜索商品名称" clearable @keyup.enter="handleSearch" style="width: 240px">
        <template #prefix><SvgIcon name="search" :size="16" /></template>
      </el-input>
      <el-button type="primary" @click="handleSearch"><SvgIcon name="search" :size="16" class="mr-8" />搜索</el-button>
      <el-button @click="handleReset"><SvgIcon name="refresh" :size="16" class="mr-8" />重置</el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="商品图片" width="100" align="center">
          <template #default="{ row }">
            <el-image :src="getImageUrl(row.mainImage)" fit="cover" class="product-image">
              <template #error><div class="image-placeholder"><SvgIcon name="image" :size="24" /></div></template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="200">
          <template #default="{ row }">
            <div class="product-name">{{ row.name }}</div>
            <div class="product-subtitle" v-if="row.subTitle">{{ row.subTitle }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="price" label="价格" width="100" align="right">
          <template #default="{ row }"><span class="price-text">¥{{ formatMoney(row.price) }}</span></template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" align="center">
          <template #default="{ row }"><span :class="{ 'stock-warning': row.stock < 10 }">{{ row.stock || 0 }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'info'" size="small">
              {{ row.status === 1 ? '已上架' : row.status === 2 ? '待审核' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <button class="action-btn edit" @click="handleEdit(row)"><SvgIcon name="edit" :size="14" /><span>编辑</span></button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="tcm-pagination">
        <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size" :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" />
      </div>
    </div>

    <el-dialog v-model="formVisible" title="编辑商品" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="商品名称" prop="name"><el-input v-model="formData.name" placeholder="请输入商品名称" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售价格" prop="price"><el-input-number v-model="formData.price" :min="0" :precision="2" :controls="false" style="width: 100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存数量"><el-input-number v-model="formData.stock" :min="0" :controls="false" style="width: 100%" disabled /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="副标题"><el-input v-model="formData.subTitle" placeholder="请输入副标题" /></el-form-item>
        <el-form-item label="商品简介"><el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入商品简介" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit"><SvgIcon name="check" :size="16" class="mr-8" />保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getStaffProductList, updateStaffProduct } from 'shared/api/staff.js'
import { formatMoney } from 'shared/utils/index.js'

const API_BASE_URL = ''
const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const formVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({ keyword: '' })
const formData = reactive({ id: null, name: '', price: null, stock: null, subTitle: '', description: '' })
const formRules = { name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }], price: [{ required: true, message: '请输入价格', trigger: 'blur' }] }
const pagination = reactive({ page: 1, size: 10, total: 0 })

function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

async function fetchProductList() {
  loading.value = true
  try {
    const params = { pageNum: pagination.page, pageSize: pagination.size, keyword: searchForm.keyword || undefined }
    const data = await getStaffProductList(params)
    tableData.value = data?.records || []
    pagination.total = data?.total || 0
  } catch (error) {
    tableData.value = [
      { id: 1, name: '精选黄芪片', categoryName: '补气类', price: 68.00, stock: 500, status: 1 },
      { id: 2, name: '长白山人参', categoryName: '人参', price: 388.00, stock: 100, status: 1 },
      { id: 3, name: '党参切片', categoryName: '补气类', price: 45.00, stock: 800, status: 1 },
      { id: 4, name: '西洋参片', categoryName: '人参', price: 198.00, stock: 200, status: 1 },
      { id: 5, name: '阿胶块', categoryName: '阿胶', price: 268.00, stock: 300, status: 1 },
      { id: 6, name: '当归片', categoryName: '补血类', price: 35.00, stock: 1000, status: 1 },
      { id: 7, name: '熟地黄', categoryName: '补血类', price: 28.00, stock: 600, status: 1 }
    ]
    pagination.total = 7
  } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; fetchProductList() }
function handleReset() { searchForm.keyword = ''; handleSearch() }
function handleEdit(row) { Object.assign(formData, row); formVisible.value = true }

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    await updateStaffProduct(formData.id, formData)
    ElMessage.success('保存成功')
    formVisible.value = false
    fetchProductList()
  } catch (error) { console.error(error) } finally { submitLoading.value = false }
}

function handleSizeChange() { pagination.page = 1; fetchProductList() }
function handlePageChange() { fetchProductList() }

onMounted(() => fetchProductList())
</script>

<style scoped>
.product-manage-page { min-height: 100%; }
.product-image { width: 60px; height: 60px; border-radius: 4px; }
.image-placeholder { width: 60px; height: 60px; display: flex; align-items: center; justify-content: center; background: var(--tcm-bg-paper-dark); border-radius: 4px; color: var(--tcm-text-placeholder); }
.product-name { font-weight: 500; color: var(--tcm-text-primary); }
.product-subtitle { font-size: 12px; color: var(--tcm-text-secondary); margin-top: 2px; }
.price-text { color: var(--tcm-vermilion); font-weight: 500; }
.stock-warning { color: var(--tcm-vermilion); font-weight: 500; }
</style>
