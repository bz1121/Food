# TasteFinder Frontend

TasteFinder美食推荐平台前端 - 基于Vue 3 + Vite

## 技术栈

- **Vue** 3.3
- **Vite** 4.x
- **Vue Router** 4
- **Pinia** 状态管理
- **Element Plus** UI组件库
- **Axios** HTTP客户端
- **高德地图** JavaScript API 2.0

## 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 配置环境变量

编辑 `.env.development`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_AMAP_KEY=your-amap-js-api-key
```

### 3. 启动开发服务器

```bash
npm run dev
```

应用将在 http://localhost:5173 启动

### 4. 构建生产版本

```bash
npm run build
```

## 项目结构

```
frontend/
├── src/
│   ├── api/                  # API模块
│   │   ├── axios.js          # Axios配置
│   │   ├── auth.js           # 认证API
│   │   └── restaurant.js     # 餐厅API
│   ├── components/           # 通用组件
│   ├── composables/          # 组合式函数
│   │   └── useGeolocation.js # 地理定位
│   ├── router/               # 路由配置
│   │   └── index.js
│   ├── stores/               # Pinia状态管理
│   │   └── auth.js           # 认证状态
│   ├── views/                # 页面组件
│   │   ├── auth/
│   │   │   ├── LoginView.vue
│   │   │   └── RegisterView.vue
│   │   ├── map/
│   │   │   └── MapView.vue   # 地图主页面
│   │   └── profile/
│   │       ├── ProfileView.vue
│   │       ├── FavoritesView.vue
│   │       └── MyReviewsView.vue
│   ├── App.vue
│   └── main.js
├── index.html
├── vite.config.js
└── package.json
```

## 主要功能

- ✅ 用户注册和登录
- ✅ 交互式地图展示
- ✅ 附近餐厅搜索
- ✅ 餐厅详情查看
- 🚧 路径规划与导航（开发中）
- 🚧 餐厅收藏管理（开发中）
- 🚧 用户评价系统（开发中）

## 测试账户

- critic_a / password123 (美食评论家)
- user_d / password123 (普通用户)

## 开发指南

参见: [../specs/001-tastefinder-api/plan.md](../specs/001-tastefinder-api/plan.md)

