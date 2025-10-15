# 🆕 全新服务器部署指南

**不用宝塔面板，纯命令行部署！**

---

## 🚀 方案1: 一键部署脚本（最简单）

### 在全新Ubuntu服务器执行

```bash
# 1. 下载部署脚本
wget https://raw.githubusercontent.com/bz1121/Food/main/ONE_COMMAND_DEPLOY.sh

# 2. 赋予执行权限
chmod +x ONE_COMMAND_DEPLOY.sh

# 3. 执行部署（约10-15分钟）
sudo ./ONE_COMMAND_DEPLOY.sh

# 等待完成
```

**部署完成后访问**: http://14.103.168.111

---

## 🛠️ 方案2: 手动逐步部署

### 完整命令（复制粘贴执行）

```bash
# 1. 安装所有软件
sudo apt update
sudo apt install -y mysql-server redis-server openjdk-17-jdk maven nginx git

# 2. 安装Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo bash -
sudo apt install -y nodejs

# 3. 配置MySQL
sudo mysql << 'EOF'
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'tastefinder_pass_123';
CREATE DATABASE tastefinder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
FLUSH PRIVILEGES;
EOF

# 4. 启动服务
sudo systemctl start mysql redis-server nginx
sudo systemctl enable mysql redis-server nginx

# 5. 克隆项目
cd /opt
sudo rm -rf Food
git clone https://github.com/bz1121/Food.git
cd Food

# 6. 构建后端
cd /opt/Food/backend
chmod +x mvnw
mvn clean package -DskipTests

# 7. 启动后端
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  > /var/log/tastefinder.log 2>&1 &

# 8. 构建前端
cd /opt/Food/frontend
npm install
npm run build

# 9. 部署前端
sudo rm -rf /var/www/html/*
sudo cp -r dist/* /var/www/html/

# 10. 配置Nginx
sudo tee /etc/nginx/sites-available/default << 'EOF'
server {
    listen 80 default_server;
    server_name _;
    
    root /var/www/html;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
EOF

# 11. 重启Nginx
sudo systemctl restart nginx

# 12. 开放防火墙
sudo ufw allow 80

echo "部署完成！访问 http://YOUR_SERVER_IP"
```

---

## ✅ 部署完成标志

**执行检查**:
```bash
# 后端健康检查
curl http://localhost:8080/actuator/health

# 应该返回
{"status":"UP"}

# 前端测试
curl http://localhost | grep "TasteFinder"

# 应该看到HTML内容
```

---

## 🌐 访问应用

```
http://14.103.168.111
```

**测试账户**:
- admin / password123

---

## 📝 如果是全新服务器

**推荐顺序**:
1. 重置服务器到Ubuntu 24.04纯净系统
2. SSH连接
3. 复制"方案2"的所有命令
4. 一次性粘贴执行
5. 等待10-15分钟
6. 访问测试

---

**全新服务器用方案2，一次性完成部署！** 🚀✅

