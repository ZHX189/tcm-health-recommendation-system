<!--
  管理员登录页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="login-container">
    <!-- 水墨背景装饰 -->
    <div class="login-bg">
      <div class="ink-wash-1"></div>
      <div class="ink-wash-2"></div>
      <div class="ink-wash-3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- Logo区域 -->
      <div class="login-header">
        <div class="logo-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="24" cy="24" r="20" stroke="currentColor" stroke-width="2"/>
            <path d="M24 8C24 8 16 16 16 24C16 28.4183 19.5817 32 24 32C28.4183 32 32 28.4183 32 24C32 16 24 8 24 8Z" fill="currentColor" opacity="0.3"/>
            <path d="M24 14V34M18 24H30" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <circle cx="24" cy="24" r="4" fill="currentColor"/>
          </svg>
        </div>
        <h1 class="login-title">管理后台</h1>
        <p class="login-subtitle">中医养生平台 · 管理员登录</p>
      </div>

      <!-- 登录表单 -->
      <el-form 
        ref="loginFormRef"
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入管理员账号"
            size="large"
          >
            <template #prefix>
              <SvgIcon name="user" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          >
            <template #prefix>
              <SvgIcon name="lock" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            <SvgIcon v-if="!loading" name="arrow-right" :size="18" class="mr-8" />
            {{ loading ? '登录中...' : '立即登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 传统纹样装饰 -->
      <div class="decoration-line"></div>

      <div class="login-footer">
        <span>还没有账号？</span>
        <el-button type="primary" link @click="$router.push('/register')">立即注册</el-button>
      </div>

      <!-- 底部版权 -->
      <div class="login-copyright">
        <span>中医养生平台 © 2026 Ti</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminLogin } from 'shared/api/admin.js'
import { setToken, setUserInfo, setUserType } from 'shared/utils/index.js'
import { USER_TYPE } from 'shared/constants/index.js'

const router = useRouter()
const route = useRoute()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 登录处理
async function handleLogin() {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true

    const data = await adminLogin({
      username: loginForm.username,
      password: loginForm.password
    })

    // 保存登录信息
    setToken(data.token)
    setUserInfo(data)
    setUserType(USER_TYPE.ADMIN)

    ElMessage.success('登录成功')
    
    // 跳转到后台首页
    const redirect = route.query.redirect || '/dashboard'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #FAF7F0 0%, #F0EDE4 50%, #E8E4DB 100%);
}

/* 水墨背景效果 */
.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.ink-wash-1 {
  position: absolute;
  top: -20%;
  left: -10%;
  width: 50%;
  height: 60%;
  background: radial-gradient(ellipse at center, rgba(139, 69, 19, 0.08) 0%, transparent 70%);
  animation: float 20s infinite ease-in-out;
}

.ink-wash-2 {
  position: absolute;
  bottom: -10%;
  right: -10%;
  width: 40%;
  height: 50%;
  background: radial-gradient(ellipse at center, rgba(47, 79, 79, 0.06) 0%, transparent 70%);
  animation: float 25s infinite ease-in-out reverse;
}

.ink-wash-3 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
  height: 80%;
  background: radial-gradient(ellipse at center, rgba(212, 160, 23, 0.03) 0%, transparent 60%);
  animation: pulse 15s infinite ease-in-out;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(20px, -20px) rotate(2deg); }
  50% { transform: translate(-10px, 10px) rotate(-1deg); }
  75% { transform: translate(15px, 15px) rotate(1deg); }
}

@keyframes pulse {
  0%, 100% { opacity: 0.5; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 0.8; transform: translate(-50%, -50%) scale(1.1); }
}

/* 登录卡片 */
.login-card {
  width: 400px;
  background: rgba(255, 254, 250, 0.95);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(44, 36, 22, 0.15);
  position: relative;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(212, 201, 184, 0.5);
}

/* Logo区域 */
.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  color: var(--tcm-ochre);
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.login-title {
  font-family: var(--tcm-font-title);
  font-size: 28px;
  color: var(--tcm-text-primary);
  margin: 0 0 8px 0;
  letter-spacing: 4px;
}

.login-subtitle {
  font-size: 14px;
  color: var(--tcm-text-secondary);
  margin: 0;
  letter-spacing: 2px;
}

/* 登录表单 */
.login-form {
  margin-top: 24px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 0 15px;
  height: 48px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-base);
  border-radius: 6px;
  box-shadow: none;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: var(--tcm-ochre-light);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--tcm-ochre);
  box-shadow: 0 0 0 2px var(--tcm-ochre-100);
}

.login-form :deep(.el-input__prefix) {
  margin-right: 8px;
}

.input-icon {
  color: var(--tcm-text-placeholder);
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  background: var(--tcm-ochre);
  border-color: var(--tcm-ochre);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-btn:hover {
  background: var(--tcm-ochre-light);
  border-color: var(--tcm-ochre-light);
}

/* 装饰线 */
.decoration-line {
  height: 4px;
  margin: 32px -40px 24px;
  background: linear-gradient(90deg, 
    transparent 0%, 
    var(--tcm-ochre-100) 20%, 
    var(--tcm-turmeric-100) 50%, 
    var(--tcm-ochre-100) 80%, 
    transparent 100%
  );
}

/* 注册链接 */
.login-footer {
  text-align: center;
  margin-bottom: 16px;
  font-size: 14px;
  color: var(--tcm-text-secondary);
}

/* 版权信息 */
.login-copyright {
  text-align: center;
}

.login-copyright span {
  font-size: 12px;
  color: var(--tcm-text-placeholder);
  letter-spacing: 1px;
}

/* 响应式 */
@media (max-width: 480px) {
  .login-card {
    width: 90%;
    padding: 32px 24px;
    margin: 16px;
  }

  .login-title {
    font-size: 24px;
  }
}
</style>
