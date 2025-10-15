import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from './auth'

describe('Auth Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })
  
  it('初始状态应该是未认证', () => {
    const store = useAuthStore()
    
    expect(store.token).toBeNull()
    expect(store.user).toBeNull()
    expect(store.isAuthenticated).toBe(false)
  })
  
  it('登录成功后应该保存token和用户信息', async () => {
    const store = useAuthStore()
    
    // Mock API响应
    vi.mock('@/api/auth', () => ({
      authApi: {
        login: vi.fn().mockResolvedValue({
          data: {
            token: 'test-token',
            user: { id: 1, username: 'testuser' }
          }
        })
      }
    }))
    
    // TODO: 实现完整的登录测试
  })
  
  it('退出登录应该清除token和用户信息', () => {
    const store = useAuthStore()
    
    // 设置初始状态
    store.token = 'test-token'
    store.user = { id: 1, username: 'testuser' }
    localStorage.setItem('token', 'test-token')
    
    // 退出登录
    store.logout()
    
    // 验证
    expect(store.token).toBeNull()
    expect(store.user).toBeNull()
    expect(localStorage.getItem('token')).toBeNull()
  })
})

