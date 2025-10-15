<template>
  <div class="my-reviews-view">
    <el-page-header @back="goBack">
      <template #content>
        <span class="page-title">我的评价</span>
      </template>
    </el-page-header>
    
    <div class="reviews-content">
      <el-empty v-if="reviews.length === 0 && !loading" description="暂无评价" />
      
      <div v-loading="loading">
        <el-card
          v-for="review in reviews"
          :key="review.id"
          class="review-card"
          shadow="hover"
        >
          <div class="review-header">
            <div>
              <h3>{{ review.restaurantName }}</h3>
              <el-rate v-model="review.rating" disabled show-score />
            </div>
            <el-button
              type="danger"
              size="small"
              :icon="Delete"
              @click="handleDelete(review.id)"
            >
              删除
            </el-button>
          </div>
          
          <div class="review-content">
            <p>{{ review.content }}</p>
          </div>
          
          <div class="review-footer">
            <span class="time">发表于 {{ formatDate(review.createdAt) }}</span>
            <div class="stats">
              <el-tag v-if="review.user?.isCertified" type="success" size="small">
                <el-icon><StarFilled /></el-icon>
                认证评论家
              </el-tag>
              <span>
                <el-icon><Promotion /></el-icon>
                {{ review.helpfulCount }} 人觉得有用
              </span>
            </div>
          </div>
        </el-card>
      </div>
      
      <el-pagination
        v-if="pagination.total > 0"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="pagination.total"
        layout="total, prev, pager, next"
        @current-change="loadReviews"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { reviewApi } from '@/api/review'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, StarFilled, Promotion } from '@element-plus/icons-vue'

const router = useRouter()

const reviews = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const pagination = ref({})

onMounted(async () => {
  await loadReviews()
})

const loadReviews = async () => {
  loading.value = true
  try {
    const response = await reviewApi.getMyReviews({
      page: currentPage.value,
      size: pageSize.value
    })
    reviews.value = response.data.data
    pagination.value = response.data.pagination
  } catch (error) {
    ElMessage.error('加载评价列表失败')
  } finally {
    loading.value = false
  }
}

const handleDelete = async (reviewId) => {
  await ElMessageBox.confirm('确定要删除这条评价吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  try {
    await reviewApi.deleteReview(reviewId)
    ElMessage.success('评价已删除')
    reviews.value = reviews.value.filter(r => r.id !== reviewId)
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const goBack = () => {
  router.back()
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('zh-CN')
}
</script>

<style scoped>
.my-reviews-view {
  padding: 0;
}

.el-page-header {
  padding: 20px;
  background: linear-gradient(135deg, #fff, #f8f9fa);
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.reviews-content {
  margin-top: 0;
}

.review-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea20, #764ba220) border-box;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.review-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #EBEEF5;
}

.review-header h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.review-content {
  padding: 16px 0;
  line-height: 1.8;
  color: #606266;
  font-size: 14px;
}

.review-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 2px dashed #EBEEF5;
}

.time {
  font-size: 13px;
  color: #909399;
}

.stats {
  display: flex;
  gap: 16px;
  align-items: center;
  font-size: 14px;
  color: #909399;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 4px 10px;
  background: linear-gradient(135deg, #f5f7fa, #f0f2f5);
  border-radius: 12px;
}

.el-pagination {
  margin-top: 24px;
  justify-content: center;
}

.review-card :deep(.el-button--danger) {
  background: linear-gradient(135deg, #F56C6C, #F78989);
  border: none;
  border-radius: 8px;
}
</style>
