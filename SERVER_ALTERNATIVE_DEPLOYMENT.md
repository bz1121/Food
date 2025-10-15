# 🛠️ 服务器部署替代方案

**问题**: Docker镜像拉取失败（服务器网络限制）  
**服务器**: 14.103.168.111

---

## 🎯 推荐方案：安装宝塔面板

### 为什么用宝塔

- ✅ **可视化界面**，不需要命令行
- ✅ **一键安装**MySQL、Nginx、Node.js、Java
- ✅ **文件管理**方便上传代码
- ✅ **内置终端**可以执行命令
- ✅ **监控面板**实时查看状态
- ✅ **中文界面**容易操作

---

## 🚀 在服务器执行

### 安装宝塔面板

```bash
wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
sudo bash install.sh
```

**安装时间**: 约5-10分钟

**完成后会显示**:
```
==================================================================
Congratulations! Installed successfully!
==================================================================
外网面板地址: http://14.103.168.111:8888/xxxxxxxx
内网面板地址: http://xx.xx.xx.xx:8888/xxxxxxxx
username: xxxxxxxx
password: xxxxxxxx
==================================================================
```

**复制这些信息！**

---

### 访问宝塔面板

**在浏览器打开**:
```
http://14.103.168.111:8888/xxxxxxxx
```

**登录后**:
1. 选择"LNMP"或"LAMP"套件
2. 勾选：
   - Nginx
   - MySQL 8.0
   - Redis
   - Java（选OpenJDK 17）
   - Node.js 18

3. 点击"一键安装"
4. 等待安装完成（约10-20分钟）

---

## 📦 宝塔面板部署TasteFinder

### 步骤1: 上传代码

**在宝塔面板**:
1. 点击"文件"
2. 进入 `/opt/Food`
3. 项目已经克隆好了 ✅

### 步骤2: 配置数据库

**在宝塔面板**:
1. 点击"数据库"
2. 点击"添加数据库"
3. 数据库名: `tastefinder`
4. 用户名: `root`
5. 密码: `tastefinder_pass_123`

### 步骤3: 启动后端

**在宝塔终端或SSH**:
```bash
cd /opt/Food/backend

# 使用宝塔安装的Java
/www/server/java/openjdk17/bin/java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod &
```

### 步骤4: 构建前端

```bash
cd /opt/Food/frontend

# 安装依赖
npm install

# 构建
npm run build

# 部署到Nginx
sudo cp -r dist/* /www/wwwroot/default/
```

### 步骤5: 配置Nginx

**在宝塔面板**:
1. 点击"网站"
2. 点击"设置"
3. "反向代理" → "添加"
4. 目标URL: `http://127.0.0.1:8080`
5. 发送域名: `$host`
6. 保存

---

## 💡 更简单的方案

### 先不用Docker，直接运行

**在服务器执行**:

```bash
# 1. 安装MySQL（宝塔面板会自动装）
# 或手动安装
sudo apt install mysql-server -y

# 2. 安装Redis
sudo apt install redis-server -y

# 3. 安装Java 17
sudo apt install openjdk-17-jdk -y

# 4. 安装Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs -y

# 5. 启动MySQL和Redis
sudo systemctl start mysql
sudo systemctl start redis

# 6. 创建数据库
sudo mysql -e "CREATE DATABASE tastefinder;"
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'tastefinder_pass_123';"

# 7. 运行后端
cd /opt/Food/backend
./mvnw clean package -DskipTests
java -jar target/*.jar &

# 8. 运行前端
cd /opt/Food/frontend
npm install
npm run build

# 9. 安装Nginx
sudo apt install nginx -y

# 10. 部署前端
sudo cp -r dist/* /var/www/html/
```

---

## 🎯 推荐操作

**最简单方式**:

1. **安装宝塔面板**:
   ```bash
   wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh
   sudo bash install.sh
   ```

2. **等待安装完成**（5-10分钟）

3. **访问宝塔面板**: http://14.103.168.111:8888

4. **在面板中可视化操作**:
   - 安装MySQL、Redis、Java、Node.js
   - 上传代码
   - 启动服务

---

**推荐安装宝塔面板，图形界面操作更简单！** 🎨✅

