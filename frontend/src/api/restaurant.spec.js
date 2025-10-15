import { describe, it, expect, beforeEach, vi } from 'vitest'
import { restaurantApi } from './restaurant'

describe('Restaurant API', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })
  
  it('应该有searchNearby方法', () => {
    expect(typeof restaurantApi.searchNearby).toBe('function')
  })
  
  it('应该有getDetail方法', () => {
    expect(typeof restaurantApi.getDetail).toBe('function')
  })
  
  // TODO: 添加更多API测试
})

