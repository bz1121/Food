import axios from './axios'

export const restaurantApi = {
  /**
   * 搜索附近餐厅
   */
  searchNearby(params) {
    return axios.get('/restaurants/search', { params })
  },
  
  /**
   * 获取餐厅详情
   */
  getDetail(poiId) {
    return axios.get(`/restaurants/${poiId}`)
  }
}

