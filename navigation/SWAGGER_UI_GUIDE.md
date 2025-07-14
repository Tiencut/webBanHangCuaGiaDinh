# Hướng Dẫn Sử Dụng Swagger UI

## Tổng Quan
Swagger UI đã được tích hợp vào dự án để test API một cách trực quan và dễ dàng mà không cần test thủ công.

## Cách Truy Cập Swagger UI

### 1. Khởi động ứng dụng
```bash
cd webBanHangCuaGiaDinh/web-ban-hang-gia-dinh
mvn spring-boot:run
```

### 2. Truy cập Swagger UI
Sau khi ứng dụng khởi động thành công, truy cập:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/api-docs

## Các API Endpoints Có Sẵn

### 1. Product Management
- **GET** `/api/products` - Lấy tất cả sản phẩm
- **GET** `/api/products/{id}` - Lấy sản phẩm theo ID
- **GET** `/api/products/code/{code}` - Lấy sản phẩm theo mã
- **GET** `/api/products/search` - Tìm kiếm sản phẩm
- **POST** `/api/products` - Tạo sản phẩm mới
- **PUT** `/api/products/{id}` - Cập nhật sản phẩm
- **DELETE** `/api/products/{id}` - Xóa sản phẩm

### 2. Order Management
- **GET** `/api/orders` - Lấy tất cả đơn hàng
- **GET** `/api/orders/{id}` - Lấy đơn hàng theo ID
- **GET** `/api/orders/customer/{customerId}` - Lấy đơn hàng theo khách hàng
- **POST** `/api/orders` - Tạo đơn hàng mới
- **PUT** `/api/orders/{id}` - Cập nhật đơn hàng
- **DELETE** `/api/orders/{id}` - Xóa đơn hàng

### 3. Customer Management
- **GET** `/api/customers` - Lấy tất cả khách hàng
- **GET** `/api/customers/{id}` - Lấy khách hàng theo ID
- **POST** `/api/customers` - Tạo khách hàng mới
- **PUT** `/api/customers/{id}` - Cập nhật khách hàng
- **DELETE** `/api/customers/{id}` - Xóa khách hàng

### 4. Category Management
- **GET** `/api/categories` - Lấy tất cả danh mục
- **GET** `/api/categories/{id}` - Lấy danh mục theo ID
- **POST** `/api/categories` - Tạo danh mục mới
- **PUT** `/api/categories/{id}` - Cập nhật danh mục
- **DELETE** `/api/categories/{id}` - Xóa danh mục

### 5. Supplier Management
- **GET** `/api/suppliers` - Lấy tất cả nhà cung cấp
- **GET** `/api/suppliers/{id}` - Lấy nhà cung cấp theo ID
- **POST** `/api/suppliers` - Tạo nhà cung cấp mới
- **PUT** `/api/suppliers/{id}` - Cập nhật nhà cung cấp
- **DELETE** `/api/suppliers/{id}` - Xóa nhà cung cấp

### 6. Product Supplier Management
- **GET** `/api/product-suppliers` - Lấy tất cả quan hệ sản phẩm-nhà cung cấp
- **GET** `/api/product-suppliers/product/{productId}` - Lấy theo sản phẩm
- **GET** `/api/product-suppliers/supplier/{supplierId}` - Lấy theo nhà cung cấp
- **POST** `/api/product-suppliers` - Tạo quan hệ mới
- **PUT** `/api/product-suppliers/{id}` - Cập nhật quan hệ
- **DELETE** `/api/product-suppliers/{id}` - Xóa quan hệ

### 7. Customer Vehicle Management
- **GET** `/api/customer-vehicles` - Lấy tất cả xe khách hàng
- **GET** `/api/customer-vehicles/customer/{customerId}` - Lấy theo khách hàng
- **POST** `/api/customer-vehicles` - Tạo xe mới
- **PUT** `/api/customer-vehicles/{id}` - Cập nhật xe
- **DELETE** `/api/customer-vehicles/{id}` - Xóa xe

## Cách Test API Trong Swagger UI

### 1. Mở Swagger UI
- Truy cập http://localhost:8080/swagger-ui.html
- Bạn sẽ thấy danh sách tất cả API endpoints được nhóm theo chức năng

### 2. Test GET Request
1. Tìm endpoint bạn muốn test (ví dụ: GET /api/products)
2. Click vào endpoint để mở rộng
3. Click "Try it out"
4. Nhập các tham số nếu cần
5. Click "Execute"
6. Xem kết quả trong phần "Responses"

### 3. Test POST Request
1. Tìm endpoint POST (ví dụ: POST /api/products)
2. Click "Try it out"
3. Trong phần "Request body", nhập JSON data:
```json
{
  "name": "Lốp xe máy",
  "code": "LXM001",
  "partNumber": "LXM-001-2024",
  "brand": "Michelin",
  "price": 150000,
  "description": "Lốp xe máy chất lượng cao",
  "category": {
    "id": 1
  }
}
```
4. Click "Execute"
5. Xem kết quả

### 4. Test PUT Request
1. Tìm endpoint PUT (ví dụ: PUT /api/products/{id})
2. Click "Try it out"
3. Nhập ID trong path parameter
4. Nhập dữ liệu cập nhật trong Request body
5. Click "Execute"

### 5. Test DELETE Request
1. Tìm endpoint DELETE (ví dụ: DELETE /api/products/{id})
2. Click "Try it out"
3. Nhập ID cần xóa
4. Click "Execute"

## Lưu Ý Quan Trọng

### 1. Database
- Ứng dụng sử dụng H2 in-memory database
- Dữ liệu sẽ bị mất khi restart ứng dụng
- Để xem database: http://localhost:8080/h2-console

### 2. Authentication
- Hiện tại authentication đã được tắt để dễ test
- Trong production cần bật lại security

### 3. CORS
- CORS đã được cấu hình cho tất cả origins
- Phù hợp cho development

### 4. Error Handling
- Tất cả API đều có error handling
- Xem response code và message để debug

## Ví Dụ Test Thực Tế

### 1. Tạo Danh Mục
```json
POST /api/categories
{
  "name": "Lốp xe",
  "description": "Các loại lốp xe máy, ô tô"
}
```

### 2. Tạo Sản Phẩm
```json
POST /api/products
{
  "name": "Lốp xe máy Michelin",
  "code": "LXM001",
  "partNumber": "LXM-001-2024",
  "brand": "Michelin",
  "price": 150000,
  "description": "Lốp xe máy chất lượng cao",
  "category": {
    "id": 1
  }
}
```

### 3. Tạo Khách Hàng
```json
POST /api/customers
{
  "name": "Nguyễn Văn A",
  "phone": "0123456789",
  "email": "nguyenvana@email.com",
  "address": "123 Đường ABC, Quận 1, TP.HCM"
}
```

### 4. Tạo Đơn Hàng
```json
POST /api/orders
{
  "customer": {
    "id": 1
  },
  "orderDetails": [
    {
      "product": {
        "id": 1
      },
      "quantity": 2,
      "unitPrice": 150000
    }
  ]
}
```

## Troubleshooting

### 1. Ứng dụng không khởi động
- Kiểm tra port 8080 có đang được sử dụng không
- Kiểm tra Java version (cần Java 21)
- Kiểm tra Maven đã cài đặt chưa

### 2. Swagger UI không hiển thị
- Kiểm tra dependency springdoc-openapi trong pom.xml
- Kiểm tra cấu hình trong application.properties
- Restart ứng dụng

### 3. API trả về lỗi
- Kiểm tra request body format
- Kiểm tra required fields
- Xem log trong console để debug

## Kết Luận
Swagger UI cung cấp giao diện trực quan để test tất cả API endpoints. Đây là công cụ rất hữu ích cho việc development và testing mà không cần sử dụng Postman hay curl commands. 