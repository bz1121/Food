# 🔧 CORS和内网穿透故障排查

**更新时间**: 2025-10-15  
**问题**: 拒绝访问 os8vc9406212.vicp.fun

---

## ✅ 已优化CORS配置

### 后端配置（开发环境）

**已修改为允许所有来源**:
```java
configuration.setAllowedOriginPatterns(List.of("*"));
```

**优点**:
- ✅ 支持任何内网穿透域名
- ✅ 支持localhost
- ✅ 方便开发测试

**注意**: 生产环境应该使用精确域名匹配

---

## 🔄 完整启动流程

### 前提条件

**1. Docker Desktop必须运行** ⚠️
- 打开Docker Desktop应用
- 等待图标变绿

**2. 启动数据库**:
```bash
docker-compose up -d
# 等待30秒
```

**3. 验证数据库**:
```bash
docker ps
# 应该看到2个容器运行
```

---

### 启动服务

**方式1: 使用一键脚本**:
```bash
START_WITH_DOCKER.bat
```

**方式2: 手动启动**:
```bash
# 后端（新终端）
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 前端（新终端）  
cd frontend
npm run dev
```

---

## 🌐 访问测试

### 本地访问

```
http://localhost:5173
```

应该正常工作。

### 内网穿透访问

```
http://os8vc9406212.vicp.fun
```

**如果还是拒绝访问，检查**:
1. ✅ Docker是否运行
2. ✅ 后端是否启动成功（看到"Started"）
3. ✅ 前端是否启动成功
4. ✅ 穿透工具是否运行

---

## 🔍 调试步骤

### 1. 测试本地访问

```
http://localhost:5173
```

**如果本地可以访问** → 问题在穿透配置  
**如果本地也不行** → 问题在服务启动

### 2. 测试后端API

```
http://localhost:8080/actuator/health
```

**应该返回**: `{"status":"UP"}`

**如果404或无响应** → 后端未启动

### 3. 测试穿透前端

```
http://os8vc9406212.vicp.fun
```

**如果能看到登录页面** → 前端穿透正常  
**如果拒绝访问** → 检查穿透工具

### 4. 测试穿透后端

```
https://os8vc9406212.vicp.fun/api/actuator/health
```

**如果返回UP** → 后端穿透正常  
**如果失败** → 后端穿透有问题

---

## 💡 最可能的原因

### 原因1: Docker未启动

**症状**: 后端报MySQL连接错误  
**解决**: 
1. 启动Docker Desktop
2. 运行 `docker-compose up -d`
3. 等待30秒
4. 重启后端

### 原因2: 穿透工具未运行

**症状**: 域名无法访问  
**解决**: 
1. 确保穿透工具运行中
2. 检查隧道状态
3. 确认端口映射正确

### 原因3: HTTPS证书问题

**症状**: HTTPS访问被拒绝  
**解决**: 
- 穿透工具提供的HTTPS可能有证书警告
- 浏览器点击"继续访问"
- 或使用HTTP访问

---

## 🎯 当前配置总结

**前端**:
- 本地: http://localhost:5173
- 外网: http://os8vc9406212.vicp.fun
- API地址: https://os8vc9406212.vicp.fun/api

**后端**:
- 本地: http://localhost:8080
- 外网: https://os8vc9406212.vicp.fun
- CORS: 允许所有来源 ✅

**数据库**:
- MySQL: 127.0.0.1:3306
- Redis: 127.0.0.1:6379

---

## 🚀 推荐操作

**立即执行**:

1. **启动Docker Desktop** ⚠️
2. **运行**: `START_WITH_DOCKER.bat`
3. **等待服务启动**（约1分钟）
4. **本地测试**: http://localhost:5173
5. **外网测试**: http://os8vc9406212.vicp.fun

---

**Docker Desktop是关键！启动后运行脚本！** 🐳✅

