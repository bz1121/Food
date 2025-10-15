<template>
  <div class="review-form">
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="评分" prop="rating">
        <el-rate
          v-model="form.rating"
          :colors="['#F56C6C', '#E6A23C', '#67C23A']"
          show-text
          :texts="['极差', '失望', '一般', '满意', '惊喜']"
        />
      </el-form-item>
      
      <el-form-item label="评价内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="8"
          placeholder="分享你的用餐体验吧！至少10字，最多2000字"
          show-word-limit
          maxlength="2000"
        />
        <div class="word-count" :class="{ 'text-danger': form.content.length < 10 }">
          {{ form.content.length }}/2000 字
          <span v-if="form.content.length < 10">（至少需要10字）</span>
        </div>
      </el-form-item>
      
      <el-form-item label="上传图片">
        <el-upload
          v-model:file-list="fileList"
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :limit="9"
          :on-exceed="handleExceed"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
        <div class="upload-tip">最多上传9张图片</div>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          发表评价
        </el-button>
        <el-button @click="handlePreview">预览</el-button>
        <el-button @click="$emit('cancel')">取消</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 预览对话框 -->
    <el-dialog v-model="previewVisible" title="评价预览" width="500px">
      <div class="preview-content">
        <div class="preview-rating">
          <el-rate v-model="form.rating" disabled show-score />
        </div>
        <div class="preview-text">
          {{ form.content }}
        </div>
        <div v-if="fileList.length > 0" class="preview-images">
          <el-image
            v-for="(file, index) in fileList"
            :key="index"
            :src="file.url"
            fit="cover"
            style="width: 100px; height: 100px; margin: 5px;"
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  restaurantInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['submit', 'cancel'])

const formRef = ref(null)
const loading = ref(false)
const previewVisible = ref(false)
const fileList = ref([])

const form = ref({
  rating: 5,
  content: ''
})

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 10, max: 2000, message: '评价内容长度在10-2000字之间', trigger: 'blur' }
  ]
}

const handleExceed = () => {
  ElMessage.warning('最多只能上传9张图片')
}

const handlePreview = () => {
  if (!form.value.content || form.value.content.length < 10) {
    ElMessage.warning('请先完成评价内容（至少10字）')
    return
  }
  previewVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        const reviewData = {
          poiId: props.restaurantInfo.poiId,
          restaurantName: props.restaurantInfo.name,
          rating: form.value.rating,
          content: form.value.content,
          images: fileList.value.map(f => f.url || f.response?.url).filter(Boolean)
        }
        
        emit('submit', reviewData)
        
      } catch (error) {
        ElMessage.error('提交失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.review-form {
  padding: 20px 0;
}

.word-count {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
}

.word-count.text-danger {
  color: #F56C6C;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.preview-content {
  padding: 20px;
}

.preview-rating {
  margin-bottom: 15px;
}

.preview-text {
  line-height: 1.8;
  color: #606266;
  margin-bottom: 15px;
}

.preview-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>

