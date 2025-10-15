import axios from './axios'

export const authApi = {
  /**
   * 用户注册
   */
  register(data) {
    return axios.post('/auth/register', data)
  },
  
  /**
   * 用户登录
   */
  login(data) {
    return axios.post('/auth/login', data)
  }
}

