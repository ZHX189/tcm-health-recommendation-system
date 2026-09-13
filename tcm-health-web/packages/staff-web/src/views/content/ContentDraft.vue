<template>
  <div class="content-draft">
    <h3>内容协助</h3>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="提交资讯草稿" name="news">
        <el-form :model="newsForm" label-width="80px" style="max-width:600px">
          <el-form-item label="标题" required>
            <el-input v-model="newsForm.title" placeholder="资讯标题" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="newsForm.category" placeholder="选择分类">
              <el-option label="行业动态" value="行业动态" />
              <el-option label="养生资讯" value="养生资讯" />
              <el-option label="养生科普" value="养生科普" />
              <el-option label="平台动态" value="平台动态" />
            </el-select>
          </el-form-item>
          <el-form-item label="摘要">
            <el-input v-model="newsForm.summary" type="textarea" :rows="2" placeholder="资讯摘要" />
          </el-form-item>
          <el-form-item label="内容" required>
            <el-input v-model="newsForm.content" type="textarea" :rows="8" placeholder="资讯内容" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="newsSubmitting" @click="submitNews">提交草稿</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="提交公告草稿" name="announcement">
        <el-form :model="announcementForm" label-width="80px" style="max-width:600px">
          <el-form-item label="标题" required>
            <el-input v-model="announcementForm.title" placeholder="公告标题" />
          </el-form-item>
          <el-form-item label="内容" required>
            <el-input v-model="announcementForm.content" type="textarea" :rows="8" placeholder="公告内容" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="annSubmitting" @click="submitAnnouncement">提交草稿</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { submitNewsDraft, submitAnnouncementDraft } from 'shared/api/staff.js'
import { ElMessage } from 'element-plus'

const activeTab = ref('news')
const newsForm = ref({ title: '', category: '', summary: '', content: '' })
const announcementForm = ref({ title: '', content: '' })
const newsSubmitting = ref(false)
const annSubmitting = ref(false)

const submitNews = async () => {
  if (!newsForm.value.title || !newsForm.value.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  newsSubmitting.value = true
  try {
    await submitNewsDraft(newsForm.value)
    ElMessage.success('资讯草稿提交成功，等待管理员审核')
    newsForm.value = { title: '', category: '', summary: '', content: '' }
  } catch (e) { ElMessage.error('提交失败') }
  finally { newsSubmitting.value = false }
}

const submitAnnouncement = async () => {
  if (!announcementForm.value.title || !announcementForm.value.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  annSubmitting.value = true
  try {
    await submitAnnouncementDraft(announcementForm.value)
    ElMessage.success('公告草稿提交成功，等待管理员审核')
    announcementForm.value = { title: '', content: '' }
  } catch (e) { ElMessage.error('提交失败') }
  finally { annSubmitting.value = false }
}
</script>

<style scoped>
.content-draft { padding: 20px; }
.content-draft h3 { margin: 0 0 16px; }
</style>
