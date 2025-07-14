@echo off

echo ========================================
echo    KHOI DONG BE + FE WEB BAN HANG GIA DINH
echo ========================================
echo.

REM --- Start Backend (Spring Boot) ---
echo Khoi dong Backend (Spring Boot)...
start cmd /k "cd /d %~dp0web-ban-hang-gia-dinh && mvn clean spring-boot:run -Dspring-boot.run.profiles=dev"

REM --- Start Frontend (Vite + Vue) ---
echo Khoi dong Frontend (Vite + Vue)...
start cmd /k "cd /d %~dp0frontend-web && npm install && npm run dev"

echo.
echo Da khoi dong ca Backend va Frontend trong 2 cua so rieng biet.
echo.
pause 