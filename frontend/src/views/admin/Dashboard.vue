<template>
  <div class="admin-dashboard-content">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon" color="#409EFF"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalUsers }}</div>
              <div class="stat-label">总用户数</div>
              <div class="stat-extra">今日新增: {{ stats.todayUsers }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon" color="#67C23A"><Star /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalReviews }}</div>
              <div class="stat-label">总评价数</div>
              <div class="stat-extra">已发布: {{ stats.publishedReviews }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon" color="#E6A23C"><Collection /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalFavorites }}</div>
              <div class="stat-label">总收藏数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon" color="#F56C6C"><Warning /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.pendingReviews }}</div>
              <div class="stat-label">待审核评价</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h4>最近评价</h4>
          </template>
          <el-empty description="暂无数据" />
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <h4>用户增长趋势</h4>
          </template>
          <el-empty description="图表开发中" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'
import { User, Star, Collection, Warning } from '@element-plus/icons-vue'

const stats = ref({
  totalUsers: 0,
  todayUsers: 0,
  totalReviews: 0,
  publishedReviews: 0,
  totalFavorites: 0,
  pendingReviews: 0
})

onMounted(async () => {
  await loadStatistics()
})

const loadStatistics = async () => {
  try {
    const response = await adminApi.getStatistics()
    stats.value = response.data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}
</script>

<style scoped>
.stat-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 12px;
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea20, #764ba220) border-box;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
  border-color: rgba(102, 126, 234, 0.3);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  font-size: 48px;
  padding: 12px;
  background: linear-gradient(135deg, #667eea10, #764ba210);
  border-radius: 12px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-top: 8px;
  font-weight: 500;
}

.stat-extra {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.admin-dashboard-content :deep(.el-card) {
  border-radius: 12px;
  border: 1px solid #EBEEF5;
}

.admin-dashboard-content :deep(.el-card__header) {
  background: linear-gradient(135deg, #f5f7fa, #f0f2f5);
  border-bottom: 2px solid #EBEEF5;
}

.admin-dashboard-content :deep(.el-card__header h4) {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>

