# 🚀 TasteFinder 快速启动指南

**最快速度**: 5分钟启动整个项目  
**更新时间**: 2025-10-14

---

## ⚡ 超快速启动（推荐）

### 方式1: 一键启动脚本

**直接双击运行**:
```
start.bat
```

这个脚本会自动：
1. ✅ 启动Docker容器（MySQL + Redis）
2. ✅ 等待数据库初始化
3. ✅ 在新窗口启动后端
4. ✅ 在新窗口启动前端

**就这么简单！** 🎉

---

## 📋 前置要求

### 必须安装

1. **Docker Desktop**
   - 下载: https://www.docker.com/products/docker-desktop
   - 版本: 20.10+

2. **Java JDK** 17+
   - 下载: https://adoptium.net/
   - 或OpenJDK 17

3. **Node.js** 18+
   - 下载: https://nodejs.org/
   - 推荐LTS版本

4. **Maven** 3.8+（推荐）
   - 下载: https://maven.apache.org/download.cgi
   - 或使用IDEA内置Maven

### 验证安装

```bash
docker --version    # 应显示20.10+
java --version      # 应显示17+
node --version      # 应显示v18+
mvn --version       # 应显示3.8+（如已安装）
```

---

## 🔧 手动启动（如果脚本失败）

### 步骤1: 启动Docker

```bash
docker-compose up -d
```

等待30秒让MySQL初始化完成。

### 步骤2: 启动后端

**方式A - 使用Maven（推荐）**:
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**方式B - 使用IDEA**:
1. 用IDEA打开 `backend` 目录
2. 右键点击 `TastefinderApplication.java`
3. 选择 "Run 'TastefinderApplication'"
4. 确保Active Profile设置为 `dev`

**方式C - 先打包再运行**:
```bash
cd backend
mvn clean package -DskipTests
java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### 步骤3: 启动前端

```bash
cd frontend
npm install
npm run dev
```

---

## 🌐 访问应用

### 前端应用
**地址**: http://localhost:5173

**测试账户**:
- `critic_a` / `password123` (美食评论家)
- `user_d` / `password123` (普通用户)

### 后端API文档
**地址**: http://localhost:8080/swagger-ui.html

可以直接在浏览器中测试所有API。

### 健康检查
**地址**: http://localhost:8080/actuator/health

应返回: `{"status":"UP"}`

---

## ⚠️ 重要配置

### 必须配置高德API密钥

**后端配置**:
编辑 `backend/src/main/resources/application-dev.yml`:
```yaml
amap:
  key: your-amap-web-service-key  # ← 替换这里
  secret: your-amap-secret         # ← 替换这里
```

**前端配置**:
编辑 `frontend/.env.development`:
```env
VITE_AMAP_KEY=your-amap-js-api-key  # ← 替换这里
```

**申请地址**: https://console.amap.com/dev/key/app

**没有密钥的话**:
- 地图无法加载
- POI搜索返回空结果
- 导航功能无法使用

---

## 🐛 常见问题

### 问题1: Docker启动失败

**错误**: `port is already allocated`

**解决**:
```bash
# 查看端口占用
netstat -ano | findstr :3306
netstat -ano | findstr :6379

# 停止占用端口的进程，或修改docker-compose.yml端口号
```

### 问题2: 后端启动失败 - 数据库连接错误

**错误**: `Access denied for user`

**解决**:
1. 确认Docker容器已启动: `docker ps`
2. 等待MySQL初始化完成（30秒）
3. 验证密码正确（`tastefinder_pass_123`）

### 问题3: Maven wrapper JAR文件缺失

**错误**: `Maven wrapper JAR file not found`

**解决**:
- 使用系统安装的Maven: `mvn spring-boot:run`
- 或使用IDEA直接运行
- 或使用提供的 `backend/start-backend.bat` 脚本

### 问题4: 前端启动失败

**错误**: `Cannot find module`

**解决**:
```bash
cd frontend
rm -rf node_modules
npm install
npm run dev
```

### 问题5: 地图不显示

**原因**: 未配置高德API密钥

**解决**: 按照上面"重要配置"章节配置密钥

---

## 📊 启动成功的标志

### 后端启动成功

看到以下日志:
```
TasteFinder Backend :: Spring Boot 3.1.5
Started TastefinderApplication in X.XXX seconds
```

访问 http://localhost:8080/actuator/health 返回 `{"status":"UP"}`

### 前端启动成功

看到以下提示:
```
  VITE vX.X.X  ready in XXX ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
```

访问 http://localhost:5173 可以看到登录页面

---

## 🎯 下一步

启动成功后：

1. ✅ 打开浏览器访问 http://localhost:5173
2. ✅ 使用测试账户登录
3. ✅ 开始探索功能！

**完整功能列表**:
- 用户注册和登录
- 地图展示和餐厅搜索
- 路径规划和导航
- 餐厅收藏管理
- 用户评价系统

---

## 📞 需要帮助？

- 📖 查看详细文档: `specs/001-tastefinder-api/quickstart.md`
- 🔧 查看部署指南: `DEPLOYMENT_GUIDE.md`
- 📋 查看实施计划: `specs/001-tastefinder-api/plan.md`
- 🐛 报告问题: GitHub Issues

---

**祝使用愉快！** 🎊

