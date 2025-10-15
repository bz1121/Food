import axios from './axios'

export const adminApi = {
  /**
   * 获取统计数据
   */
  getStatistics() {
    return axios.get('/admin/statistics')
  },
  
  /**
   * 获取所有评价
   */
  getAllReviews(params) {
    return axios.get('/admin/reviews', { params })
  },
  
  /**
   * 获取所有用户
   */
  getAllUsers(params) {
    return axios.get('/admin/users', { params })
  },
  
  /**
   * 删除评价
   */
  deleteReview(reviewId, reason) {
    return axios.delete(`/admin/reviews/${reviewId}`, {
      params: { reason }
    })
  },
  
  /**
   * 更新用户状态
   */
  updateUserStatus(userId, status) {
    return axios.put(`/admin/users/${userId}/status`, null, {
      params: { status }
    })
  }
}

