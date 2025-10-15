# 🔧 服务器部署修复命令

**问题1**: 环境变量不完整  
**问题2**: Docker镜像拉取超时

---

## 🎯 立即在服务器执行

### 1. 完善环境变量

```bash
# 删除旧的.env
rm .env

# 创建完整的.env文件
cat > .env << 'EOF'
# MySQL配置
MYSQL_ROOT_PASSWORD=tastefinder_pass_123
DB_NAME=tastefinder
DB_USERNAME=root
DB_PASSWORD=tastefinder_pass_123

# Redis配置
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT配置
JWT_SECRET=tastefinder-secret-2024-min-32-chars-production

# 高德API
AMAP_KEY=431faeae95f61be3faa8ac06a599fe27
AMAP_SECRET=

# 服务器配置
SERVER_PORT=8080
EOF

# 查看确认
cat .env
```

---

### 2. 配置Docker镜像源（解决网络问题）

```bash
# 创建Docker配置目录
sudo mkdir -p /etc/docker

# 配置国内镜像源
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com"
  ]
}
EOF

# 重启Docker
sudo systemctl daemon-reload
sudo systemctl restart docker

# 验证配置
sudo docker info | grep -A 10 "Registry Mirrors"
```

---

### 3. 重新启动服务

```bash
# 进入项目目录
cd /opt/Food

# 拉取镜像（使用镜像源，应该快很多）
sudo docker-compose -f docker-compose.prod.yml pull

# 启动服务
sudo docker-compose -f docker-compose.prod.yml up -d

# 查看容器状态
sudo docker ps

# 查看日志
sudo docker-compose -f docker-compose.prod.yml logs -f
```

---

### 4. 开放防火墙端口

```bash
# 开放80端口
sudo ufw allow 80

# 查看状态
sudo ufw status
```

---

## ✅ 成功标志

**执行`sudo docker ps`应该看到4个容器**:
```
CONTAINER ID   IMAGE                  STATUS
xxx            mysql:8.0              Up
xxx            redis:7-alpine         Up
xxx            tastefinder-backend    Up
xxx            tastefinder-frontend   Up
```

**访问测试**:
```
http://14.103.168.111
```

应该看到TasteFinder登录页面！

---

## 🔍 如果还有问题

### 镜像拉取仍超时

**手动拉取镜像**:
```bash
# 基础镜像
sudo docker pull mysql:8.0
sudo docker pull redis:7-alpine
```

### 查看详细错误

```bash
sudo docker-compose -f docker-compose.prod.yml logs
```

---

**按顺序执行上面的命令！** 🚀

