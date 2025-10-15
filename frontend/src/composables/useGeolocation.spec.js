import { describe, it, expect, beforeEach, vi } from 'vitest'
import { useGeolocation } from './useGeolocation'

describe('useGeolocation', () => {
  beforeEach(() => {
    // Mock navigator.geolocation
    global.navigator.geolocation = {
      getCurrentPosition: vi.fn()
    }
  })
  
  it('应该正确初始化', () => {
    const { location, error, loading } = useGeolocation()
    
    expect(location.value).toBeNull()
    expect(error.value).toBeNull()
    expect(loading.value).toBe(false)
  })
  
  it('定位成功应该返回坐标', async () => {
    const mockPosition = {
      coords: {
        latitude: 39.908823,
        longitude: 116.397470
      }
    }
    
    global.navigator.geolocation.getCurrentPosition.mockImplementation((success) => {
      success(mockPosition)
    })
    
    const { getCurrentLocation, location } = useGeolocation()
    
    await getCurrentLocation()
    
    expect(location.value).toEqual({
      latitude: 39.908823,
      longitude: 116.397470
    })
  })
  
  it('浏览器不支持定位应该返回错误', async () => {
    global.navigator.geolocation = undefined
    
    const { getCurrentLocation, error } = useGeolocation()
    
    try {
      await getCurrentLocation()
    } catch (e) {
      expect(error.value).toBeTruthy()
    }
  })
})

