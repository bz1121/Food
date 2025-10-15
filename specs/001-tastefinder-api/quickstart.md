# TasteFinder 快速开始指南

本指南帮助开发者快速搭建 TasteFinder 本地开发环境并运行项目。

---

## 📋 前置要求

### 必需软件

- **Java**: JDK 17+ （推荐 OpenJDK 17）
- **Node.js**: v18+ （推荐 v20 LTS）
- **Docker**: 20.10+ 和 Docker Compose
- **Maven**: 3.8+ （或使用项目内置的 mvnw）
- **Git**: 2.30+

### 可选软件

- **IntelliJ IDEA**: 推荐用于Java开发
- **VS Code**: 推荐用于前端开发
- **Postman** 或 **Insomnia**: API测试工具

### 检查安装

```bash
java --version    # 应显示 17+
node --version    # 应显示 v18+
docker --version  # 应显示 20.10+
mvn --version     # 应显示 3.8+
```

---

## 🚀 快速启动（5分钟）

### 1. 克隆项目

```bash
git clone https://github.com/your-org/tastefinder.git
cd tastefinder
```

### 2. 启动数据库（Docker）

```bash
docker-compose up -d mysql redis
```

等待MySQL初始化完成（约30秒），验证运行状态：

```bash
docker-compose ps
```

应看到 mysql 和 redis 服务状态为 `Up`。

### 3. 配置高德地图API密钥

编辑 `backend/src/main/resources/application-dev.yml`：

```yaml
amap:
  key: YOUR_AMAP_API_KEY        # 替换为你的高德API Key
  secret: YOUR_AMAP_SECRET_KEY  # 替换为你的高德Secret Key
```

> 🔗 申请高德API密钥: https://console.amap.com/dev/key/app

### 4. 启动后端服务

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

后端服务将在 `http://localhost:8080` 启动。

验证：访问 http://localhost:8080/api/health 应返回 `{"status":"UP"}`

### 5. 启动前端服务

**新开一个终端窗口：**

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:5173` 启动。

### 6. 访问应用

打开浏览器访问：**http://localhost:5173**

使用预设测试账户登录：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `critic_a` | `password123` | 美食评论家A |
| `user_d` | `password123` | 普通用户D |

---

## 📚 详细步骤

### 项目结构

```
tastefinder/
├── backend/              # Spring Boot后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
├── frontend/             # Vue 3前端
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   ├── api/
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
├── docker-compose.yml    # Docker编排文件
└── specs/                # 项目文档
    └── 001-tastefinder-api/
        ├── spec.md
        ├── plan.md
        └── data-model.md
```

### 后端开发环境配置

#### 1. 导入到 IntelliJ IDEA

1. File → Open → 选择 `backend` 目录
2. 等待 Maven 依赖下载完成
3. 右键 `Application.java` → Run
4. 确认运行配置使用 `dev` profile

#### 2. 数据库初始化

首次启动时，Flyway会自动执行迁移脚本：

```sql
backend/src/main/resources/db/migration/
├── V1__init_schema.sql       # 创建表结构
└── V2__init_test_data.sql    # 插入测试数据
```

**手动初始化**（如需要）：

```bash
docker-compose exec mysql mysql -uroot -ppassword tastefinder < backend/src/main/resources/db/migration/V1__init_schema.sql
```

#### 3. 查看API文档

启动后端后访问 Swagger UI：

**http://localhost:8080/swagger-ui.html**

可以直接在浏览器中测试所有API接口。

### 前端开发环境配置

#### 1. 安装依赖

```bash
cd frontend
npm install
```

安装的主要依赖：

- Vue 3.3
- Vue Router 4
- Pinia (状态管理)
- Element Plus (UI组件库)
- @amap/amap-jsapi-loader (高德地图)
- Axios (HTTP客户端)

#### 2. 环境变量配置

编辑 `frontend/.env.development`：

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_AMAP_KEY=YOUR_AMAP_WEB_KEY
VITE_AMAP_SECRET=YOUR_AMAP_WEB_SECRET
```

#### 3. 启动开发服务器

```bash
npm run dev
```

Vite 提供热模块替换（HMR），修改代码即时生效。

#### 4. 构建生产版本

```bash
npm run build
```

构建产物输出到 `frontend/dist/` 目录。

---

## 🐳 Docker 完整部署

### 一键启动所有服务

```bash
docker-compose up -d
```

服务说明：

- **mysql**: 数据库（端口 3306）
- **redis**: 缓存（端口 6379）
- **backend**: Spring Boot后端（端口 8080）
- **frontend**: Nginx + Vue前端（端口 80）

### 查看日志

```bash
docker-compose logs -f backend
docker-compose logs -f frontend
```

### 停止服务

```bash
docker-compose down
```

### 清理数据（危险操作）

```bash
docker-compose down -v  # 删除数据卷
```

---

## 🧪 运行测试

### 后端测试

```bash
cd backend
./mvnw test                    # 运行所有测试
./mvnw test -Dtest=UserServiceTest  # 运行单个测试类
./mvnw verify                  # 运行测试 + 代码覆盖率
```

查看覆盖率报告：`backend/target/site/jacoco/index.html`

### 前端测试

```bash
cd frontend
npm run test        # 运行单元测试（Vitest）
npm run test:ui     # 运行测试UI界面
```

---

## 🔧 常见问题

### 问题 1: Docker 容器无法启动

**症状**: `docker-compose up` 报错

**解决方案**:

```bash
# 检查端口占用
netstat -ano | findstr :3306
netstat -ano | findstr :6379

# 清理旧容器
docker-compose down -v
docker-compose up -d
```

### 问题 2: 后端连接数据库失败

**症状**: `Communications link failure`

**解决方案**:

1. 确认 MySQL 容器运行：`docker-compose ps`
2. 检查配置文件中的数据库连接信息
3. 等待 MySQL 完全初始化（首次启动需30秒）

```bash
# 测试连接
docker-compose exec mysql mysql -uroot -ppassword -e "SELECT 1"
```

### 问题 3: 前端跨域请求失败

**症状**: `CORS policy: No 'Access-Control-Allow-Origin'`

**解决方案**:

后端已配置 CORS，检查 `backend/src/main/java/com/tastefinder/config/WebConfig.java`：

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("*");
}
```

### 问题 4: 高德地图不显示

**症状**: 地图组件空白

**解决方案**:

1. 检查 `.env.development` 中的 `VITE_AMAP_KEY` 配置
2. 确认高德API Key已绑定当前域名
3. 打开浏览器控制台查看错误信息
4. 验证Key有效性：访问 https://lbs.amap.com/demo/jsapi-v2/

### 问题 5: npm install 失败

**症状**: 依赖下载超时或失败

**解决方案**:

```bash
# 使用国内镜像
npm config set registry https://registry.npmmirror.com
npm install

# 或使用 cnpm
npm install -g cnpm --registry=https://registry.npmmirror.com
cnpm install
```

---

## 📖 下一步

✅ 环境搭建完成后，建议阅读以下文档：

1. **[API 文档](./contracts/openapi.yaml)** - 了解所有API接口
2. **[数据模型](./data-model.md)** - 理解数据库结构
3. **[实施计划](./plan.md)** - 查看开发路线图
4. **[功能规范](./spec.md)** - 理解产品需求

### 开发工作流

1. 创建新分支：`git checkout -b feature/your-feature`
2. 开发功能，提交代码
3. 运行测试：`./mvnw test` 和 `npm run test`
4. 提交PR，等待Code Review
5. 合并到主分支

### 推荐学习资源

**后端 (Spring Boot)**:
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Security 文档](https://spring.io/projects/spring-security)
- [Spring Data JPA 文档](https://spring.io/projects/spring-data-jpa)

**前端 (Vue 3)**:
- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Element Plus 组件库](https://element-plus.org/zh-CN/)
- [高德地图 JavaScript API](https://lbs.amap.com/api/jsapi-v2/summary)

**工具**:
- [Docker 文档](https://docs.docker.com/)
- [Maven 指南](https://maven.apache.org/guides/)
- [Vite 文档](https://vitejs.dev/)

---

## 🆘 获取帮助

- **Issue 反馈**: https://github.com/your-org/tastefinder/issues
- **开发文档**: `specs/001-tastefinder-api/`
- **团队 Wiki**: (内部链接)
- **技术支持**: dev@tastefinder.com

---

## 📝 License

[Your License Here]

---

**Happy Coding! 🎉**

