#!/bin/bash

echo "Testing Backend API..."

# Test 1: Health check
echo "1. Testing health endpoint..."
curl -f http://localhost:8080/actuator/health || echo "Health endpoint failed"

# Test 2: Download template
echo -e "\n2. Testing template download..."
curl -f -o template_test.csv http://localhost:8080/api/suppliers/template || echo "Template download failed"

if [ -f template_test.csv ]; then
    echo "Template downloaded successfully:"
    head -3 template_test.csv
else
    echo "Template download failed"
fi

# Test 3: Create a test CSV for import
echo -e "\n3. Creating test CSV file..."
cat > test_import.csv << 'EOF'
Mã nhà cung cấp,Tên nhà cung cấp,Số điện thoại,Email,Địa chỉ,Thành phố,Quận/Huyện,Phường/Xã,Vùng miền,Nhóm nhà cung cấp,Tổng giá trị mua,Nợ hiện tại,Ngày giao dịch cuối,Ghi chú
TEST001,Nhà cung cấp Test,0123456789,test@email.com,123 Test Street,Hà Nội,Ba Đình,Phúc Xá,Miền Bắc,Nhóm A,1000000,0,01/01/2024,Test import
TEST002,Công ty Test 2,0987654321,test2@email.com,456 Test Ave,TP.HCM,Quận 1,Phường 1,Miền Nam,Nhóm B,2000000,500000,15/06/2024,Test import 2
EOF

echo "Test CSV created with 2 records"

# Test 4: Import CSV
echo -e "\n4. Testing CSV import..."
curl -f -X POST \
  -F "file=@test_import.csv" \
  -F "updateExisting=false" \
  http://localhost:8080/api/suppliers/import || echo "Import failed"

echo -e "\n\nAPI Testing completed!"

# Cleanup
rm -f template_test.csv test_import.csv
