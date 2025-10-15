import axios from './axios'

export const reviewApi = {
  /**
   * 获取餐厅评价列表
   */
  getReviews(params) {
    return axios.get('/reviews', { params })
  },
  
  /**
   * 发表评价
   */
  createReview(data) {
    return axios.post('/reviews', data)
  },
  
  /**
   * 获取我的评价列表
   */
  getMyReviews(params) {
    return axios.get('/reviews/my', { params })
  },
  
  /**
   * 删除评价
   */
  deleteReview(reviewId) {
    return axios.delete(`/reviews/${reviewId}`)
  }
}

