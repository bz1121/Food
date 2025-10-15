# 🌐 内网穿透完整配置指南

**你的穿透域名**: `os8vc9406212.vicp.fun`  
**状态**: 前端已配置 ✅ 后端已配置 ✅

---

## ✅ 已完成配置

### 1. 前端配置 ✅

**Vite配置** (`frontend/vite.config.js`):
```javascript
server: {
  host: '0.0.0.0',  // 监听所有网络接口
  allowedHosts: [
    'os8vc9406212.vicp.fun',  // 你的域名
    '.vicp.fun'                // 允许所有vicp.fun
  ]
}
```

### 2. 后端配置 ✅

**CORS配置** (`SecurityConfig.java`):
```java
.setAllowedOrigins(List.of(
  "http://localhost:5173",
  "http://os8vc9406212.vicp.fun",   // 已添加 ✅
  "https://os8vc9406212.vicp.fun"   // HTTPS也支持
))
```

---

## 🚀 完整启动步骤

### 方案1: 前后端都做穿透（推荐）

**步骤1: 配置前端穿透**
- 本地端口: 5173
- 域名: os8vc9406212.vicp.fun

**步骤2: 配置后端穿透**
- 本地端口: 8080
- 域名: 例如 `os8vc9406212-api.vicp.fun`

**步骤3: 修改前端API地址**

编辑 `frontend/.env.development`:
```env
VITE_API_BASE_URL=http://os8vc9406212-api.vicp.fun/api
```

**步骤4: 重启服务**
```bash
# 重启前端
Ctrl + C
npm run dev

# 重启后端
backend\start-backend.bat
```

---

### 方案2: 仅前端穿透（快速方案）

**如果你的穿透工具支持多端口映射**:

**配置穿透工具**:
- 端口1: 5173 → os8vc9406212.vicp.fun
- 端口2: 8080 → os8vc9406212.vicp.fun:8080

**前端API地址**:
```env
VITE_API_BASE_URL=http://os8vc9406212.vicp.fun:8080/api
```

---

### 方案3: 使用反向代理（生产环境）

**配置Nginx**:
```nginx
server {
    listen 80;
    server_name os8vc9406212.vicp.fun;
    
    # 前端
    location / {
        proxy_pass http://localhost:5173;
    }
    
    # 后端API
    location /api {
        proxy_pass http://localhost:8080;
    }
}
```

---

## 🔧 立即可用的临时方案

### 最简单的方式

**1. 配置2个穿透**:

**花生壳/ngrok等工具**:
```
前端穿透:
本地: 127.0.0.1:5173
域名: os8vc9406212.vicp.fun

后端穿透:
本地: 127.0.0.1:8080
域名: 申请另一个域名，例如 os8vc9406212-api.vicp.fun
```

**2. 修改前端配置**:

`frontend/.env.development`:
```env
VITE_API_BASE_URL=http://你的后端穿透域名/api
```

**3. 重启前后端**

**4. 访问**:
```
http://os8vc9406212.vicp.fun
```

---

## 🎯 当前配置

### 后端已支持的域名

```java
✅ http://localhost:5173
✅ http://os8vc9406212.vicp.fun
✅ https://os8vc9406212.vicp.fun
```

### 前端已支持的域名

```javascript
✅ os8vc9406212.vicp.fun
✅ 所有 .vicp.fun 子域名
✅ localhost
```

---

## 🔄 重启服务

**1. 重启后端**（让CORS配置生效）:
```bash
# 在后端终端 Ctrl+C
backend\start-backend.bat
```

**2. 重启前端**（让allowedHosts生效）:
```bash
# 在前端终端 Ctrl+C
npm run dev
```

**3. 配置内网穿透**:
- 确保两个端口都映射（5173和8080）
- 或配置Nginx反向代理

---

## 💡 推荐方案

**使用花生壳/ngrok等工具配置2个隧道**:

```
隧道1（前端）:
本地: 127.0.0.1:5173
外网: os8vc9406212.vicp.fun

隧道2（后端）:
本地: 127.0.0.1:8080
外网: os8vc9406212-backend.vicp.fun  （申请新域名）
```

**然后修改前端API地址为后端的穿透域名**

---

**重启前后端后，配置穿透工具即可！** 🌐✅

