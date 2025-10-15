# 🐧 Ubuntu服务器部署指南

**系统要求**: Ubuntu 20.04 LTS 或更高  
**推荐配置**: 2核CPU，4GB内存，50GB硬盘  
**部署方式**: Docker Compose（最简单）

---

## 📋 前置要求

### 服务器配置

**最低配置**:
- CPU: 2核
- 内存: 4GB
- 硬盘: 50GB
- 带宽: 5Mbps

**推荐配置**:
- CPU: 4核
- 内存: 8GB
- 硬盘: 100GB
- 带宽: 10Mbps

---

## 🚀 快速部署（推荐）

### 方式1: Docker Compose部署 ⭐⭐⭐

**最简单，强烈推荐！**

#### 步骤1: 安装Docker

```bash
# 更新软件包
sudo apt update

# 安装Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 安装Docker Compose
sudo apt install docker-compose -y

# 启动Docker服务
sudo systemctl start docker
sudo systemctl enable docker

# 验证安装
docker --version
docker-compose --version
```

#### 步骤2: 上传项目文件

```bash
# 使用git克隆（推荐）
git clone https://github.com/your-org/tastefinder.git
cd tastefinder

# 或使用scp上传
# scp -r ./tastefinder user@your-server:/home/user/
```

#### 步骤3: 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑配置
nano .env
```

**修改内容**:
```env
# 数据库密码（设置强密码）
DB_PASSWORD=your-strong-password

# JWT密钥（至少32字符）
JWT_SECRET=your-super-secret-jwt-key-min-32-chars

# 高德API密钥
AMAP_KEY=your-amap-key
AMAP_SECRET=your-amap-secret
```

#### 步骤4: 启动所有服务

```bash
# 使用生产环境配置启动
sudo docker-compose -f docker-compose.prod.yml up -d

# 查看日志
sudo docker-compose logs -f
```

#### 步骤5: 配置Nginx（可选）

```bash
# 安装Nginx
sudo apt install nginx -y

# 创建配置文件
sudo nano /etc/nginx/sites-available/tastefinder
```

**Nginx配置**:
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端
    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    
    # 后端API
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

**启用站点**:
```bash
sudo ln -s /etc/nginx/sites-available/tastefinder /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

#### 步骤6: 配置SSL（推荐）

```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取SSL证书
sudo certbot --nginx -d your-domain.com

# 测试自动续期
sudo certbot renew --dry-run
```

---

## 🛠️ 手动部署方式

### 方式2: 传统部署

#### 后端部署

```bash
# 安装Java 17
sudo apt install openjdk-17-jdk -y

# 验证
java -version

# 构建项目
cd backend
./mvnw clean package -DskipTests

# 运行
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=prod &

# 查看日志
tail -f nohup.out
```

#### 前端部署

```bash
# 安装Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs -y

# 验证
node --version
npm --version

# 构建前端
cd frontend
npm install
npm run build

# 部署到Nginx
sudo cp -r dist/* /var/www/html/
```

---

## 📊 服务器面板推荐

### 1. 宝塔面板（最推荐）⭐⭐⭐⭐⭐

**优点**:
- 中文界面
- 可视化管理
- 一键安装软件
- 监控和日志
- 文件管理

**安装**:
```bash
wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
sudo bash install.sh
```

**安装后**:
- 访问 http://your-server-ip:8888
- 在面板中安装Docker
- 上传项目文件
- 一键启动

---

### 2. 1Panel（开源免费）⭐⭐⭐⭐

**优点**:
- 开源免费
- 现代化界面
- Docker管理
- 轻量快速

**安装**:
```bash
curl -sSL https://resource.fit2cloud.com/1panel/package/quick_start.sh | sudo bash
```

---

### 3. Portainer（Docker专用）⭐⭐⭐

**优点**:
- Docker可视化
- 容器管理
- 镜像管理

**安装**:
```bash
sudo docker volume create portainer_data
sudo docker run -d -p 9000:9000 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v portainer_data:/data \
  portainer/portainer-ce
```

访问: http://your-server-ip:9000

---

## 🎯 最佳实践推荐

### 推荐方案：宝塔面板 + Docker

**为什么选宝塔**:
1. ✅ 中文界面，上手简单
2. ✅ 集成Docker管理
3. ✅ 文件管理方便
4. ✅ 监控和日志完善
5. ✅ SSL证书一键申请
6. ✅ 防火墙可视化配置

**部署流程**:
1. 安装宝塔面板
2. 在面板中安装Docker
3. 上传项目文件
4. 配置环境变量
5. 运行docker-compose
6. 配置Nginx反向代理
7. 申请SSL证书
8. 完成！

---

## 📁 项目文件准备

### 需要上传的文件

**必须**:
- `backend/` 目录
- `frontend/` 目录
- `docker-compose.prod.yml`
- `.env` 配置文件

**可选**:
- `nginx.conf`
- `README.md`
- 文档目录

---

## 🔒 安全配置

### 1. 防火墙

```bash
# 安装UFW
sudo apt install ufw -y

# 配置规则
sudo ufw allow 22      # SSH
sudo ufw allow 80      # HTTP
sudo ufw allow 443     # HTTPS
sudo ufw allow 8888    # 宝塔面板（如使用）

# 启用防火墙
sudo ufw enable
```

### 2. 修改SSH端口（可选）

```bash
sudo nano /etc/ssh/sshd_config
# 修改 Port 22 为其他端口
sudo systemctl restart sshd
```

### 3. 配置强密码

- 数据库密码
- JWT secret
- Redis密码（如果启用）

---

## 📊 监控和维护

### 日志查看

```bash
# Docker容器日志
sudo docker-compose logs -f backend
sudo docker-compose logs -f frontend

# 应用日志
sudo docker exec backend cat /var/log/tastefinder/application.log
```

### 数据备份

```bash
# MySQL备份
sudo docker exec mysql mysqldump -uroot -p tastefinder > backup.sql

# 定时备份（crontab）
0 2 * * * /path/to/backup.sh
```

### 更新应用

```bash
# 拉取最新代码
git pull

# 重新构建和部署
sudo docker-compose -f docker-compose.prod.yml up -d --build
```

---

## 🎊 部署完成后

### 访问地址

**前端**: http://your-domain.com  
**后端API**: http://your-domain.com/api  
**管理后台**: http://your-domain.com/admin

### 性能优化

**启用CDN**:
- 静态资源加速
- 图片CDN

**启用Redis缓存**:
- 已配置 ✅

**数据库优化**:
- 定期分析表
- 监控慢查询

---

## 📞 需要帮助？

**宝塔面板教程**: https://www.bt.cn/bbs/forum-1-1.html  
**Docker文档**: https://docs.docker.com/  
**Nginx配置**: https://nginx.org/en/docs/

---

**推荐使用宝塔面板 + Docker部署，最简单高效！** 🚀✅

---

## 🎯 下一步

1. 准备Ubuntu服务器
2. 安装宝塔面板
3. 上传项目文件
4. 配置环境变量
5. 运行docker-compose
6. 配置域名和SSL
7. 开始使用！

