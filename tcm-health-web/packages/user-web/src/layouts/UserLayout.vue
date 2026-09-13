<!--
  用户端布局组件
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <header class="user-header">
      <div class="header-container">
        <!-- Logo -->
        <router-link to="/" class="header-logo">
          <div class="header-logo-icon">
            <img v-if="siteLogo" :src="getImageUrl(siteLogo)" alt="Logo" class="site-logo-img" />
            <svg v-else viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="20" cy="20" r="17" stroke="currentColor" stroke-width="1.5"/>
              <path d="M20 6C20 6 12 14 12 20C12 24.4183 15.5817 28 20 28C24.4183 28 28 24.4183 28 20C28 14 20 6 20 6Z" fill="currentColor" opacity="0.2"/>
              <path d="M20 10V30M14 20H26" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="3" fill="currentColor"/>
            </svg>
          </div>
          <span class="header-logo-text">{{ siteName || '中医养生' }}</span>
        </router-link>

        <!-- 导航菜单 -->
        <nav class="header-nav">
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
            <SvgIcon name="home" :size="16" />
            <span>首页</span>
          </router-link>
          <router-link to="/products" class="nav-item" :class="{ active: $route.path.startsWith('/products') }">
            <SvgIcon name="herb" :size="16" />
            <span>药材商城</span>
          </router-link>
          <router-link to="/health" class="nav-item" :class="{ active: $route.path === '/health' }">
            <SvgIcon name="wellness" :size="16" />
            <span>养生方案</span>
          </router-link>
          <router-link to="/articles" class="nav-item" :class="{ active: $route.path === '/articles' }">
            <SvgIcon name="article" :size="16" />
            <span>养生文章</span>
          </router-link>
          <router-link to="/assistant" class="nav-item" :class="{ active: $route.path === '/assistant' }">
            <SvgIcon name="chat" :size="16" />
            <span>养生助手</span>
          </router-link>
          <router-link to="/community" class="nav-item" :class="{ active: $route.path.startsWith('/community') }">
            <SvgIcon name="community" :size="16" />
            <span>养生社区</span>
          </router-link>
          <router-link to="/news" class="nav-item" :class="{ active: $route.path.startsWith('/news') }">
            <SvgIcon name="news" :size="16" />
            <span>养生资讯</span>
          </router-link>
        </nav>

        <!-- 右侧操作区 -->
        <div class="header-actions">
          <!-- 购物车 -->
          <router-link to="/cart" class="cart-btn">
            <SvgIcon name="cart" :size="22" />
            <span v-if="cartCount > 0" class="cart-badge">{{ cartCount > 99 ? '99+' : cartCount }}</span>
          </router-link>

          <!-- 用户菜单 -->
          <template v-if="isLoggedIn">
            <el-dropdown trigger="click" @command="handleCommand">
              <div class="user-dropdown">
                <div class="user-avatar">
                  <img v-if="userAvatar" :src="getImageUrl(userAvatar)" alt="头像" class="avatar-img" />
                  <SvgIcon v-else name="avatar" :size="20" />
                </div>
                <span>{{ nickname || '用户' }}</span>
                <SvgIcon name="arrow-down" :size="14" />
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <SvgIcon name="user" :size="16" class="mr-8" />个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="health-record">
                    <SvgIcon name="wellness" :size="16" class="mr-8" />健康档案
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <SvgIcon name="order" :size="16" class="mr-8" />我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="collections">
                    <SvgIcon name="heart" :size="16" class="mr-8" />我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <SvgIcon name="logout" :size="16" class="mr-8" />退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" size="small" @click="$router.push('/login')">登录</el-button>
            <el-button size="small" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="user-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 底部 -->
    <footer class="user-footer">
      <div class="footer-container">
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a v-if="contactPhone" :href="'tel:' + contactPhone">{{ contactPhone }}</a>
          <a v-if="contactEmail" :href="'mailto:' + contactEmail">{{ contactEmail }}</a>
          <a href="#">帮助中心</a>
        </div>
        <div class="footer-copyright">
          {{ siteName || '中医养生平台' }} © 2026 Ti · 传承中医智慧 · 守护全民健康
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getToken, getUserInfo, clearAuth } from 'shared/utils/index.js'
import { getCartList, getSiteConfig } from 'shared/api/user.js'

const router = useRouter()
const cartCount = ref(0)
const siteName = ref('')
const siteLogo = ref('')
const contactPhone = ref('')
const contactEmail = ref('')

// 后端服务器地址
const API_BASE_URL = ''

const authToken = ref(getToken())
const currentUserInfo = ref(getUserInfo())
const isLoggedIn = computed(() => !!authToken.value)
const nickname = computed(() => {
  const info = currentUserInfo.value
  return info?.nickname || info?.username || ''
})

// 获取用户头像
const userAvatar = computed(() => {
  const info = currentUserInfo.value
  return info?.avatar || ''
})

// 获取图片URL
function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

// 获取购物车数量
async function fetchCartCount() {
  if (!isLoggedIn.value) return
  try {
    const list = await getCartList()
    cartCount.value = list?.length || 0
  } catch (error) {
    // 忽略错误
  }
}

// 处理菜单命令
function handleCommand(command) {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'health-record':
      router.push('/health-record')
      break
    case 'orders':
      router.push('/orders')
      break
    case 'collections':
      router.push('/profile?tab=collections')
      break
    case 'logout':
      clearAuth()
      authToken.value = null
      currentUserInfo.value = null
      cartCount.value = 0
      ElMessage.success('已退出登录')
      router.push('/')
      break
  }
}

// 获取站点配置
async function fetchSiteConfig() {
  try {
    const config = await getSiteConfig()
    if (config) {
      siteName.value = config.site_name || ''
      siteLogo.value = config.site_logo || ''
      contactPhone.value = config.contact_phone || ''
      contactEmail.value = config.contact_email || ''
      if (siteName.value) {
        document.title = siteName.value
      }
    }
  } catch (error) {
    // 使用默认值
  }
}

onMounted(() => {
  fetchSiteConfig()
  fetchCartCount()
})
</script>
