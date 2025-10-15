import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import LoginView from './LoginView.vue'
import ElementPlus from 'element-plus'

describe('LoginView', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
  
  it('应该正确渲染登录表单', () => {
    const wrapper = mount(LoginView, {
      global: {
        plugins: [ElementPlus],
        stubs: {
          'router-link': true
        }
      }
    })
    
    expect(wrapper.find('input[type="text"]').exists()).toBe(true)
    expect(wrapper.find('input[type="password"]').exists()).toBe(true)
    expect(wrapper.find('button').exists()).toBe(true)
  })
  
  it('用户名为空时应该显示验证错误', async () => {
    const wrapper = mount(LoginView, {
      global: {
        plugins: [ElementPlus],
        stubs: {
          'router-link': true
        }
      }
    })
    
    // TODO: 添加表单验证测试逻辑
  })
})

