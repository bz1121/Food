<template>
  <div class="navigation-panel">
    <div class="panel-header">
      <h4>路径规划</h4>
      <el-button text @click="$emit('close')">
        <el-icon><Close /></el-icon>
      </el-button>
    </div>
    
    <div class="travel-modes">
      <el-radio-group v-model="travelMode" @change="updateRoute">
        <el-radio-button label="driving">
          驾车
        </el-radio-button>
        <el-radio-button label="walking">
          步行
        </el-radio-button>
        <el-radio-button label="transit">
          公交
        </el-radio-button>
      </el-radio-group>
    </div>
    
    <div v-if="route && !loading" class="route-info">
      <div class="route-summary">
        <div class="info-item">
          <span class="label">距离</span>
          <span class="value">{{ (route.distance / 1000).toFixed(1) }} 公里</span>
        </div>
        <div class="info-item">
          <span class="label">预计时间</span>
          <span class="value">{{ formatDuration(route.duration) }}</span>
        </div>
      </div>
      
      <el-divider />
      
      <div class="route-steps">
        <div
          v-for="(step, index) in route.steps"
          :key="index"
          class="step-item"
        >
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-content">
            <p class="instruction">{{ step.instruction }}</p>
            <p class="step-meta">
              {{ (step.distance / 1000).toFixed(1) }}km · 约{{ Math.ceil(step.duration / 60) }}分钟
            </p>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="loading">
      <el-icon class="is-loading"><Loading /></el-icon>
      <p>正在规划路径...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { navigationApi } from '@/api/navigation'
import { ElMessage } from 'element-plus'
import { Close, Van, Loading } from '@element-plus/icons-vue'

const props = defineProps({
  origin: {
    type: Object,
    required: true
  },
  destination: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'routeCalculated'])

const travelMode = ref('driving')
const route = ref(null)
const loading = ref(false)

onMounted(async () => {
  await updateRoute()
})

const updateRoute = async () => {
  loading.value = true
  
  try {
    const response = await navigationApi.getRoute({
      originLat: props.origin.latitude,
      originLon: props.origin.longitude,
      destLat: props.destination.latitude,
      destLon: props.destination.longitude,
      travelMode: travelMode.value
    })
    
    route.value = response.data
    emit('routeCalculated', response.data)
    
  } catch (error) {
    console.error('路径规划失败:', error)
    ElMessage.error('路径规划失败，请重试')
  } finally {
    loading.value = false
  }
}

const formatDuration = (seconds) => {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.ceil((seconds % 3600) / 60)
  
  if (hours > 0) {
    return `${hours}小时${minutes}分钟`
  }
  return `${minutes}分钟`
}
</script>

<style scoped>
.navigation-panel {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.panel-header h4 {
  margin: 0;
}

.travel-modes {
  margin-bottom: 20px;
}

.route-info {
  margin-top: 20px;
}

.route-summary {
  display: flex;
  gap: 30px;
}

.info-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.value {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.route-steps {
  max-height: 400px;
  overflow-y: auto;
}

.step-item {
  display: flex;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #EBEEF5;
}

.step-item:last-child {
  border-bottom: none;
}

.step-number {
  width: 30px;
  height: 30px;
  background: #409EFF;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.instruction {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 14px;
}

.step-meta {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.loading {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.loading .el-icon {
  font-size: 32px;
  margin-bottom: 10px;
}
</style>

