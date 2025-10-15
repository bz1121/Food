# 🚀 服务器部署步骤

**服务器**: 14.103.168.111  
**状态**: 已连接 ✅ Docker已安装 ✅

---

## 📦 立即执行的命令

### 步骤1: 安装Docker Compose

```bash
sudo apt install docker-compose -y
```

### 步骤2: 克隆项目

```bash
# 进入部署目录
cd /opt

# 克隆GitHub项目
git clone https://github.com/bz1121/Food.git

# 进入项目目录
cd Food
```

### 步骤3: 配置环境变量

```bash
# 创建环境变量文件
cat > .env << 'EOF'
DB_PASSWORD=tastefinder_pass_123
REDIS_PASSWORD=
JWT_SECRET=tastefinder-jwt-secret-key-2024-production-min-32-chars
AMAP_KEY=431faeae95f61be3faa8ac06a599fe27
AMAP_SECRET=your-amap-secret
SERVER_PORT=8080
EOF
```

### 步骤4: 启动所有服务

```bash
# 启动
sudo docker-compose -f docker-compose.prod.yml up -d

# 查看容器状态
sudo docker ps

# 应该看到4个容器:
# - tastefinder-mysql-prod
# - tastefinder-redis-prod  
# - tastefinder-backend-prod
# - tastefinder-frontend-prod
```

### 步骤5: 查看日志

```bash
# 查看所有日志
sudo docker-compose -f docker-compose.prod.yml logs -f

# 或单独查看
sudo docker-compose logs backend
sudo docker-compose logs frontend
```

### 步骤6: 测试访问

**在浏览器访问**:
```
http://14.103.168.111
```

**应该看到登录页面** ✅

---

## 🔍 验证部署

### 检查容器

```bash
sudo docker ps
```

**应该看到4个容器都是Up状态**

### 测试后端

```bash
curl http://localhost:8080/actuator/health
```

**应该返回**: `{"status":"UP"}`

### 测试前端

```bash
curl http://localhost
```

**应该返回HTML内容**

---

## 🌐 配置防火墙

```bash
# 安装UFW（如果没有）
sudo apt install ufw -y

# 开放端口
sudo ufw allow 22      # SSH
sudo ufw allow 80      # HTTP
sudo ufw allow 443     # HTTPS

# 启用防火墙
sudo ufw enable

# 查看状态
sudo ufw status
```

---

## 📊 访问地址

**前端**: http://14.103.168.111  
**后端API**: http://14.103.168.111:8080/api  
**健康检查**: http://14.103.168.111:8080/actuator/health

---

## 🔧 常用命令

### 重启服务

```bash
sudo docker-compose -f docker-compose.prod.yml restart
```

### 停止服务

```bash
sudo docker-compose -f docker-compose.prod.yml down
```

### 更新代码

```bash
cd /opt/Food
git pull
sudo docker-compose -f docker-compose.prod.yml up -d --build
```

### 查看容器日志

```bash
# 实时日志
sudo docker-compose logs -f backend

# 最后100行
sudo docker logs --tail 100 tastefinder-backend-prod
```

---

## ⚠️ 如果遇到问题

### 问题1: 端口被占用

```bash
# 查看端口占用
sudo netstat -tunlp | grep 80
sudo netstat -tunlp | grep 8080

# 停止占用进程
sudo kill -9 <PID>
```

### 问题2: 容器启动失败

```bash
# 查看详细日志
sudo docker-compose logs backend
sudo docker-compose logs mysql

# 重新启动
sudo docker-compose down
sudo docker-compose -f docker-compose.prod.yml up -d
```

### 问题3: 数据库连接失败

```bash
# 进入MySQL容器
sudo docker exec -it tastefinder-mysql-prod mysql -uroot -p

# 检查数据库
SHOW DATABASES;
USE tastefinder;
SHOW TABLES;
```

---

## 🎯 部署完成标志

**所有容器运行**:
```bash
$ sudo docker ps
CONTAINER ID   STATUS
xxx            Up 2 minutes   tastefinder-mysql-prod
xxx            Up 2 minutes   tastefinder-redis-prod
xxx            Up 1 minute    tastefinder-backend-prod
xxx            Up 1 minute    tastefinder-frontend-prod
```

**可以访问**:
- ✅ http://14.103.168.111 显示登录页面
- ✅ 可以登录（admin/password123）
- ✅ 所有功能正常

---

## 📝 下一步优化

**1. 配置域名**:
- 绑定域名到IP
- 配置Nginx

**2. 申请SSL**:
- Let's Encrypt免费证书
- HTTPS访问

**3. 性能优化**:
- 启用缓存
- CDN加速

---

**按顺序执行命令即可！有问题随时告诉我！** 🚀

