<template>
  <div class="favorites-view">
    <el-page-header @back="goBack">
      <template #content>
        <span class="page-title">我的收藏</span>
      </template>
    </el-page-header>
    
    <div class="favorites-content">
      <el-empty v-if="favorites.length === 0 && !loading" description="暂无收藏" />
      
      <el-row :gutter="20" v-loading="loading">
        <el-col
          v-for="favorite in favorites"
          :key="favorite.id"
          :span="8"
          :xs="24"
          :sm="12"
          :md="8"
        >
          <el-card class="favorite-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span class="restaurant-name">{{ favorite.restaurantName }}</span>
                <el-button
                  type="danger"
                  size="small"
                  :icon="Delete"
                  circle
                  @click="handleRemove(favorite.id)"
                />
              </div>
            </template>
            
            <div class="favorite-info">
              <p class="address">
                <el-icon><LocationFilled /></el-icon>
                {{ favorite.address }}
              </p>
              
              <div class="meta">
                <el-rate v-model="favorite.rating" disabled show-score />
                <el-tag v-if="favorite.category" size="small">{{ favorite.category }}</el-tag>
              </div>
              
              <p v-if="favorite.notes" class="notes">
                <el-icon><Memo /></el-icon>
                {{ favorite.notes }}
              </p>
              
              <p class="time">
                收藏时间：{{ formatDate(favorite.createdAt) }}
              </p>
            </div>
            
            <template #footer>
              <el-button type="primary" size="small" @click="viewOnMap(favorite)">
                <el-icon><Location /></el-icon>
                在地图上查看
              </el-button>
            </template>
          </el-card>
        </el-col>
      </el-row>
      
      <el-pagination
        v-if="pagination.total > 0"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="pagination.total"
        layout="total, prev, pager, next"
        @current-change="loadFavorites"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useFavoriteStore } from '@/stores/favorite'
import { ElMessageBox } from 'element-plus'
import { Delete, LocationFilled, Location, Memo } from '@element-plus/icons-vue'

const router = useRouter()
const favoriteStore = useFavoriteStore()

const favorites = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const pagination = ref({})

onMounted(async () => {
  await loadFavorites()
})

const loadFavorites = async () => {
  loading.value = true
  const result = await favoriteStore.loadFavorites(currentPage.value, pageSize.value)
  favorites.value = result.data
  pagination.value = result.pagination
  loading.value = false
}

const handleRemove = async (favoriteId) => {
  await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  const result = await favoriteStore.removeFavorite(favoriteId)
  if (result.success) {
    favorites.value = favorites.value.filter(f => f.id !== favoriteId)
  }
}

const viewOnMap = (favorite) => {
  // 跳转到地图并定位到该餐厅
  router.push({
    name: 'map',
    query: {
      lat: favorite.latitude,
      lon: favorite.longitude,
      poiId: favorite.poiId
    }
  })
}

const goBack = () => {
  router.back()
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.favorites-view {
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

.favorites-content {
  margin-top: 0;
}

.favorite-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea20, #764ba220) border-box;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.favorite-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
}

.favorite-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8f9fa, #f5f7fa);
  border-bottom: 2px solid #EBEEF5;
}

.favorite-card :deep(.el-card__footer) {
  background: linear-gradient(135deg, #f9fafb, #f5f7fa);
  border-top: 2px solid #EBEEF5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.restaurant-name {
  font-size: 17px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.favorite-info {
  min-height: 150px;
  padding: 8px 0;
}

.address {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  margin-bottom: 12px;
  font-size: 14px;
}

.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-top: 8px;
  border-top: 1px dashed #EBEEF5;
}

.notes {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  color: #909399;
  font-size: 13px;
  margin: 12px 0;
  font-style: italic;
  padding: 8px 12px;
  background: #F5F7FA;
  border-radius: 6px;
  border-left: 3px solid #E6A23C;
}

.time {
  font-size: 12px;
  color: #C0C4CC;
  margin-top: 12px;
}

.el-pagination {
  margin-top: 24px;
  justify-content: center;
}

.favorite-card :deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
}

.favorite-card :deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
}
</style>
