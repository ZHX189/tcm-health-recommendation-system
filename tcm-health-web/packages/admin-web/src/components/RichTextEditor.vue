<!--
  富文本编辑器组件 - 基于WangEditor封装
  @author Ti
  @since 2026-02-03
-->
<template>
  <div class="rich-text-editor">
    <Toolbar
      class="editor-toolbar"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      class="editor-content"
      :style="{ height: height }"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script setup>
/**
 * 富文本编辑器组件
 * 基于WangEditor 5.x封装，支持Vue3
 * 
 * @props modelValue - 编辑器内容（v-model绑定）
 * @props placeholder - 占位提示文本
 * @props height - 编辑器高度
 * @props mode - 编辑器模式：default(默认) | simple(简洁)
 * @props disabled - 是否禁用
 */
import { ref, shallowRef, computed, onBeforeUnmount, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const props = defineProps({
  /** 编辑器内容，支持v-model双向绑定 */
  modelValue: {
    type: String,
    default: ''
  },
  /** 占位提示文本 */
  placeholder: {
    type: String,
    default: '请输入内容...'
  },
  /** 编辑器高度 */
  height: {
    type: String,
    default: '300px'
  },
  /** 编辑器模式：default(完整) | simple(简洁) */
  mode: {
    type: String,
    default: 'default',
    validator: (val) => ['default', 'simple'].includes(val)
  },
  /** 是否禁用编辑器 */
  disabled: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

// 编辑器实例，使用shallowRef避免深度响应式
const editorRef = shallowRef(null)

// 编辑器内容，计算属性实现双向绑定
const valueHtml = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 工具栏配置
const toolbarConfig = {
  // 排除不需要的工具
  excludeKeys: [
    'group-video', // 排除视频
    'fullScreen', // 排除全屏
    'insertTable', // 排除表格
    'codeBlock', // 排除代码块
    'todo', // 排除待办
    'emotion' // 排除表情
  ]
}

// 编辑器配置
const editorConfig = {
  placeholder: props.placeholder,
  // 图片上传配置（如需要可扩展）
  MENU_CONF: {
    uploadImage: {
      // 后端上传接口
      server: '/api/admin/upload/image',
      // 单个文件最大体积限制，默认2M
      maxFileSize: 5 * 1024 * 1024,
      // 最多可上传几个文件
      maxNumberOfFiles: 10,
      // 超时时间
      timeout: 30 * 1000,
      // 上传文件的字段名
      fieldName: 'file',
      // 自定义响应处理
      customInsert(res, insertFn) {
        // 假设后端返回格式 { code: 0, data: { url: '...' } }
        if (res.code === 0 && res.data && res.data.url) {
          const url = res.data.url.startsWith('/uploads/')
            ? res.data.url
            : res.data.url
          insertFn(url, '', '')
        }
      }
    }
  }
}

/**
 * 编辑器创建完成回调
 * @param {Object} editor - 编辑器实例
 */
function handleCreated(editor) {
  editorRef.value = editor
  
  // 如果设置了禁用，则禁用编辑器
  if (props.disabled) {
    editor.disable()
  }
}

/**
 * 内容变化回调
 * @param {Object} editor - 编辑器实例
 */
function handleChange(editor) {
  emit('change', editor.getHtml())
}

// 监听disabled属性变化
watch(() => props.disabled, (newVal) => {
  if (editorRef.value) {
    newVal ? editorRef.value.disable() : editorRef.value.enable()
  }
})

// 组件销毁前，销毁编辑器实例
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})

// 暴露方法给父组件
defineExpose({
  /** 获取编辑器实例 */
  getEditor: () => editorRef.value,
  /** 清空内容 */
  clear: () => {
    if (editorRef.value) {
      editorRef.value.clear()
    }
  },
  /** 获取纯文本内容 */
  getText: () => {
    return editorRef.value ? editorRef.value.getText() : ''
  }
})
</script>

<style scoped>
.rich-text-editor {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}

.editor-toolbar {
  border-bottom: 1px solid #e0e0e0;
}

.editor-content {
  overflow-y: auto;
}

/* 深度选择器修改编辑器内部样式 */
:deep(.w-e-text-container) {
  background-color: #fafafa;
}

:deep(.w-e-text-placeholder) {
  font-style: normal;
  color: #999;
}

/* 工具栏样式 */
:deep(.w-e-toolbar) {
  background-color: #fff;
  border-color: #e0e0e0;
}

/* 聚焦时的边框样式 */
.rich-text-editor:focus-within {
  border-color: var(--tcm-primary, #8B4513);
  box-shadow: 0 0 0 2px rgba(139, 69, 19, 0.1);
}
</style>
