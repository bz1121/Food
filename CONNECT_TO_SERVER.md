# 🔗 连接服务器指南

**服务器IP**: 14.103.168.111  
**系统**: Ubuntu（假设）

---

## 🔐 SSH连接方式

### 方式1: 使用密码连接

**Windows（PowerShell）**:
```bash
ssh root@14.103.168.111
# 或
ssh your-username@14.103.168.111
```

**输入密码后即可连接**

---

### 方式2: 使用SSH密钥（更安全）

**生成密钥**（如果没有）:
```bash
ssh-keygen -t rsa -b 4096
```

**上传公钥到服务器**:
```bash
ssh-copy-id root@14.103.168.111
```

---

## 📦 部署TasteFinder到服务器

### 完整部署流程

#### 步骤1: 连接到服务器

```bash
ssh root@14.103.168.111
```

#### 步骤2: 安装必要软件

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 安装Docker Compose
sudo apt install docker-compose -y

# 安装Git
sudo apt install git -y

# 验证安装
docker --version
docker-compose --version
git --version
```

#### 步骤3: 克隆项目

```bash
# 进入部署目录
cd /opt

# 克隆GitHub项目
git clone https://github.com/bz1121/Food.git
cd Food
```

#### 步骤4: 配置环境变量

```bash
# 创建环境变量文件
cat > .env << 'EOF'
# 数据库配置
DB_HOST=mysql
DB_PORT=3306
DB_NAME=tastefinder
DB_USERNAME=root
DB_PASSWORD=your-strong-password-here

# Redis配置
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT配置（至少32字符）
JWT_SECRET=your-super-secret-jwt-key-min-32-chars-change-this

# 高德API密钥
AMAP_KEY=431faeae95f61be3faa8ac06a599fe27
AMAP_SECRET=your-amap-secret

# 服务器配置
SERVER_PORT=8080
EOF

# 编辑配置（根据需要修改）
nano .env
```

#### 步骤5: 启动服务

```bash
# 使用生产环境配置启动
sudo docker-compose -f docker-compose.prod.yml up -d

# 查看容器状态
sudo docker ps

# 查看日志
sudo docker-compose logs -f
```

#### 步骤6: 配置防火墙

```bash
# 开放必要端口
sudo ufw allow 22      # SSH
sudo ufw allow 80      # HTTP
sudo ufw allow 443     # HTTPS
sudo ufw allow 8080    # 后端API（临时，后续用Nginx代理）

# 启用防火墙
sudo ufw enable

# 查看状态
sudo ufw status
```

#### 步骤7: 测试访问

**访问前端**:
```
http://14.103.168.111
```

**访问后端API**:
```
http://14.103.168.111:8080/actuator/health
```

应该返回: `{"status":"UP"}`

---

## 🌐 配置域名（可选）

### 如果你有域名

#### 步骤1: DNS解析

**添加A记录**:
```
类型: A
主机: @ 或 www
记录值: 14.103.168.111
```

#### 步骤2: 安装Nginx

```bash
sudo apt install nginx -y
```

#### 步骤3: 配置Nginx

```bash
sudo nano /etc/nginx/sites-available/tastefinder
```

**配置内容**:
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

**启用配置**:
```bash
sudo ln -s /etc/nginx/sites-available/tastefinder /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

#### 步骤4: 配置SSL

```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 申请SSL证书
sudo certbot --nginx -d your-domain.com

# 测试自动续期
sudo certbot renew --dry-run
```

---

## 🛠️ 使用宝塔面板（更简单）

### 安装宝塔面板

```bash
# 一键安装
wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
sudo bash install.sh
```

**安装完成后**:
- 面板地址: http://14.103.168.111:8888
- 会显示用户名和密码

**在面板中操作**:
1. 安装Docker
2. 使用文件管理上传项目
3. 终端中运行docker-compose
4. 配置域名和SSL
5. 完成！

---

## 📋 部署检查清单

**连接前**:
- [ ] 有服务器IP: 14.103.168.111 ✅
- [ ] 有SSH登录密码或密钥
- [ ] 服务器已安装Ubuntu

**部署过程**:
- [ ] SSH连接成功
- [ ] 安装Docker
- [ ] 克隆GitHub项目
- [ ] 配置.env文件
- [ ] 启动docker-compose
- [ ] 容器运行正常

**部署完成**:
- [ ] 可以访问 http://14.103.168.111
- [ ] 可以登录
- [ ] 所有功能正常

---

## 💡 快速测试命令

**从本地连接测试**:
```bash
# 测试SSH连接
ssh root@14.103.168.111

# 如果连接成功，执行
whoami
pwd
ls -la
```

---

## 🚀 最简部署方案

**最快速度**（约10分钟）:

1. **SSH连接**:
   ```bash
   ssh root@14.103.168.111
   ```

2. **一键安装宝塔**:
   ```bash
   wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
   sudo bash install.sh
   ```

3. **访问宝塔面板**:
   ```
   http://14.103.168.111:8888
   ```

4. **在面板中**:
   - 安装Docker
   - 克隆项目
   - 启动服务

5. **完成**！

---

**立即执行**: `ssh root@14.103.168.111` 开始部署！ 🚀

