<!--
  数据大盘页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="dashboard-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">数据大盘</h1>
      <p class="page-desc">实时监控平台运营数据，掌握业务动态</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-card-icon ochre">
            <SvgIcon name="money" :size="28" />
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">今日销售额</div>
            <div class="stat-card-value">{{ formatMoney(dashboardData.todaySales) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-card-icon turmeric">
            <SvgIcon name="order" :size="28" />
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">今日订单数</div>
            <div class="stat-card-value">{{ dashboardData.todayOrders || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-card-icon jade">
            <SvgIcon name="users" :size="28" />
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">今日新增用户</div>
            <div class="stat-card-value">{{ dashboardData.todayUsers || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-card-icon ink">
            <SvgIcon name="ship" :size="28" />
          </div>
          <div class="stat-card-content">
            <div class="stat-card-label">待发货订单</div>
            <div class="stat-card-value">{{ dashboardData.pendingShipOrders || 0 }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 销售趋势图 -->
      <el-col :xs="24" :lg="16">
        <div class="data-card">
          <div class="data-card-header">
            <span class="data-card-title">销售趋势</span>
            <el-radio-group v-model="trendDays" size="small" @change="fetchSalesTrend">
              <el-radio-button :value="7">近7天</el-radio-button>
              <el-radio-button :value="15">近15天</el-radio-button>
              <el-radio-button :value="30">近30天</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="salesChartRef" class="chart-container"></div>
        </div>
      </el-col>

      <!-- 订单状态分布 -->
      <el-col :xs="24" :lg="8">
        <div class="data-card">
          <div class="data-card-header">
            <span class="data-card-title">订单状态分布</span>
          </div>
          <div ref="orderChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据概览 -->
    <el-row :gutter="20" class="overview-row">
      <!-- 商品销量排行 -->
      <el-col :xs="24" :md="12">
        <div class="data-card">
          <div class="data-card-header">
            <span class="data-card-title">商品销量排行</span>
            <a href="#" class="view-more">
              查看更多
              <SvgIcon name="arrow-right" :size="14" />
            </a>
          </div>
          <div class="rank-list">
            <div 
              v-for="(item, index) in dashboardData.productRank || []" 
              :key="index"
              class="rank-item"
            >
              <span class="rank-num" :class="{ top3: index < 3 }">{{ index + 1 }}</span>
              <span class="rank-name">{{ item.productName }}</span>
              <span class="rank-value">{{ item.sales }}件</span>
            </div>
            <el-empty v-if="!dashboardData.productRank?.length" description="暂无数据" :image-size="60" />
          </div>
        </div>
      </el-col>

      <!-- 快捷操作 -->
      <el-col :xs="24" :md="12">
        <div class="data-card">
          <div class="data-card-header">
            <span class="data-card-title">快捷操作</span>
          </div>
          <div class="quick-actions">
            <div class="quick-action-item" @click="$router.push('/products')">
              <div class="action-icon ochre">
                <SvgIcon name="add" :size="24" />
              </div>
              <span>新增商品</span>
            </div>
            <div class="quick-action-item" @click="$router.push('/orders')">
              <div class="action-icon turmeric">
                <SvgIcon name="ship" :size="24" />
              </div>
              <span>订单发货</span>
            </div>
            <div class="quick-action-item" @click="$router.push('/articles')">
              <div class="action-icon jade">
                <SvgIcon name="edit" :size="24" />
              </div>
              <span>发布文章</span>
            </div>
            <div class="quick-action-item" @click="$router.push('/announcements')">
              <div class="action-icon ink">
                <SvgIcon name="announcement" :size="24" />
              </div>
              <span>发布公告</span>
            </div>
          </div>

          <!-- 系统提醒 -->
          <div class="system-alerts">
            <div class="alert-title">
              <SvgIcon name="warning" :size="16" />
              <span>系统提醒</span>
            </div>
            <div class="alert-list">
              <div v-if="dashboardData.stockWarningCount > 0" class="alert-item warning">
                <span>{{ dashboardData.stockWarningCount }} 件商品库存不足</span>
                <a href="#">立即处理</a>
              </div>
              <div v-if="dashboardData.pendingPostCount > 0" class="alert-item info">
                <span>{{ dashboardData.pendingPostCount }} 条帖子待审核</span>
                <a href="#">立即审核</a>
              </div>
              <div v-if="!dashboardData.stockWarningCount && !dashboardData.pendingPostCount" class="alert-item success">
                <SvgIcon name="success" :size="16" />
                <span>暂无待处理事项</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { getDashboard, getSalesTrend } from 'shared/api/admin.js'
import { formatMoney } from 'shared/utils/index.js'

const salesChartRef = ref(null)
const orderChartRef = ref(null)
const trendDays = ref(7)

let salesChart = null
let orderChart = null

const dashboardData = reactive({
  todaySales: 0,
  todayOrders: 0,
  todayUsers: 0,
  pendingShipOrders: 0,
  stockWarningCount: 0,
  pendingPostCount: 0,
  weekSales: 0,
  monthSales: 0,
  totalUsers: 0,
  totalProducts: 0,
  totalOrders: 0,
  salesTrend: [],
  productRank: [],
  orderStatusDist: []
})

// 初始化销售趋势图表
function initSalesChart(data) {
  if (!salesChartRef.value) return
  
  if (!salesChart) {
    salesChart = echarts.init(salesChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#FAF7F0',
      borderColor: '#D4C9B8',
      textStyle: {
        color: '#4A4036'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.map(item => item.date),
      axisLine: {
        lineStyle: { color: '#D4C9B8' }
      },
      axisLabel: {
        color: '#6B5E52'
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        show: false
      },
      axisLabel: {
        color: '#6B5E52'
      },
      splitLine: {
        lineStyle: { color: '#E6DFD2', type: 'dashed' }
      }
    },
    series: [
      {
        name: '销售额',
        type: 'line',
        smooth: true,
        data: data.map(item => item.amount || item.sales || 0),
        itemStyle: {
          color: '#8B4513'
        },
        lineStyle: {
          width: 3,
          color: '#8B4513'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(139, 69, 19, 0.3)' },
              { offset: 1, color: 'rgba(139, 69, 19, 0.05)' }
            ]
          }
        }
      }
    ]
  }

  salesChart.setOption(option)
}

// 初始化订单状态分布图表
function initOrderChart(data) {
  if (!orderChartRef.value) return
  
  if (!orderChart) {
    orderChart = echarts.init(orderChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'item',
      backgroundColor: '#FAF7F0',
      borderColor: '#D4C9B8',
      textStyle: {
        color: '#4A4036'
      }
    },
    legend: {
      bottom: '5%',
      left: 'center',
      textStyle: {
        color: '#6B5E52'
      }
    },
    series: [
      {
        name: '订单状态',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: '#FAF7F0',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.map(item => ({
          name: item.statusName,
          value: item.count
        })),
        color: ['#E6A23C', '#409EFF', '#67C23A', '#909399', '#F56C6C']
      }
    ]
  }

  orderChart.setOption(option)
}

// 获取仪表盘数据
async function fetchDashboard() {
  try {
    const data = await getDashboard()
    Object.assign(dashboardData, data)
    
    // 初始化图表
    if (data.salesTrend) {
      initSalesChart(data.salesTrend)
    }
    if (data.orderStatusDist) {
      initOrderChart(data.orderStatusDist)
    }
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
    // 使用模拟数据
    initSalesChart([
      { date: '01-28', sales: 1200 },
      { date: '01-29', sales: 1500 },
      { date: '01-30', sales: 1800 },
      { date: '01-31', sales: 1400 },
      { date: '02-01', sales: 2100 },
      { date: '02-02', sales: 1900 },
      { date: '02-03', sales: 2400 }
    ])
    initOrderChart([
      { statusName: '待支付', count: 12 },
      { statusName: '已支付', count: 25 },
      { statusName: '已发货', count: 18 },
      { statusName: '已完成', count: 45 },
      { statusName: '已取消', count: 8 }
    ])
  }
}

// 获取销售趋势
async function fetchSalesTrend() {
  try {
    const data = await getSalesTrend(trendDays.value)
    initSalesChart(data)
  } catch (error) {
    console.error('获取销售趋势失败:', error)
  }
}

// 监听窗口大小变化
function handleResize() {
  salesChart?.resize()
  orderChart?.resize()
}

onMounted(() => {
  fetchDashboard()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  salesChart?.dispose()
  orderChart?.dispose()
})
</script>

<style scoped>
.dashboard-page {
  min-height: 100%;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-row .el-col {
  margin-bottom: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-row .el-col {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.overview-row .el-col {
  margin-bottom: 20px;
}

/* 查看更多 */
.view-more {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--tcm-ochre);
  text-decoration: none;
}

.view-more:hover {
  color: var(--tcm-ochre-light);
}

/* 排行榜 */
.rank-list {
  padding: 8px 0;
}

.rank-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--tcm-border-lighter);
}

.rank-item:last-child {
  border-bottom: none;
}

.rank-num {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: var(--tcm-text-secondary);
  background: var(--tcm-bg-paper-dark);
  border-radius: 4px;
  margin-right: 12px;
}

.rank-num.top3 {
  background: var(--tcm-ochre);
  color: #fff;
}

.rank-name {
  flex: 1;
  font-size: 14px;
  color: var(--tcm-text-regular);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.rank-value {
  font-size: 14px;
  font-weight: 500;
  color: var(--tcm-ochre);
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid var(--tcm-border-lighter);
}

.quick-action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  border-radius: var(--tcm-radius-md);
  cursor: pointer;
  transition: all var(--tcm-transition-fast);
}

.quick-action-item:hover {
  background: var(--tcm-ochre-50);
}

.action-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--tcm-radius-md);
}

.action-icon.ochre {
  background: var(--tcm-ochre-100);
  color: var(--tcm-ochre);
}

.action-icon.turmeric {
  background: var(--tcm-turmeric-100);
  color: var(--tcm-turmeric);
}

.action-icon.jade {
  background: rgba(46, 139, 87, 0.1);
  color: var(--tcm-jade);
}

.action-icon.ink {
  background: rgba(47, 79, 79, 0.1);
  color: var(--tcm-ink-cyan);
}

.quick-action-item span {
  font-size: 13px;
  color: var(--tcm-text-regular);
}

/* 系统提醒 */
.system-alerts {
  padding-top: 16px;
}

.alert-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 500;
  color: var(--tcm-text-primary);
  margin-bottom: 12px;
}

.alert-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.alert-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  border-radius: var(--tcm-radius-base);
  font-size: 13px;
}

.alert-item.warning {
  background: rgba(255, 185, 15, 0.1);
  color: var(--tcm-amber);
}

.alert-item.info {
  background: rgba(47, 79, 79, 0.1);
  color: var(--tcm-ink-cyan);
}

.alert-item.success {
  background: rgba(46, 139, 87, 0.1);
  color: var(--tcm-jade);
  gap: 8px;
  justify-content: flex-start;
}

.alert-item a {
  color: inherit;
  text-decoration: none;
  font-weight: 500;
}

.alert-item a:hover {
  text-decoration: underline;
}

/* 响应式 */
@media (max-width: 768px) {
  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
