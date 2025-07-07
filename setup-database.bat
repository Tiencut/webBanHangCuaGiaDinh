@echo off
chcp 65001 >nul
echo =========================================
echo Script setup database cho Windows
echo =========================================
echo.

echo Kiểm tra MySQL service...
sc query MySQL >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MySQL service đã được cài đặt
) else (
    echo ✗ MySQL service chưa chạy. Vui lòng khởi động MySQL.
    pause
    exit /b 1
)

REM Thông tin kết nối database
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=web_ban_hang_gia_dinh
set DB_USER=root

REM Nhập password
set /p DB_PASSWORD="Nhập password MySQL cho user root: "

echo.
echo Kiểm tra kết nối database...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Kết nối database thành công
) else (
    echo ✗ Không thể kết nối database. Kiểm tra lại thông tin.
    pause
    exit /b 1
)

echo.
echo Tạo database và bảng...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% < database\script.sql
if %errorlevel% equ 0 (
    echo ✓ Tạo database và bảng thành công
) else (
    echo ✗ Lỗi tạo database và bảng
    pause
    exit /b 1
)

echo.
echo Import dữ liệu mẫu...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% < database\sample_data.sql
if %errorlevel% equ 0 (
    echo ✓ Import dữ liệu mẫu thành công
) else (
    echo ✗ Lỗi import dữ liệu mẫu
    pause
    exit /b 1
)

echo.
echo Kiểm tra dữ liệu...
for /f %%i in ('mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -D%DB_NAME% -e "SELECT COUNT(*) FROM suppliers;" -s -N 2^>nul') do set SUPPLIER_COUNT=%%i
for /f %%i in ('mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -D%DB_NAME% -e "SELECT COUNT(*) FROM products;" -s -N 2^>nul') do set PRODUCT_COUNT=%%i

echo 📊 Thống kê dữ liệu:
echo    - Suppliers: %SUPPLIER_COUNT%
echo    - Products: %PRODUCT_COUNT%

echo.
echo Cấu hình application.properties...
set APP_PROPS_FILE=web-ban-hang-gia-dinh\src\main\resources\application.properties

if exist "%APP_PROPS_FILE%" (
    copy "%APP_PROPS_FILE%" "%APP_PROPS_FILE%.backup" >nul
    
    REM Tạo file config mới
    (
        echo # Database Configuration
        echo spring.datasource.url=jdbc:mysql://%DB_HOST%:%DB_PORT%/%DB_NAME%?useSSL=false^&serverTimezone=UTC^&characterEncoding=utf8
        echo spring.datasource.username=%DB_USER%
        echo spring.datasource.password=%DB_PASSWORD%
        echo spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        echo.
        echo # JPA Configuration
        echo spring.jpa.hibernate.ddl-auto=validate
        echo spring.jpa.show-sql=false
        echo spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
        echo spring.jpa.properties.hibernate.format_sql=true
        echo.
        echo # Server Configuration
        echo server.port=8080
        echo.
        echo # File Upload Configuration
        echo spring.servlet.multipart.max-file-size=10MB
        echo spring.servlet.multipart.max-request-size=10MB
        echo.
        echo # Logging Configuration
        echo logging.level.com.giadinh.banphutung=INFO
        echo logging.pattern.console=%%d{yyyy-MM-dd HH:mm:ss} - %%msg%%n
    ) > "%APP_PROPS_FILE%"
    
    echo ✓ Cấu hình application.properties thành công
) else (
    echo ⚠ Không tìm thấy file application.properties
)

echo.
echo 🎉 SETUP DATABASE HOÀN TẤT!
echo.
echo 📋 Thông tin database:
echo    - Host: %DB_HOST%
echo    - Port: %DB_PORT%
echo    - Database: %DB_NAME%
echo    - User: %DB_USER%
echo.
echo 🚀 Bước tiếp theo:
echo    1. Chạy backend: cd web-ban-hang-gia-dinh ^&^& mvn spring-boot:run
echo    2. Chạy frontend: cd frontend-web ^&^& npm run dev
echo    3. Test import API: POST /api/suppliers/import/csv
echo.
echo 📁 File log:
echo    - Database script: database\script.sql
echo    - Sample data: database\sample_data.sql
echo    - Backup props: %APP_PROPS_FILE%.backup
echo.
echo 🔗 API Endpoints:
echo    - Import CSV: http://localhost:8080/api/suppliers/import/csv
echo    - Template: http://localhost:8080/api/suppliers/import/template
echo    - Guide: http://localhost:8080/api/suppliers/import/guide
echo.
pause
