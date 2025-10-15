import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  
  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const isCritic = computed(() => {
    return user.value?.roleType === 'FOOD_CRITIC_A' || 
           user.value?.roleType === 'FOOD_CRITIC_B'
  })
  
  // Actions
  async function login(username, password) {
    try {
      const response = await authApi.login({ username, password })
      token.value = response.data.token
      user.value = response.data.user
      
      // 保存到localStorage
      localStorage.setItem('token', token.value)
      localStorage.setItem('user', JSON.stringify(user.value))
      
      return { success: true }
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '登录失败'
      }
    }
  }
  
  async function register(userData) {
    try {
      const response = await authApi.register(userData)
      token.value = response.data.token
      user.value = response.data.user
      
      // 保存到localStorage
      localStorage.setItem('token', token.value)
      localStorage.setItem('user', JSON.stringify(user.value))
      
      return { success: true }
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '注册失败'
      }
    }
  }
  
  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
  
  return {
    token,
    user,
    isAuthenticated,
    isCritic,
    login,
    register,
    logout
  }
})

