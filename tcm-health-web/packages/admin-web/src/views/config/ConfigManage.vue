<template>
  <div class="config-manage">
    <h3>系统配置</h3>
    <el-card v-for="(group, type) in groupedConfigs" :key="type" style="margin-bottom:16px">
      <template #header>
        <span style="font-weight:600">{{ typeLabels[type] || type }}</span>
      </template>
      <el-form label-width="200px">
        <el-form-item v-for="config in group" :key="config.config_key" :label="config.config_name">
          <!-- Logo 特殊处理：图片上传 -->
          <template v-if="config.config_key === 'site_logo'">
            <div style="display:flex;gap:12px;align-items:flex-start;width:100%">
              <div class="logo-upload-area">
                <div v-if="config.config_value" class="logo-preview">
                  <img :src="getImageUrl(config.config_value)" alt="Logo" />
                </div>
                <div v-else class="logo-placeholder"><SvgIcon name="image" :size="32" /></div>
                <el-button size="small" @click="triggerLogoUpload(config)">选择图片</el-button>
                <input ref="logoInputRef" type="file" accept="image/*" style="display:none" @change="handleLogoUpload($event, config)" />
              </div>
              <div style="flex:1">
                <el-input v-model="config.config_value" placeholder="Logo图片地址" />
                <div style="font-size:12px;color:#999;margin-top:4px">支持本地上传或输入URL地址</div>
              </div>
              <el-button type="primary" size="small" @click="saveConfig(config)">保存</el-button>
            </div>
          </template>
          <!-- 普通配置项 -->
          <template v-else>
            <div style="display:flex;gap:8px;align-items:center;width:100%">
              <el-input v-model="config.config_value" style="flex:1" />
              <el-button type="primary" size="small" @click="saveConfig(config)">保存</el-button>
            </div>
          </template>
          <div v-if="config.remark" style="font-size:12px;color:#999;margin-top:4px">{{ config.remark }}</div>
        </el-form-item>
      </el-form>
    </el-card>
    <el-empty v-if="Object.keys(groupedConfigs).length === 0" description="暂无配置" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getSystemConfigs, updateSystemConfig, uploadConfigImage } from 'shared/api/admin.js'
import { ElMessage } from 'element-plus'

const configs = ref([])
const logoInputRef = ref(null)
const typeLabels = { order: '订单配置', stock: '库存配置', ai: 'AI客服配置', site: '站点配置' }
const API_BASE_URL = ''

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/uploads')) return `${API_BASE_URL}${url}`
  return url
}

const groupedConfigs = computed(() => {
  const groups = {}
  configs.value.forEach(c => {
    const type = c.config_type || 'other'
    if (!groups[type]) groups[type] = []
    groups[type].push(c)
  })
  return groups
})

const loadConfigs = async () => {
  try {
    const res = await getSystemConfigs()
    configs.value = res || []
  } catch (e) { console.error(e) }
}

const saveConfig = async (config) => {
  try {
    await updateSystemConfig(config.config_key, config.config_value)
    ElMessage.success('保存成功')
  } catch (e) { ElMessage.error('保存失败') }
}

function triggerLogoUpload() {
  // 触发隐藏的file input
  const input = document.querySelector('input[type="file"][accept="image/*"]')
  if (input) input.click()
}

async function handleLogoUpload(e, config) {
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
    const url = await uploadConfigImage(file)
    config.config_value = url
    ElMessage.success('图片上传成功，请点击保存')
  } catch (error) {
    ElMessage.error('图片上传失败')
  }
  e.target.value = ''
}

onMounted(() => loadConfigs())
</script>

<style scoped>
.config-manage { padding: 20px; max-width: 900px; }
.config-manage h3 { margin: 0 0 16px; }
.logo-upload-area { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.logo-preview { width: 80px; height: 80px; border-radius: 8px; overflow: hidden; border: 1px solid #eee; }
.logo-preview img { width: 100%; height: 100%; object-fit: contain; }
.logo-placeholder { width: 80px; height: 80px; border-radius: 8px; border: 2px dashed #ddd; display: flex; align-items: center; justify-content: center; color: #ccc; }
</style>
