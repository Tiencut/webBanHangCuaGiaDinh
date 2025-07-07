#!/bin/bash
# =========================================
# Script setup database cho Windows/Linux
# =========================================

echo "=== BẮT ĐẦU SETUP DATABASE ==="

# Kiểm tra MySQL có chạy không
echo "Kiểm tra MySQL service..."
if command -v mysql &> /dev/null; then
    echo "✓ MySQL đã được cài đặt"
else
    echo "✗ MySQL chưa được cài đặt. Vui lòng cài đặt MySQL trước."
    exit 1
fi

# Thông tin kết nối database
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="web_ban_hang_gia_dinh"
DB_USER="root"

# Nhập password
echo "Nhập password MySQL cho user root:"
read -s DB_PASSWORD

# Kiểm tra kết nối
echo "Kiểm tra kết nối database..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✓ Kết nối database thành công"
else
    echo "✗ Không thể kết nối database. Kiểm tra lại thông tin."
    exit 1
fi

# Tạo database và bảng
echo "Tạo database và bảng..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" < database/script.sql
if [ $? -eq 0 ]; then
    echo "✓ Tạo database và bảng thành công"
else
    echo "✗ Lỗi tạo database và bảng"
    exit 1
fi

# Import dữ liệu mẫu
echo "Import dữ liệu mẫu..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" < database/sample_data.sql
if [ $? -eq 0 ]; then
    echo "✓ Import dữ liệu mẫu thành công"
else
    echo "✗ Lỗi import dữ liệu mẫu"
    exit 1
fi

# Kiểm tra dữ liệu
echo "Kiểm tra dữ liệu..."
SUPPLIER_COUNT=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -D"$DB_NAME" -e "SELECT COUNT(*) FROM suppliers;" -s -N 2>/dev/null)
PRODUCT_COUNT=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -D"$DB_NAME" -e "SELECT COUNT(*) FROM products;" -s -N 2>/dev/null)

echo "📊 Thống kê dữ liệu:"
echo "   - Suppliers: $SUPPLIER_COUNT"
echo "   - Products: $PRODUCT_COUNT"

# Cấu hình application.properties
echo "Cấu hình application.properties..."
APP_PROPS_FILE="web-ban-hang-gia-dinh/src/main/resources/application.properties"

if [ -f "$APP_PROPS_FILE" ]; then
    # Backup file cũ
    cp "$APP_PROPS_FILE" "$APP_PROPS_FILE.backup"
    
    # Cập nhật database config
    sed -i "s/spring.datasource.url=.*/spring.datasource.url=jdbc:mysql:\/\/$DB_HOST:$DB_PORT\/$DB_NAME?useSSL=false\&serverTimezone=UTC\&characterEncoding=utf8/" "$APP_PROPS_FILE"
    sed -i "s/spring.datasource.username=.*/spring.datasource.username=$DB_USER/" "$APP_PROPS_FILE"
    sed -i "s/spring.datasource.password=.*/spring.datasource.password=$DB_PASSWORD/" "$APP_PROPS_FILE"
    
    echo "✓ Cấu hình application.properties thành công"
else
    echo "⚠ Không tìm thấy file application.properties"
fi

echo ""
echo "🎉 SETUP DATABASE HOÀN TẤT!"
echo ""
echo "📋 Thông tin database:"
echo "   - Host: $DB_HOST"
echo "   - Port: $DB_PORT"
echo "   - Database: $DB_NAME"
echo "   - User: $DB_USER"
echo ""
echo "🚀 Bước tiếp theo:"
echo "   1. Chạy backend: cd web-ban-hang-gia-dinh ; mvn spring-boot:run"
echo "   2. Chạy frontend: cd frontend-web ; npm run dev"
echo "   3. Test import API: POST /api/suppliers/import/csv"
echo ""
echo "📁 File log:"
echo "   - Database script: database/script.sql"
echo "   - Sample data: database/sample_data.sql"
echo "   - Backup props: $APP_PROPS_FILE.backup"
echo ""
