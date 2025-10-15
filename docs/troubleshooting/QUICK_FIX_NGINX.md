# ⚡ 快速修复Nginx配置

**直接替换宝塔默认页面！**

---

## 🎯 在服务器执行

```bash
# 1. 找到宝塔默认站点配置
ls -la /www/server/nginx/conf/vhost/

# 2. 查看都有哪些配置文件
cat /www/server/nginx/conf/vhost/*.conf

# 3. 备份并删除所有旧配置
sudo mv /www/server/nginx/conf/vhost /www/server/nginx/conf/vhost.bak
sudo mkdir /www/server/nginx/conf/vhost

# 4. 创建唯一的配置
sudo tee /www/server/nginx/conf/vhost/14.103.168.111.conf << 'EOF'
server {
    listen 80;
    server_name 14.103.168.111;
    
    root /www/wwwroot/default;
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

# 5. 测试配置
sudo nginx -t

# 6. 重启Nginx
sudo /etc/init.d/nginx restart

# 7. 测试访问
curl http://localhost | head -20
```

**应该看到HTML代码，不是"没有找到站点"** ✅

---

## 🌐 访问

**浏览器刷新**:
```
http://14.103.168.111
```

**应该看到TasteFinder登录页面！** 🎊

---

**复制命令执行！** 🚀

