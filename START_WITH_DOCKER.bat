@echo off
chcp 65001 > nul
echo ================================================
echo TasteFinder 完整启动脚本（含Docker检查）
echo ================================================
echo.

echo [检查] 检查Docker Desktop是否运行...
docker ps >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker Desktop未运行！
    echo.
    echo 请先启动Docker Desktop，然后重新运行此脚本。
    echo.
    pause
    exit /b 1
)
echo ✅ Docker Desktop运行中
echo.

echo [1/4] 启动Docker容器（MySQL + Redis）...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker容器启动失败
    pause
    exit /b 1
)
echo ✅ Docker容器已启动
echo.

echo [2/4] 等待MySQL初始化完成（5秒）...
timeout /t 5 /nobreak > nul
echo ✅ 数据库已就绪
echo.

echo [3/4] 在新窗口启动后端（Spring Boot）...
start "TasteFinder 后端" cmd /k "cd /d %~dp0backend && start-backend.bat"
echo ✅ 后端正在启动...
echo.

echo [4/4] 在新窗口启动前端（Vue 3）...
timeout /t 5 /nobreak > nul
start "TasteFinder 前端" cmd /k "cd /d %~dp0frontend && start-frontend.bat"
echo ✅ 前端正在启动...
echo.

echo ================================================
echo 所有服务正在启动中！
echo ================================================
echo.
echo 访问地址:
echo   - 本地访问: http://localhost:5173
echo   - 外网访问: http://os8vc9406212.vicp.fun
echo.
echo 测试账户:
echo   - admin / password123 (管理员)
echo   - critic_a / password123 (评论家)
echo   - user_d / password123 (普通用户)
echo.
echo API文档: http://localhost:8080/swagger-ui.html
echo.
echo ================================================
echo 请查看新打开的终端窗口以查看日志
echo ================================================
pause

