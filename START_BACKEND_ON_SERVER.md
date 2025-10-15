# 🚀 服务器启动后端完整指南

**服务器**: 14.103.168.111  
**项目路径**: /opt/Food

---

## ✅ 前提检查

**确保MySQL和Redis已安装完成**:
```bash
sudo systemctl status mysql
sudo systemctl status redis
```

都应该显示 `active (running)` ✅

---

## 🔧 启动后端

### 步骤1: 修改Redis端口配置

**宝塔安装的Redis端口是26739**，需要修改配置：

```bash
cd /opt/Food/backend/src/main/resources

# 修改Redis端口
sed -i 's/port: 6379/port: 26739/g' application.yml

# 验证修改
grep "port: 26739" application.yml
```

---

### 步骤2: 创建数据库

```bash
# 使用root连接MySQL（宝塔安装的MySQL）
mysql -uroot -p

# 输入MySQL密码（在宝塔面板"数据库"中查看）
```

**在MySQL命令行执行**:
```sql
CREATE DATABASE tastefinder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES;
EXIT;
```

---

### 步骤3: 安装Maven（如果没有）

```bash
# 安装Maven
sudo apt install maven -y

# 验证
mvn -version
```

---

### 步骤4: 构建项目

```bash
cd /opt/Food/backend

# 赋予mvnw执行权限
chmod +x mvnw

# 使用项目自带的Maven构建
./mvnw clean package -DskipTests

# 或使用系统Maven
# mvn clean package -DskipTests

# 等待构建完成（约2-3分钟）
```

**看到 `BUILD SUCCESS` 即成功** ✅

---

### 步骤5: 启动后端应用

```bash
cd /opt/Food/backend

# 后台运行
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  --spring.data.redis.port=26739 \
  > backend.log 2>&1 &

# 查看实时日志
tail -f backend.log
```

**看到以下内容表示启动成功**:
```
Started TastefinderApplication in X seconds
Tomcat started on port(s): 8080
```

按 `Ctrl+C` 退出日志查看（应用会继续运行）

---

### 步骤6: 测试后端

```bash
# 测试健康检查
curl http://localhost:8080/actuator/health

# 应该返回
{"status":"UP"}
```

---

## 🔍 查看后端进程

```bash
# 查看Java进程
ps aux | grep java

# 查看8080端口
sudo netstat -tunlp | grep 8080

# 停止后端（如需）
pkill -f tastefinder-backend
```

---

## 📋 完整启动命令（复制粘贴）

```bash
# 1. 修改Redis端口
cd /opt/Food/backend/src/main/resources
sed -i 's/port: 6379/port: 26739/g' application.yml

# 2. 构建项目
cd /opt/Food/backend
chmod +x mvnw
./mvnw clean package -DskipTests

# 3. 启动应用
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  --spring.data.redis.port=26739 \
  > backend.log 2>&1 &

# 4. 查看日志
tail -f backend.log

# 5. 测试（新终端）
curl http://localhost:8080/actuator/health
```

---

## ⚠️ 如果启动失败

**查看日志**:
```bash
cat /opt/Food/backend/backend.log
```

**常见问题**:
- MySQL连接失败 → 检查MySQL密码
- Redis连接失败 → 检查端口26739
- 端口被占用 → `sudo lsof -i :8080`

---

**等MySQL和Redis安装完成后，执行上面的命令启动后端！** 🚀✅
