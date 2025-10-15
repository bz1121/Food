@echo off
chcp 65001 > nul
echo ================================================
echo TasteFinder 后端启动脚本
echo ================================================
echo.

echo 检查Maven...
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo 找到Maven，使用系统Maven启动...
    mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dfile.encoding=UTF-8
) else (
    echo Maven未安装，请先安装Maven或JDK
    echo.
    echo 下载地址:
    echo   - Maven: https://maven.apache.org/download.cgi
    echo   - 或使用IDEA直接运行TastefinderApplication.java
    echo.
    pause
    exit /b 1
)

