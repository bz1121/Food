# TasteFinder 部署指南

**目标环境**: 生产环境  
**更新时间**: 2025-10-14

---

## 📋 部署前检查清单

### 环境要求

**服务器配置**:
- ✅ CPU: 2核及以上
- ✅ 内存: 4GB及以上
- ✅ 磁盘: 50GB及以上
- ✅ 操作系统: Linux (推荐Ubuntu 20.04+)

**软件要求**:
- ✅ Docker: 20.10+
- ✅ Docker Compose: 2.0+
- ✅ Git: 2.30+

### 必须准备的配置

**1. 高德地图API密钥** (关键):
- Web服务Key (后端使用)
- JavaScript API Key (前端使用)
- 申请地址: https://console.amap.com/dev/key/app

**2. 环境变量** (必须):
- 数据库密码
- Redis密码
- JWT secret (强密钥)
- 高德API密钥

---

## 🚀 部署步骤

### 方式1: Docker Compose部署（推荐）

#### 步骤1: 克隆代码

```bash
git clone https://github.com/your-org/tastefinder.git
cd tastefinder
```

#### 步骤2: 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑.env文件，填入真实值
nano .env
```

**必须修改的变量**:
```env
# 数据库密码（强密码）
DB_PASSWORD=your-strong-database-password

# Redis密码
REDIS_PASSWORD=your-redis-password

# JWT密钥（至少32字符）
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production

# 高德API密钥
AMAP_KEY=your-amap-web-service-key
AMAP_SECRET=your-amap-secret
```

#### 步骤3: 构建并启动服务

```bash
# 使用生产环境配置启动
docker-compose -f docker-compose.prod.yml up -d

# 查看日志
docker-compose -f docker-compose.prod.yml logs -f

# 等待所有服务启动完成（约1-2分钟）
```

#### 步骤4: 验证部署

```bash
# 检查容器状态
docker-compose -f docker-compose.prod.yml ps

# 应该看到4个容器都是Up状态:
# - tastefinder-mysql-prod
# - tastefinder-redis-prod
# - tastefinder-backend-prod
# - tastefinder-frontend-prod

# 测试健康检查
curl http://localhost:8080/actuator/health
# 应该返回: {"status":"UP"}

# 访问前端
curl http://localhost
# 应该返回HTML内容
```

#### 步骤5: 配置反向代理（可选）

如果使用Nginx作为入口:

```nginx
server {
    listen 80;
    server_name tastefinder.yourdomain.com;
    
    # 前端
    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    # 后端API
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

### 方式2: 分别构建镜像部署

#### 步骤1: 构建后端镜像

```bash
cd backend
docker build -t tastefinder-backend:1.0.0 .
```

#### 步骤2: 构建前端镜像

```bash
cd frontend
docker build -t tastefinder-frontend:1.0.0 .
```

#### 步骤3: 运行数据库

```bash
docker run -d \
  --name tastefinder-mysql \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=tastefinder \
  -e MYSQL_USER=tastefinder_user \
  -e MYSQL_PASSWORD=yourpassword \
  -p 3306:3306 \
  -v mysql_data:/var/lib/mysql \
  mysql:8.0
```

#### 步骤4: 运行Redis

```bash
docker run -d \
  --name tastefinder-redis \
  -p 6379:6379 \
  redis:7-alpine
```

#### 步骤5: 运行后端

```bash
docker run -d \
  --name tastefinder-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_PASSWORD=yourpassword \
  -e JWT_SECRET=your-jwt-secret \
  -e AMAP_KEY=your-amap-key \
  -e AMAP_SECRET=your-amap-secret \
  --link tastefinder-mysql:mysql \
  --link tastefinder-redis:redis \
  tastefinder-backend:1.0.0
```

#### 步骤6: 运行前端

```bash
docker run -d \
  --name tastefinder-frontend \
  -p 80:80 \
  --link tastefinder-backend:backend \
  tastefinder-frontend:1.0.0
```

---

## 🔒 安全配置

### 1. 更改默认密码

**必须修改**:
- MySQL root密码
- 数据库用户密码
- Redis密码
- JWT secret

### 2. 配置防火墙

```bash
# 只开放必要端口
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw enable
```

### 3. 配置SSL证书（推荐）

使用Let's Encrypt:

```bash
# 安装certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d tastefinder.yourdomain.com

# 自动续期
sudo certbot renew --dry-run
```

---

## 📊 监控和维护

### 健康检查

```bash
# 后端健康检查
curl http://localhost:8080/actuator/health

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```

### 日志查看

```bash
# 查看容器日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 查看后端应用日志
docker exec tastefinder-backend cat /var/log/tastefinder/application.log
```

### 数据备份

```bash
# 备份MySQL数据库
docker exec tastefinder-mysql mysqldump -uroot -p tastefinder > backup.sql

# 备份Redis数据
docker exec tastefinder-redis redis-cli SAVE
docker cp tastefinder-redis:/data/dump.rdb ./redis-backup.rdb
```

### 更新应用

```bash
# 拉取最新代码
git pull origin main

# 重新构建并启动
docker-compose -f docker-compose.prod.yml up -d --build

# 查看部署状态
docker-compose -f docker-compose.prod.yml ps
```

---

## 🔧 故障排查

### 问题1: 容器无法启动

**检查日志**:
```bash
docker-compose logs backend
docker-compose logs mysql
```

**常见原因**:
- 端口被占用
- 环境变量未配置
- MySQL初始化失败

### 问题2: 数据库连接失败

**检查**:
```bash
# 进入MySQL容器
docker exec -it tastefinder-mysql mysql -uroot -p

# 检查数据库
SHOW DATABASES;
USE tastefinder;
SHOW TABLES;
```

### 问题3: 高德API调用失败

**检查**:
- API密钥是否正确
- 密钥是否绑定了正确的服务
- 配额是否充足

---

## 📈 性能优化建议

### 1. 数据库优化

```sql
-- 定期分析表
ANALYZE TABLE users, restaurant_favorites, restaurant_reviews;

-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query_log';
```

### 2. Redis优化

```bash
# 配置最大内存
redis-cli CONFIG SET maxmemory 2gb
redis-cli CONFIG SET maxmemory-policy allkeys-lru
```

### 3. 应用优化

- 启用G1GC: `-XX:+UseG1GC`
- 设置最大堆内存: `-Xmx1g`
- 启用JMX监控

---

## 📋 部署检查清单

部署前确认:

- [ ] 环境变量已配置
- [ ] 数据库密码已修改
- [ ] JWT secret已修改
- [ ] 高德API密钥已配置
- [ ] 防火墙规则已设置
- [ ] SSL证书已配置（如需）
- [ ] 备份策略已制定
- [ ] 监控告警已配置

部署后验证:

- [ ] 应用可访问
- [ ] 健康检查通过
- [ ] 可以注册新用户
- [ ] 可以登录
- [ ] 地图可以加载
- [ ] POI搜索正常
- [ ] 导航功能正常
- [ ] 收藏功能正常
- [ ] 评价功能正常

---

**部署文档完成！** 🚀

按照本指南，可以在30分钟内完成生产环境部署。

