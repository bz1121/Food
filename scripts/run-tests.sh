#!/bin/bash

# TasteFinder 测试运行脚本

set -e

echo "======================================"
echo "TasteFinder 测试套件"
echo "======================================"
echo ""

# 后端测试
echo "[1/3] 运行后端测试..."
cd backend
./mvnw clean test
echo "✅ 后端测试通过"
echo ""

# 后端代码覆盖率
echo "[2/3] 生成代码覆盖率报告..."
./mvnw jacoco:report
echo "✅ 覆盖率报告: backend/target/site/jacoco/index.html"
echo ""

# 前端测试
echo "[3/3] 运行前端测试..."
cd ../frontend
npm run test
echo "✅ 前端测试通过"
echo ""

echo "======================================"
echo "✅ 所有测试通过！"
echo "======================================"

