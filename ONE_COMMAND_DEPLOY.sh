#!/bin/bash

# TasteFinder 一键部署脚本（Ubuntu服务器）
# 使用方法：
# wget https://raw.githubusercontent.com/bz1121/Food/main/ONE_COMMAND_DEPLOY.sh
# chmod +x ONE_COMMAND_DEPLOY.sh
# sudo ./ONE_COMMAND_DEPLOY.sh

set -e

echo "=========================================="
echo "TasteFinder 一键部署脚本"
echo "=========================================="
echo ""

# 1. 更新系统
echo "[1/8] 更新系统..."
apt update

# 2. 安装MySQL
echo "[2/8] 安装MySQL 8.0..."
apt install -y mysql-server
systemctl start mysql
systemctl enable mysql

# 设置MySQL
mysql << 'EOF'
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'tastefinder_pass_123';
CREATE DATABASE tastefinder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
FLUSH PRIVILEGES;
EOF

# 3. 安装Redis
echo "[3/8] 安装Redis..."
apt install -y redis-server
systemctl start redis-server
systemctl enable redis-server

# 4. 安装Java 17
echo "[4/8] 安装Java 17..."
apt install -y openjdk-17-jdk maven

# 5. 安装Node.js 18
echo "[5/8] 安装Node.js 18..."
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# 6. 克隆项目
echo "[6/8] 克隆GitHub项目..."
# 安装git
sudo apt install git -y
cd /opt
rm -rf Food
git clone https://github.com/bz1121/Food.git
cd Food

# 7. 构建并启动后端
echo "[7/8] 构建并启动后端..."
cd /opt/Food/backend
chmod +x mvnw
mvn clean package -DskipTests

nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  > /var/log/tastefinder-backend.log 2>&1 &

echo "等待后端启动..."
sleep 10

# 8. 构建并部署前端
echo "[8/8] 构建并部署前端..."
cd /opt/Food/frontend
npm install
npm run build

# 安装Nginx
apt install -y nginx

# 部署前端
rm -rf /var/www/html/*
cp -r dist/* /var/www/html/

# 配置Nginx
tee /etc/nginx/sites-available/tastefinder << 'EOF'
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
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
EOF

# 删除默认配置
rm -f /etc/nginx/sites-enabled/default
ln -sf /etc/nginx/sites-available/tastefinder /etc/nginx/sites-enabled/

# 重启Nginx
systemctl restart nginx

# 开放防火墙
ufw allow 80
ufw allow 8080

echo ""
echo "=========================================="
echo "部署完成！"
echo "=========================================="
echo ""
echo "访问地址: http://YOUR_SERVER_IP"
echo "测试账户: admin / password123"
echo ""
echo "后端日志: tail -f /var/log/tastefinder-backend.log"
echo "后端健康: curl http://localhost:8080/actuator/health"
echo ""
echo "=========================================="

