<template>
  <div class="review-list">
    <div class="review-header">
      <h3>用户评价 ({{ total }})</h3>
      <el-button type="primary" size="small" @click="$emit('write-review')">
        写评价
      </el-button>
    </div>
    
    <el-empty v-if="reviews.length === 0 && !loading" description="暂无评价，来写第一条吧！" />
    
    <div v-loading="loading">
      <div v-for="review in reviews" :key="review.id" class="review-item">
        <div class="review-user">
          <el-avatar :size="40">
            {{ review.user?.nickname?.charAt(0) || 'U' }}
          </el-avatar>
          <div class="user-info">
            <div class="username">
              {{ review.user?.nickname || review.user?.username }}
              <el-tag v-if="review.user?.isCertified" type="success" size="small" class="certified-badge">
                <el-icon><StarFilled /></el-icon>
                认证评论家
              </el-tag>
            </div>
            <div class="review-meta">
              <el-rate v-model="review.rating" disabled size="small" />
              <span class="review-time">{{ formatDate(review.createdAt) }}</span>
            </div>
          </div>
        </div>
        
        <div class="review-content">
          {{ review.content }}
        </div>
        
        <div v-if="review.images && review.images.length > 0" class="review-images">
          <el-image
            v-for="(img, index) in review.images"
            :key="index"
            :src="img"
            :preview-src-list="review.images"
            fit="cover"
            style="width: 100px; height: 100px; margin-right: 8px; border-radius: 4px;"
          />
        </div>
        
        <div class="review-actions">
          <el-button text size="small">
            <el-icon><Promotion /></el-icon>
            有用 ({{ review.helpfulCount || 0 }})
          </el-button>
        </div>
      </div>
    </div>
    
    <el-pagination
      v-if="total > 0"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="$emit('page-change', $event)"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { StarFilled, Promotion } from '@element-plus/icons-vue'

defineProps({
  reviews: {
    type: Array,
    default: () => []
  },
  total: {
    type: Number,
    default: 0
  },
  loading: {
    type: Boolean,
    default: false
  }
})

defineEmits(['write-review', 'page-change'])

const currentPage = ref(1)
const pageSize = ref(10)

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.review-list {
  padding: 20px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #EBEEF5;
}

.review-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.review-item {
  padding: 20px;
  margin-bottom: 15px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #EBEEF5;
  transition: all 0.3s;
}

.review-item:hover {
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.review-user {
  display: flex;
  gap: 12px;
  margin-bottom: 15px;
}

.user-info {
  flex: 1;
}

.username {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.certified-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.review-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.review-time {
  font-size: 12px;
  color: #909399;
}

.review-content {
  line-height: 1.8;
  color: #606266;
  margin-bottom: 15px;
}

.review-images {
  margin: 15px 0;
  display: flex;
  flex-wrap: wrap;
}

.review-actions {
  display: flex;
  gap: 15px;
  padding-top: 10px;
  border-top: 1px solid #F2F6FC;
}

.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>

