<!--
  用户端首页（含个性化推荐）
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="home-page">
    <!-- Banner区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">传承中医智慧</h1>
        <p class="hero-subtitle">守护全民健康</p>
        <p class="hero-desc">专业中医药材，科学养生方案，让健康触手可及</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="$router.push('/products')">
            <SvgIcon name="herb" :size="18" class="mr-8" />浏览药材
          </el-button>
          <el-button size="large" @click="$router.push('/health')">
            <SvgIcon name="wellness" :size="18" class="mr-8" />养生方案
          </el-button>
        </div>
      </div>
      <div class="hero-decoration">
        <div class="ink-circle"></div>
        <div class="ink-circle delay"></div>
      </div>
    </section>

    <!-- 个性化推荐引导：已登录但未填写健康档案 -->
    <section v-if="isLoggedIn && recommendation && !recommendation.hasHealthRecord" class="guide-section">
      <div class="guide-card">
        <div class="guide-icon">
          <SvgIcon name="wellness" :size="36" />
        </div>
        <div class="guide-info">
          <h3 class="guide-title">开启您的专属养生之旅</h3>
          <p class="guide-desc">填写健康档案，系统将根据您的体质类型，为您智能推荐适合的养生方案、药材和文章</p>
        </div>
        <el-button type="primary" @click="$router.push('/health-record')">
          <SvgIcon name="wellness" :size="16" class="mr-8" />立即填写健康档案
        </el-button>
      </div>
    </section>

    <!-- 体质个性化推荐：左侧精选大卡 + 右侧列表 -->
    <section v-if="recommendation && recommendation.hasHealthRecord" class="personal-section">
      <div class="section-header">
        <h2 class="section-title">
          <span class="personal-badge">{{ recommendation.constitutionType }}</span>
          为您定制的养生推荐
        </h2>
      </div>
      <el-tabs v-model="personalTab" class="personal-tabs">
        <el-tab-pane label="推荐药材" name="products">
          <div class="featured-layout" v-if="recommendation.constitutionProducts?.length">
            <div class="featured-main" @click="goToProduct(recommendation.constitutionProducts[0].id)">
              <div class="featured-main-image">
                <img :src="getImageUrl(recommendation.constitutionProducts[0].mainImage)" :alt="recommendation.constitutionProducts[0].name" />
              </div>
              <div class="featured-main-info">
                <div class="featured-main-name">{{ recommendation.constitutionProducts[0].name }}</div>
                <div class="featured-main-efficacy">{{ recommendation.constitutionProducts[0].efficacy || recommendation.constitutionProducts[0].subTitle || '' }}</div>
                <div class="featured-main-price">¥{{ recommendation.constitutionProducts[0].price }}</div>
              </div>
            </div>
            <div class="featured-side">
              <div v-for="product in recommendation.constitutionProducts.slice(1, 7)" :key="'cp-' + product.id"
                   class="featured-item" @click="goToProduct(product.id)">
                <div class="featured-item-image">
                  <img :src="getImageUrl(product.mainImage)" :alt="product.name" />
                </div>
                <div class="featured-item-info">
                  <div class="featured-item-name">{{ product.name }}</div>
                  <div class="featured-item-desc">{{ product.subTitle || product.efficacy || '' }}</div>
                  <div class="featured-item-price">¥{{ product.price }} <span class="featured-item-sales">已售{{ product.sales || 0 }}件</span></div>
                </div>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无推荐药材" :image-size="80" />
        </el-tab-pane>
        <el-tab-pane label="推荐方案" name="plans">
          <div class="health-grid" v-if="recommendation.constitutionPlans?.length">
            <div v-for="plan in recommendation.constitutionPlans" :key="'cpl-' + plan.id"
                 class="health-card" @click="goToHealthPlan(plan.id)">
              <div class="health-card-tag">{{ plan.constitutionType }}</div>
              <h3 class="health-card-title">{{ plan.title || plan.name }}</h3>
              <p class="health-card-desc">{{ plan.summary || '专业养生调理方案' }}</p>
              <div class="health-card-season">
                <SvgIcon name="calendar" :size="14" />
                <span>{{ plan.season || '四季' }}适用</span>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无推荐方案" :image-size="80" />
        </el-tab-pane>
        <el-tab-pane label="推荐文章" name="articles">
          <div class="article-grid" v-if="recommendation.constitutionArticles?.length">
            <div v-for="article in recommendation.constitutionArticles" :key="'ca-' + article.id"
                 class="article-card" @click="goToArticle(article.id)">
              <div class="article-card-cover">
                <img :src="getImageUrl(article.coverImage)" :alt="article.title" />
              </div>
              <div class="article-card-body">
                <h3 class="article-card-title">{{ article.title }}</h3>
                <p class="article-card-summary">{{ article.summary || '' }}</p>
                <div class="article-card-meta">
                  <span>{{ article.author || '' }}</span>
                  <span>{{ article.viewCount || 0 }}次阅读</span>
                </div>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无推荐文章" :image-size="80" />
        </el-tab-pane>
      </el-tabs>
    </section>

    <!-- 应季养生推荐：横向滚动卡片 + 季节主题背景 -->
    <section v-if="recommendation && (recommendation.seasonalProducts?.length || recommendation.seasonalPlans?.length)" class="seasonal-section">
      <div class="seasonal-banner">
        <div class="seasonal-banner-header">
          <h2 class="seasonal-banner-title">
            <span class="season-badge">{{ recommendation.currentSeason }}</span>
            应季养生推荐
          </h2>
          <p class="seasonal-banner-desc">顺应时令，{{ recommendation.currentSeason }}养生正当时</p>
        </div>
        <div class="seasonal-scroll-wrapper">
          <div class="seasonal-scroll">
            <div v-for="plan in recommendation.seasonalPlans" :key="'sp-' + plan.id"
                 class="seasonal-scroll-card seasonal-scroll-plan" @click="goToHealthPlan(plan.id)">
              <div class="seasonal-scroll-icon">
                <SvgIcon name="wellness" :size="28" />
              </div>
              <div class="seasonal-scroll-tag">{{ plan.constitutionType || '通用' }}</div>
              <h3 class="seasonal-scroll-title">{{ plan.title || plan.name }}</h3>
              <p class="seasonal-scroll-desc">{{ plan.summary || '应季养生调理方案' }}</p>
            </div>
            <div v-for="product in recommendation.seasonalProducts?.slice(0, 6)" :key="'spd-' + product.id"
                 class="seasonal-scroll-card seasonal-scroll-product" @click="goToProduct(product.id)">
              <div class="seasonal-scroll-img">
                <img :src="getImageUrl(product.mainImage)" :alt="product.name" />
              </div>
              <h3 class="seasonal-scroll-title">{{ product.name }}</h3>
              <p class="seasonal-scroll-desc">{{ product.subTitle || product.efficacy || '' }}</p>
              <div class="seasonal-scroll-price">¥{{ product.price }}</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 猜你喜欢：紧凑横向排列 -->
    <section v-if="recommendation && recommendation.behaviorProducts?.length" class="behavior-section">
      <div class="section-header">
        <h2 class="section-title">猜你喜欢</h2>
        <router-link to="/products" class="view-all">
          查看更多 <SvgIcon name="arrow-right" :size="14" />
        </router-link>
      </div>
      <div class="behavior-list">
        <div v-for="product in recommendation.behaviorProducts" :key="'bp-' + product.id"
             class="behavior-item" @click="goToProduct(product.id)">
          <div class="behavior-item-image">
            <img :src="getImageUrl(product.mainImage)" :alt="product.name" />
          </div>
          <div class="behavior-item-info">
            <div class="behavior-item-name">{{ product.name }}</div>
            <div class="behavior-item-desc">{{ product.subTitle || product.efficacy || '' }}</div>
            <div class="behavior-item-bottom">
              <span class="behavior-item-price">¥{{ product.price }}</span>
              <span class="behavior-item-sales">已售{{ product.sales || 0 }}件</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 分类导航 -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">药材分类</h2>
        <router-link to="/products" class="view-all">
          全部分类 <SvgIcon name="arrow-right" :size="14" />
        </router-link>
      </div>
      <div class="category-list">
        <div v-for="cat in categories" :key="cat.id" class="category-group">
          <div class="category-parent" @click="goToCategory(cat.id)">
            <div class="category-icon">
              <img v-if="cat.icon" :src="getImageUrl(cat.icon)" :alt="cat.name" class="category-icon-img" />
              <SvgIcon v-else name="herb" :size="32" />
            </div>
            <span class="category-name">{{ cat.name }}</span>
          </div>
          <div v-if="cat.children && cat.children.length > 0" class="category-children">
            <div v-for="child in cat.children" :key="child.id" class="category-child"
                 @click.stop="goToCategory(child.id)">
              <img v-if="child.icon" :src="getImageUrl(child.icon)" :alt="child.name" class="child-icon-img" />
              <SvgIcon v-else name="herb" :size="16" />
              <span>{{ child.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 热门药材 -->
    <section class="product-section">
      <div class="section-header">
        <h2 class="section-title">热门药材</h2>
        <router-link to="/products" class="view-all">
          查看更多 <SvgIcon name="arrow-right" :size="14" />
        </router-link>
      </div>
      <div class="product-grid">
        <div v-for="product in products" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <div class="product-card-image-wrapper">
            <img :src="getImageUrl(product.mainImage)" :alt="product.name" class="product-card-image" />
          </div>
          <div class="product-card-content">
            <div class="product-card-name">{{ product.name }}</div>
            <div class="product-card-desc">{{ product.subTitle || product.description || '' }}</div>
            <div class="product-card-bottom">
              <span class="product-card-price">{{ product.price }}</span>
              <span class="product-card-sales">已售{{ product.sales || 0 }}件</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 养生方案 -->
    <section class="health-section">
      <div class="section-header">
        <h2 class="section-title">养生方案</h2>
        <router-link to="/health" class="view-all">
          更多方案 <SvgIcon name="arrow-right" :size="14" />
        </router-link>
      </div>
      <div class="health-grid">
        <div v-for="plan in healthPlans" :key="plan.id" class="health-card" @click="goToHealthPlan(plan.id)">
          <div class="health-card-tag">{{ plan.constitutionType }}</div>
          <h3 class="health-card-title">{{ plan.name }}</h3>
          <p class="health-card-desc">{{ plan.description || '专业养生调理方案' }}</p>
          <div class="health-card-season">
            <SvgIcon name="calendar" :size="14" />
            <span>{{ plan.season || '四季' }}适用</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 公告通知 -->
    <section v-if="announcements.length > 0" class="announcement-section">
      <div class="section-header">
        <h2 class="section-title">平台公告</h2>
      </div>
      <div class="announcement-bar">
        <div class="announcement-list">
          <div v-for="ann in announcements" :key="ann.id" class="announcement-item" @click="showAnnouncement(ann)">
            <div class="announcement-label"><SvgIcon name="announcement" :size="14" /></div>
            <span class="announcement-title">{{ ann.title }}</span>
            <span class="announcement-time">{{ formatDate(ann.publishTime || ann.createTime) }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 公告详情对话框 -->
    <el-dialog v-model="announcementVisible" :title="currentAnnouncement?.title" width="600px">
      <div class="announcement-detail">
        <div class="announcement-detail-time">
          <SvgIcon name="clock" :size="14" />
          {{ formatDate(currentAnnouncement?.publishTime || currentAnnouncement?.createTime) }}
        </div>
        <div class="announcement-detail-content" v-html="currentAnnouncement?.content?.replace(/\n/g, '<br/>')"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductList, getProductCategories, getHealthPlanList, getAnnouncementList, getHomeRecommendations } from 'shared/api/user.js'
import { formatDate, getToken } from 'shared/utils/index.js'

const router = useRouter()
const categories = ref([])
const products = ref([])
const healthPlans = ref([])
const announcements = ref([])
const announcementVisible = ref(false)
const currentAnnouncement = ref(null)
const recommendation = ref(null)
const personalTab = ref('products')

const isLoggedIn = computed(() => !!getToken())

const API_BASE_URL = ''

function getImageUrl(url) {
  if (!url) return '/placeholder.png'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

async function fetchData() {
  try {
    const [catData, productData, planData, annData] = await Promise.all([
      getProductCategories().catch(() => []),
      getProductList({ pageNum: 1, pageSize: 8 }).catch(() => ({ records: [] })),
      getHealthPlanList({ pageNum: 1, pageSize: 4 }).catch(() => ({ records: [] })),
      getAnnouncementList({ pageNum: 1, pageSize: 5 }).catch(() => ({ records: [] }))
    ])
    categories.value = catData || []
    products.value = productData?.records || []
    healthPlans.value = planData?.records || []
    announcements.value = annData?.records || []
  } catch (error) {
    categories.value = [
      { id: 1, name: '中药材', icon: '', children: [
        { id: 6, name: '补气类', icon: '' },
        { id: 7, name: '补血类', icon: '' },
        { id: 8, name: '清热类', icon: '' }
      ]},
      { id: 2, name: '养生茶饮', icon: '', children: [
        { id: 11, name: '花草茶', icon: '' },
        { id: 12, name: '养生茶包', icon: '' }
      ]},
      { id: 3, name: '滋补品', icon: '', children: [] },
      { id: 4, name: '养生器具', icon: '', children: [] }
    ]
    products.value = [
      { id: 1, name: '野生灵芝', subTitle: '深山野生', price: 299.00, sales: 128, mainImage: '' },
      { id: 2, name: '人参片', subTitle: '长白山正品', price: 199.00, sales: 256, mainImage: '' },
      { id: 3, name: '枸杞子', subTitle: '宁夏特级', price: 58.00, sales: 512, mainImage: '' },
      { id: 4, name: '当归片', subTitle: '甘肃岷县', price: 68.00, sales: 189, mainImage: '' }
    ]
    healthPlans.value = [
      { id: 1, name: '气虚体质调养方案', constitutionType: '气虚质', season: '春季' },
      { id: 2, name: '阳虚体质温补方案', constitutionType: '阳虚质', season: '冬季' }
    ]
  }
}

async function fetchRecommendations() {
  if (!isLoggedIn.value) return
  try {
    const data = await getHomeRecommendations()
    recommendation.value = data
  } catch (error) {
    // 未登录或接口异常，不影响正常首页展示
  }
}

function goToCategory(id) {
  router.push({ path: '/products', query: { categoryId: id } })
}

function goToProduct(id) {
  router.push(`/products/${id}`)
}

function goToHealthPlan(id) {
  router.push(`/health/${id}`)
}

function goToArticle(id) {
  router.push(`/articles/${id}`)
}

function showAnnouncement(ann) {
  currentAnnouncement.value = ann
  announcementVisible.value = true
}

onMounted(() => {
  fetchData()
  fetchRecommendations()
})
</script>

<style scoped>
.home-page { padding-bottom: 40px; }

/* Hero区域 */
.hero-section {
  position: relative;
  background: linear-gradient(135deg, var(--tcm-ochre-50) 0%, var(--tcm-turmeric-50) 100%);
  border-radius: var(--tcm-radius-lg);
  padding: 60px 40px;
  margin-bottom: 40px;
  overflow: hidden;
}

.hero-content { position: relative; z-index: 1; max-width: 500px; }
.hero-title { font-family: var(--tcm-font-title); font-size: 42px; color: var(--tcm-ochre-dark); margin: 0 0 8px; letter-spacing: 4px; }
.hero-subtitle { font-family: var(--tcm-font-title); font-size: 32px; color: var(--tcm-ochre); margin: 0 0 16px; letter-spacing: 2px; }
.hero-desc { font-size: 16px; color: var(--tcm-text-secondary); margin-bottom: 24px; line-height: 1.6; }
.hero-actions { display: flex; gap: 16px; }

.hero-decoration { position: absolute; right: 10%; top: 50%; transform: translateY(-50%); }
.ink-circle { width: 200px; height: 200px; border-radius: 50%; background: radial-gradient(circle, var(--tcm-ochre-100) 0%, transparent 70%); animation: pulse 4s infinite ease-in-out; }
.ink-circle.delay { position: absolute; top: 20px; left: -40px; width: 150px; height: 150px; animation-delay: 1s; }
@keyframes pulse { 0%, 100% { transform: scale(1); opacity: 0.6; } 50% { transform: scale(1.1); opacity: 0.4; } }

/* ==================== 个性化推荐引导 ==================== */
.guide-section { margin-bottom: 40px; }
.guide-card {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 28px 32px;
  background: linear-gradient(135deg, var(--tcm-ochre-50) 0%, #fdf6ee 50%, var(--tcm-turmeric-50) 100%);
  border: 1px dashed var(--tcm-ochre-200);
  border-radius: var(--tcm-radius-lg);
}
.guide-icon {
  flex-shrink: 0;
  width: 64px; height: 64px;
  display: flex; align-items: center; justify-content: center;
  background: var(--tcm-ochre-100);
  border-radius: 50%;
  color: var(--tcm-ochre);
}
.guide-info { flex: 1; }
.guide-title { font-family: var(--tcm-font-title); font-size: 18px; color: var(--tcm-text-primary); margin: 0 0 6px; }
.guide-desc { font-size: 14px; color: var(--tcm-text-secondary); margin: 0; line-height: 1.6; }

/* ==================== 体质个性化推荐（精选布局） ==================== */
.personal-section { margin-bottom: 40px; }
.personal-badge {
  display: inline-block;
  padding: 2px 10px;
  background: var(--tcm-ochre);
  color: #fff;
  font-size: 13px;
  border-radius: 2px;
  margin-right: 8px;
  vertical-align: middle;
}
.personal-tabs :deep(.el-tabs__item) { font-size: 15px; }
.personal-tabs :deep(.el-tabs__active-bar) { background-color: var(--tcm-ochre); }
.personal-tabs :deep(.el-tabs__item.is-active) { color: var(--tcm-ochre); }

.featured-layout { display: flex; gap: 20px; min-height: 320px; }
.featured-main {
  flex: 0 0 300px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--tcm-transition-base);
  display: flex;
  flex-direction: column;
}
.featured-main:hover { box-shadow: var(--tcm-shadow-lg); border-color: var(--tcm-ochre); }
.featured-main-image { width: 100%; height: 200px; overflow: hidden; background: var(--tcm-bg-paper-dark); }
.featured-main-image img { width: 100%; height: 100%; object-fit: cover; transition: transform var(--tcm-transition-base); }
.featured-main:hover .featured-main-image img { transform: scale(1.05); }
.featured-main-info { padding: 16px; flex: 1; display: flex; flex-direction: column; justify-content: space-between; }
.featured-main-name { font-family: var(--tcm-font-title); font-size: 18px; color: var(--tcm-text-primary); margin-bottom: 8px; }
.featured-main-efficacy { font-size: 13px; color: var(--tcm-text-secondary); line-height: 1.5; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; margin-bottom: 8px; }
.featured-main-price { font-size: 20px; font-weight: 600; color: var(--tcm-vermilion); }

.featured-side { flex: 1; display: flex; flex-direction: column; gap: 10px; overflow-y: auto; }
.featured-item {
  display: flex;
  gap: 14px;
  padding: 12px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  cursor: pointer;
  transition: all var(--tcm-transition-base);
}
.featured-item:hover { border-color: var(--tcm-ochre); box-shadow: var(--tcm-shadow-sm); }
.featured-item-image { flex-shrink: 0; width: 64px; height: 64px; border-radius: var(--tcm-radius-sm); overflow: hidden; background: var(--tcm-bg-paper-dark); }
.featured-item-image img { width: 100%; height: 100%; object-fit: cover; }
.featured-item-info { flex: 1; min-width: 0; display: flex; flex-direction: column; justify-content: center; }
.featured-item-name { font-size: 14px; font-weight: 500; color: var(--tcm-text-primary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-bottom: 4px; }
.featured-item-desc { font-size: 12px; color: var(--tcm-text-secondary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-bottom: 4px; }
.featured-item-price { font-size: 14px; font-weight: 600; color: var(--tcm-vermilion); }
.featured-item-sales { font-size: 12px; font-weight: 400; color: var(--tcm-text-placeholder); margin-left: 8px; }

/* ==================== 应季养生推荐（横向滚动 + 主题背景） ==================== */
.seasonal-section { margin-bottom: 40px; }
.seasonal-banner {
  background: linear-gradient(135deg, #e8f5e9 0%, #f1f8e9 40%, #fffde7 100%);
  border: 1px solid rgba(46, 139, 87, 0.12);
  border-radius: var(--tcm-radius-lg);
  padding: 28px 32px 24px;
}
.seasonal-banner-header { margin-bottom: 20px; }
.seasonal-banner-title { font-family: var(--tcm-font-title); font-size: 22px; color: var(--tcm-text-primary); margin: 0 0 6px; }
.season-badge {
  display: inline-block;
  padding: 2px 10px;
  background: var(--tcm-jade);
  color: #fff;
  font-size: 13px;
  border-radius: 2px;
  margin-right: 8px;
  vertical-align: middle;
}
.seasonal-banner-desc { font-size: 14px; color: var(--tcm-text-secondary); margin: 0; }

.seasonal-scroll-wrapper { overflow-x: auto; margin: 0 -8px; padding: 0 8px 8px; }
.seasonal-scroll-wrapper::-webkit-scrollbar { height: 6px; }
.seasonal-scroll-wrapper::-webkit-scrollbar-thumb { background: rgba(46, 139, 87, 0.2); border-radius: 3px; }
.seasonal-scroll { display: flex; gap: 16px; padding-bottom: 4px; }

.seasonal-scroll-card {
  flex: 0 0 220px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(6px);
  border: 1px solid rgba(46, 139, 87, 0.12);
  border-radius: var(--tcm-radius-md);
  padding: 20px;
  cursor: pointer;
  transition: all var(--tcm-transition-base);
}
.seasonal-scroll-card:hover { transform: translateY(-4px); box-shadow: var(--tcm-shadow-md); }

.seasonal-scroll-icon { width: 44px; height: 44px; display: flex; align-items: center; justify-content: center; background: rgba(46, 139, 87, 0.1); border-radius: 50%; color: var(--tcm-jade); margin-bottom: 12px; }
.seasonal-scroll-tag { display: inline-block; padding: 2px 8px; background: rgba(46, 139, 87, 0.08); color: var(--tcm-jade); font-size: 11px; border-radius: 2px; margin-bottom: 8px; }
.seasonal-scroll-title { font-family: var(--tcm-font-title); font-size: 15px; color: var(--tcm-text-primary); margin: 0 0 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.seasonal-scroll-desc { font-size: 12px; color: var(--tcm-text-secondary); margin: 0; line-height: 1.5; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.seasonal-scroll-img { width: 100%; height: 120px; border-radius: var(--tcm-radius-sm); overflow: hidden; background: var(--tcm-bg-paper-dark); margin-bottom: 10px; }
.seasonal-scroll-img img { width: 100%; height: 100%; object-fit: cover; }
.seasonal-scroll-price { font-size: 15px; font-weight: 600; color: var(--tcm-vermilion); margin-top: 8px; }

/* ==================== 猜你喜欢（紧凑横向卡片） ==================== */
.behavior-section { margin-bottom: 40px; }
.behavior-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 14px; }
.behavior-item {
  display: flex;
  gap: 14px;
  padding: 14px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  cursor: pointer;
  transition: all var(--tcm-transition-base);
}
.behavior-item:hover { border-color: var(--tcm-ochre); box-shadow: var(--tcm-shadow-sm); }
.behavior-item-image { flex-shrink: 0; width: 80px; height: 80px; border-radius: var(--tcm-radius-sm); overflow: hidden; background: var(--tcm-bg-paper-dark); }
.behavior-item-image img { width: 100%; height: 100%; object-fit: cover; }
.behavior-item-info { flex: 1; min-width: 0; display: flex; flex-direction: column; justify-content: space-between; }
.behavior-item-name { font-size: 14px; font-weight: 500; color: var(--tcm-text-primary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.behavior-item-desc { font-size: 12px; color: var(--tcm-text-secondary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.behavior-item-bottom { display: flex; align-items: center; gap: 8px; }
.behavior-item-price { font-size: 15px; font-weight: 600; color: var(--tcm-vermilion); }
.behavior-item-sales { font-size: 12px; color: var(--tcm-text-placeholder); }

/* ==================== 文章网格 ==================== */
.article-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.article-card {
  display: flex;
  gap: 16px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 16px;
  cursor: pointer;
  transition: all var(--tcm-transition-base);
}
.article-card:hover { box-shadow: var(--tcm-shadow-md); border-color: var(--tcm-ochre); }
.article-card-cover { flex-shrink: 0; width: 120px; height: 90px; border-radius: var(--tcm-radius-sm); overflow: hidden; background: var(--tcm-bg-paper-dark); }
.article-card-cover img { width: 100%; height: 100%; object-fit: cover; }
.article-card-body { flex: 1; display: flex; flex-direction: column; justify-content: space-between; min-width: 0; }
.article-card-title { font-family: var(--tcm-font-title); font-size: 15px; color: var(--tcm-text-primary); margin: 0 0 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.article-card-summary { font-size: 13px; color: var(--tcm-text-secondary); margin: 0; line-height: 1.5; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.article-card-meta { display: flex; gap: 16px; font-size: 12px; color: var(--tcm-text-placeholder); }

/* 公告通知 */
.announcement-section { margin-bottom: 24px; }
.announcement-bar { background: linear-gradient(135deg, #fffbf0 0%, #fff8e8 100%); border: 1px solid #f0e0c0; border-radius: var(--tcm-radius-md, 8px); padding: 16px 20px; display: flex; align-items: flex-start; gap: 16px; }
.announcement-label { display: flex; align-items: center; gap: 6px; color: var(--tcm-ochre, #8b6914); font-weight: 600; font-size: 14px; white-space: nowrap; padding-top: 2px; }
.announcement-list { flex: 1; display: flex; flex-direction: column; gap: 8px; }
.announcement-item { display: flex; justify-content: space-between; align-items: center; cursor: pointer; padding: 6px 12px; border-radius: 6px; transition: background 0.2s; }
.announcement-item:hover { background: rgba(139, 105, 20, 0.08); }
.announcement-title { font-size: 14px; color: #333; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.announcement-time { font-size: 12px; color: #999; margin-left: 16px; white-space: nowrap; }
.announcement-detail-time { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #999; margin-bottom: 16px; }
.announcement-detail-content { font-size: 15px; color: #555; line-height: 1.8; }

/* 通用section样式 */
.section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.section-title { font-family: var(--tcm-font-title); font-size: 22px; color: var(--tcm-text-primary); margin: 0; }
.view-all { display: flex; align-items: center; gap: 4px; font-size: 14px; color: var(--tcm-ochre); text-decoration: none; }
.view-all:hover { color: var(--tcm-ochre-light); }

/* 分类导航 */
.category-section { margin-bottom: 40px; }
.category-list { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; }
.category-group { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 20px; transition: all var(--tcm-transition-base); }
.category-group:hover { border-color: var(--tcm-ochre); box-shadow: var(--tcm-shadow-md); }
.category-parent { display: flex; flex-direction: column; align-items: center; gap: 12px; cursor: pointer; padding-bottom: 16px; border-bottom: 1px solid var(--tcm-border-light); margin-bottom: 12px; }
.category-icon { width: 64px; height: 64px; display: flex; align-items: center; justify-content: center; background: var(--tcm-ochre-100); border-radius: 50%; color: var(--tcm-ochre); overflow: hidden; }
.category-icon-img { width: 100%; height: 100%; object-fit: cover; }
.category-name { font-size: 15px; font-weight: 500; color: var(--tcm-text-primary); }
.category-children { display: flex; flex-direction: column; gap: 8px; }
.category-child { display: flex; align-items: center; gap: 8px; padding: 8px 12px; border-radius: var(--tcm-radius-sm); cursor: pointer; transition: all var(--tcm-transition-base); color: var(--tcm-text-secondary); font-size: 13px; }
.category-child:hover { background: var(--tcm-ochre-50); color: var(--tcm-ochre); }
.child-icon-img { width: 20px; height: 20px; border-radius: 4px; object-fit: cover; }

/* 商品网格 */
.product-section { margin-bottom: 40px; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
.product-card-image-wrapper { width: 100%; aspect-ratio: 1; overflow: hidden; background: var(--tcm-bg-paper-dark); }
.product-card-image { width: 100%; height: 100%; object-fit: cover; transition: transform var(--tcm-transition-base); }
.product-card:hover .product-card-image { transform: scale(1.05); }
.product-card-desc { font-size: 12px; color: var(--tcm-text-secondary); margin-bottom: 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-card-bottom { display: flex; align-items: center; justify-content: space-between; }

/* 养生方案 */
.health-section { margin-bottom: 40px; }
.health-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.health-card { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); padding: 24px; transition: all var(--tcm-transition-base); cursor: pointer; }
.health-card:hover { box-shadow: var(--tcm-shadow-md); border-color: var(--tcm-ochre); }
.health-card-tag { display: inline-block; padding: 4px 12px; background: var(--tcm-ochre-100); color: var(--tcm-ochre); font-size: 12px; border-radius: 2px; margin-bottom: 12px; }
.health-card-title { font-family: var(--tcm-font-title); font-size: 18px; color: var(--tcm-text-primary); margin: 0 0 8px; }
.health-card-desc { font-size: 14px; color: var(--tcm-text-secondary); margin-bottom: 12px; }
.health-card-season { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--tcm-text-placeholder); }

@media (max-width: 1024px) {
  .product-grid { grid-template-columns: repeat(3, 1fr); }
  .category-list { grid-template-columns: repeat(3, 1fr); }
  .featured-layout { flex-direction: column; }
  .featured-main { flex: none; }
  .featured-side { max-height: none; }
  .behavior-list { grid-template-columns: 1fr; }
}
@media (max-width: 768px) {
  .product-grid { grid-template-columns: repeat(2, 1fr); }
  .category-list { grid-template-columns: repeat(2, 1fr); }
  .health-grid { grid-template-columns: 1fr; }
  .article-grid { grid-template-columns: 1fr; }
  .hero-section { padding: 40px 24px; }
  .hero-title { font-size: 32px; }
  .hero-subtitle { font-size: 24px; }
  .hero-decoration { display: none; }
  .guide-card { flex-direction: column; text-align: center; }
  .seasonal-banner { padding: 20px 16px; }
  .seasonal-scroll-card { flex: 0 0 180px; }
}
</style>
