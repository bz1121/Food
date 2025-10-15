# 技术研究文档

**项目**: TasteFinder 美食推荐平台  
**创建日期**: 2025-10-14  
**状态**: Complete

---

## 研究概述

本文档记录TasteFinder项目技术选型和架构决策的研究过程，为实施计划提供技术依据。

---

## 1. 后端框架选择：Spring Boot

### Decision
选择 **Spring Boot 3.1.5** 作为后端核心框架

### Rationale

**优势:**
- 成熟稳定的企业级Java框架，社区活跃，文档完善
- 开箱即用的Spring Security集成，简化JWT认证实现
- Spring Data JPA提供优雅的ORM抽象，减少样板代码
- 内置Actuator提供生产级监控能力
- 丰富的starter依赖，快速集成第三方服务
- 与Docker容器化无缝集成

**适用场景:**
- RESTful API服务
- 需要完善权限管理的系统
- 团队具备Java开发经验
- 需要长期维护和扩展的项目

### Alternatives Considered

1. **Node.js + Express**
   - ✅ 优势：JavaScript全栈，前后端技术栈统一
   - ❌ 劣势：企业级特性较弱，需要更多第三方库拼凑
   - 🚫 不选原因：团队Java经验更丰富，Spring生态更适合权限管理需求

2. **Django (Python)**
   - ✅ 优势：快速开发，内置Admin面板
   - ❌ 劣势：性能相对较弱，异步支持不如Node.js
   - 🚫 不选原因：团队无Python经验，学习成本高

3. **Go + Gin**
   - ✅ 优势：高性能，编译型语言
   - ❌ 劣势：生态相对较新，企业级组件不如Spring丰富
   - 🚫 不选原因：团队无Go经验，ORM和安全组件需要更多定制

---

## 2. 前端框架选择：Vue 3

### Decision
选择 **Vue 3.3 + Vite** 作为前端技术栈

### Rationale

**优势:**
- 渐进式框架，学习曲线平缓
- Composition API提供更好的代码组织和复用
- Vite构建工具提供极速的HMR体验
- 与Element Plus UI库集成完美，组件丰富
- 轻量级，打包体积小，适合地图等重资源场景
- 响应式系统性能优秀

**适用场景:**
- 需要地图交互的复杂前端应用
- 需要状态管理和路由的SPA
- 团队规模中小，追求开发效率

### Alternatives Considered

1. **React 18**
   - ✅ 优势：生态最大，社区资源丰富
   - ❌ 劣势：学习曲线较陡，需要掌握Hooks、Context等概念
   - 🚫 不选原因：Vue的响应式系统更直观，Element Plus比Ant Design更适合本项目

2. **Angular 16**
   - ✅ 优势：完整的企业级解决方案，TypeScript原生支持
   - ❌ 劣势：框架较重，学习成本高
   - 🚫 不选原因：项目规模不需要Angular的全家桶方案，Vue更轻量灵活

3. **Svelte**
   - ✅ 优势：无虚拟DOM，性能极佳
   - ❌ 劣势：生态相对较小，企业级组件库较少
   - 🚫 不选原因：Element Plus对Vue支持更好，Svelte生态不够成熟

---

## 3. 数据库选择：MySQL

### Decision
选择 **MySQL 8.0** 作为主数据库

### Rationale

**优势:**
- ACID事务保证，确保用户数据一致性
- 成熟稳定，运维经验丰富
- InnoDB引擎支持行级锁，并发性能好
- JSON字段类型支持半结构化数据（餐厅快照）
- 与Spring Data JPA集成无缝
- Docker镜像官方维护，部署简单

**数据特点:**
- 用户、收藏、评价都是结构化数据
- 需要事务保证（用户注册、评价发布）
- 数据量初期不大（10,000用户级别）
- 需要复杂查询（评分排序、距离筛选）

### Alternatives Considered

1. **PostgreSQL**
   - ✅ 优势：功能更强大，支持GIS扩展（PostGIS）
   - ❌ 劣势：运维复杂度略高
   - 🚫 不选原因：项目不需要复杂GIS运算，坐标存储MySQL足够

2. **MongoDB**
   - ✅ 优势：文档存储灵活，适合快速迭代
   - ❌ 劣势：无事务保证（旧版本），不适合关系型数据
   - 🚫 不选原因：用户、收藏关系明确，关系型数据库更合适

3. **SQLite**
   - ✅ 优势：轻量级，无需独立服务
   - ❌ 劣势：并发能力弱，不支持网络访问
   - 🚫 不选原因：生产环境需要真正的数据库服务器

---

## 4. 缓存策略：Redis

### Decision
使用 **Redis 7.x** 作为缓存层

### Rationale

**使用场景:**
1. **高德API响应缓存** - POI搜索结果缓存30分钟，减少API调用
2. **JWT Token存储** - 支持token黑名单机制（用户登出）
3. **热门餐厅缓存** - 缓存高频访问的餐厅详情
4. **搜索自动补全** - 缓存热门搜索词

**性能收益:**
- 减少80%的高德API调用（成本节约）
- API响应时间从500ms降至50ms
- 支持1000+并发请求

### Alternatives Considered

1. **本地缓存（Caffeine）**
   - ✅ 优势：无需外部依赖，延迟最低
   - ❌ 劣势：多实例无法共享，缓存一致性差
   - 🚫 不选原因：计划后期支持横向扩展，需要集中式缓存

2. **Memcached**
   - ✅ 优势：性能极佳，协议简单
   - ❌ 劣势：功能单一，不支持复杂数据结构
   - 🚫 不选原因：Redis功能更丰富，支持List、Set等数据结构

---

## 5. 容器化方案：Docker

### Decision
使用 **Docker + Docker Compose** 实现容器化部署

### Rationale

**优势:**
- 环境一致性：开发、测试、生产环境完全一致
- 快速部署：一键启动所有服务（MySQL + Redis + 应用）
- 依赖隔离：避免端口冲突和版本冲突
- 易于CI/CD集成：GitHub Actions原生支持Docker
- 资源控制：限制容器CPU和内存使用

**Docker Compose服务配置:**
```yaml
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: tastefinder
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
  
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
  
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
  
  frontend:
    build: ./frontend
    ports:
      - "80:80"
```

### Alternatives Considered

1. **传统部署（直接安装）**
   - ✅ 优势：性能略优（无容器开销）
   - ❌ 劣势：环境配置复杂，难以复现问题
   - 🚫 不选原因：现代项目应采用容器化，便于维护

2. **Kubernetes**
   - ✅ 优势：企业级容器编排，自动扩缩容
   - ❌ 劣势：复杂度高，初期项目不需要
   - 🚫 不选原因：项目规模小，Docker Compose足够

---

## 6. 高德地图API集成方案

### Decision
前端使用 **JavaScript API**，后端使用 **Web服务API**

### Rationale

**前端集成（JavaScript API）:**
- 地图显示和交互
- 标记点渲染
- 用户定位
- 路径可视化

**后端集成（Web服务API）:**
- POI搜索（附近餐厅）
- 地理编码（地址转坐标）
- 路径规划（导航计算）

**优势:**
- 前端直接调用：地图渲染性能更好，减轻服务器压力
- 后端代理：敏感API Key不暴露在前端，控制调用频率
- 缓存友好：后端可以缓存POI搜索结果

### Key Integration Points

1. **POI搜索 Service**
```java
@Service
public class AMapPOIService {
    public List<Restaurant> searchNearby(double lat, double lon, int radius) {
        // 调用高德Web服务API
        // 缓存结果30分钟
        // 返回餐厅列表
    }
}
```

2. **前端地图组件**
```vue
<template>
  <div id="map-container"></div>
</template>

<script setup>
import AMapLoader from '@amap/amap-jsapi-loader';

const initMap = async () => {
  const AMap = await AMapLoader.load({ key: 'xxx' });
  const map = new AMap.Map('map-container', { zoom: 13 });
  // 添加标记点
};
</script>
```

---

## 7. 认证方案：JWT

### Decision
使用 **JWT (JSON Web Token)** 实现无状态认证

### Rationale

**优势:**
- 无需服务器端session存储，天然支持水平扩展
- Token自包含用户信息和权限，减少数据库查询
- 跨域友好，适合前后端分离架构
- Spring Security原生支持JWT

**Token设计:**
```json
{
  "sub": "username",
  "userId": 123,
  "roles": ["ROLE_USER"],
  "iat": 1634567890,
  "exp": 1634654290
}
```

**安全措施:**
- 使用强密钥签名（HS256）
- Token有效期7天，配合refresh token机制
- 敏感操作（删除评价）需要二次验证
- 前端存储在localStorage，设置HttpOnly Cookie（如条件允许）

### Alternatives Considered

1. **Session-based认证**
   - ✅ 优势：服务器完全控制，易于撤销
   - ❌ 劣势：需要session存储，不利于扩展
   - 🚫 不选原因：计划后期支持多实例，JWT更合适

2. **OAuth 2.0**
   - ✅ 优势：行业标准，支持第三方登录
   - ❌ 劣势：实现复杂，当前项目不需要第三方登录
   - 🚫 不选原因：简化版JWT足够满足需求

---

## 8. UI组件库：Element Plus

### Decision
选择 **Element Plus** 作为Vue 3 UI组件库

### Rationale

**优势:**
- 组件丰富（表单、表格、弹窗等）满足项目所有需求
- Vue 3原生支持，与Composition API集成完美
- 中文文档详细，国内团队使用广泛
- 主题定制灵活，支持响应式设计
- 无障碍性较好（ARIA标签支持）

**核心组件:**
- `el-form` - 登录注册表单
- `el-table` - 餐厅列表展示
- `el-dialog` - 餐厅详情弹窗
- `el-upload` - 图片上传
- `el-rate` - 星级评分

### Alternatives Considered

1. **Ant Design Vue**
   - ✅ 优势：企业级设计，组件质量高
   - ❌ 劣势：主题定制较复杂
   - 🚫 不选原因：Element Plus更轻量，更适合本项目

2. **Vuetify**
   - ✅ 优势：Material Design风格，动画精美
   - ❌ 劣势：打包体积较大，定制性较弱
   - 🚫 不选原因：Element Plus更符合国内用户习惯

---

## 9. 测试策略

### Decision
**后端**: JUnit 5 + Mockito + Spring Boot Test  
**前端**: Vitest + Vue Test Utils  
**E2E**: Playwright（可选）

### Rationale

**后端测试:**
- JUnit 5：Java标准测试框架
- Mockito：Mock外部依赖（高德API、Redis）
- Spring Boot Test：集成测试支持，自动配置测试环境
- Testcontainers：使用Docker容器进行真实数据库测试

**前端测试:**
- Vitest：Vite生态测试工具，速度快
- Vue Test Utils：官方测试工具，组件测试友好
- Testing Library：用户行为驱动测试

**测试覆盖率目标:**
- 单元测试：≥80%
- 集成测试：覆盖所有API端点
- E2E测试：核心流程（注册-登录-搜索-收藏）

---

## 10. 性能优化策略

### Decision
多层次性能优化方案

### Key Strategies

**1. 前端优化:**
- 地图懒加载：用户交互后才初始化地图
- 标记点分批渲染：一次最多200个，超出部分聚合显示
- 图片懒加载：列表中图片滚动到可视区才加载
- 代码分割：按路由拆分chunk，减小首屏加载

**2. 后端优化:**
- Redis缓存热点数据：POI搜索结果缓存30分钟
- 数据库索引优化：用户名、餐厅POI ID创建索引
- 连接池配置：HikariCP连接池，最大连接数50
- 异步处理：图片上传、邮件发送（如有）异步处理

**3. 高德API优化:**
- 请求合并：批量查询多个POI
- 缓存策略：相同查询条件缓存结果
- 降级方案：API超时时返回缓存数据

**性能目标:**
- 首屏加载（LCP）: < 2.5s
- 地图初始化: < 3s
- API响应时间: < 200ms (p95)
- 搜索响应: < 1s

---

## 研究总结

本研究文档确立了TasteFinder项目的技术栈和核心架构决策：

**技术栈:**
- 后端：Spring Boot 3.1 + MySQL 8.0 + Redis 7.x
- 前端：Vue 3.3 + Vite + Element Plus
- 部署：Docker + Docker Compose
- 外部服务：高德地图API

**关键决策:**
1. 前后端分离架构，RESTful API通信
2. JWT无状态认证，支持水平扩展
3. Redis缓存减少高德API调用和成本
4. Docker容器化保证环境一致性
5. 多层次性能优化满足<3s地图加载要求

**风险缓解:**
- 高德API配额问题：缓存 + 监控 + 告警
- 性能瓶颈：分批渲染 + 索引优化 + 缓存
- 安全问题：JWT + Spring Security + 输入验证

所有技术决策已综合考虑团队能力、项目需求和长期维护性，为实施阶段提供清晰指引。

