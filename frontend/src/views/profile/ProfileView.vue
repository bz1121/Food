<template>
  <div class="profile-container">
    <div class="sidebar">
      <h3>个人中心</h3>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/profile/favorites">
          <el-icon><Star /></el-icon>
          <span>我的收藏</span>
        </el-menu-item>
        <el-menu-item index="/profile/reviews">
          <el-icon><ChatLineSquare /></el-icon>
          <span>我的评价</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Star, ChatLineSquare } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.profile-container {
  display: flex;
  height: 100vh;
  background: #f5f7fa;
}

.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #fff 0%, #f8f9fa 100%);
  border-right: 2px solid #EBEEF5;
  padding: 25px 20px;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
}

.sidebar h3 {
  margin-bottom: 24px;
  font-size: 20px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding-bottom: 12px;
  border-bottom: 2px solid #EBEEF5;
}

.sidebar :deep(.el-menu-item) {
  margin: 4px 0;
  border-radius: 8px;
  transition: all 0.3s;
}

.sidebar :deep(.el-menu-item:hover) {
  background: linear-gradient(135deg, #667eea10, #764ba210);
}

.sidebar :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.content {
  flex: 1;
  padding: 30px;
  background: #f5f7fa;
}
</style>

