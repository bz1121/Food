import axios from './axios'

export const favoriteApi = {
  /**
   * 获取收藏列表
   */
  getFavorites(params) {
    return axios.get('/favorites', { params })
  },
  
  /**
   * 添加收藏
   */
  addFavorite(data) {
    return axios.post('/favorites', data)
  },
  
  /**
   * 取消收藏
   */
  removeFavorite(favoriteId) {
    return axios.delete(`/favorites/${favoriteId}`)
  }
}

