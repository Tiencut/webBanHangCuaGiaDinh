# 🚀 Hướng Dẫn Nhanh - Sử Dụng Phần Mềm Quản Lý Tồn Kho

## 📋 Checklist Khởi Chạy

### ✅ Bước 1: Khởi Chạy Backend
```
VS Code → Ctrl+Shift+P → "Tasks: Run Task" → "Run Spring Boot App"
```
✅ Kiểm tra: Mở `http://localhost:8080` - nếu thấy trang web = thành công

### ✅ Bước 2: Khởi Chạy Frontend  
```
VS Code → Ctrl+Shift+P → "Tasks: Run Task" → "Run Frontend Dev Server"
```
✅ Kiểm tra: Mở `http://localhost:3000` - nếu thấy trang web = thành công

### ✅ Bước 3: Sử Dụng Phần Mềm
1. Mở trình duyệt: `http://localhost:3000`
2. Click menu **"Products"**
3. Xem bảng sản phẩm với cột tồn kho

## 🎯 Cách Sử Dụng Chính

### 📊 Xem Thông Tin Tồn Kho
- **Tổng tồn kho**: Nhìn cột "Tồn kho" 
- **Có sẵn**: Nhìn cột "Có sẵn"
- **Đã đặt**: Nhìn cột "Đã đặt"
- **Số nhà cung cấp**: Nhìn cột "Nhà CC"

### 🔍 Xem Chi Tiết Tồn Kho
1. **Click vào số tồn kho** của sản phẩm bất kỳ
2. Modal sẽ hiện ra với:
   - 4 thẻ thống kê màu
   - Bảng chi tiết theo nhà cung cấp
   - Giá vốn và vị trí kho

### 🔎 Tìm Kiếm & Lọc
- **Tìm kiếm**: Gõ tên sản phẩm vào ô tìm kiếm
- **Lọc danh mục**: Chọn dropdown "Danh mục"
- **Lọc trạng thái**: Chọn dropdown "Trạng thái"

## 🎨 Ý Nghĩa Màu Sắc

| Màu | Ý Nghĩa | Hành Động |
|-----|---------|-----------|
| 🟢 Xanh lá | Tồn kho đủ | Bình thường |
| 🟡 Vàng | Tồn kho thấp | Cần chú ý |
| 🔴 Đỏ | Hết hàng | Cần nhập ngay |

## 🔧 Khi Có Lỗi

### Backend Không Chạy
```
❌ Lỗi: Cannot connect to localhost:8080
✅ Giải pháp: Restart "Run Spring Boot App" task
```

### Frontend Không Chạy  
```
❌ Lỗi: Cannot connect to localhost:3000
✅ Giải pháp: Restart "Run Frontend Dev Server" task
```

### Không Thấy Dữ Liệu
```
❌ Lỗi: Bảng trống hoặc không load
✅ Giải pháp: 
1. F12 → Console → xem lỗi
2. Kiểm tra backend có chạy không
3. Refresh trang (F5)
```

## 🔧 Cài Đặt Dependencies (Lần Đầu)

### Nếu Frontend Báo Lỗi "vite is not recognized"

**Triệu chứng:**
```
'vite' is not recognized as an internal or external command
```

**Giải pháp:**
1. Mở terminal trong VS Code: `Ctrl+`` (backtick)
2. Chuyển đến thư mục frontend:
   ```
   cd frontend-web
   ```
3. Cài đặt dependencies:
   ```
   npm install
   ```
4. Đợi quá trình cài đặt hoàn tất (khoảng 1-2 phút)
5. Chạy lại frontend:
   ```
   npm run dev
   ```

### Dependencies Cần Thiết
- **Node.js**: Version 16+ 
- **npm**: Được cài cùng Node.js
- **Vue 3**: Framework frontend
- **Vite**: Build tool và dev server
- **Tailwind CSS**: Framework CSS
- **Axios**: HTTP client cho API calls

### Kiểm Tra Cài Đặt
```bash
node --version    # Kiểm tra Node.js
npm --version     # Kiểm tra npm
npm list vite     # Kiểm tra Vite
```

## 📱 Giao Diện Mẫu

```
┌─────────────────────────────────────────────────────────────────┐
│                    QUẢN LÝ SẢN PHẨM                             │
├─────────────────────────────────────────────────────────────────┤
│ Tìm: [_______] Danh mục: [All▼] Trạng thái: [Active▼] [Export] │
├─────────────────────────────────────────────────────────────────┤
│ Sản phẩm       │ Tồn kho │ Có sẵn │ Đã đặt │ Nhà CC │ Thao tác │
├────────────────┼─────────┼────────┼────────┼────────┼──────────┤
│ 🔧 Lọc dầu     │  [50]   │   45   │   5    │   2    │ ✏️ 🗑️   │
│ SKU: OIL-001   │ 👆click │        │        │        │          │
├────────────────┼─────────┼────────┼────────┼────────┼──────────┤
│ 🚗 Má phanh    │  [5]    │   5    │   0    │   1    │ ✏️ 🗑️   │
│ SKU: BRAKE-002 │ 👆click │        │        │        │          │
└────────────────┴─────────┴────────┴────────┴────────┴──────────┘
```

## 🚀 Workflow Hàng Ngày

### 🌅 Sáng (8:00 AM)
1. ✅ Mở phần mềm: `http://localhost:3000`
2. ✅ Vào trang Products
3. ✅ Kiểm tra cột tồn kho - tìm số đỏ/vàng
4. ✅ Click vào sản phẩm sắp hết để xem chi tiết

### 🌞 Trưa (12:00 PM)
1. ✅ Cập nhật tồn kho mới nhập
2. ✅ Kiểm tra đơn hàng cần xuất
3. ✅ Cập nhật hàng đã bán

### 🌙 Chiều (6:00 PM)
1. ✅ Xuất báo cáo tồn kho
2. ✅ Lập danh sách cần nhập hàng
3. ✅ Kiểm tra hàng đặt trước

## 📞 Liên Hệ Hỗ Trợ

### Khi Cần Hỗ Trợ
- **Lỗi phần mềm**: Chụp màn hình + gửi mô tả
- **Không hiểu cách dùng**: Xem lại hướng dẫn này
- **Cần tính năng mới**: Gửi yêu cầu chi tiết

### Thông Tin Kỹ Thuật
- **Backend**: http://localhost:8080
- **Frontend**: http://localhost:3000
- **Test Page**: test-inventory-frontend.html

---

**🎉 Chúc bạn sử dụng phần mềm hiệu quả!**
