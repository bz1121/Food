import { defineStore } from 'pinia'
import { ref } from 'vue'
import { favoriteApi } from '@/api/favorite'
import { ElMessage } from 'element-plus'

export const useFavoriteStore = defineStore('favorite', () => {
  const favorites = ref([])
  const loading = ref(false)
  
  /**
   * 加载收藏列表
   */
  async function loadFavorites(page = 1, size = 20) {
    loading.value = true
    try {
      const response = await favoriteApi.getFavorites({ page, size })
      favorites.value = response.data.data
      return response.data
    } catch (error) {
      ElMessage.error('加载收藏列表失败')
      return { data: [], pagination: {} }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 添加收藏
   */
  async function addFavorite(poiId, notes = '') {
    try {
      const response = await favoriteApi.addFavorite({ poiId, notes })
      ElMessage.success('收藏成功')
      return { success: true, data: response.data }
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '收藏失败')
      return { success: false }
    }
  }
  
  /**
   * 取消收藏
   */
  async function removeFavorite(favoriteId) {
    try {
      await favoriteApi.removeFavorite(favoriteId)
      favorites.value = favorites.value.filter(f => f.id !== favoriteId)
      ElMessage.success('已取消收藏')
      return { success: true }
    } catch (error) {
      ElMessage.error('取消收藏失败')
      return { success: false }
    }
  }
  
  /**
   * 检查是否已收藏
   */
  function isFavorited(poiId) {
    return favorites.value.some(f => f.poiId === poiId)
  }
  
  return {
    favorites,
    loading,
    loadFavorites,
    addFavorite,
    removeFavorite,
    isFavorited
  }
})

