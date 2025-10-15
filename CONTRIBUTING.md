# 贡献指南

感谢你考虑为 TasteFinder 贡献代码！

---

## 📋 开始之前

请先阅读：

1. [项目宪章](.specify/memory/constitution.md) - 了解核心原则
2. [快速开始](specs/001-tastefinder-api/quickstart.md) - 搭建开发环境
3. [实施计划](specs/001-tastefinder-api/plan.md) - 理解技术架构

---

## 🔧 开发环境设置

### 1. Fork并克隆仓库

```bash
git clone https://github.com/your-username/tastefinder.git
cd tastefinder
```

### 2. 创建功能分支

```bash
git checkout -b feature/your-feature-name
# 或
git checkout -b fix/your-bug-fix
```

### 3. 搭建本地环境

```bash
# 启动Docker服务
docker-compose up -d

# 安装依赖
cd backend
./mvnw clean install

cd ../frontend
npm install
```

---

## 📝 代码规范

### Java代码规范

遵循Google Java Style Guide，使用Checkstyle检查：

```bash
cd backend
./mvnw checkstyle:check
```

**关键规则**:
- 圈复杂度 ≤ 10
- 所有public类和方法必须有Javadoc
- 使用Lombok减少样板代码
- 统一使用Spring的依赖注入

### Vue代码规范

遵循Vue 3官方风格指南，使用ESLint检查：

```bash
cd frontend
npm run lint
```

**关键规则**:
- 使用Composition API（不要用Options API）
- 组件名使用PascalCase
- Props使用camelCase
- 统一使用Element Plus组件

---

## 🧪 测试要求

### 后端测试

**最低要求**: 代码覆盖率 ≥ 80%

```bash
cd backend
./mvnw test
./mvnw jacoco:report
# 查看报告: target/site/jacoco/index.html
```

**测试类型**:
- 单元测试: 所有Service类
- 集成测试: 所有Controller
- 使用Mockito模拟依赖

### 前端测试

```bash
cd frontend
npm run test
npm run test:coverage
```

**测试类型**:
- 组件测试: 关键Vue组件
- Store测试: Pinia状态管理
- Composables测试: 可复用逻辑

---

## 📦 提交代码

### Commit消息规范

使用Conventional Commits规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**类型** (type):
- `feat`: 新功能
- `fix`: Bug修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关

**示例**:
```
feat(auth): 添加用户注册功能

- 实现RegisterRequest DTO
- 添加UserService.register()方法
- 添加单元测试

Closes #123
```

### Pull Request流程

1. **确保所有测试通过**:
   ```bash
   cd backend && ./mvnw test
   cd frontend && npm run test
   ```

2. **确保代码质量检查通过**:
   ```bash
   cd backend && ./mvnw checkstyle:check
   cd frontend && npm run lint
   ```

3. **推送到你的Fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

4. **创建Pull Request**:
   - 描述清楚改动内容
   - 关联相关Issue
   - 等待Code Review

---

## 🔍 Code Review标准

### 审查要点

**功能性**:
- [ ] 功能符合需求规范
- [ ] 边界情况已处理
- [ ] 错误处理完善

**代码质量**:
- [ ] 符合项目宪章原则
- [ ] 无重复代码
- [ ] 命名清晰
- [ ] 注释充分

**测试**:
- [ ] 单元测试覆盖率≥80%
- [ ] 集成测试通过
- [ ] 边界情况已测试

**安全性**:
- [ ] 无SQL注入风险
- [ ] 无XSS风险
- [ ] 敏感数据已加密
- [ ] 权限检查完善

**性能**:
- [ ] 无N+1查询
- [ ] 适当使用缓存
- [ ] 数据库查询已优化

---

## 🐛 报告Bug

### Issue模板

```markdown
## Bug描述
简要描述bug

## 复现步骤
1. 访问...
2. 点击...
3. 看到错误...

## 预期行为
应该...

## 实际行为
实际...

## 环境信息
- 操作系统: 
- 浏览器: 
- 应用版本: 

## 截图
（如果适用）

## 额外信息
其他相关信息
```

---

## ✨ 功能请求

### Feature Request模板

```markdown
## 功能描述
清晰描述新功能

## 使用场景
作为[用户角色]，我想[做什么]，以便[达到什么目的]

## 解决方案
描述你期望的实现方式

## 替代方案
是否考虑过其他方案

## 优先级
[ ] P0 - 关键
[ ] P1 - 重要
[ ] P2 - 普通
```

---

## 📚 开发资源

### 技术文档

- [Spring Boot文档](https://spring.io/projects/spring-boot)
- [Vue 3文档](https://cn.vuejs.org/)
- [Element Plus文档](https://element-plus.org/zh-CN/)
- [高德地图API](https://lbs.amap.com/api/jsapi-v2/summary)

### 项目文档

- [功能规范](specs/001-tastefinder-api/spec.md)
- [数据模型](specs/001-tastefinder-api/data-model.md)
- [API规范](specs/001-tastefinder-api/contracts/openapi.yaml)

---

## 🙏 感谢

感谢你的贡献！每一个PR都让TasteFinder变得更好。

---

## 📞 联系方式

- GitHub Issues: https://github.com/your-org/tastefinder/issues
- 邮件: dev@tastefinder.com

