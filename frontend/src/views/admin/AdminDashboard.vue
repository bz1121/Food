<template>
  <div class="admin-dashboard">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="250px">
        <div class="admin-sidebar">
          <div class="admin-logo">
            <h2>TasteFinder</h2>
            <el-tag type="danger" size="small">管理后台</el-tag>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            router
            background-color="#2c3e50"
            text-color="#ecf0f1"
            active-text-color="#3498db"
          >
            <el-menu-item index="/admin/dashboard">
              <el-icon><DataAnalysis /></el-icon>
              <span>数据概览</span>
            </el-menu-item>
            
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            
            <el-menu-item index="/admin/reviews">
              <el-icon><Document /></el-icon>
              <span>评价审核</span>
            </el-menu-item>
            
            <el-menu-item index="/admin/statistics">
              <el-icon><PieChart /></el-icon>
              <span>数据统计</span>
            </el-menu-item>
            
            <el-menu-item @click="handleBackToApp">
              <el-icon><Back /></el-icon>
              <span>返回前台</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-container>
        <el-header>
          <div class="admin-header">
            <h3>{{ pageTitle }}</h3>
            <div class="header-actions">
              <el-tag>{{ user?.roleType }}</el-tag>
              <span>{{ user?.nickname }}</span>
              <el-button size="small" @click="handleLogout">退出</el-button>
            </div>
          </div>
        </el-header>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  DataAnalysis,
  User,
  Document,
  PieChart,
  Back
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const user = computed(() => authStore.user)
const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const titles = {
    '/admin/dashboard': '数据概览',
    '/admin/users': '用户管理',
    '/admin/reviews': '评价审核',
    '/admin/statistics': '数据统计'
  }
  return titles[route.path] || '管理后台'
})

const handleBackToApp = () => {
  router.push('/')
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-dashboard {
  height: 100vh;
  background: #f5f7fa;
}

.el-container {
  height: 100%;
}

.admin-sidebar {
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  height: 100%;
  box-shadow: 2px 0 8px rgba(0,0,0,0.1);
}

.admin-logo {
  padding: 25px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  background: linear-gradient(135deg, #667eea20, #764ba220);
}

.admin-logo h2 {
  margin: 0 0 12px 0;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 26px;
  font-weight: bold;
}

.admin-logo .el-tag {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  color: white;
}

.el-menu {
  border: none;
  background: transparent;
}

.el-menu :deep(.el-menu-item) {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s;
}

.el-menu :deep(.el-menu-item:hover) {
  background: rgba(255,255,255,0.1) !important;
}

.el-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea, #764ba2) !important;
  color: white !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, rgba(255,255,255,0.98), rgba(248,249,250,0.95));
  padding: 0 30px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  backdrop-filter: blur(10px);
}

.admin-header h3 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-actions .el-tag {
  background: linear-gradient(135deg, #667eea15, #764ba215);
  border: 1px solid #667eea40;
  color: #667eea;
}

.el-main {
  padding: 24px;
  background: #f5f7fa;
}
</style>

