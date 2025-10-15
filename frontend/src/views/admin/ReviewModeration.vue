<template>
  <div class="review-moderation">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>评价审核管理</h3>
          <el-radio-group v-model="filterStatus" size="small">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="published">已发布</el-radio-button>
            <el-radio-button label="deleted">已删除</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      
      <el-table :data="reviews" stripe v-loading="loading" style="width: 100%;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="restaurantName" label="餐厅名称" width="200" />
        <el-table-column label="用户" width="120">
          <template #default="{ row }">
            {{ row.user?.nickname || row.user?.username || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="评分" width="130">
          <template #default="{ row }">
            <el-rate :model-value="row.rating" disabled size="small" show-score />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <div style="display: flex; flex-direction: column; gap: 4px;">
              <el-tag v-if="row.user?.isCertified" type="success" size="small">
                ⭐ 认证
              </el-tag>
              <el-tag v-if="row.status === 0" type="danger" size="small">
                已删除
              </el-tag>
              <el-tag v-else type="success" size="small">
                已发布
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="发表时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <div style="display: flex; gap: 8px; flex-wrap: wrap;">
              <el-button
                size="small"
                type="primary"
                @click="handleViewDetail(scope.row)"
              >
                查看详情
              </el-button>
              <el-button
                v-show="scope.row.status !== 0"
                size="small"
                type="danger"
                @click="handleDeleteReview(scope.row)"
              >
                删除评价
              </el-button>
              <el-button
                v-show="scope.row.status === 0"
                size="small"
                type="success"
                @click="handlePublishReview(scope.row)"
              >
                恢复发布
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        @current-change="loadReviews"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
    
    <!-- 删除确认对话框 -->
    <el-dialog v-model="deleteDialogVisible" title="删除评价" width="400px">
      <el-form :model="deleteForm" label-width="80px">
        <el-form-item label="删除原因">
          <el-input
            v-model="deleteForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入删除原因，将通知用户"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确认删除</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { adminApi } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const reviews = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterStatus = ref('all')

const deleteDialogVisible = ref(false)
const deleteForm = ref({ reason: '', reviewId: null })

// 监听筛选状态变化
watch(filterStatus, () => {
  currentPage.value = 1
  loadReviews()
})

onMounted(() => {
  loadReviews()
})

const loadReviews = async () => {
  loading.value = true
  
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    
    if (filterStatus.value !== 'all') {
      params.status = filterStatus.value === 'published' ? 1 : 0
    }
    
    const response = await adminApi.getAllReviews(params)
    reviews.value = response.data.data || []
    total.value = response.data.pagination?.total || 0
    
  } catch (error) {
    console.error('加载评价失败:', error)
    ElMessage.error('加载评价失败')
  } finally {
    loading.value = false
  }
}

const handleDeleteReview = (row) => {
  deleteForm.value.reviewId = row.id
  deleteDialogVisible.value = true
}

const confirmDelete = async () => {
  try {
    await adminApi.deleteReview(deleteForm.value.reviewId, deleteForm.value.reason)
    ElMessage.success('评价已删除，已通知用户')
    
    // 即时更新UI，不重新加载列表
    const reviewIndex = reviews.value.findIndex(r => r.id === deleteForm.value.reviewId)
    if (reviewIndex !== -1) {
      reviews.value[reviewIndex].status = 0
      reviews.value[reviewIndex].deletedAt = new Date().toISOString()
      reviews.value[reviewIndex].deleteReason = deleteForm.value.reason
    }
    
    deleteDialogVisible.value = false
    deleteForm.value = { reason: '', reviewId: null }
    
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const formatDateTime = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const handleViewDetail = (row) => {
  const detailHtml = `
    <div style="padding: 20px;">
      <div style="margin-bottom: 15px;">
        <strong>餐厅名称：</strong>${row.restaurantName}
      </div>
      <div style="margin-bottom: 15px;">
        <strong>评分：</strong>${'⭐'.repeat(row.rating)}
      </div>
      <div style="margin-bottom: 15px;">
        <strong>评价内容：</strong>
        <p style="margin-top: 8px; line-height: 1.8; color: #606266;">${row.content}</p>
      </div>
      <div style="margin-bottom: 10px;">
        <strong>发表时间：</strong>${new Date(row.createdAt).toLocaleString('zh-CN')}
      </div>
      ${row.deletedAt ? `
      <div style="margin-top: 15px; padding: 10px; background: #FEF0F0; border-left: 3px solid #F56C6C; border-radius: 4px;">
        <div><strong>删除时间：</strong>${new Date(row.deletedAt).toLocaleString('zh-CN')}</div>
        <div><strong>删除原因：</strong>${row.deleteReason || '无'}</div>
      </div>
      ` : ''}
    </div>
  `
  
  ElMessageBox({
    title: `${row.user?.nickname || row.user?.username}的评价`,
    message: detailHtml,
    dangerouslyUseHTMLString: true,
    confirmButtonText: '关闭'
  })
}

const handlePublishReview = async (row) => {
  await ElMessageBox.confirm('确定要重新发布这条评价吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  })
  
  try {
    // TODO: 添加重新发布API（暂时模拟）
    // await adminApi.publishReview(row.id)
    
    // 即时更新UI
    const reviewIndex = reviews.value.findIndex(r => r.id === row.id)
    if (reviewIndex !== -1) {
      reviews.value[reviewIndex].status = 1
      reviews.value[reviewIndex].deletedAt = null
      reviews.value[reviewIndex].deleteReason = null
    }
    
    ElMessage.success('评价已重新发布')
    
  } catch (error) {
    ElMessage.error('操作失败')
  }
}
</script>

<style scoped>
.review-moderation :deep(.el-card) {
  border-radius: 12px;
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea20, #764ba220) border-box;
}

.review-moderation :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8f9fa, #f5f7fa);
  border-bottom: 2px solid #EBEEF5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.review-moderation :deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

.review-moderation :deep(.el-table__header-wrapper) {
  background: linear-gradient(135deg, #f5f7fa, #f0f2f5);
}

.review-moderation :deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
}

.review-moderation :deep(.el-button--danger) {
  background: linear-gradient(135deg, #F56C6C, #F78989);
  border: none;
}
</style>

