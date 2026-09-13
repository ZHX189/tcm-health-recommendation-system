/**
 * 用户端API
 * @author Ti
 * @since 2026-02-03
 */

import { createRequest } from '../utils/request.js'

// 用户端API基础URL
const request = createRequest('')

// ==================== 认证相关 ====================

/**
 * 用户注册
 */
export function userRegister(data) {
  return request.post('/api/user/auth/register', data)
}

/**
 * 用户登录
 */
export function userLogin(data) {
  return request.post('/api/user/auth/login', data)
}

/**
 * 用户退出
 */
export function userLogout() {
  return request.post('/api/user/auth/logout')
}

// ==================== 个人信息 ====================

/**
 * 获取个人信息
 */
export function getUserProfile() {
  return request.get('/api/user/profile')
}

/**
 * 修改个人信息
 */
export function updateUserProfile(data) {
  return request.put('/api/user/profile', data)
}

/**
 * 上传头像
 */
export function uploadAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/user/profile/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 修改密码
 */
export function updatePassword(data) {
  return request.put('/api/user/profile/password', data)
}

// ==================== 收货地址 ====================

/**
 * 地址列表
 */
export function getAddressList() {
  return request.get('/api/user/addresses')
}

/**
 * 新增地址
 */
export function addAddress(data) {
  return request.post('/api/user/addresses', data)
}

/**
 * 修改地址
 */
export function updateAddress(id, data) {
  return request.put(`/api/user/addresses/${id}`, data)
}

/**
 * 删除地址
 */
export function deleteAddress(id) {
  return request.delete(`/api/user/addresses/${id}`)
}

/**
 * 设置默认地址
 */
export function setDefaultAddress(id) {
  return request.patch(`/api/user/addresses/${id}/default`)
}

// ==================== 商品相关 ====================

/**
 * 商品列表
 */
export function getProductList(params) {
  return request.get('/api/user/products', { params })
}

/**
 * 商品详情
 */
export function getProductDetail(id) {
  return request.get(`/api/user/products/${id}`)
}

/**
 * 商品分类
 */
export function getProductCategories() {
  return request.get('/api/user/products/categories')
}

// ==================== 购物车 ====================

/**
 * 购物车列表
 */
export function getCartList() {
  return request.get('/api/user/cart')
}

/**
 * 添加到购物车
 */
export function addToCart(data) {
  return request.post('/api/user/cart', data)
}

/**
 * 修改购物车数量
 */
export function updateCartQuantity(id, quantity) {
  return request.put(`/api/user/cart/${id}`, null, { params: { quantity } })
}

/**
 * 更新选中状态
 */
export function updateCartSelected(id, selected) {
  return request.patch(`/api/user/cart/${id}/selected`, null, { params: { selected } })
}

/**
 * 全选/取消全选
 */
export function selectAllCart(selected) {
  return request.patch('/api/user/cart/select-all', null, { params: { selected } })
}

/**
 * 删除购物车商品
 */
export function deleteCartItem(id) {
  return request.delete(`/api/user/cart/${id}`)
}

/**
 * 批量删除
 */
export function batchDeleteCart(ids) {
  return request.delete('/api/user/cart/batch', { data: ids })
}

/**
 * 清空购物车
 */
export function clearCart() {
  return request.delete('/api/user/cart/clear')
}

// ==================== 订单相关 ====================

/**
 * 订单列表
 */
export function getOrderList(params) {
  return request.get('/api/user/orders', { params })
}

/**
 * 订单详情
 */
export function getOrderDetail(id) {
  return request.get(`/api/user/orders/${id}`)
}

/**
 * 创建订单
 */
export function createOrder(data) {
  return request.post('/api/user/orders', data)
}

/**
 * 支付订单
 */
export function payOrder(id, payType) {
  return request.post(`/api/user/orders/${id}/pay`, null, { params: { payType } })
}

/**
 * 取消订单
 */
export function cancelOrder(id, reason) {
  return request.post(`/api/user/orders/${id}/cancel`, null, { params: { reason } })
}

/**
 * 确认收货
 */
export function confirmOrder(id) {
  return request.post(`/api/user/orders/${id}/confirm`)
}

// ==================== 收藏相关 ====================

/**
 * 收藏列表
 */
export function getCollectionList(params) {
  return request.get('/api/user/collections', { params })
}

/**
 * 添加收藏
 */
export function addCollection(targetId, targetType) {
  return request.post('/api/user/collections', null, { params: { targetId, targetType } })
}

/**
 * 取消收藏
 */
export function deleteCollection(targetId, targetType) {
  return request.delete('/api/user/collections', { params: { targetId, targetType } })
}

/**
 * 检查是否已收藏
 */
export function checkCollection(targetId, targetType) {
  return request.get('/api/user/collections/check', { params: { targetId, targetType } })
}

// ==================== 健康档案 ====================

/**
 * 获取健康档案
 */
export function getHealthRecord() {
  return request.get('/api/user/health-record')
}

/**
 * 创建健康档案
 */
export function createHealthRecord(data) {
  return request.post('/api/user/health-record', data)
}

/**
 * 更新健康档案
 */
export function updateHealthRecord(data) {
  return request.put('/api/user/health-record', data)
}

// ==================== 养生方案 ====================

/**
 * 养生方案列表
 */
export function getHealthPlanList(params) {
  return request.get('/api/user/health-plans', { params })
}

/**
 * 养生方案详情
 */
export function getHealthPlanDetail(id) {
  return request.get(`/api/user/health-plans/${id}`)
}

// ==================== 文章相关 ====================

/**
 * 文章列表
 */
export function getArticleList(params) {
  return request.get('/api/user/articles', { params })
}

/**
 * 文章详情
 */
export function getArticleDetail(id) {
  return request.get(`/api/user/articles/${id}`)
}

// ==================== 资讯公告 ====================

/**
 * 资讯列表
 */
export function getNewsList(params) {
  return request.get('/api/user/news', { params })
}

/**
 * 资讯详情
 */
export function getNewsDetail(id) {
  return request.get(`/api/user/news/${id}`)
}

/**
 * 公告列表
 */
export function getAnnouncementList(params) {
  return request.get('/api/user/announcements', { params })
}

/**
 * 公告详情
 */
export function getAnnouncementDetail(id) {
  return request.get(`/api/user/announcements/${id}`)
}

// ==================== 评价相关 ====================

/**
 * 商品评价列表
 */
export function getProductReviews(productId, params) {
  return request.get(`/api/user/reviews/product/${productId}`, { params })
}

/**
 * 发表评价
 */
export function createReview(data) {
  return request.post('/api/user/reviews', data)
}

// ==================== AI客服 ====================

/**
 * AI对话
 */
export function chatWithAI(data) {
  return request.post('/api/user/chat', data)
}

/**
 * AI对话（SSE流式）
 * @param {Object} data - 聊天数据 { message, sessionId }
 * @param {Function} onMessage - 消息回调
 * @param {Function} onError - 错误回调
 * @param {Function} onComplete - 完成回调
 * @returns {Function} 取消函数
 */
export function chatWithAIStream(data, onMessage, onError, onComplete) {
  const token = localStorage.getItem('tcm_token')
  const controller = new AbortController()
  
  fetch('/api/user/chat/stream', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    },
    body: JSON.stringify(data),
    signal: controller.signal
  })
  .then(response => {
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error('登录已过期，请重新登录')
      }
      throw new Error('服务暂时不可用，请稍后重试')
    }
    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = '' // 用于处理不完整的SSE消息
    
    function read() {
      reader.read().then(({ done, value }) => {
        if (done) {
          // 处理缓冲区中剩余的数据
          if (buffer.trim()) {
            const content = parseSSEContent(buffer)
            if (content) {
              onMessage && onMessage(content)
            }
          }
          onComplete && onComplete()
          return
        }
        
        // 解码并追加到缓冲区
        buffer += decoder.decode(value, { stream: true })
        
        // 按SSE消息分割处理（以\n\n为分隔符）
        const messages = buffer.split('\n\n')
        // 最后一个可能是不完整的消息，保留在缓冲区
        buffer = messages.pop() || ''
        
        // 处理完整的SSE消息
        for (const msg of messages) {
          const content = parseSSEContent(msg)
          if (content) {
            onMessage && onMessage(content)
          }
        }
        
        read()
      }).catch(err => {
        if (err.name !== 'AbortError') {
          onError && onError(err)
        }
      })
    }
    
    read()
  })
  .catch(err => {
    if (err.name !== 'AbortError') {
      onError && onError(err)
    }
  })
  
  // 返回取消函数
  return () => controller.abort()
}

/**
 * 解析SSE消息内容
 * @param {string} message - SSE消息
 * @returns {string} 解析后的内容
 */
function parseSSEContent(message) {
  if (!message || !message.trim()) return ''
  
  let content = ''
  const lines = message.split('\n')
  
  for (const line of lines) {
    // SSE格式: data:内容
    if (line.startsWith('data:')) {
      content += line.slice(5) // 去掉 "data:" 前缀
    }
  }
  
  return content
}

/**
 * 清除会话
 */
export function clearChatSession(sessionId) {
  return request.delete(`/api/user/chat/session/${sessionId}`)
}


// ==================== 社区相关 ====================

/**
 * 帖子列表
 */
export function getPostList(params) {
  return request.get('/api/user/community/posts', { params })
}

/**
 * 帖子详情
 */
export function getPostDetail(id) {
  return request.get(`/api/user/community/posts/${id}`)
}

/**
 * 发布帖子
 */
export function createPost(data) {
  return request.post('/api/user/community/posts', data)
}

/**
 * 删除帖子
 */
export function deletePost(id) {
  return request.delete(`/api/user/community/posts/${id}`)
}

/**
 * 点赞/取消点赞
 */
export function togglePostLike(id) {
  return request.post(`/api/user/community/posts/${id}/like`)
}

/**
 * 帖子评论列表
 */
export function getPostComments(id) {
  return request.get(`/api/user/community/posts/${id}/comments`)
}

/**
 * 发表评论
 */
export function addPostComment(id, params) {
  return request.post(`/api/user/community/posts/${id}/comments`, null, { params })
}


// ==================== 个性化推荐 ====================

/**
 * 获取首页个性化推荐（体质+行为+季节）
 */
export function getHomeRecommendations() {
  return request.get('/api/user/recommendations/home')
}

// ==================== 站点配置 ====================

/**
 * 获取站点配置（公开接口）
 */
export function getSiteConfig() {
  return request.get('/api/user/site-config')
}
