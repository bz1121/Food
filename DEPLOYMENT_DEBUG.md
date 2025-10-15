# 🔍 部署问题排查

**服务器**: 14.103.168.111

---

## 🧪 完整检查

**在服务器执行以下检查命令**:

```bash
# 1. 检查后端是否运行
ps aux | grep tastefinder
curl http://localhost:8080/actuator/health

# 2. 检查Nginx是否运行
sudo systemctl status nginx
ps aux | grep nginx

# 3. 检查80端口是否监听
sudo netstat -tunlp | grep :80

# 4. 检查前端文件是否存在
ls -la /www/wwwroot/default/

# 5. 检查Nginx配置
cat /www/server/nginx/conf/vhost/tastefinder.conf

# 6. 测试本地访问
curl http://localhost

# 7. 检查防火墙
sudo ufw status
```

---

## 📝 把结果发给我

**执行上面所有命令，把输出发给我，我帮你分析问题！**

---

## 💡 常见问题快速修复

### 问题1: Nginx未启动

```bash
sudo systemctl start nginx
sudo systemctl status nginx
```

### 问题2: 后端未启动

```bash
cd /opt/Food/backend
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  > backend.log 2>&1 &
```

### 问题3: 80端口被占用

```bash
sudo systemctl stop nginx
sudo /www/server/nginx/sbin/nginx
```

### 问题4: 防火墙未开放

```bash
sudo ufw allow 80
sudo ufw status
```

---

**执行检查命令，告诉我结果！** 🔍

