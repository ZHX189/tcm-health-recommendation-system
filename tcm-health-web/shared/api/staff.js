/**
 * 员工端API
 * @author Ti
 * @since 2026-02-03
 */

import { createRequest } from '../utils/request.js'

// 员工端API基础URL
const request = createRequest('')

// ==================== 认证相关 ====================

/**
 * 员工登录
 */
export function staffLogin(data) {
  return request.post('/api/staff/auth/login', data)
}

/**
 * 员工退出
 */
export function staffLogout() {
  return request.post('/api/staff/auth/logout')
}

/**
 * 获取当前用户信息
 */
export function getStaffProfile() {
  return request.get('/api/staff/auth/profile')
}

/**
 * 更新个人信息
 */
export function updateStaffProfile(data) {
  return request.put('/api/staff/auth/profile', data)
}

/**
 * 修改密码
 */
export function updateStaffPassword(data) {
  return request.put('/api/staff/auth/password', data)
}

// ==================== 商品管理 ====================

/**
 * 商品列表
 */
export function getStaffProductList(params) {
  return request.get('/api/staff/products', { params })
}

/**
 * 商品详情
 */
export function getStaffProductDetail(id) {
  return request.get(`/api/staff/products/${id}`)
}

/**
 * 修改商品
 */
export function updateStaffProduct(id, data) {
  return request.put(`/api/staff/products/${id}`, data)
}

/**
 * 上传商品图片
 */
export function uploadStaffProductImage(id, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(`/api/staff/products/${id}/images`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 商品上下架（需审核）
 */
export function updateStaffProductStatus(id, status) {
  return request.patch(`/api/staff/products/${id}/status`, null, { params: { status } })
}

// ==================== 订单管理 ====================

/**
 * 订单列表
 */
export function getStaffOrderList(params) {
  return request.get('/api/staff/orders', { params })
}

/**
 * 订单详情
 */
export function getStaffOrderDetail(id) {
  return request.get(`/api/staff/orders/${id}`)
}

/**
 * 订单发货
 */
export function staffShipOrder(id, expressCompany, expressNo) {
  return request.post(`/api/staff/orders/${id}/ship`, null, {
    params: { expressCompany, expressNo }
  })
}

/**
 * 上传头像
 */
export function uploadStaffAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/staff/auth/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}


// ==================== 库存管理 ====================

export function getStaffStockList(params) {
  return request.get('/api/staff/stocks', { params })
}

export function updateStaffStock(productId, stock, remark) {
  return request.put(`/api/staff/stocks/${productId}`, null, { params: { stock, remark } })
}

export function batchUpdateStaffStock(items) {
  return request.put('/api/staff/stocks/batch', items)
}

export function getStaffStockLogs(params) {
  return request.get('/api/staff/stocks/logs', { params })
}

export function getStaffStockWarnings() {
  return request.get('/api/staff/stocks/warnings')
}

// ==================== 健康档案查看 ====================

/**
 * 健康档案列表
 */
export function getStaffHealthRecordList(params) {
  return request.get('/api/staff/health-records', { params })
}

/**
 * 健康档案详情
 */
export function getStaffHealthRecordDetail(id) {
  return request.get(`/api/staff/health-records/${id}`)
}

// ==================== 评价查看 ====================

export function getStaffReviewList(params) {
  return request.get('/api/staff/reviews', { params })
}

export function getStaffReviewDetail(id) {
  return request.get(`/api/staff/reviews/${id}`)
}

// ==================== 内容协助 ====================

export function submitNewsDraft(data) {
  return request.post('/api/staff/content/news/draft', data)
}

export function submitAnnouncementDraft(data) {
  return request.post('/api/staff/content/announcements/draft', data)
}

export function uploadStaffContentImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/staff/content/images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
