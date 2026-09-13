<!--
  管理员注册页面
  @author Ti
  @since 2026-02-06
-->
<template>
  <div class="login-container">
    <!-- 水墨背景装饰 -->
    <div class="login-bg">
      <div class="ink-wash-1"></div>
      <div class="ink-wash-2"></div>
      <div class="ink-wash-3"></div>
    </div>

    <!-- 注册卡片 -->
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
        <h1 class="login-title">管理员注册</h1>
        <p class="login-subtitle">中医养生平台 · 创建管理员账号</p>
      </div>

      <!-- 注册表单 -->
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="login-form"
        @submit.prevent="handleRegister"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入管理员账号"
            size="large"
          >
            <template #prefix>
              <SvgIcon name="user" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入昵称（可选）"
            size="large"
          >
            <template #prefix>
              <SvgIcon name="user" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号（可选）"
            size="large"
          >
            <template #prefix>
              <SvgIcon name="phone" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            show-password
          >
            <template #prefix>
              <SvgIcon name="lock" :size="18" class="input-icon" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            size="large"
            show-password
            @keyup.enter="handleRegister"
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
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '立即注册' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>已有账号？</span>
        <el-button type="primary" link @click="$router.push('/login')">返回登录</el-button>
      </div>

      <!-- 传统纹样装饰 -->
      <div class="decoration-line"></div>

      <!-- 底部版权 -->
      <div class="login-copyright">
        <span>中医养生平台 © 2026 Ti</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminRegister } from 'shared/api/admin.js'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  nickname: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

async function handleRegister() {
  if (!registerFormRef.value) return

  try {
    await registerFormRef.value.validate()
    loading.value = true

    await adminRegister({
      username: registerForm.username,
      nickname: registerForm.nickname || undefined,
      phone: registerForm.phone || undefined,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword
    })

    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
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

.login-bg {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.ink-wash-1 {
  position: absolute;
  top: -20%; left: -10%;
  width: 50%; height: 60%;
  background: radial-gradient(ellipse at center, rgba(139, 69, 19, 0.08) 0%, transparent 70%);
  animation: float 20s infinite ease-in-out;
}

.ink-wash-2 {
  position: absolute;
  bottom: -10%; right: -10%;
  width: 40%; height: 50%;
  background: radial-gradient(ellipse at center, rgba(47, 79, 79, 0.06) 0%, transparent 70%);
  animation: float 25s infinite ease-in-out reverse;
}

.ink-wash-3 {
  position: absolute;
  top: 50%; left: 50%;
  transform: translate(-50%, -50%);
  width: 80%; height: 80%;
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

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.logo-icon {
  width: 64px; height: 64px;
  margin: 0 auto 16px;
  color: var(--tcm-ochre);
}

.logo-icon svg { width: 100%; height: 100%; }

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

.login-form { margin-top: 20px; }

.login-form :deep(.el-input__wrapper) {
  padding: 0 15px; height: 48px;
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-base);
  border-radius: 6px;
  box-shadow: none;
}

.login-form :deep(.el-input__wrapper:hover) { border-color: var(--tcm-ochre-light); }
.login-form :deep(.el-input__wrapper.is-focus) { border-color: var(--tcm-ochre); box-shadow: 0 0 0 2px var(--tcm-ochre-100); }
.login-form :deep(.el-input__prefix) { margin-right: 8px; }
.input-icon { color: var(--tcm-text-placeholder); }

.login-btn {
  width: 100%; height: 48px; font-size: 16px;
  background: var(--tcm-ochre);
  border-color: var(--tcm-ochre);
  border-radius: 6px;
}

.login-btn:hover { background: var(--tcm-ochre-light); border-color: var(--tcm-ochre-light); }

.login-footer {
  text-align: center;
  margin-top: 8px;
  font-size: 14px;
  color: var(--tcm-text-secondary);
}

.decoration-line {
  height: 4px;
  margin: 24px -40px 20px;
  background: linear-gradient(90deg, transparent 0%, var(--tcm-ochre-100) 20%, var(--tcm-turmeric-100) 50%, var(--tcm-ochre-100) 80%, transparent 100%);
}

.login-copyright { text-align: center; }
.login-copyright span { font-size: 12px; color: var(--tcm-text-placeholder); letter-spacing: 1px; }

@media (max-width: 480px) {
  .login-card { width: 90%; padding: 32px 24px; margin: 16px; }
  .login-title { font-size: 24px; }
}
</style>
