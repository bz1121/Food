@echo off
chcp 65001 > nul
echo ========================================
echo BCrypt密码生成工具
echo ========================================
echo.

mvn exec:java -Dexec.mainClass="com.tastefinder.util.PasswordGenerator" -Dexec.classpathScope=compile

pause

