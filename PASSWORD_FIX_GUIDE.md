# 密码问题修复指南

**问题**: 测试账户登录失败 - 密码不匹配  
**原因**: V2__init_test_data.sql中的BCrypt hash与实际密码不对应

---

## 🔧 解决方案

### 方式1: 使用生成的新密码（推荐）

**步骤**:

1. **运行密码生成工具**:
   ```bash
   cd backend
   generate-passwords.bat
   ```

2. **复制生成的BCrypt hash**（会显示6个密码）

3. **更新V2__init_test_data.sql**:
   用生成的hash替换现有的hash

4. **重置数据库**:
   ```bash
   cd ..
   docker-compose down -v
   docker-compose up -d
   ```
   等待30秒

5. **重启后端**:
   ```bash
   cd backend
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

---

### 方式2: 使用注册功能创建新用户（最简单）

**不用修复旧密码，直接注册新用户！**

1. ✅ 后端已启动（端口8080）
2. ✅ 前端启动: `cd frontend && npm run dev`
3. ✅ 打开 http://localhost:5173
4. ✅ 点击"注册"
5. ✅ 创建新用户:
   - 用户名: `testuser`
   - 密码: `Test1234`
6. ✅ 注册后自动登录！

**这是最快的方法！** 🎉

---

### 方式3: 直接修复SQL文件

用在线BCrypt工具生成hash:
- 工具: https://bcrypt-generator.com/
- Rounds: 10
- 密码: `password123`

然后更新V2__init_test_data.sql，重置数据库。

---

## 🎯 推荐操作

**立即执行（最简单）**:

1. 打开前端: http://localhost:5173
2. 点击"注册"
3. 创建你自己的账户
4. 开始使用！

**不需要修复测试账户，直接注册就可以了！** ✅

---

## 📝 如果一定要修复测试账户

使用 `backend/generate-passwords.bat` 生成新的BCrypt hash，然后：

```sql
-- 更新V2__init_test_data.sql
INSERT INTO users (username, password, nickname, role_type) VALUES
('critic_a', '新生成的hash', '专业评论家A', 'FOOD_CRITIC_A'),
('critic_b', '新生成的hash', '西餐专家B', 'FOOD_CRITIC_B'),
... 以此类推
```

然后重置数据库和重启后端。

---

**建议**: 直接使用注册功能创建新用户是最快的！🚀

