/**
 * Axios 请求封装
 * @author Ti
 * @since 2026-02-03
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from './index.js'
import { RESULT_CODE } from '../constants/index.js'

/**
 * 创建Axios实例
 * @param {string} baseURL 基础URL
 */
export function createRequest(baseURL) {
  const instance = axios.create({
    baseURL,
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // 请求拦截器
  instance.interceptors.request.use(
    config => {
      const token = getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    },
    error => {
      return Promise.reject(error)
    }
  )

  // 响应拦截器
  instance.interceptors.response.use(
    response => {
      const { data } = response
      
      // 如果返回的不是标准格式，直接返回
      if (data.code === undefined) {
        return data
      }

      // 请求成功
      if (data.code === RESULT_CODE.SUCCESS) {
        return data.data
      }

      // 未授权，跳转登录
      if (data.code === RESULT_CODE.UNAUTHORIZED) {
        clearAuth()
        ElMessage.error('登录已过期，请重新登录')
        window.location.href = '/login'
        return Promise.reject(new Error(data.message))
      }

      // 其他错误
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message))
    },
    error => {
      // 网络错误
      if (!error.response) {
        ElMessage.error('网络连接失败，请检查网络')
        return Promise.reject(error)
      }

      const { status, data } = error.response
      
      // 尝试从响应中提取错误信息
      let errorMessage = ''
      if (data) {
        // 后端返回的错误信息可能在 message 字段
        if (data.message) {
          errorMessage = data.message
        } else if (data.error) {
          errorMessage = data.error
        } else if (typeof data === 'string') {
          errorMessage = data
        }
      }
      
      switch (status) {
        case 400:
          // 参数校验错误，显示后端返回的具体错误信息
          ElMessage.error(errorMessage || '请求参数错误')
          break
        case 401:
          clearAuth()
          ElMessage.error('登录已过期，请重新登录')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(errorMessage || '服务器内部错误')
          break
        default:
          ElMessage.error(errorMessage || `请求失败: ${status}`)
      }
      
      return Promise.reject(error)
    }
  )

  return instance
}
