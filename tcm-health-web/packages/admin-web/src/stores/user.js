/**
 * 用户状态管理
 * @author Ti
 * @since 2026-02-03
 */

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { adminLogin, adminLogout } from 'shared/api/admin.js'
import { 
  setToken, 
  setUserInfo, 
  setUserType,
  getToken,
  getUserInfo,
  getUserType,
  clearAuth 
} from 'shared/utils/index.js'
import { USER_TYPE } from 'shared/constants/index.js'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(getToken() || '')
  const userInfo = ref(getUserInfo() || null)
  const userType = ref(getUserType() || null)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userType.value === USER_TYPE.ADMIN)
  const username = computed(() => userInfo.value?.username || '')
  const nickname = computed(() => userInfo.value?.nickname || userInfo.value?.username || '')
  const avatar = computed(() => userInfo.value?.avatar || '')

  // 登录
  async function login(credentials) {
    try {
      const data = await adminLogin(credentials)
      
      // 保存登录信息
      token.value = data.token
      userInfo.value = data
      userType.value = data.userType

      setToken(data.token)
      setUserInfo(data)
      setUserType(data.userType)

      return data
    } catch (error) {
      throw error
    }
  }

  // 登出
  async function logout() {
    try {
      await adminLogout()
    } catch (error) {
      // 忽略登出错误
    } finally {
      // 清除本地状态
      token.value = ''
      userInfo.value = null
      userType.value = null
      clearAuth()
    }
  }

  // 更新用户信息
  function updateUserInfo(info) {
    userInfo.value = { ...userInfo.value, ...info }
    setUserInfo(userInfo.value)
  }

  return {
    token,
    userInfo,
    userType,
    isLoggedIn,
    isAdmin,
    username,
    nickname,
    avatar,
    login,
    logout,
    updateUserInfo
  }
})
