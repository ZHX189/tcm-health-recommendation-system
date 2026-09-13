<template>
  <div class="statistics-page">
    <h3>数据统计</h3>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <!-- 评价统计 -->
      <el-tab-pane label="评价统计" name="review">
        <div class="time-filter">
          <el-radio-group v-model="reviewDays" size="small" @change="loadReviewStats">
            <el-radio-button :value="7">近7天</el-radio-button>
            <el-radio-button :value="15">近15天</el-radio-button>
            <el-radio-button :value="30">近30天</el-radio-button>
            <el-radio-button :value="90">近90天</el-radio-button>
          </el-radio-group>
        </div>

        <el-row :gutter="16" class="stat-cards">
          <el-col :span="6">
            <div class="mini-card">
              <div class="mini-label">总评价数</div>
              <div class="mini-value">{{ reviewStats.totalCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="mini-card">
              <div class="mini-label">平均评分</div>
              <div class="mini-value">{{ reviewStats.avgRating || 0 }} <span class="unit">分</span></div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="mini-card">
              <div class="mini-label">好评率</div>
              <div class="mini-value">{{ reviewStats.goodRate || 0 }}<span class="unit">%</span></div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="mini-card">
              <div class="mini-label">差评数</div>
              <div class="mini-value warn">{{ reviewStats.badCount || 0 }}</div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-title">评分分布</div>
              <div ref="ratingChartRef" class="chart-box"></div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-title">评价趋势</div>
              <div ref="reviewTrendRef" class="chart-box"></div>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 用户统计 -->
      <el-tab-pane label="用户统计" name="user">
        <div class="time-filter">
          <el-radio-group v-model="userDays" size="small" @change="loadUserStats">
            <el-radio-button :value="7">近7天</el-radio-button>
            <el-radio-button :value="15">近15天</el-radio-button>
            <el-radio-button :value="30">近30天</el-radio-button>
            <el-radio-button :value="90">近90天</el-radio-button>
          </el-radio-group>
        </div>

        <el-row :gutter="16" class="stat-cards">
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">总用户数</div><div class="mini-value">{{ userStats.totalUsers || 0 }}</div></div>
          </el-col>
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">今日新增</div><div class="mini-value">{{ userStats.todayNew || 0 }}</div></div>
          </el-col>
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">本周新增</div><div class="mini-value">{{ userStats.weekNew || 0 }}</div></div>
          </el-col>
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">本月新增</div><div class="mini-value">{{ userStats.monthNew || 0 }}</div></div>
          </el-col>
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">活跃用户</div><div class="mini-value">{{ userStats.activeUsers || 0 }}</div></div>
          </el-col>
          <el-col :span="4">
            <div class="mini-card"><div class="mini-label">活跃率</div><div class="mini-value">{{ userStats.activeRate || 0 }}<span class="unit">%</span></div></div>
          </el-col>
        </el-row>

        <div class="chart-card">
          <div class="chart-title">用户增长趋势</div>
          <div ref="userTrendRef" class="chart-box"></div>
        </div>
      </el-tab-pane>

      <!-- 销售统计 -->
      <el-tab-pane label="销售统计" name="sales">
        <div class="time-filter">
          <el-radio-group v-model="salesDays" size="small" @change="loadSalesTrend">
            <el-radio-button :value="7">近7天</el-radio-button>
            <el-radio-button :value="15">近15天</el-radio-button>
            <el-radio-button :value="30">近30天</el-radio-button>
            <el-radio-button :value="90">近90天</el-radio-button>
          </el-radio-group>
        </div>
        <div class="chart-card">
          <div class="chart-title">销售额与订单趋势</div>
          <div ref="salesTrendRef" class="chart-box" style="height:400px"></div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getReviewStats, getUserStats, getSalesTrend } from 'shared/api/admin.js'

const activeTab = ref('review')
const reviewDays = ref(30)
const userDays = ref(30)
const salesDays = ref(30)

const reviewStats = reactive({})
const userStats = reactive({})

const ratingChartRef = ref(null)
const reviewTrendRef = ref(null)
const userTrendRef = ref(null)
const salesTrendRef = ref(null)

let ratingChart = null
let reviewTrendChart = null
let userTrendChart = null
let salesTrendChart = null

const COLORS = ['#8B4513', '#C9A86C', '#2E8B57', '#2F4F4F', '#D4A017']

async function loadReviewStats() {
  try {
    const data = await getReviewStats(reviewDays.value)
    Object.assign(reviewStats, data)
    await nextTick()
    initRatingChart(data.ratingDist || [])
    initReviewTrend(data.trend || [])
  } catch (e) { console.error(e) }
}

async function loadUserStats() {
  try {
    const data = await getUserStats(userDays.value)
    Object.assign(userStats, data)
    await nextTick()
    initUserTrend(data.growthTrend || [])
  } catch (e) { console.error(e) }
}

async function loadSalesTrend() {
  try {
    const data = await getSalesTrend(salesDays.value)
    await nextTick()
    initSalesTrend(data || [])
  } catch (e) { console.error(e) }
}

function initRatingChart(data) {
  if (!ratingChartRef.value) return
  if (!ratingChart) ratingChart = echarts.init(ratingChartRef.value)
  ratingChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.statusName), axisLabel: { color: '#6B5E52' } },
    yAxis: { type: 'value', axisLabel: { color: '#6B5E52' }, splitLine: { lineStyle: { color: '#E6DFD2', type: 'dashed' } } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    series: [{ type: 'bar', data: data.map(d => d.count), barWidth: '40%',
      itemStyle: { color: (p) => COLORS[p.dataIndex % COLORS.length], borderRadius: [4, 4, 0, 0] } }]
  })
}

function initReviewTrend(data) {
  if (!reviewTrendRef.value) return
  if (!reviewTrendChart) reviewTrendChart = echarts.init(reviewTrendRef.value)
  reviewTrendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.date), axisLabel: { color: '#6B5E52' } },
    yAxis: { type: 'value', axisLabel: { color: '#6B5E52' }, splitLine: { lineStyle: { color: '#E6DFD2', type: 'dashed' } } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    series: [{ name: '评价数', type: 'line', smooth: true, data: data.map(d => d.orderCount),
      itemStyle: { color: '#8B4513' }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [{ offset: 0, color: 'rgba(139,69,19,0.3)' }, { offset: 1, color: 'rgba(139,69,19,0.05)' }] } } }]
  })
}

function initUserTrend(data) {
  if (!userTrendRef.value) return
  if (!userTrendChart) userTrendChart = echarts.init(userTrendRef.value)
  userTrendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.date), axisLabel: { color: '#6B5E52' } },
    yAxis: { type: 'value', axisLabel: { color: '#6B5E52' }, splitLine: { lineStyle: { color: '#E6DFD2', type: 'dashed' } } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    series: [{ name: '新增用户', type: 'bar', data: data.map(d => d.orderCount), barWidth: '50%',
      itemStyle: { color: '#2E8B57', borderRadius: [4, 4, 0, 0] } }]
  })
}

function initSalesTrend(data) {
  if (!salesTrendRef.value) return
  if (!salesTrendChart) salesTrendChart = echarts.init(salesTrendRef.value)
  salesTrendChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['销售额', '订单数'], textStyle: { color: '#6B5E52' } },
    xAxis: { type: 'category', data: data.map(d => d.date), axisLabel: { color: '#6B5E52' } },
    yAxis: [
      { type: 'value', name: '销售额(元)', axisLabel: { color: '#6B5E52' }, splitLine: { lineStyle: { color: '#E6DFD2', type: 'dashed' } } },
      { type: 'value', name: '订单数', axisLabel: { color: '#6B5E52' }, splitLine: { show: false } }
    ],
    grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
    series: [
      { name: '销售额', type: 'line', smooth: true, data: data.map(d => d.amount),
        itemStyle: { color: '#8B4513' }, areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [{ offset: 0, color: 'rgba(139,69,19,0.3)' }, { offset: 1, color: 'rgba(139,69,19,0.05)' }] } } },
      { name: '订单数', type: 'bar', yAxisIndex: 1, data: data.map(d => d.orderCount), barWidth: '30%',
        itemStyle: { color: 'rgba(201,168,108,0.6)', borderRadius: [4, 4, 0, 0] } }
    ]
  })
}

function handleTabChange(tab) {
  nextTick(() => {
    if (tab === 'review') loadReviewStats()
    else if (tab === 'user') loadUserStats()
    else if (tab === 'sales') loadSalesTrend()
  })
}

function handleResize() {
  ratingChart?.resize()
  reviewTrendChart?.resize()
  userTrendChart?.resize()
  salesTrendChart?.resize()
}

onMounted(() => {
  loadReviewStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  ratingChart?.dispose()
  reviewTrendChart?.dispose()
  userTrendChart?.dispose()
  salesTrendChart?.dispose()
})
</script>

<style scoped>
.statistics-page { padding: 20px; }
.statistics-page h3 { margin: 0 0 16px; }
.time-filter { margin-bottom: 16px; }
.stat-cards { margin-bottom: 16px; }
.mini-card { background: #fff; border-radius: 8px; padding: 16px; border: 1px solid #eee; text-align: center; }
.mini-label { font-size: 13px; color: #999; margin-bottom: 8px; }
.mini-value { font-size: 24px; font-weight: 600; color: #4A4036; }
.mini-value .unit { font-size: 14px; font-weight: 400; color: #999; }
.mini-value.warn { color: #F56C6C; }
.chart-card { background: #fff; border-radius: 8px; padding: 16px; border: 1px solid #eee; margin-bottom: 16px; }
.chart-title { font-size: 14px; font-weight: 600; color: #4A4036; margin-bottom: 12px; }
.chart-box { height: 300px; }
</style>
