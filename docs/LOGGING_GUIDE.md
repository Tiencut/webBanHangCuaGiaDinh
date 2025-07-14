# Hướng Dẫn Logging - Web Bán Hàng Gia Đình

## Vị Trí File Log

Tất cả file log được lưu trong thư mục: `web-ban-hang-gia-dinh/logs/`

## Các Loại File Log

### 1. **application.log**
- **Mô tả**: Log chính của ứng dụng
- **Nội dung**: Tất cả log từ ứng dụng, Spring Boot, Hibernate
- **Vị trí**: `logs/application.log`

### 2. **error.log**
- **Mô tả**: Chỉ chứa các log lỗi (ERROR level)
- **Nội dung**: Các exception, lỗi hệ thống
- **Vị trí**: `logs/error.log`

### 3. **sql.log**
- **Mô tả**: Log các câu lệnh SQL
- **Nội dung**: Các câu lệnh SQL được thực thi bởi Hibernate
- **Vị trí**: `logs/sql.log`

## Cách Xem Log

### Sử dụng Script (Khuyến nghị)
```bash
# Chạy script xem log
view-logs.bat
```

### Xem trực tiếp
```bash
# Xem log ứng dụng
type web-ban-hang-gia-dinh\logs\application.log

# Xem log lỗi
type web-ban-hang-gia-dinh\logs\error.log

# Xem log SQL
type web-ban-hang-gia-dinh\logs\sql.log
```

## Cấu Hình Log Level

### Development (dev profile)
- **Console**: INFO level, format ngắn gọn
- **File**: DEBUG level cho ứng dụng, INFO cho Spring
- **SQL**: WARN level (ít chi tiết)

### Production (prod profile)
- **Console**: ERROR level
- **File**: WARN level cho ứng dụng, ERROR cho Spring
- **SQL**: ERROR level (tắt hoàn toàn)

## Log Rotation

- **Kích thước tối đa**: 10MB per file
- **Số file backup**: 30 files
- **Pattern**: `application-YYYY-MM-DD.i.log`

## Ví Dụ Log

### Application Log
```
2024-01-15 14:30:25 [main] INFO  c.g.b.w.WebBanHangGiaDinhApplication - Started WebBanHangGiaDinhApplication in 3.456 seconds
2024-01-15 14:30:26 [http-nio-8080-exec-1] INFO  c.g.b.w.c.ProductController - GET /api/products - 200 OK
```

### Error Log
```
2024-01-15 14:35:12 [http-nio-8080-exec-3] ERROR c.g.b.w.c.ProductController - Product not found with id: 999
java.lang.RuntimeException: Product not found
    at com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductService.findById(ProductService.java:45)
```

### SQL Log
```
2024-01-15 14:30:26 [http-nio-8080-exec-1] WARN  o.h.SQL - SELECT p FROM Product p WHERE p.category.id = ?
2024-01-15 14:30:26 [http-nio-8080-exec-1] WARN  o.h.t.d.s.BasicBinder - binding parameter [1] as [BIGINT] - [1]
```

## Troubleshooting

### Nếu không thấy file log
1. Kiểm tra thư mục `logs` đã được tạo chưa
2. Chạy ứng dụng ít nhất một lần
3. Kiểm tra quyền ghi file

### Nếu log quá nhiều
1. Chuyển sang profile `prod`: `-Dspring-boot.run.profiles=prod`
2. Hoặc chỉnh sửa `application.properties` giảm log level

### Nếu log file quá lớn
1. Log sẽ tự động rotate khi đạt 10MB
2. Có thể xóa file cũ bằng script `view-logs.bat`

## Lệnh Hữu Ích

```bash
# Xem log real-time (Windows)
powershell "Get-Content logs\application.log -Wait"

# Xem 10 dòng cuối
powershell "Get-Content logs\application.log | Select-Object -Last 10"

# Tìm lỗi trong log
findstr "ERROR" logs\application.log

# Xem log theo thời gian
findstr "2024-01-15" logs\application.log
``` 