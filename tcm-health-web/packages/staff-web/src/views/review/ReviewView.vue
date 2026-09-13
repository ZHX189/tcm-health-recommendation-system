<template>
  <div class="review-view">
    <h3>商品评价</h3>
    <el-table :data="reviews" border stripe>
      <el-table-column prop="productName" label="商品" min-width="150" />
      <el-table-column prop="nickname" label="用户" width="120" />
      <el-table-column prop="rating" label="评分" width="100" align="center">
        <template #default="{ row }">
          <el-rate v-model="row.rating" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'warning' : 'danger'">
            {{ row.status === 1 ? '已通过' : row.status === 0 ? '待审核' : '已驳回' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="170" />
    </el-table>
    <div style="display:flex;justify-content:center;margin-top:16px">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total"
                     layout="prev, pager, next" @current-change="loadReviews" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStaffReviewList } from 'shared/api/staff.js'

const reviews = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadReviews = async () => {
  try {
    const res = await getStaffReviewList({ pageNum: pageNum.value, pageSize: pageSize.value })
    reviews.value = res?.records || []
    total.value = res?.total || 0
  } catch (e) { console.error(e) }
}

onMounted(() => loadReviews())
</script>

<style scoped>
.review-view { padding: 20px; }
.review-view h3 { margin: 0 0 16px; }
</style>
