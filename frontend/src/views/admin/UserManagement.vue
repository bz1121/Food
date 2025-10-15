<template>
  <el-card>
    <template #header>
      <h3>用户管理</h3>
    </template>
    
    <el-table :data="users" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column label="角色" width="150">
        <template #default="{ row }">
          <el-tag v-if="row.roleType === 'ADMIN'" type="danger">管理员</el-tag>
          <el-tag v-else-if="row.roleType.includes('CRITIC')" type="success">评论家</el-tag>
          <el-tag v-else-if="row.roleType === 'MERCHANT'" type="warning">商家</el-tag>
          <el-tag v-else>{{ row.roleType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="最后登录" width="180">
        <template #default="{ row }">
          {{ row.lastLoginAt ? formatDate(row.lastLoginAt) : '从未登录' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success" size="small">正常</el-tag>
          <el-tag v-else type="danger" size="small">已禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <template v-if="row.roleType !== 'ADMIN'">
            <el-button
              v-if="row.status === 1"
              size="small"
              type="danger"
              @click="handleToggleUserStatus(row, 0)"
            >
              禁用账户
            </el-button>
            <el-button
              v-else
              size="small"
              type="success"
              @click="handleToggleUserStatus(row, 1)"
            >
              启用账户
            </el-button>
          </template>
          <el-tag v-else type="info" size="small">管理员</el-tag>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadUsers"
      style="margin-top: 20px; justify-content: center;"
    />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

onMounted(() => {
  loadUsers()
})

const loadUsers = async () => {
  loading.value = true
  
  try {
    const response = await adminApi.getAllUsers({
      page: currentPage.value,
      size: pageSize.value
    })
    
    users.value = response.data.data || []
    total.value = response.data.pagination?.total || 0
    
  } catch (error) {
    console.error('加载用户失败:', error)
    ElMessage.error('加载用户失败')
  } finally {
    loading.value = false
  }
}

const handleToggleUserStatus = async (user, newStatus) => {
  const action = newStatus === 0 ? '禁用' : '启用'
  
  await ElMessageBox.confirm(
    `确定要${action}用户 ${user.username} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: newStatus === 0 ? 'warning' : 'info'
    }
  )
  
  try {
    await adminApi.updateUserStatus(user.id, newStatus)
    ElMessage.success(`用户已${action}`)
    
    // 更新本地数据，避免重新加载整个列表
    const userIndex = users.value.findIndex(u => u.id === user.id)
    if (userIndex !== -1) {
      users.value[userIndex].status = newStatus
    }
    
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('zh-CN')
}
</script>

<style scoped>
.el-card {
  border-radius: 12px;
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea20, #764ba220) border-box;
}

.el-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8f9fa, #f5f7fa);
  border-bottom: 2px solid #EBEEF5;
}

.el-card :deep(h3) {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.el-card :deep(.el-table) {
  border-radius: 8px;
}

.el-card :deep(.el-table__header-wrapper) {
  background: linear-gradient(135deg, #f5f7fa, #f0f2f5);
}

.el-card :deep(.el-button--danger) {
  background: linear-gradient(135deg, #F56C6C, #F78989);
  border: none;
}
</style>

