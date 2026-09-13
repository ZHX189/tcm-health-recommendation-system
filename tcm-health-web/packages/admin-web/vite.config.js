import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      'shared': resolve(__dirname, '../../shared')
    }
  },
  server: {
    port: 3003,
    proxy: {
      '/api/admin': {
        target: 'http://localhost:8083',
        changeOrigin: true
      }
    }
  }
})
