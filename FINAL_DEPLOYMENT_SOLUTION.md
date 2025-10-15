# 🎯 TasteFinder最终部署方案

**问题**: tar文件损坏  
**解决**: 使用宝塔面板 + 不用Docker

---

## ✅ 最佳方案：宝塔面板可视化部署

### 步骤1: 访问宝塔面板

**浏览器打开**:
```
https://14.103.168.111:42216/8a2e4970
```

**登录**:
- 用户名: ybmzzksi
- 密码: 1b6a0c21

**首次打开会提示"不安全"，点击"高级" → "继续访问"**

---

### 步骤2: 在宝塔面板安装软件

**点击"软件商店"，安装**:
1. ✅ MySQL 8.0（一键安装）
2. ✅ Redis（一键安装）
3. ✅ Nginx（应该已安装）
4. ✅ Java项目一键部署（或手动装Java 17）

**等待安装完成**（约10-15分钟）

---

### 步骤3: 创建数据库

**在宝塔面板**:
1. 点击"数据库"
2. 点击"添加数据库"
3. 填写：
   - 数据库名: `tastefinder`
   - 用户名: `root`（使用root）
   - 密码: `tastefinder_pass_123`
4. 点击"提交"

---

### 步骤4: 上传并部署后端

**在宝塔面板**:
1. 点击"文件"
2. 进入 `/opt/Food/backend`
3. 点击"终端"
4. 执行：

```bash
# 构建项目
./mvnw clean package -DskipTests

# 后台运行
nohup java -jar target/tastefinder-backend-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:mysql://localhost:3306/tastefinder \
  --spring.datasource.username=root \
  --spring.datasource.password=tastefinder_pass_123 \
  > backend.log 2>&1 &

# 查看日志
tail -f backend.log
```

**看到"Started TastefinderApplication"即成功** ✅

---

### 步骤5: 部署前端

**在宝塔终端**:

```bash
cd /opt/Food/frontend

# 修改API地址为服务器IP
cat > .env.production << 'EOF'
VITE_API_BASE_URL=http://14.103.168.111:8080/api
VITE_AMAP_KEY=0f28efaf0ec413818217dbb48635b107
VITE_AMAP_SECRET=a7516785d460fa2787387b9a355c092e
VITE_AMAP_VERSION=2.0
EOF

# 安装依赖
npm install

# 构建
npm run build

# 部署到Nginx
sudo cp -r dist/* /www/wwwroot/default/
```

---

### 步骤6: 配置Nginx反向代理

**在宝塔面板**:
1. 点击"网站"
2. 找到默认站点
3. 点击"设置"
4. 点击"反向代理"
5. 添加反向代理：
   - 代理名称: `backend-api`
   - 目标URL: `http://127.0.0.1:8080`
   - 发送域名: `$host`
   - 内容替换: 留空
6. 保存

---

## 🎊 完成！

**访问**: http://14.103.168.111

**应该看到TasteFinder登录页面！** ✅

**测试账户**:
- admin / password123（管理员）
- critic_a / password123（评论家）
- user_d / password123（用户）

---

## 📝 宝塔面板常用功能

**监控**: 实时CPU、内存、网络  
**文件**: 在线编辑、上传下载  
**终端**: SSH终端  
**计划任务**: 定时重启、备份  
**安全**: 防火墙、SSH配置  

---

**立即访问宝塔面板开始部署！** 🎨🚀

