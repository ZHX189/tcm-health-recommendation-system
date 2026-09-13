<!--
  养生方案详情页面
  @author Ti
  @since 2026-02-05
-->
<template>
  <div class="health-plan-detail-page" v-loading="loading">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/health' }">养生方案</el-breadcrumb-item>
        <el-breadcrumb-item>{{ plan.name || '方案详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="detail-container" v-if="plan.id">
      <!-- 方案头部 -->
      <div class="plan-header">
        <div class="plan-tags">
          <span class="plan-tag constitution">{{ plan.constitutionType }}</span>
          <span class="plan-tag season">{{ plan.season || '四季' }}适用</span>
        </div>
        <h1 class="plan-title">{{ plan.name }}</h1>
        <p class="plan-desc">{{ plan.description }}</p>
        <div class="plan-meta">
          <span><SvgIcon name="clock" :size="14" />发布时间：{{ formatDate(plan.createTime) }}</span>
          <span><SvgIcon name="view" :size="14" />浏览：{{ plan.viewCount || 0 }}次</span>
        </div>
      </div>

      <!-- 方案内容 -->
      <div class="plan-content">
        <!-- 饮食调理 -->
        <div class="content-section" v-if="plan.dietAdvice">
          <h3 class="section-title"><SvgIcon name="food" :size="20" />饮食调理</h3>
          <div class="section-body" v-html="formatContent(plan.dietAdvice)"></div>
        </div>

        <!-- 起居养生 -->
        <div class="content-section" v-if="plan.lifestyleAdvice">
          <h3 class="section-title"><SvgIcon name="lifestyle" :size="20" />起居养生</h3>
          <div class="section-body" v-html="formatContent(plan.lifestyleAdvice)"></div>
        </div>

        <!-- 运动建议 -->
        <div class="content-section" v-if="plan.exerciseAdvice">
          <h3 class="section-title"><SvgIcon name="sport" :size="20" />运动建议</h3>
          <div class="section-body" v-html="formatContent(plan.exerciseAdvice)"></div>
        </div>

        <!-- 睡眠建议 -->
        <div class="content-section" v-if="plan.sleepAdvice">
          <h3 class="section-title"><SvgIcon name="sleep" :size="20" />睡眠建议</h3>
          <div class="section-body" v-html="formatContent(plan.sleepAdvice)"></div>
        </div>

        <!-- 情志调摄 -->
        <div class="content-section" v-if="plan.emotionAdvice">
          <h3 class="section-title"><SvgIcon name="emotion" :size="20" />情志调摄</h3>
          <div class="section-body" v-html="formatContent(plan.emotionAdvice)"></div>
        </div>

        <!-- 穴位保健 -->
        <div class="content-section" v-if="plan.acupointAdvice">
          <h3 class="section-title"><SvgIcon name="acupoint" :size="20" />穴位保健</h3>
          <div class="section-body" v-html="formatContent(plan.acupointAdvice)"></div>
        </div>

        <!-- 药材建议 -->
        <div class="content-section" v-if="plan.medicineAdvice">
          <h3 class="section-title"><SvgIcon name="medicine" :size="20" />药材建议</h3>
          <div class="section-body" v-html="formatContent(plan.medicineAdvice)"></div>
        </div>

        <!-- 禁忌事项 -->
        <div class="content-section" v-if="plan.taboo">
          <h3 class="section-title"><SvgIcon name="taboo" :size="20" />禁忌事项</h3>
          <div class="section-body" v-html="formatContent(plan.taboo)"></div>
        </div>

        <!-- 详细内容 -->
        <div class="content-section" v-if="plan.content">
          <h3 class="section-title"><SvgIcon name="detail" :size="20" />详细内容</h3>
          <div class="section-body rich-content" v-html="plan.content"></div>
        </div>
      </div>

      <!-- 返回按钮 -->
      <div class="back-action">
        <el-button @click="$router.push('/health')">
          <SvgIcon name="arrow-left" :size="16" class="mr-8" />返回方案列表
        </el-button>
      </div>
    </div>

    <el-empty v-if="!loading && !plan.id" description="方案不存在或已下架" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getHealthPlanDetail } from 'shared/api/user.js'
import { formatDate } from 'shared/utils/index.js'

const route = useRoute()
const loading = ref(false)
const plan = ref({})

// 格式化内容（将换行转为HTML）
function formatContent(text) {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

async function fetchPlan() {
  loading.value = true
  try {
    const data = await getHealthPlanDetail(route.params.id)
    plan.value = data || {}
  } catch (error) {
    console.error('获取方案详情失败:', error)
    // 模拟数据
    plan.value = {
      id: route.params.id,
      name: '气虚体质春季调养方案',
      constitutionType: '气虚质',
      season: '春季',
      description: '针对气虚体质人群，在春季进行温和调养，增强体质，提升免疫力。',
      dietAdvice: '1. 宜食用补气健脾的食物，如山药、大枣、莲子、黄芪等\n2. 少食生冷、油腻、辛辣刺激性食物\n3. 可适当食用粥类，如山药粥、红枣粥等\n4. 多吃新鲜蔬菜水果，补充维生素',
      lifestyleAdvice: '1. 保证充足睡眠，每天7-8小时\n2. 避免过度劳累，注意劳逸结合\n3. 居室保持通风，温度适宜\n4. 春季早睡早起，顺应自然规律',
      exerciseAdvice: '1. 选择柔和的运动方式，如太极拳、八段锦、散步等\n2. 运动强度不宜过大，以微微出汗为宜\n3. 每天运动30-60分钟\n4. 避免剧烈运动和大汗淋漓',
      emotionAdvice: '1. 保持心情舒畅，避免过度思虑\n2. 多与朋友交流，参加社交活动\n3. 培养兴趣爱好，陶冶情操\n4. 遇事不急躁，保持平和心态',
      acupointAdvice: '1. 足三里：位于小腿外侧，常按可补气健脾\n2. 气海穴：位于脐下1.5寸，可培补元气\n3. 关元穴：位于脐下3寸，可温补下元\n4. 每日按摩5-10分钟，力度适中',
      viewCount: 256,
      createTime: '2026-02-01'
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchPlan())
</script>

<style scoped>
.health-plan-detail-page { min-height: 60vh; }

.detail-container { max-width: 900px; margin: 0 auto; }

.plan-header {
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 32px;
  margin-bottom: 24px;
}

.plan-tags { display: flex; gap: 12px; margin-bottom: 16px; }

.plan-tag {
  display: inline-block;
  padding: 4px 12px;
  font-size: 12px;
  border-radius: 2px;
}

.plan-tag.constitution {
  background: var(--tcm-ochre-100);
  color: var(--tcm-ochre);
}

.plan-tag.season {
  background: rgba(46, 139, 87, 0.1);
  color: var(--tcm-jade);
}

.plan-title {
  font-family: var(--tcm-font-title);
  font-size: 28px;
  color: var(--tcm-text-primary);
  margin: 0 0 16px;
}

.plan-desc {
  font-size: 15px;
  color: var(--tcm-text-secondary);
  line-height: 1.8;
  margin-bottom: 20px;
}

.plan-meta {
  display: flex;
  gap: 24px;
  font-size: 13px;
  color: var(--tcm-text-placeholder);
}

.plan-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.plan-content {
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 32px;
  margin-bottom: 24px;
}

.content-section { margin-bottom: 32px; }
.content-section:last-child { margin-bottom: 0; }

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: var(--tcm-font-title);
  font-size: 18px;
  color: var(--tcm-ochre);
  margin: 0 0 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--tcm-border-lighter);
}

.section-body {
  font-size: 14px;
  color: var(--tcm-text-regular);
  line-height: 2;
}

.rich-content :deep(p) { margin-bottom: 12px; }
.rich-content :deep(img) { max-width: 100%; border-radius: 8px; }

.back-action { text-align: center; }
</style>
