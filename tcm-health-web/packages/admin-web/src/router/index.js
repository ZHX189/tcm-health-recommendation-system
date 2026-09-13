/**
 * 管理员端路由配置
 * @author Ti
 * @since 2026-02-03
 */

import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getUserType } from 'shared/utils/index.js'
import { USER_TYPE } from 'shared/constants/index.js'

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
    meta: { title: '管理员注册', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../layouts/AdminLayout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/DashboardView.vue'),
        meta: { title: '数据大盘', icon: 'dashboard' }
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('../views/user/UserManage.vue'),
        meta: { title: '用户管理', icon: 'users' }
      },
      {
        path: 'staffs',
        name: 'StaffManage',
        component: () => import('../views/staff/StaffManage.vue'),
        meta: { title: '员工管理', icon: 'staff' }
      },
      {
        path: 'products',
        name: 'ProductManage',
        component: () => import('../views/product/ProductManage.vue'),
        meta: { title: '商品管理', icon: 'product' }
      },
      {
        path: 'categories',
        name: 'CategoryManage',
        component: () => import('../views/product/CategoryManage.vue'),
        meta: { title: '分类管理', icon: 'category' }
      },
      {
        path: 'health-records',
        name: 'HealthRecordManage',
        component: () => import('../views/health/HealthRecordManage.vue'),
        meta: { title: '健康档案', icon: 'health-record' }
      },
      {
        path: 'orders',
        name: 'OrderManage',
        component: () => import('../views/order/OrderManage.vue'),
        meta: { title: '订单管理', icon: 'order' }
      },
      {
        path: 'reviews',
        name: 'ReviewManage',
        component: () => import('../views/review/ReviewManage.vue'),
        meta: { title: '评价管理', icon: 'comment' }
      },
      {
        path: 'articles',
        name: 'ArticleManage',
        component: () => import('../views/content/ArticleManage.vue'),
        meta: { title: '文章管理', icon: 'article' }
      },
      {
        path: 'health-plans',
        name: 'HealthPlanManage',
        component: () => import('../views/content/HealthPlanManage.vue'),
        meta: { title: '养生方案', icon: 'health-plan' }
      },
      {
        path: 'news',
        name: 'NewsManage',
        component: () => import('../views/content/NewsManage.vue'),
        meta: { title: '资讯管理', icon: 'news' }
      },
      {
        path: 'announcements',
        name: 'AnnouncementManage',
        component: () => import('../views/content/AnnouncementManage.vue'),
        meta: { title: '公告管理', icon: 'announcement' }
      },
      {
        path: 'posts',
        name: 'PostManage',
        component: () => import('../views/community/PostManage.vue'),
        meta: { title: '帖子审核', icon: 'community' }
      },
      {
        path: 'comments',
        name: 'CommentManage',
        component: () => import('../views/community/CommentManage.vue'),
        meta: { title: '评论审核', icon: 'comment-audit' }
      },
      {
        path: 'stocks',
        name: 'StockManage',
        component: () => import('../views/stock/StockManage.vue'),
        meta: { title: '库存管理', icon: 'stock' }
      },
      {
        path: 'config',
        name: 'ConfigManage',
        component: () => import('../views/config/ConfigManage.vue'),
        meta: { title: '系统配置', icon: 'setting' }
      },
      {
        path: 'statistics',
        name: 'StatisticsView',
        component: () => import('../views/statistics/StatisticsView.vue'),
        meta: { title: '数据统计', icon: 'chart' }
      },
      {
        path: 'data-export',
        name: 'DataExport',
        component: () => import('../views/export/DataExport.vue'),
        meta: { title: '数据导出', icon: 'download' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 中医养生平台管理后台` : '中医养生平台管理后台'

  // 不需要认证的页面直接放行
  if (to.meta.requiresAuth === false) {
    next()
    return
  }

  // 检查是否已登录
  const token = getToken()
  const userType = getUserType()

  if (!token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 检查用户类型是否为管理员
  if (userType !== USER_TYPE.ADMIN) {
    next({ name: 'Login' })
    return
  }

  next()
})

export default router
