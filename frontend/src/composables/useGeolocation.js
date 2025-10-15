import { ref } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 地理定位Composable
 */
export function useGeolocation() {
  const location = ref(null)
  const error = ref(null)
  const loading = ref(false)
  
  /**
   * 获取当前位置
   */
  const getCurrentLocation = () => {
    if (!navigator.geolocation) {
      error.value = '您的浏览器不支持定位功能'
      ElMessage.warning('您的浏览器不支持定位功能，请手动选择位置')
      return Promise.reject(error.value)
    }
    
    loading.value = true
    
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          location.value = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          }
          loading.value = false
          resolve(location.value)
        },
        (err) => {
          error.value = '获取位置失败：' + err.message
          loading.value = false
          ElMessage.error('获取位置失败，请检查权限设置或手动输入位置')
          reject(error.value)
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0
        }
      )
    })
  }
  
  return {
    location,
    error,
    loading,
    getCurrentLocation
  }
}

