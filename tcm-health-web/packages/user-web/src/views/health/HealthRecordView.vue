<!--
  健康档案页面
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="health-record-page">
    <div class="user-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>健康档案</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="record-container" v-loading="loading">
      <!-- 标题 -->
      <div class="record-header">
        <h2>我的健康档案</h2>
        <p class="record-desc">完善您的健康档案，为养生方案推荐提供依据</p>
      </div>

      <!-- 档案表单 -->
      <div class="record-form-card">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" label-position="right">
          <!-- 基本信息 -->
          <div class="form-section">
            <h3 class="section-title"><SvgIcon name="user" :size="18" />基本信息</h3>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="form.realName" placeholder="请输入真实姓名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年龄" prop="age">
                  <el-input-number v-model="form.age" :min="1" :max="150" placeholder="请输入年龄" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="身高(cm)" prop="height">
                  <el-input-number v-model="form.height" :min="50" :max="250" :precision="1" placeholder="请输入身高" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="体重(kg)" prop="weight">
                  <el-input-number v-model="form.weight" :min="20" :max="300" :precision="1" placeholder="请输入体重" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="血型">
                  <el-select v-model="form.bloodType" placeholder="请选择血型" style="width: 100%">
                    <el-option label="A型" value="A型" />
                    <el-option label="B型" value="B型" />
                    <el-option label="AB型" value="AB型" />
                    <el-option label="O型" value="O型" />
                    <el-option label="未知" value="未知" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="体质类型">
                  <el-select v-model="form.constitutionType" placeholder="请选择体质类型" style="width: 100%">
                    <el-option label="平和质" value="平和质" />
                    <el-option label="气虚质" value="气虚质" />
                    <el-option label="阳虚质" value="阳虚质" />
                    <el-option label="阴虚质" value="阴虚质" />
                    <el-option label="痰湿质" value="痰湿质" />
                    <el-option label="湿热质" value="湿热质" />
                    <el-option label="血瘀质" value="血瘀质" />
                    <el-option label="气郁质" value="气郁质" />
                    <el-option label="特禀质" value="特禀质" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 健康状况 -->
          <div class="form-section">
            <h3 class="section-title"><SvgIcon name="wellness" :size="18" />健康状况</h3>
            <el-form-item label="病史">
              <el-input v-model="form.medicalHistory" type="textarea" :rows="3" placeholder="请描述您的病史，如：高血压、糖尿病等，无则填'无'" />
            </el-form-item>
            <el-form-item label="过敏史">
              <el-input v-model="form.allergyHistory" type="textarea" :rows="3" placeholder="请描述您的过敏情况，如：花粉过敏、青霉素过敏等，无则填'无'" />
            </el-form-item>
            <el-form-item label="家族病史">
              <el-input v-model="form.familyHistory" type="textarea" :rows="3" placeholder="请描述家族中常见疾病，无则填'无'" />
            </el-form-item>
          </div>

          <!-- 生活习惯 -->
          <div class="form-section">
            <h3 class="section-title"><SvgIcon name="article" :size="18" />生活习惯</h3>
            <el-form-item label="生活习惯">
              <el-input v-model="form.lifestyle" type="textarea" :rows="2" placeholder="请描述您的日常生活习惯，如作息时间等" />
            </el-form-item>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="饮食偏好">
                  <el-input v-model="form.dietPreference" placeholder="如：清淡为主、喜辣等" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="睡眠质量">
                  <el-select v-model="form.sleepQuality" placeholder="请选择睡眠质量" style="width: 100%">
                    <el-option label="很好" value="很好" />
                    <el-option label="良好" value="良好" />
                    <el-option label="一般" value="一般" />
                    <el-option label="较差" value="较差" />
                    <el-option label="很差" value="很差" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="运动频率">
                  <el-select v-model="form.exerciseFrequency" placeholder="请选择运动频率" style="width: 100%">
                    <el-option label="每天运动" value="每天运动" />
                    <el-option label="每周3-5次" value="每周3-5次" />
                    <el-option label="每周1-2次" value="每周1-2次" />
                    <el-option label="很少运动" value="很少运动" />
                    <el-option label="几乎不运动" value="几乎不运动" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 备注 -->
          <div class="form-section">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="其他需要补充的健康信息" />
            </el-form-item>
          </div>

          <!-- 操作按钮 -->
          <div class="form-actions">
            <el-button type="primary" size="large" @click="handleSave" :loading="saving">
              <SvgIcon name="check" :size="16" class="mr-8" />
              {{ hasRecord ? '更新档案' : '创建档案' }}
            </el-button>
            <el-button size="large" @click="handleReset">
              <SvgIcon name="refresh" :size="16" class="mr-8" />
              重置
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getHealthRecord, createHealthRecord, updateHealthRecord } from 'shared/api/user.js'

const loading = ref(false)
const saving = ref(false)
const hasRecord = ref(false)
const formRef = ref(null)

const form = reactive({
  realName: '',
  age: null,
  height: null,
  weight: null,
  bloodType: '',
  constitutionType: '',
  medicalHistory: '',
  allergyHistory: '',
  familyHistory: '',
  lifestyle: '',
  dietPreference: '',
  sleepQuality: '',
  exerciseFrequency: '',
  remark: ''
})

const rules = {
  realName: [{ max: 50, message: '真实姓名最多50个字符', trigger: 'blur' }]
}

// 加载健康档案
async function fetchRecord() {
  loading.value = true
  try {
    const data = await getHealthRecord()
    if (data) {
      hasRecord.value = true
      Object.assign(form, {
        realName: data.realName || '',
        age: data.age || null,
        height: data.height || null,
        weight: data.weight || null,
        bloodType: data.bloodType || '',
        constitutionType: data.constitutionType || '',
        medicalHistory: data.medicalHistory || '',
        allergyHistory: data.allergyHistory || '',
        familyHistory: data.familyHistory || '',
        lifestyle: data.lifestyle || '',
        dietPreference: data.dietPreference || '',
        sleepQuality: data.sleepQuality || '',
        exerciseFrequency: data.exerciseFrequency || '',
        remark: data.remark || ''
      })
    }
  } catch (error) {
    // 无档案，保持空表单
    hasRecord.value = false
  } finally {
    loading.value = false
  }
}

// 保存档案
async function handleSave() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    saving.value = true
    if (hasRecord.value) {
      await updateHealthRecord(form)
    } else {
      await createHealthRecord(form)
    }
    ElMessage.success(hasRecord.value ? '档案更新成功' : '档案创建成功')
    hasRecord.value = true
  } catch (error) {
    if (error !== 'cancel') console.error('保存失败:', error)
  } finally {
    saving.value = false
  }
}

// 重置表单
function handleReset() {
  fetchRecord()
}

onMounted(() => {
  fetchRecord()
})
</script>

<style scoped>
.health-record-page {
  min-height: 60vh;
}

.record-container {
  max-width: 900px;
  margin: 0 auto;
}

.record-header {
  margin-bottom: 24px;
}

.record-header h2 {
  font-family: var(--tcm-font-title);
  font-size: 22px;
  color: var(--tcm-text-primary);
  margin: 0 0 8px 0;
}

.record-desc {
  font-size: 14px;
  color: var(--tcm-text-secondary);
  margin: 0;
}

.record-form-card {
  background: var(--tcm-bg-paper);
  border: 1px solid var(--tcm-border-light);
  border-radius: var(--tcm-radius-md);
  padding: 32px;
}

.form-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--tcm-border-lighter);
}

.form-section:last-of-type {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: var(--tcm-font-title);
  font-size: 16px;
  color: var(--tcm-text-primary);
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--tcm-primary);
  width: fit-content;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid var(--tcm-border-lighter);
}

@media (max-width: 768px) {
  .record-form-card {
    padding: 16px;
  }
  .el-col {
    width: 100%;
    max-width: 100%;
    flex: 0 0 100%;
  }
}
</style>
