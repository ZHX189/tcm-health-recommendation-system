<!--
  个人中心页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="profile-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item>个人中心</el-breadcrumb-item></el-breadcrumb>
    </div>

    <div class="profile-layout">
      <aside class="profile-sidebar">
        <div class="profile-avatar">
          <el-upload class="avatar-uploader" :action="uploadUrl" :headers="uploadHeaders" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" accept="image/*">
            <div class="profile-avatar-img">
              <img v-if="userInfo.avatar" :src="getImageUrl(userInfo.avatar)" alt="头像" />
              <SvgIcon v-else name="avatar" :size="40" />
            </div>
            <div class="avatar-upload-tip">点击更换头像</div>
          </el-upload>
          <div class="profile-name">{{ userInfo.nickname || userInfo.username || '用户' }}</div>
        </div>
        <nav class="profile-menu">
          <a class="profile-menu-item" :class="{ active: activeTab === 'info' }" @click="activeTab = 'info'"><SvgIcon name="user" :size="18" />个人信息</a>
          <a class="profile-menu-item" @click="router.push('/health-record')"><SvgIcon name="wellness" :size="18" />健康档案</a>
          <a class="profile-menu-item" :class="{ active: activeTab === 'address' }" @click="activeTab = 'address'"><SvgIcon name="location" :size="18" />收货地址</a>
          <a class="profile-menu-item" :class="{ active: activeTab === 'collections' }" @click="activeTab = 'collections'"><SvgIcon name="heart" :size="18" />我的收藏</a>
          <a class="profile-menu-item" :class="{ active: activeTab === 'security' }" @click="activeTab = 'security'"><SvgIcon name="lock" :size="18" />账号安全</a>
        </nav>
      </aside>

      <main class="profile-content">
        <!-- 个人信息 -->
        <div v-if="activeTab === 'info'" class="content-card">
          <div class="card-header"><h3>个人信息</h3></div>
          <div class="card-body">
            <el-form :model="userForm" label-width="80px">
              <el-form-item label="用户名"><el-input v-model="userForm.username" disabled><template #prefix><SvgIcon name="user" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="昵称"><el-input v-model="userForm.nickname" placeholder="请输入昵称"><template #prefix><SvgIcon name="avatar" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="手机号"><el-input v-model="userForm.phone" placeholder="请输入手机号"><template #prefix><SvgIcon name="phone" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="邮箱"><el-input v-model="userForm.email" placeholder="请输入邮箱"><template #prefix><SvgIcon name="email" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="userForm.gender">
                  <el-radio :value="0">未知</el-radio>
                  <el-radio :value="1">男</el-radio>
                  <el-radio :value="2">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="生日">
                <el-date-picker v-model="userForm.birthday" type="date" placeholder="请选择生日" value-format="YYYY-MM-DD" style="width:100%" />
              </el-form-item>
              <el-form-item><el-button type="primary" @click="handleSaveProfile"><SvgIcon name="check" :size="16" class="mr-8" />保存修改</el-button></el-form-item>
            </el-form>
          </div>
        </div>

        <!-- 收货地址 -->
        <div v-if="activeTab === 'address'" class="content-card">
          <div class="card-header"><h3>收货地址</h3><el-button type="primary" size="small" @click="openAddressDialog()"><SvgIcon name="add" :size="14" class="mr-8" />新增地址</el-button></div>
          <div class="card-body">
            <div v-for="addr in addresses" :key="addr.id" class="address-item">
              <div class="address-info">
                <div class="address-name">{{ addr.receiverName }} <span class="address-phone">{{ addr.receiverPhone }}</span><el-tag v-if="addr.isDefault" size="small" type="success">默认</el-tag></div>
                <div class="address-detail">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</div>
              </div>
              <div class="address-actions">
                <button class="action-btn edit" @click="openAddressDialog(addr)"><SvgIcon name="edit" :size="14" />编辑</button>
                <button class="action-btn" @click="handleSetDefault(addr)" v-if="!addr.isDefault"><SvgIcon name="check" :size="14" />设为默认</button>
                <button class="action-btn delete" @click="handleDeleteAddress(addr.id)"><SvgIcon name="delete" :size="14" />删除</button>
              </div>
            </div>
            <el-empty v-if="addresses.length === 0" description="暂无收货地址" />
          </div>
        </div>

        <!-- 我的收藏 -->
        <div v-if="activeTab === 'collections'" class="content-card">
          <div class="card-header"><h3>我的收藏</h3></div>
          <div class="card-body" v-loading="collectionsLoading">
            <div class="collections-grid" v-if="collections.length > 0">
              <div v-for="item in collections" :key="item.id" class="collection-item">
                <div class="collection-img-wrapper" @click="goToDetail(item)">
                  <img :src="getCollectionImage(item)" :alt="getCollectionName(item)" class="collection-img" />
                  <button class="collection-remove" @click.stop="handleRemoveCollection(item)" title="取消收藏"><SvgIcon name="heart" :size="16" /></button>
                </div>
                <div class="collection-info" @click="goToDetail(item)">
                  <div class="collection-title">{{ getCollectionName(item) }}</div>
                  <div class="collection-meta">
                    <span class="collection-type">{{ getTypeName(item.targetType) }}</span>
                    <span class="collection-price" v-if="item.targetType === 1 && getCollectionPrice(item)">¥{{ getCollectionPrice(item) }}</span>
                  </div>
                </div>
                <button class="collection-cancel-btn" @click="handleRemoveCollection(item)"><SvgIcon name="heart" :size="14" />取消收藏</button>
              </div>
            </div>
            <el-empty v-else description="暂无收藏" />
          </div>
        </div>

        <!-- 账号安全 -->
        <div v-if="activeTab === 'security'" class="content-card">
          <div class="card-header"><h3>修改密码</h3></div>
          <div class="card-body">
            <el-form :model="passwordForm" label-width="100px">
              <el-form-item label="原密码"><el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码"><template #prefix><SvgIcon name="lock" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="新密码"><el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码"><template #prefix><SvgIcon name="key" :size="16" /></template></el-input></el-form-item>
              <el-form-item label="确认密码"><el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码"><template #prefix><SvgIcon name="key" :size="16" /></template></el-input></el-form-item>
              <el-form-item><el-button type="primary" @click="handleChangePassword"><SvgIcon name="check" :size="16" class="mr-8" />修改密码</el-button></el-form-item>
            </el-form>
          </div>
        </div>
      </main>
    </div>

    <!-- 地址编辑弹窗 -->
    <el-dialog v-model="addressDialogVisible" :title="addressForm.id ? '编辑地址' : '新增地址'" width="500px">
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="收货人"><el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名"><template #prefix><SvgIcon name="user" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="手机号"><el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号"><template #prefix><SvgIcon name="phone" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="省份"><el-input v-model="addressForm.province" placeholder="请输入省份"><template #prefix><SvgIcon name="location" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="城市"><el-input v-model="addressForm.city" placeholder="请输入城市"><template #prefix><SvgIcon name="location" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="区县"><el-input v-model="addressForm.district" placeholder="请输入区县"><template #prefix><SvgIcon name="location" :size="16" /></template></el-input></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="addressForm.detailAddress" type="textarea" :rows="2" placeholder="请输入详细地址" /></el-form-item>
        <el-form-item label="默认地址"><el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserProfile, updateUserProfile, updatePassword, getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress, getCollectionList, deleteCollection, getProductDetail, getArticleDetail, getHealthPlanDetail } from 'shared/api/user.js'
import { getUserInfo, setUserInfo, getToken } from 'shared/utils/index.js'

const route = useRoute()
const router = useRouter()
const activeTab = ref(route.query.tab || 'info')
const userInfo = ref(getUserInfo() || {})
const addresses = ref([])
const collections = ref([])
const collectionsLoading = ref(false)

// 后端服务器地址
const API_BASE_URL = ''

// 上传配置
const uploadUrl = `${API_BASE_URL}/api/user/profile/avatar`
const uploadHeaders = computed(() => ({ Authorization: `Bearer ${getToken()}` }))

// 获取图片URL
function getImageUrl(url) {
  if (!url) return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"%3E%3Crect fill="%23f5f5f5" width="100" height="100"/%3E%3Ctext x="50" y="55" text-anchor="middle" fill="%23ccc" font-size="14"%3E暂无图片%3C/text%3E%3C/svg%3E'
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return `${API_BASE_URL}${url.startsWith('/') ? '' : '/'}${url}`
}

const userForm = reactive({ username: '', nickname: '', phone: '', email: '', gender: 0, birthday: '' })
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

// 地址表单
const addressDialogVisible = ref(false)
const addressForm = reactive({ id: null, receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 })

async function fetchProfile() {
  try {
    const data = await getUserProfile()
    Object.assign(userForm, data)
    userInfo.value = data
    setUserInfo(data)
  } catch (error) {
    Object.assign(userForm, userInfo.value)
  }
}

async function fetchAddresses() {
  try {
    const data = await getAddressList()
    addresses.value = data || []
  } catch (error) {
    addresses.value = []
  }
}

async function fetchCollections() {
  collectionsLoading.value = true
  try {
    const data = await getCollectionList()
    // 处理分页数据格式
    let rawList = []
    if (data && data.records) {
      rawList = data.records || []
    } else if (Array.isArray(data)) {
      rawList = data
    }
    
    // 根据 targetType 获取详细信息
    const detailedList = await Promise.all(
      rawList.map(async (item) => {
        try {
          let detail = {}
          if (item.targetType === 1) {
            // 商品
            detail = await getProductDetail(item.targetId)
          } else if (item.targetType === 2) {
            // 文章
            detail = await getArticleDetail(item.targetId)
          } else if (item.targetType === 3) {
            // 养生方案
            detail = await getHealthPlanDetail(item.targetId)
          }
          return { ...item, ...detail }
        } catch (e) {
          console.error('获取详情失败:', e)
          return item
        }
      })
    )
    
    collections.value = detailedList
    console.log('收藏列表(含详情):', collections.value)
  } catch (error) {
    console.error('获取收藏列表失败:', error)
    collections.value = []
  } finally {
    collectionsLoading.value = false
  }
}

async function handleSaveProfile() {
  try {
    await updateUserProfile(userForm)
    ElMessage.success('保存成功')
    fetchProfile()
  } catch (error) { console.error(error) }
}

async function handleChangePassword() {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    ElMessage.warning('请填写完整密码信息')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  try {
    await updatePassword(passwordForm)
    ElMessage.success('密码修改成功')
    Object.assign(passwordForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  } catch (error) { console.error(error) }
}

// 打开地址弹窗
function openAddressDialog(addr = null) {
  if (addr) {
    Object.assign(addressForm, addr)
  } else {
    Object.assign(addressForm, { id: null, receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 })
  }
  addressDialogVisible.value = true
}

// 保存地址
async function handleSaveAddress() {
  if (!addressForm.receiverName || !addressForm.receiverPhone || !addressForm.detailAddress) {
    ElMessage.warning('请填写完整地址信息')
    return
  }
  try {
    if (addressForm.id) {
      await updateAddress(addressForm.id, addressForm)
      ElMessage.success('地址修改成功')
    } else {
      await addAddress(addressForm)
      ElMessage.success('地址添加成功')
    }
    addressDialogVisible.value = false
    fetchAddresses()
  } catch (error) { console.error(error) }
}

// 删除地址
async function handleDeleteAddress(id) {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗？', '提示', { type: 'warning' })
    await deleteAddress(id)
    ElMessage.success('删除成功')
    fetchAddresses()
  } catch (error) { if (error !== 'cancel') console.error(error) }
}

// 设为默认地址
async function handleSetDefault(addr) {
  try {
    await setDefaultAddress(addr.id)
    ElMessage.success('设置成功')
    fetchAddresses()
  } catch (error) { console.error(error) }
}

// 获取收藏类型名称
function getTypeName(type) {
  const types = { 1: '商品', 2: '文章', 3: '养生方案' }
  return types[type] || '未知'
}

// 获取收藏项图片（兼容多种字段名）
function getCollectionImage(item) {
  const imageUrl = item.mainImage || item.coverImage || item.image || item.img || ''
  return getImageUrl(imageUrl)
}

// 获取收藏项名称（兼容多种字段名）
function getCollectionName(item) {
  return item.name || item.title || item.productName || '未知'
}

// 获取收藏项价格
function getCollectionPrice(item) {
  return item.price || item.productPrice || ''
}

// 跳转详情
function goToDetail(item) {
  if (item.targetType === 1) router.push(`/products/${item.targetId}`)
  else if (item.targetType === 2) router.push(`/articles/${item.targetId}`)
  else if (item.targetType === 3) router.push(`/health/${item.targetId}`)
}

// 取消收藏
async function handleRemoveCollection(item) {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', { type: 'warning' })
    await deleteCollection(item.targetId, item.targetType)
    ElMessage.success('已取消收藏')
    fetchCollections()
  } catch (error) { if (error !== 'cancel') console.error(error) }
}

// 头像上传成功回调
function handleAvatarSuccess(response) {
  if (response.code === 200) {
    userInfo.value.avatar = response.data
    setUserInfo(userInfo.value)
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传前校验
function beforeAvatarUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) { ElMessage.error('只能上传图片文件!'); return false }
  if (!isLt2M) { ElMessage.error('图片大小不能超过2MB!'); return false }
  return true
}

// 监听tab切换，加载收藏数据
watch(activeTab, (val) => {
  if (val === 'collections') {
    fetchCollections()
  }
})

onMounted(() => {
  fetchProfile()
  fetchAddresses()
  if (activeTab.value === 'collections') fetchCollections()
})
</script>


<style scoped>
.profile-page { min-height: 60vh; }
.profile-layout { display: flex; gap: 24px; }
.profile-sidebar { width: 220px; flex-shrink: 0; }
.profile-content { flex: 1; }
.content-card { background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-md); }
.card-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; border-bottom: 1px solid var(--tcm-border-lighter); }
.card-header h3 { font-family: var(--tcm-font-title); font-size: 16px; margin: 0; color: var(--tcm-text-primary); }
.card-body { padding: 20px; }
.address-item { display: flex; align-items: center; justify-content: space-between; padding: 16px; border: 1px solid var(--tcm-border-lighter); border-radius: var(--tcm-radius-base); margin-bottom: 12px; }
.address-name { font-size: 14px; color: var(--tcm-text-primary); margin-bottom: 8px; display: flex; align-items: center; gap: 12px; }
.address-phone { color: var(--tcm-text-secondary); }
.address-detail { font-size: 13px; color: var(--tcm-text-secondary); }
.address-actions { display: flex; gap: 12px; }
.action-btn { display: flex; align-items: center; gap: 4px; padding: 6px 12px; border: 1px solid var(--tcm-border-light); border-radius: var(--tcm-radius-base); background: var(--tcm-bg-paper); color: var(--tcm-text-secondary); cursor: pointer; font-size: 13px; transition: all 0.2s; }
.action-btn:hover { border-color: var(--tcm-primary); color: var(--tcm-primary); }
.action-btn.delete:hover { border-color: var(--tcm-error); color: var(--tcm-error); }

/* 头像上传样式 */
.profile-avatar { text-align: center; margin-bottom: 20px; }
.avatar-uploader { display: inline-block; cursor: pointer; }
.profile-avatar-img { width: 80px; height: 80px; border-radius: 50%; background: var(--tcm-bg-paper-dark); display: flex; align-items: center; justify-content: center; overflow: hidden; margin: 0 auto; border: 2px solid var(--tcm-border-light); transition: border-color 0.2s; }
.profile-avatar-img:hover { border-color: var(--tcm-primary); }
.profile-avatar-img img { width: 100%; height: 100%; object-fit: cover; }
.avatar-upload-tip { font-size: 12px; color: var(--tcm-text-placeholder); margin-top: 8px; }
.profile-name { font-size: 16px; font-weight: 500; color: var(--tcm-text-primary); margin-top: 12px; }

/* 收藏列表样式 */
.collections-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.collection-item { position: relative; background: var(--tcm-bg-paper); border: 1px solid var(--tcm-border-lighter); border-radius: var(--tcm-radius-base); overflow: hidden; transition: all 0.2s; }
.collection-item:hover { box-shadow: var(--tcm-shadow-md); transform: translateY(-2px); }
.collection-img-wrapper { position: relative; cursor: pointer; }
.collection-img { width: 100%; height: 140px; object-fit: cover; display: block; }
.collection-info { padding: 12px; cursor: pointer; }
.collection-title { font-size: 14px; color: var(--tcm-text-primary); margin-bottom: 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.collection-meta { display: flex; align-items: center; justify-content: space-between; }
.collection-type { font-size: 12px; color: var(--tcm-text-placeholder); }
.collection-price { font-size: 14px; color: var(--tcm-error); font-weight: 500; }
.collection-remove { position: absolute; top: 8px; right: 8px; width: 32px; height: 32px; border-radius: 50%; background: rgba(255,255,255,0.9); border: none; color: var(--tcm-error); cursor: pointer; display: flex; align-items: center; justify-content: center; opacity: 0; transition: all 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.15); }
.collection-item:hover .collection-remove { opacity: 1; }
.collection-remove:hover { background: var(--tcm-error); color: #fff; transform: scale(1.1); }
.collection-cancel-btn { display: flex; align-items: center; justify-content: center; gap: 6px; width: 100%; padding: 10px; border: none; border-top: 1px solid var(--tcm-border-lighter); background: var(--tcm-bg-paper); color: var(--tcm-text-secondary); cursor: pointer; font-size: 13px; transition: all 0.2s; }
.collection-cancel-btn:hover { background: #fff5f5; color: var(--tcm-error); }
.collection-cancel-btn .tcm-icon { color: var(--tcm-error); }

@media (max-width: 1024px) { .collections-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .profile-layout { flex-direction: column; } .profile-sidebar { width: 100%; } .collections-grid { grid-template-columns: 1fr; } }
</style>
