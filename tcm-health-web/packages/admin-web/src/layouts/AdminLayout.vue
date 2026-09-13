<!--
  管理员端布局组件
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="admin-layout">
    <!-- 侧边栏 - 固定不滚动 -->
    <aside class="admin-sidebar" :class="{ collapsed: isCollapsed }">
      <!-- Logo -->
      <div class="admin-logo">
        <div class="admin-logo-icon">
          <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="16" cy="16" r="14" stroke="currentColor" stroke-width="1.5"/>
            <path d="M16 6C16 6 10 12 10 16C10 19.3137 12.6863 22 16 22C19.3137 22 22 19.3137 22 16C22 12 16 6 16 6Z" fill="currentColor" opacity="0.2"/>
            <path d="M16 10V22M12 16H20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            <circle cx="16" cy="16" r="2.5" fill="currentColor"/>
          </svg>
        </div>
        <h1 v-show="!isCollapsed">养生平台</h1>
      </div>
      <!-- 菜单 -->
      <nav class="admin-menu">
        <el-menu :default-active="currentPath" :collapse="isCollapsed" :collapse-transition="false" router>
          <el-menu-item index="/dashboard"><SvgIcon name="dashboard" :size="20" class="menu-icon" /><span>数据大盘</span></el-menu-item>
          <el-sub-menu index="user-manage">
            <template #title><SvgIcon name="users" :size="20" class="menu-icon" /><span>用户中心</span></template>
            <el-menu-item index="/users"><SvgIcon name="user" :size="18" class="menu-icon" /><span>用户管理</span></el-menu-item>
            <el-menu-item index="/staffs"><SvgIcon name="staff" :size="18" class="menu-icon" /><span>员工管理</span></el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="product-manage">
            <template #title><SvgIcon name="product" :size="20" class="menu-icon" /><span>商品中心</span></template>
            <el-menu-item index="/products"><SvgIcon name="herb" :size="18" class="menu-icon" /><span>商品管理</span></el-menu-item>
            <el-menu-item index="/categories"><SvgIcon name="category" :size="18" class="menu-icon" /><span>分类管理</span></el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/health-records"><SvgIcon name="wellness" :size="20" class="menu-icon" /><span>健康档案</span></el-menu-item>
          <el-menu-item index="/orders"><SvgIcon name="order" :size="20" class="menu-icon" /><span>订单管理</span></el-menu-item>
          <el-menu-item index="/reviews"><SvgIcon name="comment" :size="20" class="menu-icon" /><span>评价管理</span></el-menu-item>
          <el-sub-menu index="content-manage">
            <template #title><SvgIcon name="content" :size="20" class="menu-icon" /><span>内容中心</span></template>
            <el-menu-item index="/articles"><SvgIcon name="article" :size="18" class="menu-icon" /><span>文章管理</span></el-menu-item>
            <el-menu-item index="/health-plans"><SvgIcon name="health-plan" :size="18" class="menu-icon" /><span>养生方案</span></el-menu-item>
            <el-menu-item index="/news"><SvgIcon name="news" :size="18" class="menu-icon" /><span>资讯管理</span></el-menu-item>
            <el-menu-item index="/announcements"><SvgIcon name="announcement" :size="18" class="menu-icon" /><span>公告管理</span></el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="community-manage">
            <template #title><SvgIcon name="community" :size="20" class="menu-icon" /><span>社区管理</span></template>
            <el-menu-item index="/posts"><SvgIcon name="post" :size="18" class="menu-icon" /><span>帖子审核</span></el-menu-item>
            <el-menu-item index="/comments"><SvgIcon name="comment" :size="18" class="menu-icon" /><span>评论审核</span></el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/stocks"><SvgIcon name="stock" :size="20" class="menu-icon" /><span>库存管理</span></el-menu-item>
          <el-sub-menu index="system-manage">
            <template #title><SvgIcon name="setting" :size="20" class="menu-icon" /><span>系统管理</span></template>
            <el-menu-item index="/config"><SvgIcon name="setting" :size="18" class="menu-icon" /><span>系统配置</span></el-menu-item>
            <el-menu-item index="/statistics"><SvgIcon name="chart" :size="18" class="menu-icon" /><span>数据统计</span></el-menu-item>
            <el-menu-item index="/data-export"><SvgIcon name="download" :size="18" class="menu-icon" /><span>数据导出</span></el-menu-item>
          </el-sub-menu>
        </el-menu>
      </nav>
      <div class="admin-sidebar-footer" v-show="!isCollapsed"><span>v1.0.0</span></div>
    </aside>

    <!-- 主内容区 -->
    <main class="admin-main">
      <header class="admin-header">
        <div class="admin-header-left">
          <div class="collapse-btn" @click="toggleCollapse"><SvgIcon :name="isCollapsed ? 'menu-unfold' : 'menu-fold'" :size="20" /></div>
          <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item v-if="currentRoute.meta.title">{{ currentRoute.meta.title }}</el-breadcrumb-item></el-breadcrumb>
        </div>
        <div class="admin-header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="admin-user-dropdown">
              <div class="admin-user-avatar">
                <img v-if="userStore.avatar" :src="getAvatarUrl(userStore.avatar)" class="header-avatar-img" />
                <SvgIcon v-else name="avatar" :size="20" />
              </div>
              <span class="admin-user-name">{{ userStore.nickname || '管理员' }}</span>
              <SvgIcon name="arrow-down" :size="14" />
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile"><SvgIcon name="user" :size="16" class="mr-8" />个人信息</el-dropdown-item>
                <el-dropdown-item command="password"><SvgIcon name="lock" :size="16" class="mr-8" />修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout"><SvgIcon name="logout" :size="16" class="mr-8" />退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <div class="admin-content"><router-view v-slot="{ Component }"><transition name="fade" mode="out-in"><component :is="Component" /></transition></router-view></div>
    </main>

    <!-- 个人信息对话框 -->
    <el-dialog v-model="profileVisible" title="个人信息" width="500px">
      <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="80px">
        <el-form-item label="头像">
          <div class="avatar-uploader" @click="triggerAvatarUpload">
            <img v-if="profileForm.avatar" :src="getAvatarUrl(profileForm.avatar)" class="avatar-preview" />
            <div v-else class="avatar-placeholder"><SvgIcon name="camera" :size="32" /></div>
            <div class="avatar-overlay"><SvgIcon name="camera" :size="20" /><span>更换头像</span></div>
          </div>
          <input ref="avatarInputRef" type="file" accept="image/*" style="display:none" @change="handleAvatarChange" />
        </el-form-item>
        <el-form-item label="用户名"><el-input v-model="profileForm.username" disabled><template #prefix><SvgIcon name="user" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="profileForm.nickname" placeholder="请输入昵称"><template #prefix><SvgIcon name="nickname" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="profileForm.phone" placeholder="请输入手机号"><template #prefix><SvgIcon name="phone" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="profileForm.email" placeholder="请输入邮箱"><template #prefix><SvgIcon name="email" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="profileForm.gender">
            <el-radio :value="0">未知</el-radio>
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="profileForm.birthday" type="date" placeholder="请选择生日" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="profileVisible = false">取消</el-button><el-button type="primary" :loading="profileLoading" @click="handleSaveProfile">保存</el-button></template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordVisible" title="修改密码" width="450px">
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword"><el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码"><template #prefix><SvgIcon name="lock" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="新密码" prop="newPassword"><el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码"><template #prefix><SvgIcon name="lock" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword"><el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码"><template #prefix><SvgIcon name="lock" :size="16" /></template></el-input></el-form-item>
      </el-form>
      <template #footer><el-button @click="passwordVisible = false">取消</el-button><el-button type="primary" :loading="passwordLoading" @click="handleSavePassword">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user.js'
import { getAdminProfile, updateAdminProfile, updateAdminPassword, uploadAdminAvatar } from 'shared/api/admin.js'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapsed = ref(false)
const currentPath = computed(() => route.path)
const currentRoute = computed(() => route)

const profileVisible = ref(false)
const profileLoading = ref(false)
const profileFormRef = ref(null)
const avatarInputRef = ref(null)
const profileForm = reactive({ username: '', nickname: '', phone: '', email: '', avatar: '', gender: 0, birthday: '' })

// 后端服务器地址
const API_BASE_URL = ''

// 获取头像URL
function getAvatarUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}
const profileRules = {
  nickname: [{ max: 50, message: '昵称最多50个字符', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

const passwordVisible = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref(null)
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请再次输入新密码', trigger: 'blur' }, { validator: (rule, value, callback) => { if (value !== passwordForm.newPassword) { callback(new Error('两次输入的密码不一致')) } else { callback() } }, trigger: 'blur' }]
}

function toggleCollapse() { isCollapsed.value = !isCollapsed.value }

async function handleCommand(command) {
  if (command === 'profile') { await openProfileDialog() }
  else if (command === 'password') { openPasswordDialog() }
  else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
      await userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch (error) {}
  }
}

async function openProfileDialog() {
  try {
    const data = await getAdminProfile()
    Object.assign(profileForm, { username: data.username || '', nickname: data.nickname || '', phone: data.phone || '', email: data.email || '', avatar: data.avatar || '', gender: data.gender ?? 0, birthday: data.birthday || '' })
  } catch (error) {
    Object.assign(profileForm, { username: userStore.username || '', nickname: userStore.nickname || '', phone: '', email: '', avatar: userStore.avatar || '', gender: 0, birthday: '' })
  }
  profileVisible.value = true
}

function triggerAvatarUpload() {
  avatarInputRef.value?.click()
}

async function handleAvatarChange(e) {
  const file = e.target.files?.[0]
  if (!file) return
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过2MB')
    return
  }
  try {
    const url = await uploadAdminAvatar(file)
    profileForm.avatar = url
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
  e.target.value = ''
}

async function handleSaveProfile() {
  if (!profileFormRef.value) return
  try {
    await profileFormRef.value.validate()
    profileLoading.value = true
    await updateAdminProfile({ nickname: profileForm.nickname, phone: profileForm.phone, email: profileForm.email, avatar: profileForm.avatar, gender: profileForm.gender, birthday: profileForm.birthday })
    userStore.updateUserInfo({ nickname: profileForm.nickname, avatar: profileForm.avatar })
    ElMessage.success('保存成功')
    profileVisible.value = false
  } catch (error) { console.error(error) } finally { profileLoading.value = false }
}

function openPasswordDialog() { Object.assign(passwordForm, { oldPassword: '', newPassword: '', confirmPassword: '' }); passwordVisible.value = true }

async function handleSavePassword() {
  if (!passwordFormRef.value) return
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    await updateAdminPassword({ oldPassword: passwordForm.oldPassword, newPassword: passwordForm.newPassword, confirmPassword: passwordForm.confirmPassword })
    ElMessage.success('密码修改成功，请重新登录')
    passwordVisible.value = false
    await userStore.logout()
    router.push('/login')
  } catch (error) { console.error(error) } finally { passwordLoading.value = false }
}
</script>

<style scoped>
.admin-sidebar { transition: width 0.3s ease; }
.admin-sidebar.collapsed { width: 64px; }
.admin-sidebar.collapsed .admin-logo h1 { display: none; }
.admin-sidebar.collapsed + .admin-main { margin-left: 64px; }
.menu-icon { margin-right: 10px; flex-shrink: 0; }
.admin-sidebar.collapsed .menu-icon { margin-right: 0; }
:deep(.el-sub-menu__title) { height: 48px; line-height: 48px; margin: 2px 8px; border-radius: var(--tcm-radius-base); }
:deep(.el-sub-menu__title:hover) { background: var(--tcm-ochre-50); }
:deep(.el-menu--inline .el-menu-item) { padding-left: 52px !important; height: 44px; line-height: 44px; }
:deep(.el-dropdown-menu__item) { display: flex; align-items: center; }
:deep(.el-breadcrumb) { font-size: 14px; }
:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) { color: var(--tcm-text-primary); font-weight: 500; }
.admin-user-avatar { width: 32px; height: 32px; border-radius: 50%; background: var(--tcm-ochre-100); display: flex; align-items: center; justify-content: center; color: var(--tcm-ochre); overflow: hidden; }
.header-avatar-img { width: 100%; height: 100%; object-fit: cover; }
.avatar-uploader { position: relative; width: 80px; height: 80px; border-radius: 50%; overflow: hidden; cursor: pointer; background: var(--tcm-ochre-50); border: 2px dashed var(--tcm-border-light); transition: all 0.3s; }
.avatar-uploader:hover { border-color: var(--tcm-ochre); }
.avatar-uploader:hover .avatar-overlay { opacity: 1; }
.avatar-preview { width: 100%; height: 100%; object-fit: cover; }
.avatar-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: var(--tcm-text-placeholder); }
.avatar-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; flex-direction: column; align-items: center; justify-content: center; color: #fff; font-size: 12px; opacity: 0; transition: opacity 0.3s; gap: 4px; }
:deep(.el-input__prefix) { color: var(--tcm-text-secondary); }
</style>
