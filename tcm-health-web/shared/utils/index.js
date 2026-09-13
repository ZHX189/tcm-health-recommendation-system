/**
 * 共享工具函数
 * @author Ti
 * @since 2026-02-03
 */

import { STORAGE_KEYS } from '../constants/index.js'

/**
 * 获取Token
 */
export function getToken() {
  return localStorage.getItem(STORAGE_KEYS.TOKEN)
}

/**
 * 设置Token
 */
export function setToken(token) {
  localStorage.setItem(STORAGE_KEYS.TOKEN, token)
}

/**
 * 移除Token
 */
export function removeToken() {
  localStorage.removeItem(STORAGE_KEYS.TOKEN)
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  const info = localStorage.getItem(STORAGE_KEYS.USER_INFO)
  return info ? JSON.parse(info) : null
}

/**
 * 设置用户信息
 */
export function setUserInfo(info) {
  localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify(info))
}

/**
 * 移除用户信息
 */
export function removeUserInfo() {
  localStorage.removeItem(STORAGE_KEYS.USER_INFO)
}

/**
 * 获取用户类型
 */
export function getUserType() {
  return parseInt(localStorage.getItem(STORAGE_KEYS.USER_TYPE)) || null
}

/**
 * 设置用户类型
 */
export function setUserType(type) {
  localStorage.setItem(STORAGE_KEYS.USER_TYPE, type)
}

/**
 * 移除用户类型
 */
export function removeUserType() {
  localStorage.removeItem(STORAGE_KEYS.USER_TYPE)
}

/**
 * 清除所有登录信息
 */
export function clearAuth() {
  removeToken()
  removeUserInfo()
  removeUserType()
}

/**
 * 格式化日期时间
 * @param {string|Date} date 日期
 * @param {string} format 格式
 */
export function formatDateTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hour)
    .replace('mm', minute)
    .replace('ss', second)
}

/**
 * 格式化日期
 */
export function formatDate(date) {
  return formatDateTime(date, 'YYYY-MM-DD')
}

/**
 * 格式化金额
 * @param {number} amount 金额
 */
export function formatMoney(amount) {
  if (amount === null || amount === undefined) return '0.00'
  return Number(amount).toFixed(2)
}

/**
 * 防抖函数
 * @param {Function} fn 函数
 * @param {number} delay 延迟时间
 */
export function debounce(fn, delay = 300) {
  let timer = null
  return function(...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

/**
 * 节流函数
 * @param {Function} fn 函数
 * @param {number} interval 间隔时间
 */
export function throttle(fn, interval = 300) {
  let lastTime = 0
  return function(...args) {
    const now = Date.now()
    if (now - lastTime >= interval) {
      lastTime = now
      fn.apply(this, args)
    }
  }
}

/**
 * 深拷贝
 * @param {any} obj 对象
 */
export function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj)
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  const cloned = {}
  for (const key in obj) {
    if (Object.prototype.hasOwnProperty.call(obj, key)) {
      cloned[key] = deepClone(obj[key])
    }
  }
  return cloned
}

/**
 * 生成唯一ID
 */
export function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}
