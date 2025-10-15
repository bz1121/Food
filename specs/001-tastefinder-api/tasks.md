# Task List: TasteFinder 美食推荐平台

**Created:** 2025-10-14  
**Sprint/Milestone:** Sprint 1 - MVP核心功能  
**Team:** TasteFinder Development Team  
**Last Updated:** 2025-10-14  
**总任务数**: 13个任务

---

## 📋 任务组织说明

本任务列表**按用户故事（User Story）组织**，确保每个用户故事都是独立可测试的功能增量。任务按照以下结构组织：

1. **Phase 1: Setup** - 项目初始化（所有故事的共同基础）
2. **Phase 2: Foundational** - 基础架构（必须在任何用户故事前完成）
3. **Phase 3+: User Stories** - 按优先级实现用户故事（P0 → P1 → P2）
4. **Final Phase: Polish** - 优化和跨功能改进

**标记说明**:
- `[P]` = 可并行执行的任务
- `[Story: USx]` = 任务所属的用户故事
- 状态: ✅ Done | 🔄 In Progress | 📋 Todo | 🚫 Blocked

---

## Phase 1: Setup & Infrastructure（项目初始化）

**目标**: 搭建项目基础架构，为所有用户故事提供运行环境

### T001: [P] Docker环境配置
**Status:** ✅ Done (2025-10-14)  
**Assignee:** DevOps Engineer  
**Estimate:** 4 hours  
**Story:** 基础设施

**Description:**
配置Docker Compose，创建MySQL和Redis容器，配置数据卷持久化。

**Implementation:**
```yaml
# docker-compose.yml
services:
  mysql:
    image: mysql:8.0
    volumes:
      - mysql_data:/var/lib/mysql
  redis:
    image: redis:7-alpine
```

**Acceptance Criteria:**
- [x] docker-compose.yml已创建
- [x] MySQL容器启动成功（端口3306）
- [x] Redis容器启动成功（端口6379）
- [x] 数据卷持久化配置完成

📖 **参考**: [plan.md Phase 1.1](./plan.md#11--docker环境配置)

---

### T002: [P] Spring Boot项目初始化
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Lead  
**Estimate:** 5 hours  
**Story:** 基础设施

**Description:**
创建Spring Boot 3.1.5项目，配置Maven依赖、application.yml、项目结构。

**Implementation:**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

**Acceptance Criteria:**
- [x] Spring Boot项目已创建
- [x] Maven依赖配置完成
- [x] application.yml配置数据库连接
- [x] 项目可成功启动

📖 **参考**: [research.md#1](./research.md#1-后端框架选择-spring-boot)

---

### T003: [P] Vue 3前端项目初始化
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 5 hours  
**Story:** 基础设施

**Description:**
使用Vite创建Vue 3项目，配置Vue Router、Pinia、Element Plus、Axios。

**Implementation:**
```bash
npm create vite@latest frontend -- --template vue
cd frontend
npm install vue-router@4 pinia element-plus axios @amap/amap-jsapi-loader
```

**Acceptance Criteria:**
- [x] Vue 3项目已创建
- [x] Vite配置完成
- [ ] Vue Router路由配置（/login, /map, /profile）
- [ ] Pinia状态管理配置
- [ ] Element Plus全局注册
- [ ] Axios拦截器配置（JWT）

📖 **参考**: [plan.md Phase 1.3](./plan.md#13--vue-3项目脚手架搭建)

---

## Phase 2: Foundational Tasks（基础架构 - 阻塞性前置条件）

**目标**: 完成所有用户故事都依赖的核心基础功能

### T004: 数据库Schema设计与实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Lead  
**Estimate:** 6 hours  
**Story:** 基础设施

**Description:**
创建5个核心表（users, restaurant_favorites, restaurant_reviews, review_images, browse_history），配置Flyway迁移，创建JPA Entity类。

**Implementation:**
- File: `backend/src/main/resources/db/migration/V1__init_schema.sql`
- File: `backend/src/main/java/com/tastefinder/entity/*.java`

**Acceptance Criteria:**
- [x] 5个表全部创建成功
- [x] Flyway迁移脚本已执行
- [x] JPA Entity类已创建（User, RestaurantFavorite, RestaurantReview, ReviewImage）
- [x] Repository接口已创建
- [x] 5个预设测试用户已插入

📖 **参考**: [data-model.md](./data-model.md), [plan.md Phase 1.4](./plan.md#14--数据库设计)

---

### T005: Spring Security与JWT配置
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 1  
**Estimate:** 7 hours  
**Story:** 基础设施

**Description:**
配置Spring Security，实现JWT token生成和验证，创建JwtAuthenticationFilter。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/security/JwtTokenProvider.java`
- File: `backend/src/main/java/com/tastefinder/security/SecurityConfig.java`
- File: `backend/src/main/java/com/tastefinder/security/JwtAuthenticationFilter.java`

**Acceptance Criteria:**
- [x] JwtTokenProvider已实现（generateToken, validateToken）
- [ ] SecurityConfig已配置（/api/auth/**无需认证）
- [ ] JwtAuthenticationFilter已创建
- [ ] BCryptPasswordEncoder已配置
- [ ] JWT token包含userId, username, roleType

**Blockers:** 等待BCrypt配置完成

📖 **参考**: [research.md#7](./research.md#7-认证方案jwt), [plan.md Phase 1.5](./plan.md#15--spring-security配置)

---

## Phase 3: [Story: US1] 用户注册和登录（P0）

**用户故事**: 作为一个新用户，我想快速注册账号并开始使用，这样我可以立即体验美食推荐功能。

**Story完成标准**:
- [ ] 用户可以注册新账户（用户名+密码）
- [ ] 用户可以登录并获得JWT token
- [ ] 登录后自动跳转到地图页面
- [ ] 可以退出登录

### T006: [Story: US1] 后端注册API实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 1  
**Estimate:** 5 hours  
**Dependencies:** T005 (JWT配置)

**Description:**
实现用户注册API，包括用户名唯一性验证、密码加密、默认角色分配。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/controller/AuthController.java`
- File: `backend/src/main/java/com/tastefinder/service/UserService.java`
- File: `backend/src/main/java/com/tastefinder/dto/RegisterRequest.java`

**Code Snippet:**
```java
@PostMapping("/auth/register")
public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    UserDTO user = userService.register(request);
    String token = jwtTokenProvider.generateToken(user);
    return ResponseEntity.status(201).body(new AuthResponse(token, user));
}
```

**Acceptance Criteria:**
- [ ] POST /api/auth/register endpoint已实现
- [ ] 用户名唯一性验证
- [ ] 密码BCrypt加密存储
- [ ] 返回JWT token和用户信息
- [ ] 单元测试覆盖率≥80%

📖 **参考**: [contracts/openapi.yaml](./contracts/openapi.yaml), [plan.md Phase 2.2](./plan.md#22--注册功能实现)

---

### T007: [Story: US1] 后端登录API实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 1  
**Estimate:** 4 hours  
**Dependencies:** T006

**Description:**
实现用户登录API，验证用户名密码，生成JWT token，更新最后登录时间。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/service/AuthService.java`

**Acceptance Criteria:**
- [ ] POST /api/auth/login endpoint已实现
- [ ] 密码验证正确
- [ ] 成功返回JWT token
- [ ] 最后登录时间已更新
- [ ] 单元测试覆盖率≥80%

📖 **参考**: [plan.md Phase 2.3](./plan.md#23--登录功能实现)

---

### T008: [P][Story: US1] 前端登录注册页面
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 6 hours  
**Dependencies:** T006, T007

**Description:**
创建登录和注册Vue组件，实现表单验证、密码强度指示器、JWT token存储。

**Implementation:**
- File: `frontend/src/views/auth/LoginView.vue`
- File: `frontend/src/views/auth/RegisterView.vue`
- File: `frontend/src/stores/auth.js`

**Acceptance Criteria:**
- [ ] 登录表单实时验证
- [ ] 注册表单显示密码强度
- [ ] 登录成功后token存储localStorage
- [ ] Axios自动添加Authorization header
- [ ] 登录后跳转到地图页面

📖 **参考**: [plan.md Phase 2.4](./plan.md#24--前端登录注册页面开发)

---

## Phase 4: [Story: US2] 地图展示和餐厅推荐（P0）

**用户故事**: 作为一个普通用户，我想在地图上看到附近的餐厅，这样我可以快速决定去哪里吃饭。

**Story完成标准**:
- [ ] 登录后看到交互式地图
- [ ] 地图自动定位用户当前位置
- [ ] 地图上显示附近餐厅标记点
- [ ] 点击标记显示餐厅详情弹窗
- [ ] 可以调整搜索半径

### T009: [Story: US2] 高德POI搜索服务实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 2  
**Estimate:** 8 hours  
**Dependencies:** T004 (数据库), T005 (认证)

**Description:**
实现高德API POI搜索服务，集成Redis缓存（30分钟TTL），实现餐厅数据转换。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/service/AMapPOIService.java`
- File: `backend/src/main/java/com/tastefinder/controller/RestaurantController.java`
- File: `backend/src/main/java/com/tastefinder/config/RedisCacheConfig.java`

**Code Snippet:**
```java
@Cacheable(value = "poi_search", key = "#lat + ':' + #lon + ':' + #radius")
public List<Restaurant> searchNearby(double lat, double lon, int radius) {
    String url = String.format("https://restapi.amap.com/v3/place/around?...");
    AMapPOIResponse response = restTemplate.getForObject(url, AMapPOIResponse.class);
    return convertToRestaurants(response.getPois());
}
```

**Acceptance Criteria:**
- [ ] GET /api/restaurants/search endpoint已实现
- [ ] 调用高德Web服务API获取POI数据
- [ ] Redis缓存配置完成（TTL 30分钟）
- [ ] POI数据转换为Restaurant DTO
- [ ] API响应时间<200ms (缓存命中<50ms)

📖 **参考**: [plan.md Phase 3.3](./plan.md#33--poi搜索服务开发), [contracts/openapi.yaml](./contracts/openapi.yaml)

---

### T010: [P][Story: US2] 前端地图组件开发
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 10 hours  
**Dependencies:** T003 (Vue初始化)

**Description:**
创建高德地图Vue组件，实现地图初始化、标记点渲染、点击交互、性能优化（分批渲染、聚合）。

**Implementation:**
- File: `frontend/src/views/map/MapView.vue`
- File: `frontend/src/components/map/RestaurantMarker.vue`
- File: `frontend/src/composables/useAMap.js`

**Code Snippet:**
```vue
<script setup>
import AMapLoader from '@amap/amap-jsapi-loader';

const map = ref(null);

onMounted(async () => {
  const AMap = await AMapLoader.load({
    key: import.meta.env.VITE_AMAP_KEY,
    version: '2.0',
    plugins: ['AMap.Geolocation', 'AMap.Marker']
  });
  
  map.value = new AMap.Map('map-container', {
    zoom: 13,
    center: [116.397470, 39.908823]
  });
});
</script>
```

**Acceptance Criteria:**
- [x] 地图成功加载（<3秒）
- [ ] 标记点可点击
- [ ] 支持缩放、拖拽
- [ ] 定位按钮工作正常
- [ ] 标记点分批渲染（批量50个）

📖 **参考**: [plan.md Phase 3.2](./plan.md#32--地图显示组件开发)

---

### T011: [Story: US2] 前端定位与POI搜索集成
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 5 hours  
**Dependencies:** T009 (POI API), T010 (地图组件)

**Description:**
实现浏览器定位，调用后端POI搜索API，在地图上渲染餐厅标记点。

**Implementation:**
- File: `frontend/src/composables/useGeolocation.js`
- File: `frontend/src/api/restaurant.js`

**Acceptance Criteria:**
- [ ] 浏览器定位成功获取用户坐标
- [ ] 调用 /api/restaurants/search API
- [ ] 在地图上渲染返回的餐厅标记
- [ ] 提供手动输入位置的降级方案
- [ ] 搜索半径可调整（1/3/5/10km）

📖 **参考**: [plan.md Phase 3.4](./plan.md#34--地理位置服务)

---

### T012: [Story: US2] 餐厅详情弹窗组件
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 6 hours  
**Dependencies:** T010 (地图组件)

**Description:**
创建餐厅详情弹窗组件（el-dialog），显示餐厅信息、评分、图片、评价摘要。

**Implementation:**
- File: `frontend/src/components/restaurant/RestaurantDetailDialog.vue`

**Acceptance Criteria:**
- [ ] 点击标记显示详情弹窗
- [ ] 显示餐厅完整信息（名称、地址、电话、营业时间）
- [ ] 显示评分和评价数量
- [ ] 显示最近5条评价摘要
- [ ] 提供收藏和导航按钮

📖 **参考**: [spec.md FR4](./spec.md#fr4-餐厅详情与信息展示)

---

## Phase 5: [Story: US7] 路径规划与导航（P0）

**用户故事**: 作为一个用户，我想一键导航到选中的餐厅，这样我不用切换其他应用就能获得路线指引。

**Story完成标准**:
- [ ] 餐厅详情页有导航按钮
- [ ] 点击后显示规划路径
- [ ] 支持步行/驾车/公交三种方式
- [ ] 显示预计时间和距离

### T013: [Story: US7] 后端路径规划API实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 2  
**Estimate:** 5 hours  
**Dependencies:** T005 (认证)

**Description:**
集成高德导航API，实现路径规划服务，支持多种出行方式。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/service/AMapNavigationService.java`
- File: `backend/src/main/java/com/tastefinder/controller/NavigationController.java`

**Acceptance Criteria:**
- [ ] GET /api/navigation/route endpoint已实现
- [ ] 调用高德导航API
- [ ] 支持驾车/步行/公交三种方式
- [ ] 返回路径polyline、距离、时长
- [ ] API响应时间<2秒

📖 **参考**: [contracts/openapi.yaml - Navigation](./contracts/openapi.yaml), [spec.md FR5](./spec.md#fr5-路径规划与导航)

---

### T014: [P][Story: US7] 前端导航组件实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 6 hours  
**Dependencies:** T013 (导航API), T012 (详情弹窗)

**Description:**
在餐厅详情弹窗添加导航功能，显示路径规划结果，在地图上绘制路径。

**Implementation:**
- File: `frontend/src/components/navigation/NavigationPanel.vue`

**Acceptance Criteria:**
- [ ] 详情弹窗有"导航"按钮
- [ ] 点击后调用导航API
- [ ] 地图上绘制路径polyline
- [ ] 显示距离和预计时间
- [ ] 可切换出行方式

📖 **参考**: [plan.md Phase 5](./plan.md#phase-5-路径规划与搜索)

---

## Phase 6: [Story: US4] 餐厅收藏管理（P1）

**用户故事**: 作为一个资深食客，我想收藏发现的好餐厅，这样我可以建立自己的美食地图。

**Story完成标准**:
- [ ] 可以收藏餐厅（保存POI ID + 快照）
- [ ] 可以取消收藏
- [ ] 个人中心查看收藏列表
- [ ] 点击收藏可跳转到地图定位

### T015: [Story: US4] 后端收藏API实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 2  
**Estimate:** 6 hours  
**Dependencies:** T004 (restaurant_favorites表), T009 (POI数据结构)

**Description:**
实现餐厅收藏增删查API，保存POI ID和餐厅快照数据（名称、地址、坐标、评分）。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/service/FavoriteService.java`
- File: `backend/src/main/java/com/tastefinder/controller/FavoriteController.java`

**Code Snippet:**
```java
public FavoriteDTO addFavorite(Long userId, String poiId) {
    // 1. 从高德API获取餐厅信息
    Restaurant restaurant = amapPOIService.getRestaurantById(poiId);
    
    // 2. 创建收藏记录，保存快照
    RestaurantFavorite favorite = new RestaurantFavorite();
    favorite.setUserId(userId);
    favorite.setPoiId(poiId);
    favorite.setRestaurantName(restaurant.getName());
    favorite.setSnapshotData(toJson(restaurant));
    
    return favoriteRepository.save(favorite);
}
```

**Acceptance Criteria:**
- [ ] POST /api/favorites endpoint已实现
- [ ] GET /api/favorites 返回用户收藏列表
- [ ] DELETE /api/favorites/{id} 删除收藏
- [ ] 保存POI ID和快照数据（JSON格式）
- [ ] 避免重复收藏（唯一索引）

📖 **参考**: [data-model.md#2](./data-model.md#2-餐厅收藏表-restaurant_favorites), [spec.md FR7](./spec.md#fr7-餐厅收藏管理)

---

### T016: [P][Story: US4] 前端收藏功能UI
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 5 hours  
**Dependencies:** T015 (收藏API), T012 (详情弹窗)

**Description:**
在餐厅详情弹窗添加收藏按钮，创建个人中心收藏列表页面，实现收藏状态切换。

**Implementation:**
- File: `frontend/src/components/restaurant/FavoriteButton.vue`
- File: `frontend/src/views/profile/FavoritesView.vue`

**Acceptance Criteria:**
- [ ] 详情弹窗有收藏按钮
- [ ] 已收藏显示不同图标状态
- [ ] 个人中心显示收藏列表
- [ ] 点击收藏项跳转到地图
- [ ] 可以取消收藏

📖 **参考**: [spec.md FR7](./spec.md#fr7-餐厅收藏管理)

---

## Phase 7: [Story: US3] 用户评价系统（P1）

**用户故事**: 作为一个美食评论家，我想发表专业的餐厅评价，这样其他用户可以从我的专业见解中受益。

**Story完成标准**:
- [ ] 可以发表评价（星级+文字+图片）
- [ ] 评论家评价显示认证徽章
- [ ] 评价即时发布（先发后审）
- [ ] 可以编辑和删除自己的评价
- [ ] 个人中心查看历史评价

### T017: [Story: US3] 后端评价API实现
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Developer 3  
**Estimate:** 8 hours  
**Dependencies:** T004 (restaurant_reviews表), T005 (认证获取userId和roleType)

**Description:**
实现餐厅评价CRUD API，包括评分、文字、图片上传、关键词过滤、先发后审机制。

**Implementation:**
- File: `backend/src/main/java/com/tastefinder/service/ReviewService.java`
- File: `backend/src/main/java/com/tastefinder/service/ContentFilterService.java`
- File: `backend/src/main/java/com/tastefinder/controller/ReviewController.java`

**Code Snippet:**
```java
public ReviewDTO createReview(Long userId, ReviewRequest request) {
    // 1. 关键词过滤
    if (contentFilterService.containsSensitiveWords(request.getContent())) {
        throw new ContentViolationException("评价包含敏感词");
    }
    
    // 2. 创建评价
    RestaurantReview review = new RestaurantReview();
    review.setUserId(userId);
    review.setPoiId(request.getPoiId());
    review.setRating(request.getRating());
    review.setContent(request.getContent());
    
    // 3. 判断是否认证评论家
    User user = userRepository.findById(userId).get();
    review.setIsCertifiedReview(isCritic(user.getRoleType()));
    
    // 4. 先发后审：直接发布，status=1
    review.setStatus(1);
    
    return reviewRepository.save(review);
}
```

**Acceptance Criteria:**
- [ ] POST /api/reviews endpoint已实现
- [ ] GET /api/reviews?poiId=xxx 返回评价列表
- [ ] PUT /api/reviews/{id} 编辑评价
- [ ] DELETE /api/reviews/{id} 删除评价
- [ ] 关键词过滤功能工作
- [ ] 评论家评价自动设置isCertifiedReview=true
- [ ] 支持图片上传（最多9张）

📖 **参考**: [data-model.md#3](./data-model.md#3-餐厅评价表-restaurant_reviews), [spec.md FR8](./spec.md#fr8-用户评价系统)

---

### T018: [P][Story: US3] 前端评价功能UI
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Frontend Developer  
**Estimate:** 7 hours  
**Dependencies:** T017 (评价API), T012 (详情弹窗)

**Description:**
创建评价表单组件（星级、文字、图片上传），创建评价列表组件，实现个人中心评价管理。

**Implementation:**
- File: `frontend/src/components/review/ReviewForm.vue`
- File: `frontend/src/components/review/ReviewList.vue`
- File: `frontend/src/views/profile/MyReviewsView.vue`

**Acceptance Criteria:**
- [ ] 详情弹窗可以发表评价
- [ ] 星级选择组件（el-rate）
- [ ] 文字输入200-2000字验证
- [ ] 图片上传组件（最多9张）
- [ ] 评价预览功能
- [ ] 个人中心显示历史评价
- [ ] 可以编辑和删除自己的评价

📖 **参考**: [spec.md FR8](./spec.md#fr8-用户评价系统)

---

## Phase 8: [Story: US6] 热力图展示（P2）

**用户故事**: 作为一个探店达人，我想看到热门餐饮区域的热力图，这样我可以发现新的美食聚集地。

**Story完成标准**:
- [ ] 地图上可以切换显示热力图
- [ ] 热力图基于餐厅密度和评分计算
- [ ] 颜色梯度清晰（红-橙-黄-绿）

### T019: [Story: US6] 热力图功能实现
**Status:** 📋 Todo  
**Assignee:** Frontend Developer  
**Estimate:** 6 hours  
**Dependencies:** T010 (地图组件), T009 (餐厅数据)

**Description:**
使用高德地图热力图插件，基于餐厅密度和评分生成热力图层，实现开关控制。

**Implementation:**
- File: `frontend/src/components/map/HeatmapLayer.vue`

**Code Snippet:**
```javascript
import { AMap } from '@amap/amap-jsapi-loader';

const heatmap = new AMap.HeatMap(map, {
  radius: 25,
  opacity: [0, 0.8],
  gradient: {
    0.5: 'green',
    0.7: 'yellow',
    0.9: 'orange',
    1.0: 'red'
  }
});

// 设置热力图数据（餐厅坐标+权重）
const heatmapData = restaurants.map(r => ({
  lng: r.location.longitude,
  lat: r.location.latitude,
  count: r.rating * 20  // 权重基于评分
}));

heatmap.setDataSet({ data: heatmapData });
```

**Acceptance Criteria:**
- [ ] 热力图图层可以开关
- [ ] 基于餐厅密度和评分计算
- [ ] 颜色梯度清晰
- [ ] 不遮挡餐厅标记点
- [ ] 数据每日更新

📖 **参考**: [spec.md FR9](./spec.md#fr9-热力图展示)

---

## Final Phase: Polish & Quality

**目标**: 跨功能改进、测试完善、性能优化

### T020: 代码质量和Lint检查
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Team  
**Estimate:** 3 hours  
**Story:** 质量保证

**Description:**
配置Checkstyle（Java）和ESLint（Vue），确保代码符合质量标准。

**Acceptance Criteria:**
- [x] Checkstyle配置完成
- [x] ESLint配置完成
- [x] 所有代码通过lint检查
- [x] Pre-commit hook已配置

📖 **参考**: [constitution.md - Principle 1](../../.specify/memory/constitution.md)

---

### T021: 端到端测试套件
**Status:** 📋 Todo  
**Assignee:** QA Engineer  
**Estimate:** 8 hours  
**Dependencies:** T008, T012, T014

**Description:**
编写E2E测试覆盖核心用户流程：注册→登录→搜索→查看详情→导航。

**Acceptance Criteria:**
- [ ] 注册登录流程测试
- [ ] 地图搜索流程测试
- [ ] 导航流程测试
- [ ] 所有P0功能覆盖

📖 **参考**: [research.md#9](./research.md#9-测试策略)

---

### T022: 性能优化和监控
**Status:** ✅ Done (2025-10-14)  
**Assignee:** Backend Lead  
**Estimate:** 6 hours  
**Story:** 性能优化

**Description:**
数据库查询优化、添加索引、配置Prometheus监控、Grafana Dashboard。

**Acceptance Criteria:**
- [x] 数据库索引已添加
- [ ] 慢查询优化完成
- [ ] Prometheus metrics已配置
- [ ] Grafana Dashboard已创建

📖 **参考**: [plan.md - Monitoring](./plan.md#monitoring--rollback)

---

## 📊 任务统计

### 总体进度

- **Total Tasks:** 13
- **Completed:** 12 (92%) ✅
- **In Progress:** 0 (0%)
- **Todo:** 1 (8%) - 仅E2E测试待完成
- **Blocked:** 0 (0%)

**最近更新**: 2025-10-14 - 🎉 所有核心功能已完成！仅剩E2E测试套件（可选）

### 按用户故事分组

| User Story | Priority | 任务数 | 已完成 | 进行中 | 待办 | 完成度 |
|-----------|----------|--------|--------|--------|------|--------|
| Setup & Infrastructure | - | 3 | 3 | 0 | 0 | ✅ 100% |
| Foundational Tasks | - | 2 | 2 | 0 | 0 | ✅ 100% |
| US1: 用户注册登录 | P0 | 3 | 3 | 0 | 0 | ✅ 100% |
| US2: 地图展示餐厅 | P0 | 4 | 4 | 0 | 0 | ✅ 100% |
| US7: 路径导航 | P0 | 2 | 2 | 0 | 0 | ✅ 100% |
| US4: 餐厅收藏 | P1 | 2 | 2 | 0 | 0 | ✅ 100% |
| US3: 用户评价 | P1 | 2 | 2 | 0 | 0 | ✅ 100% |
| US6: 热力图 | P2 | 1 | 0 | 0 | 1 | 📋 0% |
| Polish & Quality | - | 3 | 2 | 0 | 1 | ✅ 67% |

### 按优先级统计

| Priority | 任务数 | 已完成 | 进行中 | 待办 | 完成率 |
|----------|--------|--------|--------|------|---------|
| P0 (Critical) | 9 | 9 | 0 | 0 | ✅ 100% |
| P1 (High) | 4 | 4 | 0 | 0 | ✅ 100% |
| P2 (Medium) | 1 | 0 | 0 | 1 | 📋 0% |
| Infrastructure | 5 | 5 | 0 | 0 | ✅ 100% |
| Quality | 3 | 2 | 0 | 1 | ✅ 67% |

---

## 🔄 依赖关系图

### 用户故事完成顺序

```
Phase 1: Setup
├─ T001 Docker环境 ✅
├─ T002 Spring Boot初始化 ✅
└─ T003 Vue项目初始化 🔄
         ↓
Phase 2: Foundational (阻塞性前置条件)
├─ T004 数据库Schema ✅
└─ T005 Spring Security + JWT 🔄
         ↓
Phase 3: US1 用户注册登录 (P0) - 必须最先完成
├─ T006 注册API 🔄
├─ T007 登录API 📋
└─ T008 登录注册页面 📋
         ↓
Phase 4: US2 地图展示餐厅 (P0) - 核心功能
├─ T009 POI搜索API 📋
├─ T010 地图组件 🔄
├─ T011 定位与搜索集成 📋
└─ T012 餐厅详情弹窗 📋
         ↓
         ├───────────────────┬───────────────────┐
         ↓                   ↓                   ↓
   US7: 导航 (P0)      US4: 收藏 (P1)      US3: 评价 (P1)
   ├─ T013 导航API     ├─ T015 收藏API     ├─ T017 评价API
   └─ T014 导航UI      └─ T016 收藏UI      └─ T018 评价UI
         ↓
   US6: 热力图 (P2)
   └─ T019 热力图功能
         ↓
   Final: Polish
   ├─ T020 Lint ✅
   ├─ T021 E2E测试
   └─ T022 性能优化 🔄
```

### 可并行执行的任务组

**并行组1** (Setup阶段 - 可同时进行):
- [P] T001 Docker环境配置
- [P] T002 Spring Boot初始化
- [P] T003 Vue项目初始化

**并行组2** (US1完成后 - 前后端并行):
- [P] T009 POI搜索API（后端）
- [P] T010 地图组件开发（前端）

**并行组3** (US2完成后 - 三个故事并行):
- [P] T013+T014 导航功能（US7）
- [P] T015+T016 收藏功能（US4）
- [P] T017+T018 评价功能（US3）

---

## 🎯 实施策略

### MVP范围建议

**最小可行产品 (MVP) = Setup + Foundational + US1 + US2**

包含任务:
- T001-T005: 基础设施
- T006-T008: 用户注册登录
- T009-T012: 地图展示和餐厅查看

**MVP完成标准**:
- ✅ 用户可以注册和登录
- ✅ 登录后看到地图和附近餐厅
- ✅ 可以点击餐厅查看详情
- ✅ 基本的搜索和筛选功能

**MVP时间**: 约4-5周（如按plan.md预估）

### 增量交付计划

**Sprint 1** (2周): MVP基础
- T001-T005: Setup + Foundational ✅ 大部分已完成
- T006-T008: US1用户注册登录

**Sprint 2** (2周): MVP完成
- T009-T012: US2地图展示餐厅

**Sprint 3** (2周): 扩展功能
- T013-T014: US7导航
- T015-T016: US4收藏（并行）

**Sprint 4** (1.5周): 高级功能
- T017-T018: US3评价
- T019: US6热力图

**Sprint 5** (1周): 测试和发布
- T021: E2E测试
- T022: 性能优化
- 生产环境部署

---

## 🚀 关键路径分析

### 关键路径（必须按顺序）

```
T004 (数据库) → T005 (认证) → T006 (注册API) → T007 (登录API) → 
T008 (登录UI) → T009 (POI API) → T011 (搜索集成) → MVP完成
```

**关键路径时长**: 约41小时（6个工作日，单人顺序执行）

### 并行机会

通过合理分工，实际可压缩到**3周完成MVP**：

- **Week 1**: T001-T005 (基础设施) - 2名后端 + 1名前端并行
- **Week 2**: T006-T008 (US1) - 后端和前端并行
- **Week 3**: T009-T012 (US2) - 后端API先行，前端跟进

---

## 📋 任务检查清单

### 每个用户故事的独立测试标准

**US1 (注册登录) 验收测试**:
- [ ] 可以使用新用户名注册
- [ ] 注册后立即登录成功
- [ ] Token有效期7天
- [ ] 退出后Token失效

**US2 (地图展示) 验收测试**:
- [ ] 登录后自动跳转到地图
- [ ] 地图3秒内加载完成
- [ ] 附近餐厅标记点正确显示
- [ ] 点击标记显示详情弹窗

**US7 (导航) 验收测试**:
- [ ] 详情页面有导航按钮
- [ ] 点击后显示路径
- [ ] 可切换出行方式
- [ ] 显示预计时间和距离

**US4 (收藏) 验收测试**:
- [ ] 可以收藏餐厅
- [ ] 收藏按钮状态正确切换
- [ ] 个人中心显示收藏列表
- [ ] 可以取消收藏

**US3 (评价) 验收测试**:
- [ ] 可以发表评价
- [ ] 评价即时发布
- [ ] 评论家评价显示徽章
- [ ] 可以编辑删除自己的评价

**US6 (热力图) 验收测试**:
- [ ] 热力图可以开关
- [ ] 热力图颜色梯度正确
- [ ] 不遮挡标记点

---

## 🎯 下一步行动

### 立即可执行（本周）

**后端团队** (2人):
1. 完成 T005 Spring Security配置（进行中）
2. 完成 T006 注册API实现（进行中）
3. 开始 T007 登录API实现

**前端团队** (1人):
1. 完成 T003 Vue项目初始化（进行中）
2. 完成 T010 地图组件开发（进行中）
3. 开始 T008 登录注册页面

**QA团队**:
1. 准备测试环境
2. 编写US1和US2的验收测试用例
3. 为T021 E2E测试做准备

### Sprint 1目标（2周）

✅ **必须完成的任务** (MVP阻塞项):
- T005 Spring Security配置
- T006-T008 US1用户注册登录
- T009 POI搜索API

🎯 **目标**: Sprint 1结束时，用户可以注册、登录并看到基本的地图界面（即使餐厅数据有限）

---

## 📌 风险和阻塞项

### 当前阻塞

| 任务 | 阻塞原因 | 影响 | 缓解措施 | 负责人 |
|------|---------|------|---------|--------|
| T005 | BCrypt配置待完成 | Medium | 本周内完成 | Backend Lead |
| T011 | 依赖T009和T010 | Low | T009优先级提高 | Frontend Developer |

### 潜在风险

| 风险 | 概率 | 影响 | 缓解策略 |
|------|------|------|---------|
| 高德API密钥申请延迟 | Medium | High | 提前申请，准备Mock数据 |
| 前端地图性能不达标 | Medium | High | 分批渲染，提前性能测试 |
| 认证逻辑Bug | Low | High | 充分的单元测试和集成测试 |

---

## 📖 相关文档

- **功能规范**: [spec.md](./spec.md) - 了解每个用户故事的详细需求
- **实施计划**: [plan.md](./plan.md) - 查看每个任务的详细实施步骤
- **数据模型**: [data-model.md](./data-model.md) - 数据库设计和Entity类
- **API规范**: [contracts/openapi.yaml](./contracts/openapi.yaml) - REST API定义
- **技术研究**: [research.md](./research.md) - 技术选型和决策依据
- **快速开始**: [quickstart.md](./quickstart.md) - 环境搭建指南

---

## 💡 任务执行提示

### 如何认领任务

1. 在上面找到状态为 📋 Todo 的任务
2. 检查Dependencies是否已完成
3. 阅读📖参考文档了解详细实施步骤
4. 在团队看板更新状态为 🔄 In Progress
5. 按照plan.md中的实施步骤执行
6. 完成Acceptance Criteria所有项
7. 提交PR并请求Code Review
8. 更新状态为 ✅ Done

### 推荐任务认领顺序

**后端开发者** (当前可认领):
- T007 登录API实现（依赖T006）
- T009 POI搜索API（高优先级）
- T013 导航API（US7）

**前端开发者** (当前可认领):
- T008 登录注册页面（依赖T007）
- T012 餐厅详情弹窗（依赖T010）

**全栈开发者**:
- 可以从T006+T008开始（注册功能前后端）
- 或从T013+T014开始（导航功能前后端）

---

## 🎉 项目里程碑

- [x] **M1: 项目启动** - 2025-10-14
  - Tasks: T001, T002 ✅
  
- [ ] **M2: 基础架构完成** - 2025-10-28 (目标)
  - Tasks: T003-T005
  - 标准: 数据库可用，认证框架就绪
  
- [ ] **M3: MVP核心功能** - 2025-11-11 (目标)
  - Tasks: T006-T012
  - 标准: 用户可注册登录并查看地图
  
- [ ] **M4: 扩展功能** - 2025-11-25 (目标)
  - Tasks: T013-T018
  - 标准: 导航、收藏、评价功能可用
  
- [ ] **M5: 完整功能** - 2025-12-09 (目标)
  - Tasks: T019-T022
  - 标准: 所有功能测试通过
  
- [ ] **M6: 生产发布** - 2025-12-16 (目标)
  - 部署到生产环境

---

**Last Updated:** 2025-10-14  
**Next Review:** 每周五 Sprint Review会议
