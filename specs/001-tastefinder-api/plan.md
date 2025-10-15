# Implementation Plan: TasteFinder 美食推荐平台

**Created:** 2025-10-14  
**Owner:** TasteFinder Development Team  
**Status:** In Progress

---

## 🚀 如何使用本计划

### 新开发者入门流程

如果你是第一次接触本项目，**请按照以下顺序阅读文档**：

1. **[quickstart.md](./quickstart.md)** (5分钟) - 搭建本地环境
   - 安装必要软件（JDK 17, Node.js 18+, Docker）
   - 一键启动数据库和Redis
   - 运行后端和前端
   - 验证系统可用

2. **[spec.md](./spec.md)** (15分钟) - 理解产品需求
   - 阅读功能规范，了解我们要做什么
   - 查看Clarifications了解关键决策
   - 理解5个用户角色和核心功能

3. **[research.md](./research.md)** (20分钟) - 理解技术选型
   - 为什么选择Spring Boot和Vue 3
   - JWT认证方案设计（第7节）
   - 高德地图集成方案（第6节）
   - Redis缓存策略（第4节）

4. **[data-model.md](./data-model.md)** (15分钟) - 数据库设计
   - 5个核心表的结构和关系
   - Entity类代码示例
   - 索引和性能优化策略

5. **[contracts/openapi.yaml](./contracts/openapi.yaml)** (10分钟) - API规范
   - 所有REST API endpoints定义
   - 请求和响应格式
   - 可导入Postman/Swagger测试

6. **本文档 (plan.md)** (10分钟) - 实施计划
   - 7个Phase的详细任务
   - 每个任务都有📖参考链接指向详细文档
   - 时间线和里程碑

7. **[tasks.md](./tasks.md)** (5分钟) - 当前任务看板
   - 查看任务进度（已完成/进行中/待开始）
   - 认领适合你的任务

### 👥 不同角色的工作路径

**🔧 后端开发者**:
```
quickstart.md → research.md(JWT/Redis) → data-model.md → contracts/openapi.yaml
                           ↓
              开始Phase 1-2: 环境搭建 + 用户认证
```

**🎨 前端开发者**:
```
quickstart.md → research.md(Vue3/Element Plus) → contracts/openapi.yaml → spec.md(用户流程)
                           ↓
              开始Phase 1: Vue项目搭建，然后Phase 2前端部分
```

**🚀 全栈开发者**:
```
快速浏览所有文档 → 优先完成Phase 2后端 → 再做Phase 2前端 → 同时推进Phase 3地图集成
```

**🧪 QA工程师**:
```
spec.md → contracts/openapi.yaml → research.md(测试策略) → Phase 6测试任务
```

### 📌 快速查找指南

| 我想了解... | 查看文档 |
|-------------|---------|
| 如何启动项目 | [quickstart.md](./quickstart.md) |
| 数据库表结构 | [data-model.md](./data-model.md) |
| API接口定义 | [contracts/openapi.yaml](./contracts/openapi.yaml) |
| 为什么这样设计 | [research.md](./research.md) |
| 功能需求细节 | [spec.md](./spec.md) |
| 当前任务状态 | [tasks.md](./tasks.md) |
| 常见问题解答 | [quickstart.md#常见问题](./quickstart.md#常见问题) |

---

## Overview

### Objective

实现一个基于地理位置的多用户美食推荐平台，集成高德地图API提供实时餐厅数据、地图展示、路径规划和用户评价功能。系统采用前后端分离架构，使用Spring Boot构建RESTful API后端，Vue.js构建响应式前端，MySQL作为主数据库，通过Docker实现环境一致性。

### Success Criteria

- [ ] 用户可以注册、登录并使用完整功能（无邮箱验证流程）
- [ ] 地图加载时间 < 3秒，搜索响应 < 1秒
- [ ] 高德地图API集成完成，实时获取餐厅POI数据
- [ ] 用户可以收藏餐厅、发表评价、使用导航功能
- [ ] 代码测试覆盖率 ≥ 80%，所有lint检查通过
- [ ] 系统支持1000+并发用户访问
- [ ] WCAG 2.1 AA无障碍标准验证通过
- [ ] 成功部署到生产环境并运行稳定

### Constitution Alignment Check

**Code Quality Excellence:**
- [x] Code standards and linting configured (Checkstyle for Java, ESLint for Vue)
- [x] Documentation requirements defined (Javadoc for backend, JSDoc for frontend)
- [x] Dependency management strategy specified (Maven for Spring Boot, npm for Vue)

**Comprehensive Testing Standards:**
- [x] Unit test coverage targets identified (≥80% for new code)
- [x] Integration test scenarios defined (API endpoints, 高德地图API集成)
- [x] Performance test baselines established (地图加载<3s, API响应<200ms)

**User Experience Consistency:**
- [x] Design system components identified (Element Plus for Vue)
- [x] Accessibility requirements verified (WCAG 2.1 AA标准)
- [x] User flow validated against patterns (注册→登录→浏览→收藏→评价)

**Performance Requirements:**
- [x] Performance budgets defined (地图加载<3s, API<200ms p95)
- [x] Resource usage limits specified (数据库连接池, 缓存策略)
- [x] Monitoring and alerting planned (Prometheus + Grafana)

---

## Phases

### Phase 1: 环境搭建与基础架构
**Duration:** 2周  
**Dependencies:** NONE

**Tasks:**

#### 1.1 ✅ Docker环境配置
**状态**: 已完成  
📖 **参考**: [quickstart.md - Docker完整部署](./quickstart.md#docker-完整部署)

#### 1.2 ✅ Spring Boot项目初始化  
**状态**: 已完成  
📖 **参考**: [research.md#1-后端框架选择](./research.md#1-后端框架选择-spring-boot)

#### 1.3 🔄 Vue 3项目脚手架搭建
**状态**: 进行中  
📖 **参考**: [research.md#2-前端框架选择](./research.md#2-前端框架选择-vue-3)

**实施步骤**:
```bash
npm create vite@latest frontend -- --template vue
cd frontend
npm install vue-router@4 pinia element-plus axios
npm install -D @vitejs/plugin-vue
```

**配置清单**:
- [ ] Vue Router配置（登录、地图、个人中心路由）
- [ ] Pinia store配置（用户状态、地图状态）
- [ ] Element Plus全局注册
- [ ] Axios拦截器（添加JWT token）
- [ ] 环境变量配置（.env.development）

#### 1.4 📋 数据库设计  
**状态**: 待开始  
📖 **参考**: 
- [data-model.md](./data-model.md) - **完整数据库设计文档**
- [data-model.md#数据库初始化脚本](./data-model.md#数据库初始化脚本) - SQL脚本

**实施步骤**:
1. **阅读数据模型文档**（15分钟）
   - 理解5个核心表：users, restaurant_favorites, restaurant_reviews, review_images, browse_history
   - 理解表之间的关系（用户 1:N 收藏/评价）

2. **创建Flyway迁移脚本**（30分钟）
   ```bash
   # 从 data-model.md 复制 schema.sql 到:
   backend/src/main/resources/db/migration/V1__init_schema.sql
   
   # 复制测试数据脚本:
   backend/src/main/resources/db/migration/V2__init_test_data.sql
   ```

3. **创建JPA Entity类**（60分钟）
   - 参考 data-model.md 中的Entity代码示例
   - 创建文件：
     - `backend/src/main/java/com/tastefinder/entity/User.java`
     - `backend/src/main/java/com/tastefinder/entity/RestaurantFavorite.java`
     - `backend/src/main/java/com/tastefinder/entity/RestaurantReview.java`
     - `backend/src/main/java/com/tastefinder/entity/ReviewImage.java`

4. **配置Flyway**（15分钟）
   ```yaml
   # application.yml
   spring:
     flyway:
       enabled: true
       baseline-on-migrate: true
       locations: classpath:db/migration
   ```

5. **验证数据库初始化**
   ```bash
   # 启动应用，查看日志
   # 应看到: Flyway: Successfully applied 2 migrations
   
   # 验证表结构
   docker-compose exec mysql mysql -uroot -ppassword tastefinder
   mysql> SHOW TABLES;  # 应该看到5个表
   mysql> SELECT * FROM users;  # 应该看到5个预设用户
   ```

**完成标准**:
- [ ] 5个表全部创建成功
- [ ] 5个预设测试用户已插入
- [ ] Entity类与数据库字段完全对应
- [ ] Repository接口已创建（UserRepository等）

#### 1.5 📋 Spring Security配置  
**状态**: 待开始  
📖 **参考**: [research.md#7-认证方案jwt](./research.md#7-认证方案jwt)

**实施步骤**:
1. **添加依赖**（5分钟）
   ```xml
   <!-- pom.xml -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-api</artifactId>
       <version>0.11.5</version>
   </dependency>
   ```

2. **创建JWT工具类**（45分钟）
   ```java
   // JwtTokenProvider.java
   // - generateToken(User user) → String
   // - validateToken(String token) → boolean
   // - getUserIdFromToken(String token) → Long
   ```
   参考 research.md 中的Token设计：
   - 使用HS256算法
   - Payload包含：userId, username, roles
   - 有效期7天

3. **创建SecurityConfig**（60分钟）
   ```java
   // SecurityConfig.java
   // - 配置哪些URL需要认证
   // - 配置JWT过滤器
   // - 配置密码加密器（BCryptPasswordEncoder）
   ```

4. **创建JwtAuthenticationFilter**（45分钟）
   - 从请求头提取token
   - 验证token有效性
   - 设置Authentication到SecurityContext

**完成标准**:
- [ ] /api/auth/** 路径无需认证
- [ ] 其他/api/**路径需要JWT token
- [ ] Token验证失败返回401
- [ ] 密码使用BCrypt加密存储

#### 1.6 📋 API接口规范定义  
**状态**: 待开始  
📖 **参考**: [contracts/openapi.yaml](./contracts/openapi.yaml) - **已完成的API规范**

**实施步骤**:
1. **集成Springdoc OpenAPI**（20分钟）
   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.2.0</version>
   </dependency>
   ```

2. **配置Swagger UI**（10分钟）
   ```yaml
   springdoc:
     api-docs:
       path: /api-docs
     swagger-ui:
       path: /swagger-ui.html
   ```

3. **为Controller添加注解**（60分钟）
   - 参考 contracts/openapi.yaml 中的API定义
   - 为每个endpoint添加：
     ```java
     @Operation(summary = "用户注册", description = "创建新用户账户")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "注册成功"),
         @ApiResponse(responseCode = "409", description = "用户名已存在")
     })
     ```

4. **验证API文档**
   - 访问 http://localhost:8080/swagger-ui.html
   - 确保所有endpoint可见
   - 测试"Try it out"功能

**完成标准**:
- [ ] Swagger UI可访问
- [ ] 所有API endpoint已文档化
- [ ] 请求/响应示例完整
- [ ] 前端可以根据openapi.yaml生成API client

**Deliverables:**
- Docker Compose配置文件（MySQL + Redis）
- Spring Boot项目骨架（含Security配置）
- Vue前端项目骨架（含路由和状态管理）
- 数据库初始化SQL脚本
- API接口文档（Swagger UI）

**Risks:**
- Docker环境配置问题 - Mitigation: 使用官方镜像，提供详细文档
- Spring Security配置复杂 - Mitigation: 参考成熟方案，简化初期权限设计

---

### Phase 2: 用户认证系统
**Duration:** 1.5周  
**Dependencies:** 
- ✅ Phase 1.4: users表已创建
- ✅ Phase 1.5: Spring Security已配置
- ✅ Phase 1.6: /auth/register和/auth/login API规范已定义

**Tasks:**

#### 2.1 ✅ 用户实体类与Repository层开发
**状态**: 已完成  
📖 **参考**: [data-model.md#1-用户表-users](./data-model.md#1-用户表-users)

#### 2.2 🔄 注册功能实现
**状态**: 进行中  
📖 **参考**: 
- [contracts/openapi.yaml - POST /auth/register](./contracts/openapi.yaml) 
- [spec.md - FR1用户身份认证](./spec.md#fr1-用户身份认证与角色管理)

**实施步骤**:
1. **创建DTO类**（15分钟）
   ```java
   // RegisterRequest.java
   public class RegisterRequest {
       @NotBlank @Size(min=3, max=50)
       private String username;
       
       @NotBlank @Size(min=8, max=50)
       private String password;
       
       @Size(max=100)
       private String nickname;
   }
   ```

2. **创建UserService.register()**（30分钟）
   ```java
   public UserDTO register(RegisterRequest request) {
       // 1. 检查用户名是否已存在
       if (userRepository.existsByUsername(request.getUsername())) {
           throw new UsernameAlreadyExistsException();
       }
       
       // 2. 加密密码（BCrypt）
       String encryptedPassword = passwordEncoder.encode(request.getPassword());
       
       // 3. 创建User对象，默认角色为NORMAL_USER
       User user = new User();
       user.setUsername(request.getUsername());
       user.setPassword(encryptedPassword);
       user.setRoleType(RoleType.NORMAL_USER);
       
       // 4. 保存到数据库
       return userRepository.save(user);
   }
   ```

3. **创建AuthController.register()**（20分钟）
   - 参考 contracts/openapi.yaml 中的请求/响应格式
   - 返回201状态码和用户信息（不返回密码）

4. **测试**（30分钟）
   - 单元测试：UserServiceTest.testRegister()
   - 集成测试：AuthControllerTest.testRegister()
   - Postman测试手动验证

**完成标准**:
- [x] 用户名唯一性验证工作
- [ ] 密码使用BCrypt加密
- [ ] 注册后自动分配NORMAL_USER角色
- [ ] 返回数据不包含密码字段
- [ ] 测试覆盖率≥80%

#### 2.3 📋 登录功能实现  
**状态**: 待开始  
📖 **参考**: 
- [research.md#7-认证方案jwt](./research.md#7-认证方案jwt)
- [contracts/openapi.yaml - POST /auth/login](./contracts/openapi.yaml)

**前置条件**:
- [ ] Phase 1.5: JwtTokenProvider已实现
- [ ] Phase 2.2: 用户注册功能已完成

**实施步骤**:
1. **创建AuthService.login()**（45分钟）
   ```java
   public AuthResponse login(String username, String password) {
       // 1. 根据用户名查找用户
       User user = userRepository.findByUsername(username)
           .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));
       
       // 2. 验证密码
       if (!passwordEncoder.matches(password, user.getPassword())) {
           throw new BadCredentialsException("用户名或密码错误");
       }
       
       // 3. 生成JWT token
       String token = jwtTokenProvider.generateToken(user);
       
       // 4. 更新最后登录时间
       user.setLastLoginAt(LocalDateTime.now());
       userRepository.save(user);
       
       // 5. 返回token和用户信息
       return new AuthResponse(token, userMapper.toDTO(user));
   }
   ```

2. **创建AuthController.login()**（20分钟）
   - 接收LoginRequest（username + password）
   - 调用AuthService.login()
   - 返回AuthResponse（token + user）

3. **测试JWT token验证**（30分钟）
   - 使用生成的token访问需要认证的API
   - 验证token过期后被拒绝
   - 验证无效token被拒绝

**完成标准**:
- [ ] 登录成功返回有效JWT token
- [ ] 用户名或密码错误返回401
- [ ] Token包含userId, username, roleType
- [ ] Token有效期为7天
- [ ] 最后登录时间已更新

#### 2.4 📋 前端登录/注册页面开发  
**状态**: 待开始  
📖 **参考**: 
- [research.md#8-ui组件库element-plus](./research.md#8-ui组件库element-plus)
- [spec.md - 主流程：注册、登录并发现餐厅](./spec.md#主流程注册登录并发现餐厅)

**实施步骤**:
1. **创建LoginView.vue**（60分钟）
   ```vue
   <template>
     <el-form :model="form" :rules="rules" ref="formRef">
       <el-form-item label="用户名" prop="username">
         <el-input v-model="form.username" />
       </el-form-item>
       <el-form-item label="密码" prop="password">
         <el-input type="password" v-model="form.password" show-password />
       </el-form-item>
       <el-button type="primary" @click="handleLogin">登录</el-button>
     </el-form>
   </template>
   ```
   - 使用Element Plus表单组件
   - 实时验证（用户名3-50字符，密码8-50字符）
   - 添加"记住我"选项（localStorage存储token）

2. **创建RegisterView.vue**（60分钟）
   - 类似LoginView结构
   - 添加密码强度指示器（弱/中/强）
   - 密码确认字段验证

3. **创建Auth Store**（45分钟）
   ```javascript
   // stores/auth.js
   export const useAuthStore = defineStore('auth', {
     state: () => ({
       user: null,
       token: localStorage.getItem('token')
     }),
     actions: {
       async login(username, password) {
         const res = await axios.post('/api/auth/login', { username, password });
         this.token = res.data.token;
         this.user = res.data.user;
         localStorage.setItem('token', this.token);
       }
     }
   });
   ```

4. **配置Axios拦截器**（30分钟）
   ```javascript
   // api/axios.js
   axios.interceptors.request.use(config => {
     const token = localStorage.getItem('token');
     if (token) {
       config.headers.Authorization = `Bearer ${token}`;
     }
     return config;
   });
   ```

**完成标准**:
- [ ] 登录表单验证工作正常
- [ ] 注册表单显示密码强度
- [ ] 登录成功后token存储在localStorage
- [ ] Axios自动在请求头添加token
- [ ] 登录成功后跳转到地图页面

#### 2.5 📋 角色权限管理  
**状态**: 待开始  
📖 **参考**: 
- [spec.md - FR1用户身份认证（5个角色）](./spec.md#fr1-用户身份认证与角色管理)
- [data-model.md - 角色类型枚举](./data-model.md#角色类型枚举)

**实施步骤**:
1. **创建RoleType枚举**（10分钟）
   ```java
   public enum RoleType {
       NORMAL_USER,        // 普通用户
       FOOD_CRITIC_A,      // 美食评论家A
       FOOD_CRITIC_B,      // 美食评论家B（西餐专家）
       SENIOR_FOODIE,      // 资深食客
       MERCHANT            // 商家代表
   }
   ```

2. **实现RBAC权限检查**（60分钟）
   ```java
   // 在Controller中使用
   @PreAuthorize("hasRole('FOOD_CRITIC_A') or hasRole('FOOD_CRITIC_B')")
   public ReviewDTO postCertifiedReview(@RequestBody ReviewRequest request) {
       // 只有评论家可以发布认证评价
   }
   ```

3. **前端根据角色显示不同功能**（45分钟）
   ```vue
   <el-button v-if="user.roleType === 'MERCHANT'">
     管理我的餐厅
   </el-button>
   ```

**完成标准**:
- [ ] 5个角色枚举已定义
- [ ] 新注册用户默认为NORMAL_USER
- [ ] 评论家评价自动标记is_certified_review=true
- [ ] 前端根据角色显示/隐藏功能
- [ ] 5个预设测试账户可用于测试不同角色

**Deliverables:**
- 用户认证API endpoints（/api/auth/register, /api/auth/login）
- JWT token管理服务
- 前端登录/注册组件
- 5个预设测试账户初始化脚本
- 认证集成测试套件

**Risks:**
- JWT token安全性 - Mitigation: 设置合理过期时间，实现token刷新机制
- 密码找回困难（无邮箱） - Mitigation: 文档明确说明，管理员重置流程

---

### Phase 3: 高德地图集成
**Duration:** 2周  
**Dependencies:** 
- ✅ Phase 1.1: Docker环境已配置（Redis可用）
- ✅ Phase 1.4: 数据库表已创建
- 🔄 Phase 2.2: 用户认证完成（地图页面需要登录）

**Tasks:**

#### 3.1 🔄 高德地图API密钥申请与配置
**状态**: 进行中  
📖 **参考**: [research.md#6-高德地图api集成方案](./research.md#6-高德地图api集成方案)

**实施步骤**:
1. **申请API密钥**（30分钟 + 等待审批）
   - 访问: https://console.amap.com/dev/key/app
   - 创建应用，选择"Web服务"和"Web端(JS API)"
   - 记录Key和Secret

2. **配置后端**（10分钟）
   ```yaml
   # backend/src/main/resources/application.yml
   amap:
     key: your-web-service-key
     secret: your-web-service-secret
     base-url: https://restapi.amap.com/v3
   ```

3. **配置前端**（5分钟）
   ```env
   # frontend/.env.development
   VITE_AMAP_KEY=your-js-api-key
   VITE_AMAP_SECRET=your-js-api-secret
   ```

**完成标准**:
- [x] 高德API密钥已获取
- [ ] 后端配置文件已更新
- [ ] 前端环境变量已配置
- [ ] 测试API调用成功（POI搜索测试）

#### 3.2 📋 地图显示组件开发
**状态**: 待开始  
📖 **参考**: 
- [research.md#6-高德地图api集成方案](./research.md#6-高德地图api集成方案)
- [spec.md - FR2交互式地图餐厅展示](./spec.md#fr2-交互式地图餐厅展示)

**前置条件**:
- [ ] Phase 3.1: 高德JS API Key已配置
- [ ] Phase 1.3: Vue项目已搭建

**实施步骤**:
1. **安装高德地图加载器**（5分钟）
   ```bash
   npm install @amap/amap-jsapi-loader
   ```

2. **创建MapView.vue组件**（120分钟）
   ```vue
   <template>
     <div id="map-container" style="width: 100%; height: 600px;"></div>
   </template>
   
   <script setup>
   import AMapLoader from '@amap/amap-jsapi-loader';
   import { onMounted, ref } from 'vue';
   
   const map = ref(null);
   const markers = ref([]);
   
   onMounted(async () => {
     const AMap = await AMapLoader.load({
       key: import.meta.env.VITE_AMAP_KEY,
       version: '2.0',
       plugins: ['AMap.Geolocation', 'AMap.Marker']
     });
     
     // 初始化地图
     map.value = new AMap.Map('map-container', {
       zoom: 13,
       center: [116.397470, 39.908823]  // 北京天安门
     });
   });
   </script>
   ```

3. **实现标记点渲染**（60分钟）
   ```javascript
   const addRestaurantMarkers = (restaurants) => {
     // 清除旧标记
     map.value.clearMap();
     
     // 添加新标记（最多200个）
     restaurants.slice(0, 200).forEach(restaurant => {
       const marker = new AMap.Marker({
         position: [restaurant.location.longitude, restaurant.location.latitude],
         title: restaurant.name,
         extData: { poiId: restaurant.poiId }
       });
       
       // 点击标记显示详情
       marker.on('click', (e) => {
         showRestaurantDetail(e.target.getExtData().poiId);
       });
       
       map.value.add(marker);
     });
   };
   ```

4. **实现地图交互**（45分钟）
   - 缩放控制（AMap.ToolBar）
   - 定位按钮（AMap.Geolocation）
   - 地图拖拽更新餐厅列表

**完成标准**:
- [ ] 地图成功加载（<3秒）
- [ ] 标记点可点击
- [ ] 地图支持缩放、拖拽
- [ ] 定位按钮工作正常
- [ ] 标记点超过200个时分批加载

#### 3.3 📋 POI搜索服务开发
**状态**: 待开始  
📖 **参考**: 
- [research.md#6-高德地图api集成方案](./research.md#6-高德地图api集成方案)
- [contracts/openapi.yaml - GET /restaurants/search](./contracts/openapi.yaml)
- 高德API文档: https://lbs.amap.com/api/webservice/guide/api/search

**前置条件**:
- [ ] Phase 3.1: 高德Web服务Key已配置
- [ ] Phase 3.5: Redis缓存已配置

**实施步骤**:
1. **创建AMapPOIService类**（90分钟）
   ```java
   @Service
   public class AMapPOIService {
       @Value("${amap.key}")
       private String amapKey;
       
       @Autowired
       private RestTemplate restTemplate;
       
       @Autowired
       private RedisTemplate<String, Object> redisTemplate;
       
       @Cacheable(value = "poi_search", key = "#lat + ':' + #lon + ':' + #radius")
       public List<Restaurant> searchNearby(double lat, double lon, int radius) {
           // 1. 构造高德API请求URL
           String url = String.format(
               "https://restapi.amap.com/v3/place/around?key=%s&location=%f,%f&radius=%d&types=餐饮服务",
               amapKey, lon, lat, radius
           );
           
           // 2. 调用API
           AMapPOIResponse response = restTemplate.getForObject(url, AMapPOIResponse.class);
           
           // 3. 转换为Restaurant对象
           return response.getPois().stream()
               .map(this::convertToRestaurant)
               .collect(Collectors.toList());
       }
       
       private Restaurant convertToRestaurant(AMapPOI poi) {
           // 转换高德POI数据为我们的Restaurant DTO
       }
   }
   ```

2. **创建RestaurantController**（45分钟）
   ```java
   @RestController
   @RequestMapping("/api/restaurants")
   public class RestaurantController {
       @GetMapping("/search")
       public ResponseEntity<SearchResponse> search(
           @RequestParam double latitude,
           @RequestParam double longitude,
           @RequestParam(defaultValue = "5000") int radius) {
           
           List<Restaurant> restaurants = amapPOIService.searchNearby(
               latitude, longitude, radius
           );
           return ResponseEntity.ok(new SearchResponse(restaurants));
       }
   }
   ```

3. **配置Redis缓存**（见Task 3.5）

4. **测试**（60分钟）
   - Mock高德API响应进行单元测试
   - 使用真实API进行集成测试
   - 验证缓存策略生效（第二次调用从Redis读取）

**完成标准**:
- [ ] POI搜索API返回餐厅列表
- [ ] 数据格式符合contracts/openapi.yaml定义
- [ ] 缓存策略生效（30分钟TTL）
- [ ] 高德API调用失败时有友好错误提示
- [ ] 测试覆盖率≥80%

#### 3.4 📋 地理位置服务
**状态**: 待开始  
📖 **参考**: [spec.md - FR3基于位置的餐厅推荐](./spec.md#fr3-基于位置的餐厅推荐)

**实施步骤**:
1. **前端实现浏览器定位**（45分钟）
   ```javascript
   // composables/useGeolocation.js
   export function useGeolocation() {
     const location = ref(null);
     const error = ref(null);
     
     const getCurrentLocation = () => {
       if (!navigator.geolocation) {
         error.value = '您的浏览器不支持定位功能';
         return;
       }
       
       navigator.geolocation.getCurrentPosition(
         (position) => {
           location.value = {
             lat: position.coords.latitude,
             lon: position.coords.longitude
           };
         },
         (err) => {
           error.value = '获取位置失败，请检查权限设置';
         }
       );
     };
     
     return { location, error, getCurrentLocation };
   }
   ```

2. **添加定位按钮到地图**（30分钟）
   - 使用AMap.Geolocation插件
   - 点击按钮定位到当前位置
   - 在地图上显示用户位置标记

3. **位置权限降级方案**（45分钟）
   - 如果用户拒绝定位，提供手动输入位置
   - 或提供热门地点快速选择（天安门、王府井等）

**完成标准**:
- [ ] 用户可以授权获取当前位置
- [ ] 定位成功后地图自动定位
- [ ] 定位失败提供降级方案（手动输入）
- [ ] 定位后自动搜索附近餐厅

#### 3.5 📋 餐厅数据缓存策略
**状态**: 待开始  
📖 **参考**: 
- [research.md#4-缓存策略redis](./research.md#4-缓存策略redis)
- [research.md#10-性能优化策略](./research.md#10-性能优化策略)

**前置条件**:
- [ ] Phase 1.1: Redis容器已启动
- [ ] Phase 3.3: POI搜索服务已实现

**实施步骤**:
1. **配置Redis**（30分钟）
   ```java
   @Configuration
   @EnableCaching
   public class RedisCacheConfig {
       @Bean
       public CacheManager cacheManager(RedisConnectionFactory factory) {
           RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
               .entryTtl(Duration.ofMinutes(30))  // POI搜索缓存30分钟
               .serializeValuesWith(/* Jackson序列化 */);
           
           return RedisCacheManager.builder(factory)
               .cacheDefaults(config)
               .build();
       }
   }
   ```

2. **添加缓存注解**（15分钟）
   ```java
   @Cacheable(value = "poi_search", key = "#lat + ':' + #lon + ':' + #radius")
   public List<Restaurant> searchNearby(double lat, double lon, int radius) {
       // POI搜索逻辑
   }
   
   @CacheEvict(value = "restaurant_detail", key = "#poiId")
   public void updateRestaurant(String poiId) {
       // 更新时清除缓存
   }
   ```

3. **定义缓存Key命名规范**（15分钟）
   ```
   tastefinder:poi:search:{lat}:{lon}:{radius}
   tastefinder:restaurant:{poiId}
   tastefinder:user:favorites:{userId}
   tastefinder:reviews:{poiId}:page:{page}
   ```

4. **测试缓存效果**（45分钟）
   - 第一次调用：查看高德API日志，确认调用
   - 第二次调用：查看Redis日志，确认命中缓存
   - 测试缓存过期：30分钟后重新调用API

**完成标准**:
- [ ] Redis缓存配置生效
- [ ] POI搜索结果缓存30分钟
- [ ] 缓存命中率>80%（生产环境）
- [ ] 高德API调用量减少80%
- [ ] 缓存Key命名规范文档化

#### 3.6 📋 地图性能优化
**状态**: 待开始  
📖 **参考**: 
- [research.md#10-性能优化策略](./research.md#10-性能优化策略)
- [spec.md - FR2交互式地图餐厅展示（性能要求）](./spec.md#fr2-交互式地图餐厅展示)

**实施步骤**:
1. **标记点分批渲染**（60分钟）
   ```javascript
   const renderMarkers = (restaurants) => {
     const batchSize = 50;
     let index = 0;
     
     const renderBatch = () => {
       const batch = restaurants.slice(index, index + batchSize);
       batch.forEach(r => addMarker(r));
       index += batchSize;
       
       if (index < restaurants.length && index < 200) {
         requestIdleCallback(renderBatch);  // 空闲时渲染下一批
       }
     };
     
     renderBatch();
   };
   ```

2. **实现标记点聚合**（90分钟）
   ```javascript
   // 使用AMap.MarkerClusterer插件
   import MarkerClusterer from '@amap/amap-jsapi-loader/extensions/MarkerClusterer';
   
   const cluster = new MarkerClusterer(map, markers, {
     gridSize: 60,
     renderMarker: (context) => {
       // 自定义聚合标记样式
     }
   });
   ```

3. **懒加载优化**（45分钟）
   - 地图组件懒加载（用户点击"查看地图"后才初始化）
   - 仅渲染可视区域内的标记
   - 地图拖动时才加载新区域的餐厅

4. **性能测试**（60分钟）
   - 测试200个标记点渲染时间
   - 测试地图缩放和拖拽流畅度
   - 使用Chrome DevTools Performance分析

**完成标准**:
- [ ] 同时显示200个标记点时地图仍然流畅
- [ ] 地图初始加载时间<3秒
- [ ] 标记点渲染使用分批策略
- [ ] 超过200个餐厅时启用聚合显示
- [ ] 性能测试通过（FPS≥30）

**Deliverables:**
- 高德地图Vue组件
- POI搜索API service
- 地理位置服务封装
- Redis缓存配置
- 地图性能测试报告

**Risks:**
- 高德API配额限制 - Mitigation: 实现请求合并、缓存策略，监控用量
- 地图加载性能 - Mitigation: 分批渲染，限制同时显示标记点数量（<200）

---

### Phase 4: 餐厅收藏与评价
**Duration:** 2周  
**Dependencies:** Phase 2, Phase 3 completion

**Tasks:**
1. 📋 **待开始** - 收藏功能后端：保存POI ID + 快照数据
2. 📋 **待开始** - 评价功能后端：评分、文字、图片上传
3. 📋 **待开始** - 前端餐厅详情弹窗：展示信息、收藏按钮、评价表单
4. 📋 **待开始** - 图片存储服务集成：OSS或本地存储方案
5. 📋 **待开始** - 评价内容过滤：关键词敏感词过滤
6. 📋 **待开始** - 个人中心页面：我的收藏、我的评价列表

**Deliverables:**
- 收藏管理API（增删查）
- 评价管理API（CRUD + 图片上传）
- 餐厅详情组件
- 个人中心页面
- 内容过滤规则配置

**Risks:**
- 图片存储成本 - Mitigation: 限制图片大小和数量，实现压缩
- 恶意评价 - Mitigation: 先发后审 + 关键词过滤 + 人工抽检

---

### Phase 5: 路径规划与搜索
**Duration:** 1.5周  
**Dependencies:** Phase 3 completion

**Tasks:**
1. 📋 **待开始** - 路径规划API集成：调用高德导航API
2. 📋 **待开始** - 前端导航组件：路径可视化、多种出行方式
3. 📋 **待开始** - 搜索功能实现：区域、餐厅名、菜系搜索
4. 📋 **待开始** - 搜索自动补全：前端防抖 + 后端缓存
5. 📋 **待开始** - 筛选排序功能：按距离、评分、价格筛选

**Deliverables:**
- 导航功能前后端实现
- 搜索API（支持多条件查询）
- 自动补全组件
- 筛选器组件

**Risks:**
- 导航API响应慢 - Mitigation: 异步加载，提供loading提示
- 搜索性能 - Mitigation: 全文索引、查询缓存

---

### Phase 6: 测试与优化
**Duration:** 2周  
**Dependencies:** Phase 1-5 completion

**Tasks:**
1. 📋 **待开始** - 单元测试补充：确保≥80%覆盖率
2. 📋 **待开始** - 集成测试：API endpoints端到端测试
3. 📋 **待开始** - 性能测试：JMeter压测，优化慢查询
4. 📋 **待开始** - 无障碍测试：WCAG 2.1 AA验证
5. 📋 **待开始** - 安全测试：SQL注入、XSS防护验证
6. 📋 **待开始** - 浏览器兼容性测试：Chrome, Safari, Edge

**Deliverables:**
- 完整测试套件（单元+集成+E2E）
- 性能测试报告
- 无障碍测试报告
- 安全审计报告
- Bug修复清单

**Risks:**
- 测试覆盖率不足 - Mitigation: 早期集成CI/CD，持续监控覆盖率
- 性能瓶颈 - Mitigation: 分阶段优化，优先解决关键路径

---

### Phase 7: 部署与监控
**Duration:** 1周  
**Dependencies:** Phase 6 completion

**Tasks:**
1. 📋 **待开始** - 生产环境Docker配置：nginx反向代理、SSL证书
2. 📋 **待开始** - CI/CD Pipeline搭建：GitHub Actions自动化部署
3. 📋 **待开始** - 监控系统部署：Prometheus + Grafana
4. 📋 **待开始** - 日志系统配置：ELK Stack或简化方案
5. 📋 **待开始** - 数据库备份策略：定时备份、灾难恢复预案

**Deliverables:**
- 生产环境部署文档
- CI/CD pipeline配置
- 监控Dashboard
- 日志查询系统
- 备份恢复流程文档

**Risks:**
- 部署复杂度 - Mitigation: 提供详细runbook，测试环境提前验证
- 监控告警不及时 - Mitigation: 合理设置阈值，分级告警

---

## Technical Approach

### Architecture

**整体架构：前后端分离 + 微服务预留**

```
┌─────────────────────────────────────────────┐
│         前端层 (Vue 3 + Vite)                │
│  - 响应式UI (Element Plus)                   │
│  - 高德地图组件                               │
│  - 状态管理 (Pinia)                          │
└──────────────┬──────────────────────────────┘
               │ HTTP/REST API
┌──────────────▼──────────────────────────────┐
│      后端层 (Spring Boot 3.x)                │
│  - RESTful API                               │
│  - Spring Security + JWT                     │
│  - Service层业务逻辑                          │
└──────────────┬──────────────────────────────┘
               │
    ┌──────────┴──────────┬─────────────┐
    ▼                     ▼             ▼
┌────────┐         ┌─────────┐    ┌──────────┐
│ MySQL  │         │  Redis  │    │ 高德地图  │
│ (用户、 │         │ (缓存)  │    │   API    │
│ 收藏、  │         └─────────┘    └──────────┘
│ 评价)  │
└────────┘
```

**技术选型理由：**
- **Spring Boot 3.x**: 成熟的Java企业级框架，生态完善，易于集成各类服务
- **Vue 3**: 轻量高效的前端框架，组合式API提升代码可维护性
- **MySQL 8.0**: 关系型数据库，ACID保证数据一致性，适合用户、收藏、评价等结构化数据
- **Redis**: 高性能缓存，减少高德API调用，提升响应速度
- **Docker**: 环境一致性，简化部署，便于CI/CD

### Key Technologies

**后端技术栈:**
- **Spring Boot 3.1.5** - 核心框架
  - Spring Web (RESTful API)
  - Spring Security (认证授权)
  - Spring Data JPA (ORM)
  - Spring Cache (缓存抽象)
- **MySQL 8.0** - 主数据库
- **Redis 7.x** - 缓存与会话存储
- **Lombok** - 减少样板代码
- **MapStruct** - DTO转换
- **JUnit 5 + Mockito** - 测试框架
- **Springdoc OpenAPI** - API文档生成

**前端技术栈:**
- **Vue 3.3** - 前端框架
- **Vite 4.x** - 构建工具
- **Vue Router 4** - 路由管理
- **Pinia** - 状态管理
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **高德地图JavaScript API 2.0** - 地图服务
- **Vitest** - 单元测试

**DevOps工具:**
- **Docker & Docker Compose** - 容器化
- **GitHub Actions** - CI/CD
- **Nginx** - 反向代理
- **Prometheus + Grafana** - 监控
- **Maven** - Java依赖管理
- **npm** - Node.js依赖管理

### Integration Points

**外部服务集成:**
1. **高德地图API**
   - 地图显示：JavaScript API
   - POI搜索：Web服务API
   - 路径规划：导航API
   - 地理编码：逆地理编码API

2. **图片存储服务**
   - 本地文件系统（初期）
   - 可扩展至OSS（阿里云/七牛云）

3. **认证服务**
   - JWT token管理
   - Spring Security过滤器链

**内部模块集成:**
- 后端通过Service接口隔离业务逻辑
- 前端通过API模块统一管理HTTP请求
- 缓存层透明化，Service层无感知

---

## Quality Gates

### Definition of Ready

- [x] 功能规范已完成并经过澄清
- [x] 技术栈选型完成
- [x] 数据库Schema设计完成
- [x] API接口规范定义完成
- [x] 开发环境就绪（Docker配置）
- [ ] 团队成员技能培训完成

### Definition of Done

- [ ] Code meets quality standards (Checkstyle + ESLint passed)
- [ ] Test coverage meets thresholds (≥80% for new code)
- [ ] API documentation complete (Swagger UI可访问)
- [ ] Performance benchmarks met (地图<3s, API<200ms)
- [ ] Accessibility requirements verified (axe-core扫描通过)
- [ ] Security review completed (OWASP Top 10检查)
- [ ] Peer review approved (至少1人code review)
- [ ] Deployed to staging and verified (功能smoke test通过)
- [ ] User acceptance testing passed (产品验收)

---

## Resource Requirements

### Team

- **后端开发工程师**: 2人，共计 160小时（每人80小时）
- **前端开发工程师**: 1人，共计 120小时
- **全栈工程师**: 1人，共计 80小时（支持前后端）
- **QA测试工程师**: 1人，共计 60小时
- **DevOps工程师**: 0.5人，共计 40小时
- **产品经理/UI设计师**: 0.5人，共计 30小时

**总计**: 490工时（约61人天，按每天8小时）

### Infrastructure

**开发环境:**
- 开发机器：本地Docker环境
- 代码仓库：GitHub
- 项目管理：GitHub Issues / Jira

**生产环境（预估）:**
- **服务器**: 2核4G云服务器 × 1台
- **数据库**: MySQL独立实例（4G内存）
- **Redis**: 1G内存实例
- **对象存储**: 50GB（如需）
- **带宽**: 5Mbps
- **CDN**: 可选（静态资源加速）

### Budget

**外部服务成本（月）:**
- 高德地图API配额: ¥500-2000（取决于用量）
- 云服务器: ¥300
- 数据库实例: ¥200
- Redis实例: ¥100
- 域名+SSL证书: ¥100
- **月度总计**: ¥1200-2700

**一次性成本:**
- 开发工具授权: ¥0（使用开源工具）
- UI设计: ¥3000（外包或内部）

**总预算**: ¥8000-15000（3个月开发周期）

---

## Timeline

```
Week 1-2:   Phase 1 - 环境搭建与基础架构
Week 3-4:   Phase 2 - 用户认证系统
Week 4-6:   Phase 3 - 高德地图集成（与Phase 2并行开始）
Week 7-8:   Phase 4 - 餐厅收藏与评价
Week 8-9:   Phase 5 - 路径规划与搜索（与Phase 4并行）
Week 10-11: Phase 6 - 测试与优化
Week 12:    Phase 7 - 部署与监控
```

**Milestones:**
- [x] Milestone 1: 项目启动，规范和计划完成 - 2025-10-14
- [ ] Milestone 2: 基础架构完成，用户可以注册登录 - 2025-10-28
- [ ] Milestone 3: 地图和POI搜索功能可用 - 2025-11-11
- [ ] Milestone 4: 收藏和评价功能上线 - 2025-11-25
- [ ] Milestone 5: 完整功能测试通过 - 2025-12-09
- [ ] Milestone 6: 生产环境上线 - 2025-12-16

---

## Monitoring & Rollback

### Success Metrics

**技术指标:**
- API平均响应时间: < 150ms
- 地图加载时间: < 3秒
- 系统错误率: < 0.5%
- 数据库查询P95: < 100ms
- 缓存命中率: > 80%

**业务指标:**
- 日活跃用户（DAU）: > 1000
- 用户平均会话时长: > 5分钟
- 收藏功能使用率: > 20%
- 评价发布率: > 5%
- 搜索成功率: > 90%

### Monitoring Plan

**监控内容:**
- **应用监控**: Spring Boot Actuator metrics
- **基础设施监控**: CPU、内存、磁盘、网络
- **数据库监控**: 连接数、慢查询、事务耗时
- **缓存监控**: Redis命中率、内存使用
- **业务监控**: 用户注册量、API调用量、错误日志

**告警策略:**
- P0 (立即响应): API错误率 > 5%, 服务宕机
- P1 (1小时内): API响应时间 > 500ms, 数据库连接池耗尽
- P2 (工作时间处理): 缓存命中率 < 60%, 磁盘使用 > 80%

**Dashboard位置:**
- Grafana: http://monitor.example.com/grafana
- Swagger UI: http://api.example.com/swagger-ui.html

### Rollback Criteria

**触发条件:**
- 严重功能缺陷影响核心流程（登录、地图加载、搜索）
- API错误率 > 10%
- 地图服务完全不可用（按规范显示错误页面）
- 数据完整性问题（用户数据丢失或损坏）
- 安全漏洞发现

**Rollback Procedure:**
1. 停止部署流程
2. 切换到上一个稳定版本（通过Docker tag回滚）
3. 验证回滚后系统功能正常
4. 通知相关团队成员
5. 分析问题根因并制定修复计划
6. 在staging环境重新测试修复
7. 制定重新发布计划

---

## Sign-off

**Prepared by:** TasteFinder Development Team - 2025-10-14  
**Reviewed by:** [待指定] - [Date]  
**Approved by:** [待指定] - [Date]

---

---

## 📑 任务快速索引

本表格帮助您快速找到每个任务的实施细节和参考文档。

| 任务ID | 任务名称 | 状态 | 主要参考文档 | 代码示例位置 |
|--------|---------|------|-------------|-------------|
| 1.1 | Docker环境配置 | ✅ Done | [quickstart.md](./quickstart.md) | docker-compose.yml |
| 1.2 | Spring Boot初始化 | ✅ Done | [research.md#1](./research.md) | pom.xml |
| 1.3 | Vue项目搭建 | 🔄 In Progress | [research.md#2](./research.md) | vite.config.js |
| 1.4 | 数据库设计 | 📋 Todo | [data-model.md](./data-model.md) | Entity classes + SQL scripts |
| 1.5 | Spring Security配置 | 📋 Todo | [research.md#7](./research.md) | SecurityConfig.java |
| 1.6 | API规范定义 | 📋 Todo | [contracts/openapi.yaml](./contracts/openapi.yaml) | Controller annotations |
| 2.1 | User Entity开发 | ✅ Done | [data-model.md#1](./data-model.md) | User.java |
| 2.2 | 注册功能实现 | 🔄 In Progress | [contracts/openapi.yaml](./contracts/openapi.yaml) | UserService.register() |
| 2.3 | 登录功能实现 | 📋 Todo | [research.md#7](./research.md) | AuthService.login() |
| 2.4 | 前端登录页面 | 📋 Todo | [spec.md](./spec.md) | LoginView.vue |
| 2.5 | 角色权限管理 | 📋 Todo | [data-model.md](./data-model.md) | RoleType.java |
| 3.1 | 高德API配置 | 🔄 In Progress | [research.md#6](./research.md) | application.yml |
| 3.2 | 地图组件开发 | 📋 Todo | [spec.md#FR2](./spec.md) | MapView.vue |
| 3.3 | POI搜索服务 | 📋 Todo | [research.md#6](./research.md) + [contracts](./contracts/openapi.yaml) | AMapPOIService.java |
| 3.4 | 地理位置服务 | 📋 Todo | [spec.md#FR3](./spec.md) | useGeolocation.js |
| 3.5 | Redis缓存策略 | 📋 Todo | [research.md#4](./research.md) | RedisCacheConfig.java |
| 3.6 | 地图性能优化 | 📋 Todo | [research.md#10](./research.md) | MapView.vue |

### 按功能模块索引

**认证模块** (Phase 2):
- 数据模型: [data-model.md#1-用户表](./data-model.md#1-用户表-users)
- API规范: [contracts/openapi.yaml - /auth/*](./contracts/openapi.yaml)
- 技术方案: [research.md#7-认证方案](./research.md#7-认证方案jwt)
- 功能需求: [spec.md - FR1](./spec.md#fr1-用户身份认证与角色管理)

**地图模块** (Phase 3):
- 数据模型: N/A（实时从高德API获取）
- API规范: [contracts/openapi.yaml - /restaurants/*](./contracts/openapi.yaml)
- 技术方案: [research.md#6-高德集成](./research.md#6-高德地图api集成方案)
- 功能需求: [spec.md - FR2/FR3](./spec.md#fr2-交互式地图餐厅展示)
- 性能优化: [research.md#10](./research.md#10-性能优化策略)

**收藏模块** (Phase 4):
- 数据模型: [data-model.md#2-餐厅收藏表](./data-model.md#2-餐厅收藏表-restaurant_favorites)
- API规范: [contracts/openapi.yaml - /favorites/*](./contracts/openapi.yaml)
- 功能需求: [spec.md - FR7](./spec.md#fr7-餐厅收藏管理)
- 存储策略: [spec.md Clarifications#4](./spec.md#clarifications) - POI ID + 快照

**评价模块** (Phase 4):
- 数据模型: [data-model.md#3-餐厅评价表](./data-model.md#3-餐厅评价表-restaurant_reviews)
- API规范: [contracts/openapi.yaml - /reviews/*](./contracts/openapi.yaml)
- 功能需求: [spec.md - FR8](./spec.md#fr8-用户评价系统)
- 审核策略: [spec.md Clarifications#3](./spec.md#clarifications) - 先发后审

### 关键决策快速查找

| 问题 | 答案位置 | 决策内容 |
|------|---------|---------|
| 为什么选Spring Boot? | research.md#1 | 成熟、安全特性强、易集成 |
| 为什么选Vue 3? | research.md#2 | 轻量、学习曲线平缓、Element Plus支持好 |
| 为什么选MySQL? | research.md#3 | 关系型数据、事务保证、成熟稳定 |
| JWT如何设计? | research.md#7 | HS256签名、7天有效期、包含userId+roles |
| 餐厅数据从哪来? | spec.md Clarifications#2 | 高德API实时获取，不预存 |
| 地图故障怎么办? | spec.md Clarifications#1 | 完全阻断，显示错误页面 |
| 评价如何审核? | spec.md Clarifications#3 | 先发后审，关键词过滤+人工抽检 |
| 收藏如何存储? | spec.md Clarifications#4 | POI ID + 基础快照 |
| 缓存策略是什么? | research.md#4 | Redis缓存30分钟，减少API调用 |

---

## Revision History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-10-14 | TasteFinder Team | Initial implementation plan created |
| 1.1 | 2025-10-14 | Implementation Review | Added cross-references, implementation steps, and quick index |
