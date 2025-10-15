import axios from './axios'

export const navigationApi = {
  /**
   * 获取导航路径
   */
  getRoute(params) {
    return axios.get('/navigation/route', { params })
  }
}

