/**
 * 管理员端API
 * @author Ti
 * @since 2026-02-03
 */

import { createRequest } from '../utils/request.js'

// 管理员端API基础URL
const request = createRequest('')

// ==================== 认证相关 ====================

/**
 * 管理员登录
 */
export function adminLogin(data) {
  return request.post('/api/admin/auth/login', data)
}

/**
 * 管理员注册
 */
export function adminRegister(data) {
  return request.post('/api/admin/auth/register', data)
}

/**
 * 管理员退出
 */
export function adminLogout() {
  return request.post('/api/admin/auth/logout')
}

/**
 * 获取当前用户信息
 */
export function getAdminProfile() {
  return request.get('/api/admin/auth/profile')
}

/**
 * 更新个人信息
 */
export function updateAdminProfile(data) {
  return request.put('/api/admin/auth/profile', data)
}

/**
 * 修改密码
 */
export function updateAdminPassword(data) {
  return request.put('/api/admin/auth/password', data)
}

/**
 * 上传头像
 */
export function uploadAdminAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/admin/auth/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 数据统计 ====================

/**
 * 仪表盘数据
 */
export function getDashboard() {
  return request.get('/api/admin/statistics/dashboard')
}

/**
 * 销售趋势
 */
export function getSalesTrend(days = 7) {
  return request.get('/api/admin/statistics/sales-trend', { params: { days } })
}

// ==================== 员工管理 ====================

/**
 * 员工列表
 */
export function getStaffList(params) {
  return request.get('/api/admin/staffs', { params })
}

/**
 * 创建员工
 */
export function createStaff(data) {
  return request.post('/api/admin/staffs', data)
}

/**
 * 员工详情
 */
export function getStaffDetail(id) {
  return request.get(`/api/admin/staffs/${id}`)
}

/**
 * 禁用/启用员工
 */
export function updateStaffStatus(id, status) {
  return request.patch(`/api/admin/staffs/${id}/status`, null, { params: { status } })
}

// ==================== 用户管理 ====================

/**
 * 用户列表
 */
export function getUserList(params) {
  return request.get('/api/admin/users', { params })
}

/**
 * 用户详情
 */
export function getUserDetail(id) {
  return request.get(`/api/admin/users/${id}`)
}

/**
 * 禁用/启用用户
 */
export function updateUserStatus(id, status) {
  return request.patch(`/api/admin/users/${id}/status`, null, { params: { status } })
}

// ==================== 商品管理 ====================

/**
 * 商品列表
 */
export function getAdminProductList(params) {
  return request.get('/api/admin/products', { params })
}

/**
 * 商品详情
 */
export function getAdminProductDetail(id) {
  return request.get(`/api/admin/products/${id}`)
}

/**
 * 新增商品
 */
export function addProduct(data) {
  return request.post('/api/admin/products', data)
}

/**
 * 修改商品
 */
export function updateProduct(id, data) {
  return request.put(`/api/admin/products/${id}`, data)
}

/**
 * 删除商品
 */
export function deleteProduct(id) {
  return request.delete(`/api/admin/products/${id}`)
}

/**
 * 商品上下架
 */
export function updateProductStatus(id, status) {
  return request.patch(`/api/admin/products/${id}/status`, null, { params: { status } })
}

/**
 * 上传商品图片
 */
export function uploadProductImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/admin/products/images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 分类列表
 */
export function getAdminCategoryList() {
  return request.get('/api/admin/products/categories')
}

/**
 * 新增分类
 */
export function addCategory(data) {
  return request.post('/api/admin/products/categories', data)
}

/**
 * 修改分类
 */
export function updateCategory(id, data) {
  return request.put(`/api/admin/products/categories/${id}`, data)
}

/**
 * 删除分类
 */
export function deleteCategory(id) {
  return request.delete(`/api/admin/products/categories/${id}`)
}

// ==================== 订单管理 ====================

/**
 * 订单列表
 */
export function getAdminOrderList(params) {
  return request.get('/api/admin/orders', { params })
}

/**
 * 订单详情
 */
export function getAdminOrderDetail(id) {
  return request.get(`/api/admin/orders/${id}`)
}

/**
 * 订单发货
 */
export function shipOrder(id, expressCompany, expressNo) {
  return request.post(`/api/admin/orders/${id}/ship`, null, {
    params: { expressCompany, expressNo }
  })
}

/**
 * 修改订单状态
 */
export function updateOrderStatus(id, status) {
  return request.patch(`/api/admin/orders/${id}/status`, null, { params: { status } })
}

// ==================== 内容管理 ====================

// 文章管理
export function getAdminArticleList(params) {
  return request.get('/api/admin/content/articles', { params })
}

export function addArticle(data) {
  return request.post('/api/admin/content/articles', data)
}

export function updateArticle(id, data) {
  return request.put(`/api/admin/content/articles/${id}`, data)
}

export function deleteArticle(id) {
  return request.delete(`/api/admin/content/articles/${id}`)
}

export function updateArticleStatus(id, status) {
  return request.patch(`/api/admin/content/articles/${id}/status`, null, { params: { status } })
}

// 养生方案管理
export function getAdminHealthPlanList(params) {
  return request.get('/api/admin/content/health-plans', { params })
}

export function addHealthPlan(data) {
  return request.post('/api/admin/content/health-plans', data)
}

export function updateHealthPlan(id, data) {
  return request.put(`/api/admin/content/health-plans/${id}`, data)
}

export function deleteHealthPlan(id) {
  return request.delete(`/api/admin/content/health-plans/${id}`)
}

export function updateHealthPlanStatus(id, status) {
  return request.patch(`/api/admin/content/health-plans/${id}/status`, null, { params: { status } })
}

// 资讯管理
export function getAdminNewsList(params) {
  return request.get('/api/admin/content/news', { params })
}

export function addNews(data) {
  return request.post('/api/admin/content/news', data)
}

export function updateNews(id, data) {
  return request.put(`/api/admin/content/news/${id}`, data)
}

export function deleteNews(id) {
  return request.delete(`/api/admin/content/news/${id}`)
}

export function updateNewsStatus(id, status) {
  return request.patch(`/api/admin/content/news/${id}/status`, null, { params: { status } })
}

// 公告管理
export function getAdminAnnouncementList(params) {
  return request.get('/api/admin/content/announcements', { params })
}

export function addAnnouncement(data) {
  return request.post('/api/admin/content/announcements', data)
}

export function updateAnnouncement(id, data) {
  return request.put(`/api/admin/content/announcements/${id}`, data)
}

export function deleteAnnouncement(id) {
  return request.delete(`/api/admin/content/announcements/${id}`)
}

export function updateAnnouncementStatus(id, status) {
  return request.patch(`/api/admin/content/announcements/${id}/status`, null, { params: { status } })
}

// 上传图片
export function uploadContentImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/admin/content/images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}


// ==================== 健康档案管理 ====================

/**
 * 健康档案列表
 */
export function getAdminHealthRecordList(params) {
  return request.get('/api/admin/health-records', { params })
}

/**
 * 健康档案详情
 */
export function getAdminHealthRecordDetail(id) {
  return request.get(`/api/admin/health-records/${id}`)
}

/**
 * 删除健康档案
 */
export function deleteHealthRecord(id) {
  return request.delete(`/api/admin/health-records/${id}`)
}

// ==================== 评价管理 ====================

/**
 * 评价列表
 */
export function getAdminReviewList(params) {
  return request.get('/api/admin/reviews', { params })
}

/**
 * 评价详情
 */
export function getAdminReviewDetail(id) {
  return request.get(`/api/admin/reviews/${id}`)
}

/**
 * 审核评价
 */
export function auditReview(id, status) {
  return request.patch(`/api/admin/reviews/${id}/audit`, null, { params: { status } })
}

/**
 * 回复评价
 */
export function replyReview(id, replyContent) {
  return request.post(`/api/admin/reviews/${id}/reply`, null, { params: { replyContent } })
}

/**
 * 删除评价
 */
export function deleteReview(id) {
  return request.delete(`/api/admin/reviews/${id}`)
}


// ==================== 社区审核 ====================

export function getAdminPostList(params) {
  return request.get('/api/admin/community/posts', { params })
}

export function getAdminPostDetail(id) {
  return request.get(`/api/admin/community/posts/${id}`)
}

export function auditPost(id, status) {
  return request.patch(`/api/admin/community/posts/${id}/audit`, null, { params: { status } })
}

export function deletePost(id) {
  return request.delete(`/api/admin/community/posts/${id}`)
}

export function getAdminCommentList(params) {
  return request.get('/api/admin/community/comments', { params })
}

export function auditComment(id, status) {
  return request.patch(`/api/admin/community/comments/${id}/audit`, null, { params: { status } })
}

export function deleteComment(id) {
  return request.delete(`/api/admin/community/comments/${id}`)
}

// ==================== 库存管理 ====================

export function getAdminStockList(params) {
  return request.get('/api/admin/stocks', { params })
}

export function updateAdminStock(productId, stock, remark) {
  return request.put(`/api/admin/stocks/${productId}`, null, { params: { stock, remark } })
}

export function batchUpdateAdminStock(items) {
  return request.put('/api/admin/stocks/batch', items)
}

export function getAdminStockLogs(params) {
  return request.get('/api/admin/stocks/logs', { params })
}

export function getAdminStockWarnings() {
  return request.get('/api/admin/stocks/warnings')
}

// ==================== 系统配置 ====================

export function getSystemConfigs() {
  return request.get('/api/admin/config')
}

export function updateSystemConfig(configKey, configValue) {
  return request.put(`/api/admin/config/${configKey}`, null, { params: { configValue } })
}

export function batchUpdateSystemConfigs(configs) {
  return request.put('/api/admin/config/batch', configs)
}

export function uploadConfigImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/admin/config/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 增强统计 ====================

/**
 * 评价统计
 */
export function getReviewStats(days = 30) {
  return request.get('/api/admin/statistics/review-stats', { params: { days } })
}

/**
 * 用户统计
 */
export function getUserStats(days = 30) {
  return request.get('/api/admin/statistics/user-stats', { params: { days } })
}

// ==================== 数据导出 ====================

/**
 * 导出订单
 */
export function exportOrders(params) {
  return request.get('/api/admin/export/orders', { params, responseType: 'blob' })
}

/**
 * 导出用户
 */
export function exportUsers() {
  return request.get('/api/admin/export/users', { responseType: 'blob' })
}

/**
 * 导出商品
 */
export function exportProducts() {
  return request.get('/api/admin/export/products', { responseType: 'blob' })
}

/**
 * 导出评价
 */
export function exportReviews() {
  return request.get('/api/admin/export/reviews', { responseType: 'blob' })
}
