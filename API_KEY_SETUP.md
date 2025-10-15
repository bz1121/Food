# 🔑 高德地图API密钥配置指南

**问题**: USERKEY_PLAT_NOMATCH - 密钥平台不匹配  
**原因**: 后端和前端需要不同类型的密钥

---

## 📋 需要两个密钥

### 1. Web服务密钥（后端使用）✅

**你已有的密钥**: `431faeae95f61be3faa8ac06a599fe27`  
**用途**: 后端调用POI搜索、导航API  
**配置位置**: `backend/src/main/resources/application-dev.yml`  
**状态**: ✅ 已正确配置

---

### 2. JavaScript API密钥（前端使用）❌

**需要申请**: 一个新的JavaScript API密钥  
**用途**: 前端显示地图  
**配置位置**: `frontend/.env.development`  
**状态**: ❌ 当前使用的是Web服务密钥（不匹配）

---

## 🔧 如何申请JavaScript API密钥

### 步骤1: 登录高德开放平台

访问: https://console.amap.com/dev/key/app

### 步骤2: 创建应用

1. 点击"创建新应用"
2. 填写应用名称: `TasteFinder前端`
3. 保存

### 步骤3: 添加Key

1. 点击"添加Key"
2. **服务平台**: 选择 **"Web端(JS API)"**  ← 重要！
3. **Key名称**: `TasteFinder-JS-API`
4. **绑定域名**: 
   - 开发环境填: `localhost`
   - 生产环境填: 你的域名
5. 提交

### 步骤4: 获取密钥

- 复制显示的Key（类似: `a1b2c3d4e5f6g7h8i9j0...`）

---

## 🔧 配置密钥

### 后端配置（已完成）✅

`backend/src/main/resources/application-dev.yml`:
```yaml
amap:
  key: 431faeae95f61be3faa8ac06a599fe27  # ✅ Web服务密钥
  secret: your-dev-amap-secret
```

### 前端配置（需要更新）❌

`frontend/.env.development`:
```env
VITE_API_BASE_URL=http://localhost:8080/api

# 替换为你的JavaScript API密钥
VITE_AMAP_KEY=你刚申请的JavaScript-API密钥

VITE_AMAP_VERSION=2.0
```

---

## 🚀 配置后

1. **保存文件**
2. **重启前端**:
   ```bash
   # 在前端终端按 Ctrl+C 停止
   # 然后重新运行
   npm run dev
   ```
3. **刷新浏览器**
4. **地图应该可以正常加载了！** ✅

---

## 💡 临时解决方案

如果暂时无法申请新密钥，可以：

1. **先测试其他功能**（不依赖地图）:
   - ✅ 用户注册
   - ✅ 用户登录
   - ✅ 查看个人中心
   - ✅ 查看收藏列表（空）
   - ✅ 查看评价列表（空）

2. **使用API文档测试后端**:
   - http://localhost:8080/swagger-ui.html
   - 测试所有API接口

---

## 📝 密钥类型对照表

| 密钥类型 | 用途 | 申请平台 | 当前状态 |
|---------|------|---------|---------|
| Web服务 | 后端POI搜索、导航 | Web服务 | ✅ 已配置 |
| **JavaScript API** | **前端地图显示** | **Web端(JS API)** | ❌ **需申请** |

---

**关键**: 一定要选择 **"Web端(JS API)"** 平台！🔑

