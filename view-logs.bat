@echo off
echo ========================================
echo    XEM LOG FILES - WEB BAN HANG GIA DINH
echo ========================================
echo.

cd web-ban-hang-gia-dinh

if not exist logs (
    echo Thuc muc logs chua duoc tao. Hay chay ung dung truoc.
    pause
    exit /b 1
)

echo Cac file log co san:
echo.
dir logs /b
echo.

:menu
echo Chon loai log de xem:
echo 1. Application log (tat ca log)
echo 2. Error log (chi loi)
echo 3. SQL log (cau lenh SQL)
echo 4. Xem log moi nhat (tail)
echo 5. Xoa tat ca log
echo 6. Thoat
echo.
set /p choice="Nhap lua chon (1-6): "

if "%choice%"=="1" goto app_log
if "%choice%"=="2" goto error_log
if "%choice%"=="3" goto sql_log
if "%choice%"=="4" goto tail_log
if "%choice%"=="5" goto clear_logs
if "%choice%"=="6" goto exit
goto menu

:app_log
echo.
echo ========================================
echo    APPLICATION LOG
echo ========================================
if exist logs\application.log (
    type logs\application.log
) else (
    echo File application.log chua co.
)
echo.
pause
goto menu

:error_log
echo.
echo ========================================
echo    ERROR LOG
echo ========================================
if exist logs\error.log (
    type logs\error.log
) else (
    echo File error.log chua co.
)
echo.
pause
goto menu

:sql_log
echo.
echo ========================================
echo    SQL LOG
echo ========================================
if exist logs\sql.log (
    type logs\sql.log
) else (
    echo File sql.log chua co.
)
echo.
pause
goto menu

:tail_log
echo.
echo ========================================
echo    LOG MOI NHAT (10 dong cuoi)
echo ========================================
if exist logs\application.log (
    powershell "Get-Content logs\application.log | Select-Object -Last 10"
) else (
    echo File application.log chua co.
)
echo.
pause
goto menu

:clear_logs
echo.
echo Ban co chac muon xoa tat ca log? (y/n)
set /p confirm="Nhap y de xac nhan: "
if /i "%confirm%"=="y" (
    del /q logs\*.log
    echo Da xoa tat ca log.
) else (
    echo Huy xoa log.
)
echo.
pause
goto menu

:exit
echo.
echo Tam biet!
pause 