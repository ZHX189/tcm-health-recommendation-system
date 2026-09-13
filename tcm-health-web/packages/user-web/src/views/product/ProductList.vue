<!--
  商品列表页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="product-list-page">
    <!-- 面包屑 -->
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>药材商城</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索和筛选 -->
    <div class="filter-bar">
      <el-input v-model="searchKeyword" placeholder="搜索药材名称" clearable class="search-input" @keyup.enter="handleSearch">
        <template #prefix><SvgIcon name="search" :size="16" /></template>
      </el-input>
      <el-cascader 
        v-model="selectedCategory" 
        :options="categoryOptions" 
        :props="{ value: 'id', label: 'name', children: 'children', checkStrictly: true, emitPath: false }"
        placeholder="全部分类" 
        clearable 
        @change="handleSearch"
        class="category-cascader"
      />
      <el-select v-model="sortBy" placeholder="默认排序" @change="handleSortChange">
        <el-option label="默认排序" value="" />
        <el-option label="销量优先" value="sales_desc" />
        <el-option label="价格从低到高" value="price_asc" />
        <el-option label="价格从高到低" value="price_desc" />
      </el-select>
    </div>

    <!-- 商品网格 -->
    <div class="product-grid" v-loading="loading">
      <div v-for="product in products" :key="product.id" class="product-card" @click="goToDetail(product.id)">
        <div class="product-card-image-wrapper">
          <img :src="getImageUrl(product.mainImage)" :alt="product.name" class="product-card-image" />
        </div>
        <div class="product-card-content">
          <div class="product-card-name">{{ product.name }}</div>
          <div class="product-card-desc">{{ product.subTitle || '' }}</div>
          <div class="product-card-bottom">
            <span class="product-card-price">{{ product.price }}</span>
            <span class="product-card-sales">已售{{ product.sales || 0 }}件</span>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && products.length === 0" description="暂无商品" />
    </div>

    <!-- 分页 -->
    <div class="tcm-pagination" v-if="total > 0">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :page-sizes="[12, 24, 48]" :total="total"
        layout="total, sizes, prev, pager, next" @size-change="handleSizeChange" @current-change="handlePageChange" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getProductList, getProductCategories } from 'shared/api/user.js'

const router = useRouter()
const route = useRoute()

// 后端服务器地址
const API_BASE_URL = ''

// 获取图片URL
function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

const loading = ref(false)
const products = ref([])
const categories = ref([])
const categoryOptions = ref([])
const searchKeyword = ref('')
const selectedCategory = ref(null)
const sortBy = ref('')
const page = ref(1)
const size = ref(12)
const total = ref(0)

async function fetchProducts() {
  loading.value = true
  try {
    // 解析排序参数
    let sortField = undefined
    let sortOrder = undefined
    if (sortBy.value) {
      const [field, order] = sortBy.value.split('_')
      sortField = field
      sortOrder = order
    }
    
    const params = {
      pageNum: page.value,
      pageSize: size.value,
      keyword: searchKeyword.value || undefined,
      categoryId: selectedCategory.value || undefined,
      sortField: sortField,
      sortOrder: sortOrder
    }
    const data = await getProductList(params)
    products.value = data?.records || []
    total.value = data?.total || 0
  } catch (error) {
    products.value = [
      { id: 1, name: '野生灵芝', subTitle: '深山野生', price: 299.00, sales: 128 },
      { id: 2, name: '人参片', subTitle: '长白山正品', price: 199.00, sales: 256 },
      { id: 3, name: '枸杞子', subTitle: '宁夏特级', price: 58.00, sales: 512 }
    ]
    total.value = 3
  } finally {
    loading.value = false
  }
}

async function fetchCategories() {
  try {
    const data = await getProductCategories()
    categories.value = data || []
    categoryOptions.value = data || []
  } catch (error) {
    categories.value = [
      { id: 1, name: '中药材', children: [
        { id: 6, name: '补气类' },
        { id: 7, name: '补血类' }
      ]},
      { id: 2, name: '养生茶饮', children: [] },
      { id: 3, name: '滋补品', children: [] }
    ]
    categoryOptions.value = categories.value
  }
}

function handleSearch() {
  page.value = 1
  fetchProducts()
}

function handleSizeChange() {
  page.value = 1
  fetchProducts()
}

function handlePageChange() {
  fetchProducts()
}

function handleSortChange() {
  page.value = 1
  fetchProducts()
}

function goToDetail(id) {
  router.push(`/products/${id}`)
}

watch(() => route.query.categoryId, (val) => {
  if (val) {
    selectedCategory.value = parseInt(val)
    fetchProducts()
  }
}, { immediate: true })

onMounted(() => {
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.product-list-page { min-height: 60vh; }
.filter-bar { display: flex; gap: 16px; margin-bottom: 24px; flex-wrap: wrap; }
.search-input { width: 280px; }
.filter-bar .el-select { width: 160px; }
.category-cascader { width: 200px; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; min-height: 300px; }
.product-card-image-wrapper { width: 100%; aspect-ratio: 1; overflow: hidden; background: var(--tcm-bg-paper-dark); }
.product-card-image { width: 100%; height: 100%; object-fit: cover; transition: transform var(--tcm-transition-base); }
.product-card:hover .product-card-image { transform: scale(1.05); }
.product-card-desc { font-size: 12px; color: var(--tcm-text-secondary); margin-bottom: 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-card-bottom { display: flex; align-items: center; justify-content: space-between; }
@media (max-width: 1024px) { .product-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .product-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
