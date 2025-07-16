@echo off

echo ========================================
echo    KHOI DONG FRONTEND (VITE + VUE)
echo ========================================
echo.

cd /d %~dp0FE

echo Dang cai cac goi npm (neu can)...
npm install

echo Khoi dong Frontend (npm run dev)...
npm run dev

echo.
echo Da khoi dong Frontend. Nhan Ctrl+C de dung.
pause 