<!--
  养生助手页面
  @author Ti
  @since 2026-02-05
-->
<template>
  <div class="assistant-page">
    <div class="assistant-container">
      <!-- 左侧会话列表 -->
      <aside class="session-sidebar">
        <div class="sidebar-header">
          <h3>养生助手</h3>
          <button class="new-chat-btn" @click="createNewSession">
            <SvgIcon name="plus" :size="16" />
            <span>新对话</span>
          </button>
        </div>
        <div class="session-list">
          <div 
            v-for="session in sessions" 
            :key="session.id"
            class="session-item"
            :class="{ active: currentSessionId === session.id }"
            @click="switchSession(session.id)"
          >
            <SvgIcon name="chat" :size="16" />
            <span class="session-title">{{ session.title }}</span>
            <button class="delete-btn" @click.stop="deleteSession(session.id)" title="删除">
              <SvgIcon name="delete" :size="14" />
            </button>
          </div>
        </div>
      </aside>

      <!-- 右侧聊天区域 -->
      <main class="chat-main">
        <!-- 聊天消息区 -->
        <div class="chat-messages" ref="messagesContainer">
          <!-- 欢迎消息 -->
          <div v-if="messages.length === 0" class="welcome-section">
            <div class="welcome-icon">
              <svg viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="40" cy="40" r="35" stroke="currentColor" stroke-width="2"/>
                <path d="M40 12C40 12 24 28 24 40C24 48.8366 31.1634 56 40 56C48.8366 56 56 48.8366 56 40C56 28 40 12 40 12Z" fill="currentColor" opacity="0.15"/>
                <path d="M40 20V60M28 40H52" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <circle cx="40" cy="40" r="6" fill="currentColor"/>
              </svg>
            </div>
            <h2>你好，我是养生小助手</h2>
            <p>我可以为您解答中医养生相关问题，推荐适合的药材和养生方案</p>
            <div class="quick-questions">
              <button v-for="q in quickQuestions" :key="q" @click="sendQuickQuestion(q)">
                {{ q }}
              </button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div v-for="(msg, index) in messages" :key="index" class="message-item" :class="msg.role">
            <div class="message-avatar">
              <template v-if="msg.role === 'user'">
                <img v-if="getUserAvatarUrl()" :src="getUserAvatarUrl()" alt="头像" class="user-avatar-img" />
                <SvgIcon v-else name="avatar" :size="24" />
              </template>
              <div v-else class="assistant-avatar">
                <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="16" cy="16" r="14" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M16 5C16 5 10 11 10 16C10 19.3137 12.6863 22 16 22C19.3137 22 22 19.3137 22 16C22 11 16 5 16 5Z" fill="currentColor" opacity="0.2"/>
                  <path d="M16 8V24M11 16H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                  <circle cx="16" cy="16" r="2.5" fill="currentColor"/>
                </svg>
              </div>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="renderMarkdown(msg.content)"></div>
              <div class="message-time">{{ formatTime(msg.time) }}</div>
            </div>
          </div>

          <!-- 加载中 -->
          <div v-if="isLoading" class="message-item assistant">
            <div class="message-avatar">
              <div class="assistant-avatar">
                <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="16" cy="16" r="14" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M16 5C16 5 10 11 10 16C10 19.3137 12.6863 22 16 22C19.3137 22 22 19.3137 22 16C22 11 16 5 16 5Z" fill="currentColor" opacity="0.2"/>
                  <path d="M16 8V24M11 16H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                  <circle cx="16" cy="16" r="2.5" fill="currentColor"/>
                </svg>
              </div>
            </div>
            <div class="message-content">
              <div class="message-text typing">
                <span v-if="streamingContent">{{ streamingContent }}</span>
                <span v-else class="typing-dots">
                  <span></span><span></span><span></span>
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-area">
          <div class="input-wrapper">
            <textarea
              v-model="inputMessage"
              placeholder="请输入您的问题，例如：黄芪有什么功效？"
              @keydown.enter.exact.prevent="sendMessage"
              @keydown.enter.shift.exact="newLine"
              :disabled="isLoading"
              rows="1"
              ref="inputRef"
            ></textarea>
            <button 
              class="send-btn" 
              @click="sendMessage" 
              :disabled="!inputMessage.trim() || isLoading"
            >
              <SvgIcon v-if="!isLoading" name="send" :size="20" />
              <span v-else class="loading-spinner"></span>
            </button>
          </div>
          <div class="input-tips">
            <span>按 Enter 发送，Shift + Enter 换行</span>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { chatWithAIStream, clearChatSession } from 'shared/api/user.js'
import { getToken, getUserInfo } from 'shared/utils/index.js'
import { useRouter } from 'vue-router'

const router = useRouter()

// 获取用户信息
const userInfo = computed(() => getUserInfo() || {})

// 获取用户头像URL
function getUserAvatarUrl() {
  const info = userInfo.value
  if (info && info.avatar) {
    // 如果是完整URL直接返回，否则拼接基础URL
    if (info.avatar.startsWith('http')) {
      return info.avatar
    }
    return info.avatar
  }
  return ''
}

// 快捷问题
const quickQuestions = [
  '黄芪有什么功效？',
  '气虚体质如何调理？',
  '推荐一些补血的药材',
  '春季养生有什么建议？'
]

// 会话列表
const sessions = ref([])
const currentSessionId = ref('')
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const streamingContent = ref('')
const messagesContainer = ref(null)
const inputRef = ref(null)

// 取消请求函数
let cancelRequest = null

// 生成会话ID
function generateSessionId() {
  return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

// 创建新会话
function createNewSession() {
  const sessionId = generateSessionId()
  const newSession = {
    id: sessionId,
    title: '新对话',
    messages: [],
    createTime: new Date()
  }
  sessions.value.unshift(newSession)
  currentSessionId.value = sessionId
  messages.value = []
  saveSessions()
}

// 切换会话
function switchSession(sessionId) {
  if (isLoading.value) {
    ElMessage.warning('请等待当前回复完成')
    return
  }
  currentSessionId.value = sessionId
  const session = sessions.value.find(s => s.id === sessionId)
  if (session) {
    messages.value = session.messages || []
  }
  scrollToBottom()
}

// 删除会话
async function deleteSession(sessionId) {
  const index = sessions.value.findIndex(s => s.id === sessionId)
  if (index > -1) {
    sessions.value.splice(index, 1)
    
    // 如果删除的是当前会话，切换到第一个或创建新会话
    if (currentSessionId.value === sessionId) {
      if (sessions.value.length > 0) {
        switchSession(sessions.value[0].id)
      } else {
        createNewSession()
      }
    }
    
    saveSessions()
    
    // 清除服务端会话
    try {
      await clearChatSession(sessionId)
    } catch (e) {
      // 忽略错误
    }
  }
}

// 发送消息
async function sendMessage() {
  const content = inputMessage.value.trim()
  if (!content || isLoading.value) return

  // 检查登录状态
  if (!getToken()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 添加用户消息
  const userMessage = {
    role: 'user',
    content: content,
    time: new Date()
  }
  messages.value.push(userMessage)
  inputMessage.value = ''
  
  // 更新会话标题（使用第一条消息）
  const currentSession = sessions.value.find(s => s.id === currentSessionId.value)
  if (currentSession && currentSession.title === '新对话') {
    currentSession.title = content.length > 20 ? content.substring(0, 20) + '...' : content
  }

  isLoading.value = true
  streamingContent.value = ''
  scrollToBottom()

  try {
    // 调用SSE流式接口
    cancelRequest = chatWithAIStream(
      {
        message: content,
        sessionId: currentSessionId.value
      },
      // onMessage
      (chunk) => {
        streamingContent.value += chunk
        scrollToBottom()
      },
      // onError
      (error) => {
        console.error('聊天错误:', error)
        const errorMsg = error.message || '服务暂时不可用'
        
        // 如果已经有内容，保存已有内容
        if (streamingContent.value) {
          messages.value.push({
            role: 'assistant',
            content: streamingContent.value,
            time: new Date()
          })
          saveCurrentSession()
        }
        
        if (errorMsg.includes('登录已过期') || errorMsg.includes('重新登录')) {
          ElMessage.warning('登录已过期，请重新登录')
          router.push('/login')
        } else if (!streamingContent.value) {
          // 只有在没有内容时才显示错误
          ElMessage.error(errorMsg)
        }
        
        isLoading.value = false
        streamingContent.value = ''
        scrollToBottom()
      },
      // onComplete
      () => {
        // 添加助手消息
        if (streamingContent.value) {
          messages.value.push({
            role: 'assistant',
            content: streamingContent.value,
            time: new Date()
          })
        }
        isLoading.value = false
        streamingContent.value = ''
        saveCurrentSession()
        scrollToBottom()
      }
    )
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败，请重试')
    isLoading.value = false
  }
}

// 发送快捷问题
function sendQuickQuestion(question) {
  inputMessage.value = question
  sendMessage()
}

// 换行
function newLine() {
  inputMessage.value += '\n'
}

// 滚动到底部
function scrollToBottom() {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 简单的Markdown渲染
function renderMarkdown(text) {
  if (!text) return ''
  return text
    // 代码块
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code class="language-$1">$2</code></pre>')
    // 行内代码
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    // 粗体
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    // 斜体
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    // 链接
    .replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank">$1</a>')
    // 换行
    .replace(/\n/g, '<br>')
    // 列表
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/(<li>.*<\/li>)/s, '<ul>$1</ul>')
    // 数字列表
    .replace(/^\d+\. (.+)$/gm, '<li>$1</li>')
}

// 保存当前会话
function saveCurrentSession() {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  if (session) {
    session.messages = [...messages.value]
  }
  saveSessions()
}

// 保存会话到本地存储
function saveSessions() {
  try {
    localStorage.setItem('tcm_chat_sessions', JSON.stringify(sessions.value))
  } catch (e) {
    console.error('保存会话失败:', e)
  }
}

// 加载会话
function loadSessions() {
  try {
    const saved = localStorage.getItem('tcm_chat_sessions')
    if (saved) {
      sessions.value = JSON.parse(saved)
      if (sessions.value.length > 0) {
        currentSessionId.value = sessions.value[0].id
        messages.value = sessions.value[0].messages || []
      } else {
        createNewSession()
      }
    } else {
      createNewSession()
    }
  } catch (e) {
    console.error('加载会话失败:', e)
    createNewSession()
  }
}

onMounted(() => {
  loadSessions()
  inputRef.value?.focus()
})
</script>


<style scoped>
.assistant-page {
  min-height: calc(100vh - 140px);
  background: var(--tcm-bg-base);
  padding: 20px;
}

.assistant-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  gap: 20px;
  height: calc(100vh - 180px);
  min-height: 600px;
}

/* 左侧会话列表 */
.session-sidebar {
  width: 260px;
  background: var(--tcm-bg-paper);
  border-radius: var(--tcm-radius-lg);
  box-shadow: var(--tcm-shadow-sm);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid var(--tcm-border-light);
}

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--tcm-text-primary);
  margin: 0 0 12px 0;
}

.new-chat-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 16px;
  background: var(--tcm-primary);
  color: #333333;
  border: none;
  border-radius: var(--tcm-radius-base);
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.new-chat-btn:hover {
  background: var(--tcm-primary-dark);
  color: #333333;
}

.session-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.session-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border-radius: var(--tcm-radius-base);
  cursor: pointer;
  transition: all 0.2s;
  color: var(--tcm-text-secondary);
}

.session-item:hover {
  background: var(--tcm-bg-hover);
}

.session-item.active {
  background: var(--tcm-primary-light);
  color: var(--tcm-primary);
}

.session-title {
  flex: 1;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.delete-btn {
  opacity: 0;
  padding: 4px;
  background: none;
  border: none;
  color: var(--tcm-text-muted);
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s;
}

.session-item:hover .delete-btn {
  opacity: 1;
}

.delete-btn:hover {
  color: var(--tcm-danger);
  background: rgba(239, 68, 68, 0.1);
}

/* 右侧聊天区域 */
.chat-main {
  flex: 1;
  background: var(--tcm-bg-paper);
  border-radius: var(--tcm-radius-lg);
  box-shadow: var(--tcm-shadow-sm);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

/* 欢迎区域 */
.welcome-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;
  padding: 40px;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  color: var(--tcm-primary);
  margin-bottom: 24px;
}

.welcome-section h2 {
  font-size: 24px;
  font-weight: 600;
  color: var(--tcm-text-primary);
  margin: 0 0 12px 0;
}

.welcome-section p {
  font-size: 15px;
  color: var(--tcm-text-secondary);
  margin: 0 0 32px 0;
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
}

.quick-questions button {
  padding: 10px 20px;
  background: var(--tcm-bg-base);
  border: 1px solid var(--tcm-border-light);
  border-radius: 20px;
  color: var(--tcm-text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.quick-questions button:hover {
  border-color: var(--tcm-primary);
  color: var(--tcm-primary);
  background: var(--tcm-primary-light);
}

/* 消息样式 */
.message-item {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--tcm-bg-base);
  color: var(--tcm-text-secondary);
}

.message-item.user .message-avatar {
  background: var(--tcm-primary);
  color: #333333;
  overflow: hidden;
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.assistant-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--tcm-primary);
}

.assistant-avatar svg {
  width: 28px;
  height: 28px;
}

.message-content {
  max-width: 70%;
}

.message-text {
  padding: 14px 18px;
  border-radius: 16px;
  font-size: 15px;
  line-height: 1.6;
  word-break: break-word;
}

.message-item.user .message-text {
  background: linear-gradient(135deg, var(--tcm-primary-light) 0%, #e8d5c4 100%);
  color: var(--tcm-text-primary);
  border-bottom-right-radius: 4px;
}

.message-item.assistant .message-text {
  background: var(--tcm-bg-base);
  color: var(--tcm-text-primary);
  border-bottom-left-radius: 4px;
}

.message-text :deep(code) {
  background: rgba(0, 0, 0, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', monospace;
  font-size: 13px;
}

.message-text :deep(pre) {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text :deep(pre code) {
  background: none;
  padding: 0;
}

.message-text :deep(strong) {
  font-weight: 600;
}

.message-text :deep(ul), .message-text :deep(ol) {
  margin: 8px 0;
  padding-left: 20px;
}

.message-text :deep(li) {
  margin: 4px 0;
}

.message-time {
  font-size: 12px;
  color: var(--tcm-text-muted);
  margin-top: 6px;
  padding: 0 4px;
}

.message-item.user .message-time {
  text-align: right;
}

/* 打字动画 */
.typing-dots {
  display: inline-flex;
  gap: 4px;
}

.typing-dots span {
  width: 8px;
  height: 8px;
  background: var(--tcm-primary);
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out both;
}

.typing-dots span:nth-child(1) { animation-delay: -0.32s; }
.typing-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区域 */
.chat-input-area {
  padding: 16px 24px 20px;
  border-top: 1px solid var(--tcm-border-light);
  background: var(--tcm-bg-paper);
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-end;
  background: var(--tcm-bg-base);
  border: 1px solid var(--tcm-border-light);
  border-radius: 12px;
  padding: 8px 12px;
  transition: all 0.2s;
}

.input-wrapper:focus-within {
  border-color: var(--tcm-primary);
  box-shadow: 0 0 0 3px var(--tcm-primary-light);
}

.input-wrapper textarea {
  flex: 1;
  border: none;
  background: none;
  resize: none;
  font-size: 15px;
  line-height: 1.5;
  color: var(--tcm-text-primary);
  max-height: 120px;
  min-height: 24px;
}

.input-wrapper textarea:focus {
  outline: none;
}

.input-wrapper textarea::placeholder {
  color: var(--tcm-text-muted);
}

.send-btn {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--tcm-primary) 0%, var(--tcm-primary-dark) 100%);
  color: #333333;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(139, 90, 43, 0.3);
}

.send-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, var(--tcm-primary-dark) 0%, #6b4423 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(139, 90, 43, 0.4);
  color: #333333;
}

.send-btn:disabled {
  background: var(--tcm-text-muted);
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(51, 51, 51, 0.3);
  border-top-color: #333333;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.input-tips {
  margin-top: 8px;
  font-size: 12px;
  color: var(--tcm-text-muted);
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .assistant-container {
    flex-direction: column;
    height: auto;
  }

  .session-sidebar {
    width: 100%;
    max-height: 200px;
  }

  .chat-main {
    min-height: 500px;
  }

  .message-content {
    max-width: 85%;
  }

  .quick-questions {
    flex-direction: column;
  }

  .quick-questions button {
    width: 100%;
  }
}
</style>
