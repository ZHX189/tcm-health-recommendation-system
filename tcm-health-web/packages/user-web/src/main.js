/**
 * 用户端入口文件
 * @author Ti
 * @since 2026-02-03
 */

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router/index.js'
import { initIcons } from 'shared/components/index.js'
import SvgIcon from 'shared/components/SvgIcon.vue'
import 'shared/styles/index.css'
import './styles/user.css'

initIcons()

const app = createApp(App)
app.component('SvgIcon', SvgIcon)
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn, size: 'default' })
app.mount('#app')
