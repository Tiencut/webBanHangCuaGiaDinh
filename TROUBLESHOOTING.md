# 🔧 Khắc Phục Sự Cố - Troubleshooting

## ❌ Lỗi Thường Gặp & Cách Sửa

### 1. Frontend Không Chạy - "vite is not recognized"

**Lỗi:**
```
'vite' is not recognized as an internal or external command
```

**Cách sửa:**
1. Mở Terminal: `Ctrl + backtick` 
2. Chuyển đến frontend: `cd frontend-web`
3. Cài đặt: `npm install`
4. Chạy lại: `npm run dev`

### 2. Backend Không Khởi Động

**Lỗi:**
```
Port 8080 already in use
```

**Cách sửa:**
1. Tắt ứng dụng đang dùng port 8080
2. Hoặc restart VS Code
3. Chạy lại task "Run Spring Boot App"

### 3. Không Kết Nối Database

**Lỗi:**
```
Could not connect to database
```

**Cách sửa:**
1. Kiểm tra file `application.properties`
2. Đảm bảo database đang chạy
3. Kiểm tra username/password

### 4. Trang Web Trống

**Lỗi:**
- Trang `http://localhost:3000` không hiển thị gì
- Hoặc lỗi "Cannot GET /"

**Cách sửa:**
1. Kiểm tra console: `F12 → Console`
2. Đảm bảo backend chạy trên port 8080
3. Restart frontend task

### 5. API Không Hoạt Động

**Lỗi:**
- Dữ liệu không load
- Network error trong console

**Cách sửa:**
1. Kiểm tra `http://localhost:8080/api/products`
2. Nếu không mở được → restart backend
3. Kiểm tra CORS settings

## ✅ Checklist Khắc Phục

### Bước 1: Kiểm Tra Cơ Bản
- [ ] VS Code đã mở workspace
- [ ] Node.js đã cài đặt: `node --version`
- [ ] npm hoạt động: `npm --version`

### Bước 2: Cài Đặt Dependencies
- [ ] Chạy `npm install` trong thư mục frontend-web
- [ ] Không có lỗi trong quá trình cài đặt
- [ ] File `node_modules` đã được tạo

### Bước 3: Khởi Chạy Services
- [ ] Backend chạy thành công (port 8080)
- [ ] Frontend chạy thành công (port 3000)
- [ ] Không có task nào báo lỗi

### Bước 4: Test Kết Nối
- [ ] `http://localhost:8080` mở được
- [ ] `http://localhost:3000` mở được
- [ ] API `http://localhost:8080/api/products` trả về dữ liệu

## 🚀 Lệnh Nhanh

### Reset Hoàn Toàn
```bash
# Tắt tất cả tasks
# Xóa node_modules (nếu cần)
cd frontend-web
rm -rf node_modules
npm install
```

### Kiểm Tra Port
```bash
# Windows
netstat -an | findstr :8080
netstat -an | findstr :3000

# Tắt process nếu cần
taskkill /f /pid <PID>
```

### Log Debug
```bash
# Xem log backend trong VS Code Terminal
# Xem log frontend trong Browser Console (F12)
```

## 📞 Khi Cần Hỗ Trợ

### Thông Tin Cần Cung Cấp
1. **Lỗi cụ thể**: Copy/paste error message
2. **Hệ điều hành**: Windows/Mac/Linux
3. **Node.js version**: `node --version`
4. **Bước đã làm**: Mô tả chi tiết

### Screenshots Hữu Ích
- VS Code Terminal với error
- Browser Console (F12)
- Task output trong VS Code

---

**💡 Tip: Hầu hết lỗi đều giải quyết được bằng việc restart tasks và cài lại dependencies!**
