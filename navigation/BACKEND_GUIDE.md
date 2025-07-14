# Hướng dẫn chạy và test Backend

## Cách chạy Backend

### 1. Build và compile project
Chạy file: `build-backend.bat`
```batch
cd web-ban-hang-gia-dinh
mvn clean compile
```

### 2. Chạy Spring Boot application
Chạy file: `start-backend.bat`
```batch
cd web-ban-hang-gia-dinh  
mvn spring-boot:run
```

### 3. Test API endpoints
Chạy file: `test-api.bat` (sau khi backend đã start)
```batch
curl -X GET http://localhost:8080/api/health
curl -X GET http://localhost:8080/api/test
curl -X GET http://localhost:8080/api/suppliers/template
```

## Endpoints có sẵn

### Health Check
- **GET** `/api/health` - Kiểm tra backend có chạy không
- **GET** `/api/test` - Test endpoint đơn giản

### Supplier Import
- **POST** `/api/suppliers/import` - Import nhà cung cấp từ CSV
  - Parameters: 
    - `file`: MultipartFile (CSV file)
    - `updateExisting`: boolean (default: false)
- **GET** `/api/suppliers/template` - Download file template CSV

### Database
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (để trống)

## Xử lý lỗi thường gặp

### 1. Lỗi compilation
- Kiểm tra Java version (cần Java 17+)
- Chạy `mvn clean compile` để xem lỗi chi tiết
- Đảm bảo dependencies trong pom.xml đúng

### 2. Lỗi khi start application
- Kiểm tra port 8080 có bị chiếm không
- Xem log trong terminal khi chạy `mvn spring-boot:run`
- Kiểm tra H2 database configuration

### 3. Lỗi import CSV
- Đảm bảo file CSV đúng format UTF-8
- Kiểm tra file có header đúng format không
- File size không quá 10MB

## Test với Frontend

1. Start backend: `start-backend.bat`
2. Start frontend: `npm run dev` (trong thư mục frontend-web)
3. Truy cập: http://localhost:3000
4. Test import CSV tại màn Suppliers

## Logs và Debug

### Xem logs
- Backend logs hiển thị trong terminal khi chạy
- Check file: `web-ban-hang-gia-dinh/app.log`

### Debug database
- Truy cập H2 Console: http://localhost:8080/h2-console
- Chạy SQL: `SELECT * FROM suppliers;`

### Test import manual
```bash
curl -X POST http://localhost:8080/api/suppliers/import \
  -F "file=@template_nha_cung_cap.csv" \
  -F "updateExisting=false"
```

## Cấu hình

### Database (application.properties)
```properties
# H2 in-memory database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# JPA auto-create tables
spring.jpa.hibernate.ddl-auto=create-drop
```

### File Upload
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Troubleshooting

1. **Port 8080 đã được sử dụng**: Thay đổi port trong application.properties
2. **Java version không đúng**: Cài Java 17+ hoặc thay đổi java.version trong pom.xml
3. **Maven không hoạt động**: Sử dụng `.\mvnw.cmd` thay vì `mvn`
4. **Import không hoạt động**: Kiểm tra CORS settings và file format
