<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>TasteFinder 登录</h2>
          <p class="subtitle">发现身边的美食</p>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <div class="register-link">
            还没有账号？
            <router-link to="/register">立即注册</router-link>
          </div>
        </el-form-item>
      </el-form>
      
      <el-divider>快速登录</el-divider>
      <div class="test-accounts">
        <div class="account-tip">
          <div class="tip-title">测试账号：</div>
          <div class="account-grid">
            <div class="account-item" @click="quickLogin('admin', 'password123')">
              <el-tag type="danger" effect="dark" size="large">
                <span style="font-weight: 600;">🛠️ 管理员</span>
              </el-tag>
              <div class="account-desc">admin / password123</div>
            </div>
            <div class="account-item" @click="quickLogin('critic_a', 'password123')">
              <el-tag type="success" effect="dark" size="large">
                <span style="font-weight: 600;">⭐ 评论家</span>
              </el-tag>
              <div class="account-desc">critic_a / password123</div>
            </div>
            <div class="account-item" @click="quickLogin('user_d', 'password123')">
              <el-tag type="primary" effect="dark" size="large">
                <span style="font-weight: 600;">👤 用户</span>
              </el-tag>
              <div class="account-desc">user_d / password123</div>
            </div>
          </div>
          <div class="click-hint">💡 点击卡片快速登录</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在3-50个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 50, message: '密码长度在8-50个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      const result = await authStore.login(form.value.username, form.value.password)
      
      loading.value = false
      
      if (result.success) {
        ElMessage.success({
          message: '登录成功！欢迎回来 🎉',
          duration: 2000
        })
        setTimeout(() => {
          router.push('/')
        }, 500)
      } else {
        ElMessage.error(result.message)
      }
    }
  })
}

// 快速登录功能
const quickLogin = async (username, password) => {
  form.value.username = username
  form.value.password = password
  
  // 自动登录
  loading.value = true
  const result = await authStore.login(username, password)
  loading.value = false
  
  if (result.success) {
    ElMessage.success({
      message: `快速登录成功！欢迎 ${authStore.user?.nickname} 🎉`,
      duration: 2000
    })
    setTimeout(() => {
      router.push('/')
    }, 500)
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  border-radius: 50%;
  top: -200px;
  left: -200px;
}

.login-container::after {
  content: '';
  position: absolute;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255,255,255,0.08) 0%, transparent 70%);
  border-radius: 50%;
  bottom: -150px;
  right: -150px;
}

.login-card {
  width: 480px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  z-index: 10;
}

.card-header {
  text-align: center;
  padding: 10px 0;
}

.card-header h2 {
  margin: 0;
  font-size: 32px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  margin: 12px 0 0 0;
  color: #909399;
  font-size: 15px;
  font-weight: 500;
}

.register-link {
  text-align: center;
  width: 100%;
  font-size: 14px;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.register-link a:hover {
  color: #764ba2;
  text-decoration: underline;
}

.test-accounts {
  padding: 10px 0;
}

.account-tip {
  width: 100%;
}

.tip-title {
  text-align: center;
  color: #606266;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px;
}

.account-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}

.account-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 12px;
  background: linear-gradient(135deg, #f8f9fa, #f5f7fa);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
}

.account-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
  border-color: rgba(102, 126, 234, 0.3);
  background: linear-gradient(135deg, #ffffff, #f8f9fa);
}

.account-item .el-tag {
  cursor: pointer;
  padding: 8px 16px;
  font-size: 13px;
}

.account-desc {
  font-size: 12px;
  color: #909399;
  text-align: center;
  font-family: 'Courier New', monospace;
}

.click-hint {
  text-align: center;
  font-size: 12px;
  color: #C0C4CC;
  margin-top: 8px;
  font-style: italic;
}
</style>

