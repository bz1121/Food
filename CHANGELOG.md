# 更新日志

本文档记录TasteFinder项目的所有重要变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [1.0.0] - 2025-10-14

### 新增功能 🎉

#### 用户认证系统
- ✅ 用户注册功能（用户名+密码）
- ✅ 用户登录功能（JWT token认证）
- ✅ 5个预设测试账户（5种用户角色）
- ✅ 角色权限管理（RBAC）
- ✅ 登录状态持久化

#### 地图展示与搜索
- ✅ 高德地图交互式展示
- ✅ 浏览器地理定位
- ✅ 基于位置的POI搜索
- ✅ 餐厅标记点渲染（最多200个）
- ✅ 左侧餐厅列表联动
- ✅ 搜索半径调整（1/3/5/10km）
- ✅ 餐厅详情弹窗

#### 路径规划与导航
- ✅ 路径规划API集成
- ✅ 支持驾车/步行/公交三种方式
- ✅ 显示距离和预计时间
- ✅ 导航步骤详细说明
- ✅ 导航面板UI组件

#### 餐厅收藏管理
- ✅ 收藏餐厅功能（POI ID + 快照）
- ✅ 收藏列表查看（分页）
- ✅ 取消收藏功能
- ✅ 从收藏跳转到地图

#### 用户评价系统
- ✅ 发表评价（星级 + 文字）
- ✅ 评论家认证徽章
- ✅ 先发后审机制
- ✅ 关键词敏感词过滤
- ✅ 查看历史评价
- ✅ 删除评价功能

### 技术实现 🛠️

#### 后端
- ✅ Spring Boot 3.1.5
- ✅ Spring Security + JWT
- ✅ Spring Data JPA
- ✅ MySQL 8.0数据库（5个表）
- ✅ Redis缓存（30分钟TTL）
- ✅ Flyway数据库迁移
- ✅ Springdoc OpenAPI文档

#### 前端
- ✅ Vue 3.3 + Composition API
- ✅ Vite 4.5构建工具
- ✅ Vue Router 4路由管理
- ✅ Pinia状态管理
- ✅ Element Plus UI组件库
- ✅ 高德地图JavaScript API 2.0

#### DevOps
- ✅ Docker Compose配置
- ✅ Dockerfile（前后端）
- ✅ GitHub Actions CI/CD
- ✅ Checkstyle代码质量
- ✅ ESLint代码规范

### 文档 📚

- ✅ 功能规范文档（spec.md）
- ✅ 详细实施计划（plan.md）
- ✅ 技术研究文档（research.md）
- ✅ 数据模型文档（data-model.md）
- ✅ API规范（OpenAPI 3.0）
- ✅ 快速开始指南（quickstart.md）
- ✅ 部署指南（DEPLOYMENT_GUIDE.md）

### 测试 🧪

- ✅ JUnit 5 + Mockito测试框架
- ✅ Vitest前端测试框架
- ✅ 单元测试示例（5个）
- ✅ 集成测试示例（2个）
- ✅ Jacoco代码覆盖率工具

---

## [Unreleased]

### 计划中的功能 📋

#### v1.1.0
- [ ] 热力图功能
- [ ] 图片实际上传功能
- [ ] 高级搜索筛选
- [ ] E2E测试套件

#### v1.2.0
- [ ] 用户头像上传
- [ ] 评价图片展示
- [ ] 实时通知系统
- [ ] 个人偏好设置

#### v2.0.0
- [ ] 移动端适配
- [ ] 微信小程序
- [ ] 社交分享功能
- [ ] 餐厅预订集成

---

## 版本说明

### 版本号格式: MAJOR.MINOR.PATCH

- **MAJOR**: 不兼容的API变更
- **MINOR**: 向下兼容的功能新增
- **PATCH**: 向下兼容的Bug修复

---

更新时间: 2025-10-14

