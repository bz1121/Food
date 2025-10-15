<template>
  <div class="map-container">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <div class="logo">TasteFinder</div>
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索餐厅、菜系..."
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
      <div class="user-menu">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32">{{ user?.nickname?.charAt(0) }}</el-avatar>
            <span>{{ user?.nickname }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
              <el-dropdown-item command="reviews">我的评价</el-dropdown-item>
              <el-dropdown-item v-if="user?.roleType === 'ADMIN'" divided command="admin">
                <span style="color: #E6A23C;">🛠️ 管理后台</span>
              </el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    
    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左侧餐厅列表 -->
      <div class="restaurant-list">
        <div class="list-header">
          <h3>附近餐厅 ({{ displayedRestaurants.length }}{{ restaurants.length > 50 ? '/'+restaurants.length : '' }})</h3>
          <el-button size="small" @click="relocate">
            <el-icon><LocationInformation /></el-icon>
            重新定位
          </el-button>
        </div>
        
        <el-scrollbar height="calc(100vh - 140px)">
          <div class="list-content">
            <div
              v-for="restaurant in displayedRestaurants"
              :key="restaurant.poiId"
              class="restaurant-item"
              @click="selectRestaurant(restaurant)"
            >
              <div class="restaurant-info">
                <h4>{{ restaurant.name }}</h4>
                <p class="address">{{ restaurant.address }}</p>
                <div class="meta">
                  <el-rate v-model="restaurant.rating" disabled show-score text-color="#ff9900" />
                  <span class="distance">{{ (restaurant.distance / 1000).toFixed(1) }}km</span>
                </div>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
      
      <!-- 右侧地图 -->
      <div class="map-area">
        <div id="amap-container" ref="mapContainer"></div>
        
        <!-- 搜索半径选择器 -->
        <div class="radius-selector">
          <div style="margin-bottom: 8px; font-size: 12px; color: #909399;">搜索半径</div>
          <el-radio-group v-model="searchRadius" @change="handleRadiusChange" size="small">
            <el-radio-button :label="1000">1km</el-radio-button>
            <el-radio-button :label="3000">3km</el-radio-button>
            <el-radio-button :label="5000">5km</el-radio-button>
            <el-radio-button :label="10000">10km</el-radio-button>
          </el-radio-group>
        </div>
      </div>
    </div>
    
    <!-- 餐厅详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="selectedRestaurant?.name"
      width="800px"
      destroy-on-close
    >
      <el-tabs v-if="selectedRestaurant" v-model="activeTab">
        <!-- 餐厅详情Tab -->
        <el-tab-pane label="餐厅详情" name="detail">
          <div class="restaurant-detail">
            <div class="detail-section">
              <p><strong>地址：</strong>{{ selectedRestaurant.address }}</p>
              <p v-if="selectedRestaurant.phone"><strong>电话：</strong>{{ selectedRestaurant.phone }}</p>
              <p><strong>类型：</strong>{{ selectedRestaurant.category }}</p>
              <p><strong>距离：</strong>{{ (selectedRestaurant.distance / 1000).toFixed(2) }}公里</p>
            </div>
            
            <div class="actions">
              <el-button type="primary" @click="handleNavigate">
                <el-icon><Location /></el-icon>
                导航
              </el-button>
              <el-button 
                :type="selectedRestaurant.isFavorited ? 'warning' : 'default'"
                @click="handleToggleFavorite"
              >
                <el-icon><Star :style="{ color: selectedRestaurant.isFavorited ? '#F7BA2A' : '' }" /></el-icon>
                {{ selectedRestaurant.isFavorited ? '取消收藏' : '收藏' }}
              </el-button>
            </div>
          </div>
        </el-tab-pane>
        
        <!-- 评价列表Tab -->
        <el-tab-pane label="用户评价" name="reviews">
          <ReviewList
            :reviews="restaurantReviews"
            :total="reviewTotal"
            :loading="reviewsLoading"
            @write-review="handleShowReviewForm"
            @page-change="loadRestaurantReviews"
          />
        </el-tab-pane>
        
        <!-- 发表评价Tab -->
        <el-tab-pane label="发表评价" name="write-review">
          <ReviewForm
            v-if="activeTab === 'write-review'"
            :restaurant-info="selectedRestaurant"
            @submit="handleSubmitReview"
            @cancel="activeTab = 'reviews'"
          />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useGeolocation } from '@/composables/useGeolocation'
import { restaurantApi } from '@/api/restaurant'
import { ElMessage, ElLoading, ElMessageBox } from 'element-plus'
import { Search, Location, LocationInformation, Star, ChatLineSquare } from '@element-plus/icons-vue'
import AMapLoader from '@amap/amap-jsapi-loader'
import NavigationPanel from '@/components/navigation/NavigationPanel.vue'
import ReviewList from '@/components/review/ReviewList.vue'
import ReviewForm from '@/components/review/ReviewForm.vue'
import { useFavoriteStore } from '@/stores/favorite'
import { reviewApi } from '@/api/review'
import { favoriteApi } from '@/api/favorite'

const router = useRouter()
const authStore = useAuthStore()
const favoriteStore = useFavoriteStore()
const { location: userLocation, getCurrentLocation } = useGeolocation()

const user = computed(() => authStore.user)

// 地图相关
const mapContainer = ref(null)
const map = ref(null)
const markers = ref([])

// 餐厅数据
const restaurants = ref([])
const selectedRestaurant = ref(null)
const detailDialogVisible = ref(false)

// 性能优化：限制显示数量
const displayedRestaurants = computed(() => restaurants.value.slice(0, 50))

// 搜索相关
const searchKeyword = ref('')
const searchRadius = ref(5000)

// 导航相关
const showNavigation = ref(false)
const routeLine = ref(null)
const drivingInstance = ref(null)

// 评价相关
const activeTab = ref('detail')
const showReviewForm = ref(false)
const restaurantReviews = ref([])
const reviewsLoading = ref(false)
const reviewTotal = ref(0)

// 搜索范围圆圈
const searchCircle = ref(null)

onMounted(async () => {
  // 初始化地图
  await initMap()
  
  // 获取用户位置并搜索餐厅
  await relocate()
})

/**
 * 初始化高德地图
 */
const initMap = async () => {
  try {
    window._AMapSecurityConfig = {
      securityJsCode: import.meta.env.VITE_AMAP_SECRET
    }
    
    window.AMap = await AMapLoader.load({
      key: import.meta.env.VITE_AMAP_KEY,
      version: '2.0',
      plugins: [
        'AMap.Driving',
        'AMap.Geocoder',
        'AMap.ToolBar',
        'AMap.Scale',
        'AMap.Geolocation',
        'AMap.InfoWindow'
      ]
    })
    
    // 创建地图实例
    map.value = new window.AMap.Map('amap-container', {
      zoom: 13,
      center: [116.397470, 39.908823],
      viewMode: '2D',
      showIndoorMap: false
    })
    
    // 添加工具条控件
    map.value.addControl(new window.AMap.ToolBar({
      position: 'RB'  // 右下角
    }))
    
    // 添加比例尺
    map.value.addControl(new window.AMap.Scale({
      position: 'LB'  // 左下角
    }))
    
    // 添加定位控件
    const geolocation = new window.AMap.Geolocation({
      position: 'RT',  // 右上角
      offset: [10, 10],
      enableHighAccuracy: true,
      timeout: 10000,
      buttonPosition: 'RB'
    })
    
    geolocation.getCurrentPosition((status, result) => {
      if (status === 'complete') {
        userLocation.value = {
          latitude: result.position.lat,
          longitude: result.position.lng
        }
        // 搜索附近餐厅
        searchRestaurants(result.position.lat, result.position.lng, searchRadius.value)
      }
    })
    
    map.value.addControl(geolocation)
    
    ElMessage.success('地图加载成功')
    
  } catch (error) {
    console.error('地图初始化失败:', error)
    ElMessage.error('地图加载失败，请检查API密钥是否正确配置')
  }
}

/**
 * 重新定位并搜索
 */
const relocate = async () => {
  const loadingInstance = ElLoading.service({ text: '正在定位...' })
  
  try {
    const loc = await getCurrentLocation()
    
    // 地图定位到当前位置
    if (map.value) {
      map.value.setCenter([loc.longitude, loc.latitude])
    }
    
    // 搜索附近餐厅
    await searchRestaurants(loc.latitude, loc.longitude, searchRadius.value)
    
  } catch (error) {
    // 定位失败，使用默认位置
    loadingInstance.close()
    
    ElMessageBox.alert(
      '由于浏览器安全限制，HTTP协议的外网域名无法使用地理定位功能。\n\n将使用默认位置（北京天安门）进行搜索。\n\n如需使用定位功能，请：\n1. 使用HTTPS访问\n2. 或本地访问 http://localhost:5173',
      '定位功能提示',
      {
        confirmButtonText: '使用默认位置',
        type: 'info'
      }
    ).then(async () => {
      const loading2 = ElLoading.service({ text: '正在搜索...' })
      try {
        // 使用北京天安门作为默认位置
        userLocation.value = { latitude: 39.908823, longitude: 116.397470 }
        
        if (map.value) {
          map.value.setCenter([116.397470, 39.908823])
        }
        
        await searchRestaurants(39.908823, 116.397470, searchRadius.value)
      } finally {
        loading2.close()
      }
    })
    
    return
  } finally {
    loadingInstance.close()
  }
}

/**
 * 搜索餐厅
 */
const searchRestaurants = async (latitude, longitude, radius) => {
  try {
    const response = await restaurantApi.searchNearby({
      latitude,
      longitude,
      radius,
      keyword: searchKeyword.value
    })
    
    restaurants.value = response.data.data || []
    
    // 检查哪些餐厅已被收藏
    await checkFavoritedRestaurants()
    
    // 更新搜索范围圆圈
    updateSearchCircle(longitude, latitude, radius)
    
    // 在地图上添加标记点
    addMarkersToMap(restaurants.value)
    
    ElMessage.success(`找到 ${restaurants.value.length} 家餐厅`)
    
  } catch (error) {
    console.error('搜索餐厅失败:', error)
    ElMessage.error('搜索餐厅失败，请重试')
  }
}

/**
 * 检查餐厅是否已收藏
 */
const checkFavoritedRestaurants = async () => {
  try {
    // 获取用户的收藏列表
    const response = await favoriteApi.getFavorites({ page: 1, size: 1000 })
    const favorites = response.data.data || []
    
    // 创建POI ID到收藏ID的映射
    const favoriteMap = {}
    favorites.forEach(f => {
      favoriteMap[f.poiId] = f.id
    })
    
    // 标记已收藏的餐厅并保存favoriteId
    restaurants.value.forEach(restaurant => {
      if (favoriteMap[restaurant.poiId]) {
        restaurant.isFavorited = true
        restaurant.favoriteId = favoriteMap[restaurant.poiId]
      } else {
        restaurant.isFavorited = false
        restaurant.favoriteId = null
      }
    })
    
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

/**
 * 更新搜索范围圆圈
 */
const updateSearchCircle = (longitude, latitude, radius) => {
  if (!map.value || !window.AMap) return
  
  // 移除旧圆圈
  if (searchCircle.value) {
    map.value.remove(searchCircle.value)
  }
  
  // 创建新圆圈显示搜索范围
  searchCircle.value = new window.AMap.Circle({
    center: [longitude, latitude],
    radius: radius,  // 半径（米）
    strokeColor: '#409EFF',  // 边框颜色
    strokeWeight: 2,  // 边框宽度
    strokeOpacity: 0.8,
    fillColor: '#409EFF',  // 填充颜色
    fillOpacity: 0.15,  // 填充透明度
    zIndex: 10
  })
  
  map.value.add(searchCircle.value)
}

/**
 * 在地图上添加标记点（性能优化版）
 */
const addMarkersToMap = (restaurants) => {
  if (!map.value || !window.AMap) return
  
  // 清除旧标记
  if (markers.value.length > 0) {
    map.value.remove(markers.value)
    markers.value = []
  }
  
  // 清除之前的路径规划
  if (drivingInstance.value) {
    drivingInstance.value.clear()
    drivingInstance.value = null
  }
  
  // 性能优化：限制标记数量到50个，避免卡顿
  const displayRestaurants = restaurants.slice(0, 50)
  
  // 保存到全局供InfoWindow使用
  window.currentRestaurants = displayRestaurants
  
  // 分批渲染标记点（每批10个）
  const batchSize = 10
  let currentIndex = 0
  
  const renderBatch = () => {
    const endIndex = Math.min(currentIndex + batchSize, displayRestaurants.length)
    
    for (let i = currentIndex; i < endIndex; i++) {
      const restaurant = displayRestaurants[i]
      
      // 创建美化的自定义标记
      const markerContent = `
        <div style="
          position: relative;
          width: 36px;
          height: 36px;
          background: linear-gradient(135deg, #667eea, #764ba2);
          border-radius: 50% 50% 50% 0;
          transform: rotate(-45deg);
          box-shadow: 0 3px 12px rgba(102, 126, 234, 0.4);
          border: 3px solid white;
          transition: all 0.3s;
        ">
          <div style="
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%) rotate(45deg);
            font-size: 18px;
          ">🍴</div>
        </div>
      `
      
      const marker = new window.AMap.Marker({
        position: [restaurant.location.longitude, restaurant.location.latitude],
        title: restaurant.name,
        content: markerContent,
        offset: new window.AMap.Pixel(-18, -36),
        extData: { restaurant, index: i }
      })
      
      // 点击时创建美化的InfoWindow
      marker.on('click', (e) => {
        const data = e.target.getExtData()
        showInfoWindow(data.restaurant, data.index, marker.getPosition())
      })
      
      map.value.add(marker)
      markers.value.push(marker)
    }
    
    currentIndex = endIndex
    
    // 如果还有剩余，继续渲染下一批
    if (currentIndex < displayRestaurants.length) {
      requestIdleCallback(renderBatch)
    } else {
      // 渲染完成，调整视野
      map.value.setFitView(null, false, [80, 80, 80, 80])
    }
  }
  
  // 开始分批渲染
  renderBatch()
}

// 全局函数供InfoWindow按钮调用
window.showRestaurantDetail = (idx) => {
  if (window.currentRestaurants && window.currentRestaurants[idx]) {
    selectRestaurant(window.currentRestaurants[idx])
  }
}

/**
 * 显示美化的信息窗体（懒加载）
 */
const showInfoWindow = (restaurant, index, position) => {
  const infoContent = `
    <div style="
      padding: 20px;
      min-width: 320px;
      background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
      border-radius: 16px;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
    ">
      <div style="
        margin-bottom: 16px;
        padding-bottom: 16px;
        border-bottom: 2px solid #EBEEF5;
      ">
        <h3 style="
          margin: 0 0 12px 0;
          font-size: 18px;
          font-weight: 600;
          color: #303133;
          background: linear-gradient(135deg, #667eea, #764ba2);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        ">${restaurant.name}</h3>
        
        <div style="display: flex; align-items: flex-start; gap: 8px; margin-bottom: 10px;">
          <span style="font-size: 16px; line-height: 1.6;">📍</span>
          <span style="color: #606266; font-size: 13px; line-height: 1.6; flex: 1;">
            ${restaurant.address || '地址未知'}
          </span>
        </div>
        
        ${restaurant.phone ? `
        <div style="display: flex; align-items: center; gap: 8px;">
          <span style="font-size: 16px;">📞</span>
          <span style="color: #606266; font-size: 13px;">${restaurant.phone}</span>
        </div>
        ` : ''}
      </div>
      
      <div style="
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 8px 16px;
        background: linear-gradient(135deg, #67C23A15, #67C23A08);
        border-radius: 20px;
        margin-bottom: 16px;
      ">
        <span style="font-size: 16px;">📏</span>
        <span style="
          color: #67C23A;
          font-weight: 600;
          font-size: 15px;
        ">距离 ${(restaurant.distance / 1000).toFixed(2)} 公里</span>
      </div>
      
      <button
        onclick="window.showRestaurantDetail(${index})"
        style="
          width: 100%;
          padding: 12px;
          background: linear-gradient(135deg, #667eea, #764ba2);
          color: white;
          border: none;
          border-radius: 10px;
          cursor: pointer;
          font-size: 15px;
          font-weight: 500;
          box-shadow: 0 4px 12px rgba(102, 126, 234, 0.35);
          transition: all 0.3s;
        "
        onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 16px rgba(102, 126, 234, 0.45)'"
        onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='0 4px 12px rgba(102, 126, 234, 0.35)'"
      >
        <span style="font-size: 16px;">✨</span> 查看详情
      </button>
    </div>
  `
  
  // 关闭之前的InfoWindow
  if (map.value) {
    map.value.clearInfoWindow()
  }
  
  const infoWindow = new window.AMap.InfoWindow({
    content: infoContent,
    offset: new window.AMap.Pixel(0, -40),
    closeWhenClickMap: true
  })
  
  infoWindow.open(map.value, position)
  
  // 地图平滑移动到标记位置
  if (map.value) {
    map.value.setCenter(position, true, 300)
  }
}

/**
 * 选择餐厅并显示详情
 */
const selectRestaurant = (restaurant) => {
  selectedRestaurant.value = restaurant
  detailDialogVisible.value = true
  activeTab.value = 'detail'
  showReviewForm.value = false
  
  // 加载餐厅评价
  loadRestaurantReviews(restaurant.poiId)
  
  // 地图定位到选中的餐厅
  if (map.value) {
    map.value.setCenter([restaurant.location.longitude, restaurant.location.latitude])
  }
}

/**
 * 加载餐厅评价列表
 */
const loadRestaurantReviews = async (poiId, page = 1) => {
  if (!poiId) poiId = selectedRestaurant.value?.poiId
  if (!poiId) return
  
  reviewsLoading.value = true
  
  try {
    const response = await reviewApi.getReviews({
      poiId,
      page,
      size: 10
    })
    
    restaurantReviews.value = response.data.data || []
    reviewTotal.value = response.data.pagination?.total || 0
    
  } catch (error) {
    console.error('加载评价失败:', error)
  } finally {
    reviewsLoading.value = false
  }
}

/**
 * 显示评价表单
 */
const handleShowReviewForm = () => {
  activeTab.value = 'write-review'
}

/**
 * 提交评价
 */
const handleSubmitReview = async (reviewData) => {
  try {
    await reviewApi.createReview(reviewData)
    ElMessage.success('评价发表成功！')
    
    // 重新加载评价列表
    await loadRestaurantReviews(selectedRestaurant.value.poiId)
    
    // 切换回评价列表Tab
    activeTab.value = 'reviews'
    
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '发表评价失败')
  }
}

/**
 * 处理搜索
 */
const handleSearch = async () => {
  const loc = userLocation.value || { latitude: 39.908823, longitude: 116.397470 }
  await searchRestaurants(loc.latitude, loc.longitude, searchRadius.value)
}

/**
 * 搜索半径改变（实时更新）
 */
const handleRadiusChange = async () => {
  const loc = userLocation.value || { latitude: 39.908823, longitude: 116.397470 }
  
  // 显示loading
  const loading = ElLoading.service({ text: '正在搜索...' })
  
  try {
    await searchRestaurants(loc.latitude, loc.longitude, searchRadius.value)
  } finally {
    loading.close()
  }
}


/**
 * 切换收藏状态
 */
const handleToggleFavorite = async () => {
  if (selectedRestaurant.value.isFavorited) {
    // 取消收藏
    await handleUnfavorite()
  } else {
    // 添加收藏
    await handleFavorite()
  }
}

/**
 * 添加收藏
 */
const handleFavorite = async () => {
  if (!selectedRestaurant.value.location) {
    ElMessage.error('餐厅位置信息不完整')
    return
  }
  
  const favoriteData = {
    poiId: selectedRestaurant.value.poiId,
    restaurantName: selectedRestaurant.value.name || '未知餐厅',
    address: selectedRestaurant.value.address || '',
    latitude: Number(selectedRestaurant.value.location.latitude) || 0,
    longitude: Number(selectedRestaurant.value.location.longitude) || 0,
    rating: selectedRestaurant.value.rating ? Number(selectedRestaurant.value.rating) : null,
    category: selectedRestaurant.value.category || '',
    notes: ''
  }
  
  try {
    const response = await favoriteApi.addFavorite(favoriteData)
    if (response.status === 201) {
      ElMessage.success('收藏成功！')
      selectedRestaurant.value.isFavorited = true
      selectedRestaurant.value.favoriteId = response.data.id
      
      // 更新restaurants列表中的状态
      const restaurant = restaurants.value.find(r => r.poiId === selectedRestaurant.value.poiId)
      if (restaurant) {
        restaurant.isFavorited = true
        restaurant.favoriteId = response.data.id
      }
    }
  } catch (error) {
    console.error('收藏失败:', error)
    if (error.response?.status === 409) {
      ElMessage.warning('您已收藏过该餐厅')
      selectedRestaurant.value.isFavorited = true
    } else {
      ElMessage.error(error.response?.data?.message || '收藏失败，请稍后重试')
    }
  }
}

/**
 * 取消收藏
 */
const handleUnfavorite = async () => {
  if (!selectedRestaurant.value.favoriteId) {
    // 如果没有favoriteId，从收藏列表中查找
    try {
      const response = await favoriteApi.getFavorites({ page: 1, size: 1000 })
      const favorites = response.data.data || []
      const favorite = favorites.find(f => f.poiId === selectedRestaurant.value.poiId)
      
      if (favorite) {
        selectedRestaurant.value.favoriteId = favorite.id
      } else {
        ElMessage.error('未找到收藏记录')
        return
      }
    } catch (error) {
      ElMessage.error('获取收藏记录失败')
      return
    }
  }
  
  try {
    await favoriteApi.removeFavorite(selectedRestaurant.value.favoriteId)
    ElMessage.success('已取消收藏')
    selectedRestaurant.value.isFavorited = false
    selectedRestaurant.value.favoriteId = null
    
    // 更新restaurants列表中的状态
    const restaurant = restaurants.value.find(r => r.poiId === selectedRestaurant.value.poiId)
    if (restaurant) {
      restaurant.isFavorited = false
      restaurant.favoriteId = null
    }
  } catch (error) {
    ElMessage.error('取消收藏失败')
  }
}

/**
 * 处理导航（使用高德JavaScript API）
 */
const handleNavigate = () => {
  if (!userLocation.value) {
    ElMessage.warning('请先获取您的当前位置')
    return
  }
  
  if (!map.value || !window.AMap || !selectedRestaurant.value) {
    ElMessage.error('地图未初始化')
    return
  }
  
  // 清除之前的路径规划
  if (drivingInstance.value) {
    drivingInstance.value.clear()
  }
  
  // 使用高德JavaScript API的Driving插件
  drivingInstance.value = new window.AMap.Driving({
    map: map.value,
    hideMarkers: false,
    autoFitView: true
  })
  
  const startPoint = [userLocation.value.longitude, userLocation.value.latitude]
  const endPoint = [
    selectedRestaurant.value.location.longitude,
    selectedRestaurant.value.location.latitude
  ]
  
  drivingInstance.value.search(startPoint, endPoint, (status, result) => {
    if (status === 'complete') {
      const distance = (result.routes[0].distance / 1000).toFixed(1)
      const duration = Math.ceil(result.routes[0].time / 60)
      ElMessage.success(`路径规划成功！距离约${distance}公里，预计${duration}分钟`)
      console.log('导航结果:', result)
    } else {
      ElMessage.error('路径规划失败：' + result)
    }
  })
  
  showNavigation.value = false
}

/**
 * 路径规划完成（备用）
 */
const handleRouteCalculated = (route) => {
  console.log('Route calculated:', route)
}

/**
 * 处理用户菜单命令
 */
const handleCommand = (command) => {
  if (command === 'logout') {
    authStore.logout()
    router.push('/login')
  } else if (command === 'admin') {
    router.push('/admin/dashboard')
  } else {
    router.push(`/profile/${command}`)
  }
}
</script>

<style scoped>
.map-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fa;
}

.top-navbar {
  display: flex;
  align-items: center;
  padding: 12px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  z-index: 1000;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(45deg, #fff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-right: 40px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.search-box {
  flex: 1;
  max-width: 600px;
}

.search-box :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  padding: 8px 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  backdrop-filter: blur(10px);
}

.user-menu {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.2);
  transition: all 0.3s;
  color: white;
  font-weight: 500;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.restaurant-list {
  width: 400px;
  background: #fff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
}

.list-header {
  padding: 20px;
  background: linear-gradient(135deg, #667eea20 0%, #764ba220 100%);
  border-bottom: 2px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.list-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.list-content {
  padding: 15px;
}

.restaurant-item {
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #667eea, #764ba2) border-box;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.restaurant-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.25);
  border-color: rgba(102, 126, 234, 0.5);
}

.restaurant-info h4 {
  margin: 0 0 10px 0;
  font-size: 17px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.address {
  margin: 0 0 10px 0;
  font-size: 13px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 4px;
}

.address::before {
  content: '📍';
}

.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  padding-top: 8px;
  border-top: 1px dashed #EBEEF5;
}

.distance {
  color: #67C23A;
  font-weight: 600;
  font-size: 15px;
  padding: 4px 12px;
  background: linear-gradient(135deg, #67C23A20, #67C23A10);
  border-radius: 20px;
}

.map-area {
  flex: 1;
  position: relative;
}

#amap-container {
  width: 100%;
  height: 100%;
}

.locate-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 100;
}

.radius-selector {
  position: absolute;
  top: 70px;
  right: 20px;
  z-index: 100;
  background: #fff;
  padding: 8px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.restaurant-detail {
  padding: 24px 0;
}

.detail-section {
  margin-bottom: 24px;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #f0f2f5 100%);
  border-radius: 12px;
}

.detail-section p {
  margin: 12px 0;
  line-height: 1.8;
  font-size: 14px;
  color: #606266;
}

.detail-section p strong {
  color: #303133;
  font-weight: 600;
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.actions .el-button {
  flex: 1;
  height: 44px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 10px;
}

.actions .el-button--primary {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.actions .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.5);
}

.locate-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 100;
}

.radius-selector {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 999;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(255, 255, 255, 0.95));
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.5);
}

.radius-selector :deep(.el-radio-button__inner) {
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 500;
}

.radius-selector :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-color: transparent;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.4);
}

.restaurant-detail {
  padding: 20px 0;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section p {
  margin: 10px 0;
  line-height: 1.6;
}
</style>

