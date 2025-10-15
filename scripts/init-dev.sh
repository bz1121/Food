#!/bin/bash

# TasteFinder 开发环境初始化脚本

set -e

echo "======================================"
echo "TasteFinder 开发环境初始化"
echo "======================================"
echo ""

# 检查Docker
echo "[1/6] 检查Docker..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker未安装，请先安装Docker"
    exit 1
fi
echo "✅ Docker已安装: $(docker --version)"
echo ""

# 检查Docker Compose
echo "[2/6] 检查Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose未安装"
    exit 1
fi
echo "✅ Docker Compose已安装: $(docker-compose --version)"
echo ""

# 启动数据库
echo "[3/6] 启动MySQL和Redis..."
docker-compose up -d mysql redis
echo "⏳ 等待MySQL初始化（30秒）..."
sleep 30
echo "✅ 数据库服务已启动"
echo ""

# 检查Java
echo "[4/6] 检查Java..."
if ! command -v java &> /dev/null; then
    echo "⚠️ Java未安装，将无法运行后端"
else
    echo "✅ Java已安装: $(java -version 2>&1 | head -n 1)"
fi
echo ""

# 检查Node.js
echo "[5/6] 检查Node.js..."
if ! command -v node &> /dev/null; then
    echo "⚠️ Node.js未安装，将无法运行前端"
else
    echo "✅ Node.js已安装: $(node --version)"
    
    # 安装前端依赖
    echo "📦 安装前端依赖..."
    cd frontend
    npm install
    cd ..
    echo "✅ 前端依赖安装完成"
fi
echo ""

# 配置提示
echo "[6/6] 配置提示..."
echo ""
echo "⚠️ 请务必配置以下内容："
echo ""
echo "1. 高德地图API密钥："
echo "   - 后端: backend/src/main/resources/application-dev.yml"
echo "   - 前端: frontend/.env.development"
echo ""
echo "2. 启动应用："
echo "   后端: cd backend && ./mvnw spring-boot:run"
echo "   前端: cd frontend && npm run dev"
echo ""
echo "3. 访问地址："
echo "   前端: http://localhost:5173"
echo "   API文档: http://localhost:8080/swagger-ui.html"
echo ""
echo "======================================"
echo "✅ 开发环境初始化完成！"
echo "======================================"

