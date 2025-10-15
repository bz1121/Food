# 📤 GitHub上传指南

**项目**: TasteFinder美食推荐平台  
**状态**: 本地Git仓库已初始化 ✅

---

## ✅ 已完成

- ✅ Git仓库已初始化
- ✅ 所有文件已添加
- ✅ 初始提交已创建
- ✅ README.md已添加

---

## 🚀 上传到GitHub

### 步骤1: 创建GitHub仓库

**访问**: https://github.com/new

**填写信息**:
- Repository name: `tastefinder`
- Description: `🍽️ TasteFinder美食推荐平台 - 基于Spring Boot 3 + Vue 3的智能美食推荐系统`
- Public/Private: 选择你需要的
- **不要**勾选"Add README"（我们已有README）
- **不要**勾选".gitignore"（我们已有）
- **不要**勾选"License"（我们已有）

**点击**: "Create repository"

---

### 步骤2: 关联远程仓库

**复制GitHub给你的仓库地址**，例如：
```
https://github.com/your-username/tastefinder.git
```

**在本地执行**:
```bash
git remote add origin https://github.com/your-username/tastefinder.git
git branch -M main
```

---

### 步骤3: 推送到GitHub

```bash
git push -u origin main
```

**如果需要登录**:
- 输入GitHub用户名
- 输入Personal Access Token（不是密码）

**获取Token**: https://github.com/settings/tokens

---

## 🔑 GitHub Personal Access Token

### 创建Token

1. 访问: https://github.com/settings/tokens
2. 点击"Generate new token (classic)"
3. Note: `TasteFinder Deploy`
4. 勾选权限: `repo`（所有）
5. 点击"Generate token"
6. **复制token**（只显示一次！）

### 使用Token

**推送时**:
```
Username: your-github-username
Password: 粘贴你的token  ← 不是GitHub密码！
```

---

## 📋 完整命令

**在项目根目录执行**:

```bash
# 1. 关联远程仓库（替换为你的仓库地址）
git remote add origin https://github.com/your-username/tastefinder.git

# 2. 设置主分支
git branch -M main

# 3. 推送到GitHub
git push -u origin main
```

**第一次推送需要输入GitHub凭证**

---

## 🎯 推送后

### 访问你的仓库

```
https://github.com/your-username/tastefinder
```

**应该看到**:
- ✅ 120+个文件
- ✅ 精美的README
- ✅ 完整的目录结构
- ✅ 所有代码和文档

---

## 📁 .gitignore检查

**已忽略的文件**（不会上传）:
- `node_modules/`
- `target/`
- `.env` （环境变量）
- `logs/`
- IDE配置文件

**会上传的文件**:
- 所有源代码
- `.env.example` （环境变量模板）
- 配置文件
- 文档
- Docker配置

---

## 🔒 安全提示

**⚠️ 不要上传敏感信息**:
- ❌ API密钥
- ❌ 数据库密码
- ❌ JWT secret

**已配置**:
- ✅ `.gitignore`已配置
- ✅ `.env`被忽略
- ✅ 敏感信息不会上传

**检查**:
```bash
# 查看将要提交的文件
git status

# 确保没有.env文件
git ls-files | grep .env
# 应该只看到.env.example
```

---

## 📝 提交规范

**使用Conventional Commits**:

```bash
git commit -m "feat: 添加新功能"
git commit -m "fix: 修复bug"
git commit -m "docs: 更新文档"
git commit -m "style: 代码格式"
git commit -m "refactor: 重构代码"
git commit -m "test: 添加测试"
```

---

## 🌟 GitHub仓库配置

### 推荐设置

**Repository Settings**:
- ✅ 添加Description
- ✅ 添加Topics: `spring-boot`, `vue3`, `food`, `maps`, `restaurant`
- ✅ 添加License: MIT
- ✅ 启用Issues
- ✅ 启用Wiki（可选）

**README badges**: 已添加 ✅

---

## 🎊 完成后

### 克隆到服务器

```bash
# SSH到Ubuntu服务器
ssh user@your-server

# 克隆项目
git clone https://github.com/your-username/tastefinder.git
cd tastefinder

# 开始部署
docker-compose -f docker-compose.prod.yml up -d
```

---

## 📞 需要帮助？

**当前状态**: 
- ✅ 本地Git仓库已初始化
- ✅ 所有文件已提交
- 📋 等待关联GitHub远程仓库

**下一步**: 
1. 在GitHub创建仓库
2. 复制仓库地址
3. 执行上面的git remote命令
4. 推送到GitHub

---

**准备好GitHub仓库地址后，执行命令即可上传！** 🚀

