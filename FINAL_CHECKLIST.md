# ✅ TasteFinder 最终启动检查清单

**更新时间**: 2025-10-15  
**用途**: 确保所有服务正常启动

---

## 🔍 启动前检查

### 1. Docker容器必须运行 ✅

**检查命令**:
```bash
docker ps
```

**应该看到**:
- tastefinder-mysql
- tastefinder-redis

**如果没有，执行**:
```bash
docker-compose up -d
```

---

### 2. 等待MySQL初始化

**首次启动需要30秒**

**验证MySQL就绪**:
```bash
docker-compose exec mysql mysql -uroot -ptastefinder_pass_123 -e "SHOW DATABASES;"
```

应该看到 `tastefinder` 数据库。

---

### 3. 启动后端

```bash
backend\start-backend.bat
```

**成功标志**:
```
Started TastefinderApplication in X seconds
Tomcat started on port(s): 8080
```

---

### 4. 启动前端

```bash
frontend\start-frontend.bat
```

**成功标志**:
```
➜  Local:   http://localhost:5173/
➜  Network: http://192.168.x.x:5173/  ← 支持内网穿透
```

---

## 🌐 内网穿透配置

### 前提条件

**必须完成**:
1. ✅ Docker容器运行
2. ✅ 后端成功启动（端口8080）
3. ✅ 前端成功启动（端口5173）

### 穿透配置

**前端**:
- 本地端口: 5173
- 外网域名: os8vc9406212.vicp.fun ✅

**后端**（建议）:
- 本地端口: 8080
- 外网域名: 需要配置（例如 api.xxx.com）

**如果后端也有穿透**:

编辑 `frontend/.env.development`:
```env
VITE_API_BASE_URL=http://你的后端穿透域名/api
```

---

## 🚀 完整启动流程

### 一键启动（本地）

```bash
start.bat
```

这会自动启动：
1. Docker容器
2. 后端应用（新窗口）
3. 前端应用（新窗口）

### 手动启动

```bash
# 1. 启动Docker
docker-compose up -d

# 2. 等待30秒

# 3. 启动后端（新终端）
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dfile.encoding=UTF-8

# 4. 启动前端（新终端）
cd frontend
npm run dev
```

---

## 🔧 常见错误

### 错误1: MySQL连接失败

**症状**: `Connection refused`  
**原因**: Docker容器未启动  
**解决**: 
```bash
docker-compose up -d
# 等待30秒
```

### 错误2: CORS错误

**症状**: `Access-Control-Allow-Origin`  
**原因**: 后端未配置穿透域名  
**解决**: 已在SecurityConfig中添加 ✅

### 错误3: Vite host blocked

**症状**: `This host is not allowed`  
**原因**: Vite未配置域名  
**解决**: 已在vite.config.js中添加 ✅

---

## 📋 检查清单

**启动前**:
- [ ] Docker Desktop已启动
- [ ] docker-compose up -d 已执行
- [ ] MySQL容器运行中
- [ ] Redis容器运行中

**启动后端前**:
- [ ] MySQL已就绪（等待30秒）
- [ ] 数据库可连接

**启动后端后**:
- [ ] 看到"Started TastefinderApplication"
- [ ] 端口8080已监听
- [ ] http://localhost:8080/actuator/health 返回UP

**启动前端后**:
- [ ] 看到Local和Network地址
- [ ] http://localhost:5173 可访问
- [ ] 可以登录

**内网穿透**:
- [ ] 前端域名配置 ✅
- [ ] 后端CORS配置 ✅
- [ ] 前端allowedHosts配置 ✅
- [ ] 后端也需要穿透（如需外网访问API）

---

## 🎯 当前状态

**前端**: ✅ 支持内网穿透  
**后端**: ⚠️ 需要检查Docker容器

**下一步**: 
1. 启动Docker容器
2. 重启后端
3. 测试内网穿透

---

**按照此清单操作，确保万无一失！** ✅🚀

