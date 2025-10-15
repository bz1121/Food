# 🌐 内网穿透访问指南

**问题**: 内网穿透无法访问前端应用  
**原因**: Vite默认只监听localhost  
**解决**: 配置监听所有网络接口

---

## ✅ 已修复配置

### Vite配置更新

**文件**: `frontend/vite.config.js`

**修改**:
```javascript
server: {
  host: '0.0.0.0',  // 监听所有网络接口
  port: 5173,
  // ... 其他配置
}
```

---

## 🔄 重启前端

**在前端终端**:
1. 按 `Ctrl + C` 停止
2. 运行: `npm run dev`
3. 看到启动信息:
   ```
   ➜  Local:   http://localhost:5173/
   ➜  Network: http://192.168.x.x:5173/  ← 应该显示这行
   ```

---

## 🌐 内网穿透配置

### 映射配置

**前端**:
- 本地地址: `0.0.0.0:5173`
- 内网IP: `192.168.x.x:5173`
- 外网域名: 你的穿透域名

**后端**:
- 本地地址: `localhost:8080`
- 需要单独映射（如果外网访问）

---

## ⚠️ 注意事项

### 如果使用内网穿透访问

**1. CORS配置**（已完成）:
- Vite已启用CORS
- 后端SecurityConfig已配置CORS

**2. 后端API访问**:

如果前端通过外网域名访问，后端也需要配置：

**选项A**: 后端也做内网穿透
- 映射8080端口
- 前端修改API地址为外网地址

**选项B**: 使用前端代理（推荐）
- 前端已配置proxy
- `/api`请求会自动转发到`localhost:8080`
- 但内网穿透可能不支持proxy

**选项C**: 修改前端API地址

编辑 `frontend/.env.development`:
```env
# 如果后端有外网地址
VITE_API_BASE_URL=http://your-backend-domain/api
```

---

## 🚀 完整配置示例

### 场景1: 本地开发（默认）

**前端**: http://localhost:5173  
**后端**: http://localhost:8080  
**配置**: 无需修改

### 场景2: 局域网访问

**前端**: http://192.168.1.100:5173  
**后端**: http://192.168.1.100:8080  
**配置**: 
```javascript
// vite.config.js
server: {
  host: '0.0.0.0'  // ✅ 已配置
}
```

### 场景3: 内网穿透

**前端**: http://your-domain.com  
**后端**: http://api.your-domain.com  

**前端配置**:
```env
VITE_API_BASE_URL=http://api.your-domain.com/api
```

**后端配置**:
```yaml
# SecurityConfig.java CORS origins
.allowedOrigins(List.of(
  "http://your-domain.com",
  "https://your-domain.com"
))
```

---

## 🔧 常见问题

### Q: 重启后还是连接失败？

**检查清单**:
1. ✅ Vite配置 `host: '0.0.0.0'`
2. ✅ 前端已重启
3. ✅ 看到 `Network: http://192.168.x.x:5173`
4. ✅ 防火墙允许5173端口
5. ✅ 内网穿透工具正确配置

### Q: 前端可访问，但API请求失败？

**解决方案**:

**选项1**: 后端也做穿透（推荐）
```
前端穿透域名: http://app.example.com
后端穿透域名: http://api.example.com
```

**选项2**: 修改前端API地址
```env
VITE_API_BASE_URL=http://后端穿透地址/api
```

### Q: CORS错误？

**后端添加你的外网域名**:
```java
// SecurityConfig.java
.allowedOrigins(List.of(
  "http://localhost:5173",
  "http://your-domain.com",  // 添加这行
  "https://your-domain.com"
))
```

---

## 🎯 下一步

**1. 重启前端**:
```bash
Ctrl + C
npm run dev
```

**2. 查看启动信息**:
应该看到:
```
➜  Network: http://192.168.x.x:5173/
```

**3. 配置内网穿透**:
- 本地端口: 5173
- 协议: HTTP

**4. 测试访问**:
- 使用穿透域名访问
- 应该可以正常打开

---

**Vite配置已更新！重启前端即可支持内网穿透！** 🌐✅

