# 🎨 宝塔面板部署TasteFinder

**最简单的方法：用宝塔面板图形界面配置！**

---

## 🚀 使用宝塔面板部署

### 步骤1: 登录宝塔面板

**浏览器访问**:
```
https://14.103.168.111:42216/8a2e4970
```

**登录**:
- 用户名: ybmzzksi
- 密码: 1b6a0c21

（首次访问点击"高级" → "继续访问"）

---

### 步骤2: 添加网站

**在宝塔面板**:

1. 点击左侧 **"网站"**
2. 点击 **"添加站点"**
3. 填写信息：
   - **域名**: `14.103.168.111`
   - **根目录**: `/www/wwwroot/default`
   - **PHP版本**: 纯静态
4. 点击 **"提交"**

---

### 步骤3: 配置反向代理

**在站点设置中**:

1. 找到刚创建的站点（14.103.168.111）
2. 点击 **"设置"**
3. 点击 **"反向代理"**
4. 点击 **"添加反向代理"**
5. 填写：
   - **代理名称**: `backend-api`
   - **目标URL**: `http://127.0.0.1:8080`
   - **发送域名**: `$host`
   - **缓存**: 关闭
6. **高级选项** → **自定义配置**:
   ```nginx
   location /api {
       proxy_pass http://127.0.0.1:8080;
       proxy_set_header Host $host;
       proxy_set_header X-Real-IP $remote_addr;
   }
   ```
7. 点击 **"保存"**

---

### 步骤4: 删除默认配置

**在宝塔面板**:

1. 点击"网站"
2. 找到其他默认站点（如果有）
3. 点击"删除"（保留我们刚创建的14.103.168.111站点）

---

### 步骤5: 重启Nginx

**在宝塔面板**:

1. 点击左侧"软件商店"
2. 找到"Nginx"
3. 点击"重载配置"或"重启"

---

## 🎊 访问TasteFinder

**浏览器刷新**:
```
http://14.103.168.111
```

**应该看到TasteFinder登录页面！** ✅

---

## 📝 或者命令行快速修复

**在服务器执行**:

```bash
# 1. 查找宝塔的站点配置文件
ls -la /www/server/nginx/conf/vhost/

# 2. 查看主配置
cat /www/server/nginx/conf/nginx.conf | grep "default_server"

# 3. 直接编辑主配置（最简单）
sudo nano /www/server/nginx/conf/nginx.conf

# 找到server块，修改root路径为：
# root /www/wwwroot/default;

# 添加API反向代理：
# location /api {
#     proxy_pass http://127.0.0.1:8080;
# }

# 保存退出(Ctrl+X, Y, Enter)

# 4. 重启Nginx
sudo systemctl restart nginx

# 5. 测试
curl http://localhost | head -20
```

---

**推荐用宝塔面板图形界面配置更简单！** 🎨✅

