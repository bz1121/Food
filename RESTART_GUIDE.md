# 🔄 重启指南

**问题**: Flyway checksum不匹配  
**原因**: V2文件被修改，但数据库已用旧版本执行过

---

## ✅ 解决方案

### 步骤1: 清理并重启

**在后端终端**:
```bash
cd backend
mvn clean
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 步骤2: 如果还有问题

**完全重置数据库**:
```bash
docker-compose down -v
docker-compose up -d
# 等待30秒
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 🎯 现在执行

**在后端终端运行**:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**成功标志**:
```
Started TasteFinder Application in X seconds
```

然后用 `critic_a` / `password123` 登录！✅

