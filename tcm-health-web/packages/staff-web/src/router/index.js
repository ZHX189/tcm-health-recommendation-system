/**
 * 员工端路由配置
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
    meta: { title: '员工登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../layouts/StaffLayout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/DashboardView.vue'),
        meta: { title: '工作台', icon: 'dashboard' }
      },
      {
        path: 'products',
        name: 'ProductManage',
        component: () => import('../views/product/ProductManage.vue'),
        meta: { title: '商品管理', icon: 'product' }
      },
      {
        path: 'orders',
        name: 'OrderManage',
        component: () => import('../views/order/OrderManage.vue'),
        meta: { title: '订单管理', icon: 'order' }
      },
      {
        path: 'stocks',
        name: 'StockManage',
        component: () => import('../views/stock/StockManage.vue'),
        meta: { title: '库存管理', icon: 'stock' }
      },
      {
        path: 'health-records',
        name: 'HealthRecordList',
        component: () => import('../views/health/HealthRecordList.vue'),
        meta: { title: '健康档案', icon: 'health-record' }
      },
      {
        path: 'reviews',
        name: 'ReviewView',
        component: () => import('../views/review/ReviewView.vue'),
        meta: { title: '评价查看', icon: 'comment' }
      },
      {
        path: 'content',
        name: 'ContentDraft',
        component: () => import('../views/content/ContentDraft.vue'),
        meta: { title: '内容协助', icon: 'document' }
      }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 员工工作台` : '员工工作台'
  if (to.meta.requiresAuth === false) { next(); return }
  const token = getToken()
  const userType = getUserType()
  if (!token) { next({ name: 'Login', query: { redirect: to.fullPath } }); return }
  if (userType !== USER_TYPE.STAFF) { next({ name: 'Login' }); return }
  next()
})

export default router
