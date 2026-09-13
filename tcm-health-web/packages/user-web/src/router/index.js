/**
 * 用户端路由配置
 * @author Ti
 * @since 2026-02-03
 */

import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from 'shared/utils/index.js'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/LoginView.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/login/RegisterView.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../layouts/UserLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/home/HomeView.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'products',
        name: 'ProductList',
        component: () => import('../views/product/ProductList.vue'),
        meta: { title: '商品列表' }
      },
      {
        path: 'products/:id',
        name: 'ProductDetail',
        component: () => import('../views/product/ProductDetail.vue'),
        meta: { title: '商品详情' }
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('../views/cart/CartView.vue'),
        meta: { title: '购物车', requiresAuth: true }
      },
      {
        path: 'checkout',
        name: 'Checkout',
        component: () => import('../views/order/CheckoutView.vue'),
        meta: { title: '订单结算', requiresAuth: true }
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: () => import('../views/order/OrderList.vue'),
        meta: { title: '我的订单', requiresAuth: true }
      },
      {
        path: 'orders/:id',
        name: 'OrderDetail',
        component: () => import('../views/order/OrderDetail.vue'),
        meta: { title: '订单详情', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/profile/ProfileView.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      },
      {
        path: 'health-record',
        name: 'HealthRecord',
        component: () => import('../views/health/HealthRecordView.vue'),
        meta: { title: '健康档案', requiresAuth: true }
      },
      {
        path: 'health',
        name: 'HealthPlan',
        component: () => import('../views/health/HealthPlanList.vue'),
        meta: { title: '养生方案' }
      },
      {
        path: 'health/:id',
        name: 'HealthPlanDetail',
        component: () => import('../views/health/HealthPlanDetail.vue'),
        meta: { title: '方案详情' }
      },
      {
        path: 'articles',
        name: 'ArticleList',
        component: () => import('../views/article/ArticleList.vue'),
        meta: { title: '养生文章' }
      },
      {
        path: 'articles/:id',
        name: 'ArticleDetail',
        component: () => import('../views/article/ArticleDetail.vue'),
        meta: { title: '文章详情' }
      },
      {
        path: 'assistant',
        name: 'Assistant',
        component: () => import('../views/assistant/AssistantView.vue'),
        meta: { title: '养生助手', requiresAuth: true }
      },
      {
        path: 'community',
        name: 'CommunityList',
        component: () => import('../views/community/CommunityList.vue'),
        meta: { title: '养生社区' }
      },
      {
        path: 'community/:id',
        name: 'CommunityDetail',
        component: () => import('../views/community/CommunityDetail.vue'),
        meta: { title: '帖子详情' }
      },
      {
        path: 'news',
        name: 'NewsList',
        component: () => import('../views/news/NewsList.vue'),
        meta: { title: '养生资讯' }
      },
      {
        path: 'news/:id',
        name: 'NewsDetail',
        component: () => import('../views/news/NewsDetail.vue'),
        meta: { title: '资讯详情' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 中医养生平台` : '中医养生平台'

  if (to.meta.requiresAuth === false) {
    next()
    return
  }

  if (to.meta.requiresAuth && !getToken()) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  next()
})

export default router
