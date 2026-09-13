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
    port: 3002,
    proxy: {
      '/api/staff': {
        target: 'http://localhost:8082',
        changeOrigin: true
      }
    }
  }
})
