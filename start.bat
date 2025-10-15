@echo off
chcp 65001 > nul
echo ================================================
echo TasteFinder 美食推荐平台 - 快速启动脚本
echo ================================================
echo.

echo [1/4] 启动Docker容器（MySQL + Redis）...
docker-compose up -d
echo 完成 - Docker容器已启动
echo.

echo [2/4] 等待MySQL初始化完成...
timeout /t 15 /nobreak > nul
echo 完成 - 数据库已就绪
echo.

echo [3/4] 在新窗口启动后端（Spring Boot）...
start "TasteFinder 后端" cmd /k "cd /d %~dp0backend && start-backend.bat"
echo 完成 - 后端正在启动...
echo.

echo [4/4] 在新窗口启动前端（Vue 3）...
timeout /t 3 /nobreak > nul
start "TasteFinder 前端" cmd /k "cd /d %~dp0frontend && start-frontend.bat"
echo 完成 - 前端正在启动...
echo.

echo ================================================
echo 所有服务正在启动中！
echo ================================================
echo.
echo 重要提示 - 请先配置高德API密钥:
echo   - 后端: backend\src\main\resources\application-dev.yml
echo   - 前端: frontend\.env.development
echo.
echo 测试账户:
echo   - critic_a / password123 (美食评论家)
echo   - user_d / password123 (普通用户)
echo.
echo 访问地址:
echo   - 前端应用: http://localhost:5173
echo   - API文档: http://localhost:8080/swagger-ui.html
echo.
echo ================================================
echo 请查看新打开的终端窗口以查看日志
echo ================================================
pause

