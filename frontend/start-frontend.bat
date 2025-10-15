@echo off
chcp 65001 > nul
echo ================================================
echo TasteFinder 前端启动脚本
echo ================================================
echo.

echo 检查Node.js...
where node >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Node.js未安装！
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

echo Node.js版本: 
node --version
echo.

echo 检查依赖...
if not exist "node_modules\" (
    echo 首次运行，正在安装依赖...
    npm install
) else (
    echo 依赖已安装
)
echo.

echo 启动开发服务器...
npm run dev

