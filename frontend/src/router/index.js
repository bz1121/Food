import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/auth/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/auth/RegisterView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      name: 'map',
      component: () => import('../views/map/MapView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/profile/ProfileView.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: 'favorites',
          name: 'favorites',
          component: () => import('../views/profile/FavoritesView.vue')
        },
        {
          path: 'reviews',
          name: 'reviews',
          component: () => import('../views/profile/MyReviewsView.vue')
        }
      ]
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/AdminDashboard.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'dashboard',
          name: 'admin-dashboard',
          component: () => import('../views/admin/Dashboard.vue')
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('../views/admin/UserManagement.vue')
        },
        {
          path: 'reviews',
          name: 'admin-reviews',
          component: () => import('../views/admin/ReviewModeration.vue')
        },
        {
          path: 'statistics',
          name: 'admin-statistics',
          component: () => import('../views/admin/Statistics.vue')
        }
      ]
    }
  ]
})

// 路由守卫：检查认证状态和权限
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresAdmin) {
    // 检查管理员权限
    if (authStore.user?.roleType !== 'ADMIN') {
      alert('访问被拒绝：需要管理员权限')
      next('/')
    } else {
      next()
    }
  } else if (!to.meta.requiresAuth && authStore.isAuthenticated && to.path !== '/') {
    next('/')
  } else {
    next()
  }
})

export default router

