# ✅ 正确的内网穿透配置

**发现问题**: 端口映射反了！

---

## ❌ 当前错误配置

```
HTTP  http://os8vc9406212.vicp.fun  → 127.0.0.1:8080  ❌ 错了！
HTTPS https://os8vc9406212.vicp.fun → 127.0.0.1:5173  ❌ 错了！
```

**问题**:
- 前端访问8080（后端端口）→ 打不开页面
- 后端映射到5173（前端端口）→ API无法访问

---

## ✅ 正确配置应该是

### 方案A: 前后端都用HTTP（推荐）

```
前端:
HTTP  http://os8vc9406212.vicp.fun       → 127.0.0.1:5173  ✅
协议: HTTP

后端:
HTTP  http://os8vc9406212.vicp.fun:8080  → 127.0.0.1:8080  ✅
协议: HTTP
```

**前端API配置**:
```env
VITE_API_BASE_URL=http://os8vc9406212.vicp.fun:8080/api
```

---

### 方案B: 使用不同域名（更好）

**如果你有2个域名或支持子域名**:

```
前端:
HTTP  http://app.your-domain.com    → 127.0.0.1:5173  ✅
或    http://os8vc9406212.vicp.fun  → 127.0.0.1:5173  ✅

后端:
HTTP  http://api.your-domain.com    → 127.0.0.1:8080  ✅
或    http://os8vc9406212-api.vicp.fun → 127.0.0.1:8080  ✅
```

**前端API配置**:
```env
VITE_API_BASE_URL=http://api.your-domain.com/api
```

---

## 🔧 立即修正

### 在你的穿透工具中修改

**隧道1 - 前端**:
```
名称: TasteFinder-Frontend
协议: HTTP
内网地址: 127.0.0.1:5173  ← 改这里！
外网域名: http://os8vc9406212.vicp.fun
```

**隧道2 - 后端**:
```
名称: TasteFinder-Backend
协议: HTTP
内网地址: 127.0.0.1:8080  ← 改这里！
外网域名: http://os8vc9406212.vicp.fun:8080
或申请新域名
```

---

## 📝 修改后的前端配置

**编辑** `frontend/.env.development`:

**如果后端使用端口**:
```env
VITE_API_BASE_URL=http://os8vc9406212.vicp.fun:8080/api
```

**如果后端使用子域名**:
```env
VITE_API_BASE_URL=http://你的后端域名/api
```

---

## 🔄 重启服务

**修改穿透配置后**:
1. 确保前端映射到5173 ✅
2. 确保后端映射到8080 ✅
3. 修改前端.env.development
4. 重启前端: `npm run dev`
5. 访问测试

---

## 🎯 验证配置

**测试前端**:
```
http://os8vc9406212.vicp.fun
应该看到：登录页面 ✅
```

**测试后端**:
```
http://os8vc9406212.vicp.fun:8080/actuator/health
应该返回：{"status":"UP"} ✅
```

---

**修正端口映射，前端→5173，后端→8080！** 🔧✅

