<!--
  养生方案列表页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="health-plan-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item>养生方案</el-breadcrumb-item></el-breadcrumb>
    </div>
    <div class="page-header"><h1 class="page-title">养生方案</h1><p class="page-desc">根据您的体质类型，定制专属养生调理方案</p></div>

    <div class="filter-bar">
      <el-select v-model="selectedType" placeholder="全部体质" clearable @change="handleFilterChange">
        <el-option label="平和质" value="平和质" /><el-option label="气虚质" value="气虚质" /><el-option label="阳虚质" value="阳虚质" />
        <el-option label="阴虚质" value="阴虚质" /><el-option label="痰湿质" value="痰湿质" />
      </el-select>
      <el-select v-model="selectedSeason" placeholder="全部季节" clearable @change="handleFilterChange">
        <el-option label="春季" value="春季" /><el-option label="夏季" value="夏季" /><el-option label="秋季" value="秋季" /><el-option label="冬季" value="冬季" /><el-option label="四季" value="四季通用" />
      </el-select>
    </div>

    <div class="plan-grid" v-loading="loading">
      <div v-for="plan in plans" :key="plan.id" class="plan-card">
        <div class="plan-tag">{{ plan.constitutionType }}</div>
        <h3 class="plan-title">{{ plan.title || plan.name }}</h3>
        <p class="plan-desc">{{ plan.description || '专业养生调理方案，根据体质特点精心设计' }}</p>
        <div class="plan-meta">
          <span class="plan-season"><SvgIcon name="calendar" :size="14" />{{ plan.season || '四季' }}适用</span>
        </div>
        <el-button type="primary" size="small" @click="viewDetail(plan)">查看详情</el-button>
      </div>
      <el-empty v-if="!loading && plans.length === 0" description="暂无养生方案" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHealthPlanList } from 'shared/api/user.js'

const router = useRouter()
const loading = ref(false)
const plans = ref([])
const selectedType = ref('')
const selectedSeason = ref('')

async function fetchPlans() {
  loading.value = true
  try {
    const params = { 
      pageNum: 1, 
      pageSize: 20
    }
    // 只有选择了值才传递参数
    if (selectedType.value) {
      params.constitutionType = selectedType.value
    }
    if (selectedSeason.value) {
      params.season = selectedSeason.value
    }
    const data = await getHealthPlanList(params)
    plans.value = data?.records || []
  } catch (error) {
    plans.value = [
      { id: 1, title: '气虚体质春季调养方案', constitutionType: '气虚质', season: '春季', description: '针对气虚体质人群，在春季进行温和调养' },
      { id: 2, title: '阳虚体质冬季温补方案', constitutionType: '阳虚质', season: '冬季', description: '针对阳虚体质人群，在冬季进行温补调理' },
      { id: 3, title: '阴虚体质秋季滋阴方案', constitutionType: '阴虚质', season: '秋季', description: '针对阴虚体质人群，在秋季进行滋阴润燥' }
    ]
  } finally { loading.value = false }
}

function handleFilterChange() {
  fetchPlans()
}

function viewDetail(plan) {
  router.push(`/health/${plan.id}`)
}

onMounted(() => fetchPlans())
</script>

<style scoped>
.health-plan-page { min-height: 60vh; }
.page-desc { font-size: 14px; color: var(--tcm-text-secondary); margin-top: 8px; }
.filter-bar { display: flex; gap: 16px; margin-bottom: 24px; }
.filter-bar .el-select { width: 160px; }
.plan-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.plan-card { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 24px; transition: all var(--tcm-transition-base); }
.plan-card:hover { box-shadow: var(--tcm-shadow-md); transform: translateY(-4px); }
.plan-tag { display: inline-block; padding: 4px 12px; background: var(--tcm-ochre-100); color: var(--tcm-ochre); font-size: 12px; border-radius: 2px; margin-bottom: 12px; }
.plan-title { font-family: var(--tcm-font-title); font-size: 18px; color: var(--tcm-text-primary); margin: 0 0 12px; }
.plan-desc { font-size: 14px; color: var(--tcm-text-secondary); line-height: 1.6; margin-bottom: 16px; }
.plan-meta { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--tcm-text-placeholder); margin-bottom: 16px; }
@media (max-width: 1024px) { .plan-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .plan-grid { grid-template-columns: 1fr; } }
</style>
