# 📦 Hệ thống Quản lý Tồn Kho Đa Nguồn Cung Ứng

## 🎯 Tổng Quan

Hệ thống quản lý tồn kho đa nguồn cung ứng cho phép theo dõi và quản lý tồn kho sản phẩm từ nhiều nhà cung cấp khác nhau. Hệ thống bao gồm:

- **Backend**: Spring Boot với JPA/Hibernate
- **Frontend**: Vue.js 3 với Composition API
- **Database**: PostgreSQL/MySQL với Flyway migration
- **API**: RESTful APIs cho tất cả các thao tác inventory

## 🚀 Tính Năng Đã Hoàn Thành

### Backend (Spring Boot)

#### 1. Entity & Database
- ✅ **Inventory Entity**: Quản lý tồn kho theo từng nhà cung cấp
- ✅ **Database Migration**: Tạo bảng inventory với sample data
- ✅ **Relationships**: Liên kết Product, Supplier, và Inventory

#### 2. Repository Layer
- ✅ **InventoryRepository**: Các query tính toán tồn kho
  - `findTotalStockByProduct()`: Tổng tồn kho theo sản phẩm
  - `findAvailableStockByProduct()`: Tồn kho có sẵn
  - `findReservedStockByProduct()`: Tồn kho đã đặt trước
  - `countSuppliersByProduct()`: Đếm số nhà cung cấp

#### 3. Service Layer
- ✅ **InventoryService**: Business logic cho inventory
  - Nhập hàng (Stock In)
  - Xuất hàng (Stock Out)
  - Đặt trước hàng (Reserve Stock)
  - Tính toán thống kê

#### 4. Controller Layer
- ✅ **InventoryController**: API endpoints
  - `POST /api/inventory/stock-in`: Nhập hàng
  - `POST /api/inventory/stock-out`: Xuất hàng
  - `POST /api/inventory/reserve`: Đặt trước hàng
  - `GET /api/inventory/product/{id}/details`: Chi tiết tồn kho
  - `GET /api/inventory/statistics`: Thống kê tồn kho

- ✅ **ProductController**: API tích hợp inventory
  - `GET /api/products/with-inventory`: Sản phẩm kèm thông tin tồn kho
  - `GET /api/products/{id}/inventory`: Thông tin tồn kho sản phẩm
  - `GET /api/products/{id}/total-stock`: Tổng tồn kho

### Frontend (Vue.js 3)

#### 1. API Services
- ✅ **inventoryAPI**: Service cho các API inventory
- ✅ **productsAPI**: Bổ sung API inventory cho products

#### 2. Components
- ✅ **Products.vue**: Hiển thị sản phẩm với thông tin tồn kho
  - Cột tồn kho tương tác
  - Modal chi tiết tồn kho
  - Thống kê theo nhà cung cấp

#### 3. UI Features
- ✅ **Inventory Columns**: Hiển thị tổng tồn kho, có sẵn, đã đặt, số nhà cung cấp
- ✅ **Stock Details Modal**: Chi tiết tồn kho theo nhà cung cấp
- ✅ **Interactive Stock Display**: Click vào tồn kho để xem chi tiết

## 🔧 Cấu Trúc Dự Án

```
web-ban-hang-gia-dinh/
├── src/main/java/com/giadinh/banphutung/web_ban_hang_gia_dinh/
│   ├── entity/
│   │   └── Inventory.java                 # Entity quản lý tồn kho
│   ├── repository/
│   │   └── InventoryRepository.java       # Repository với custom queries
│   ├── service/
│   │   └── InventoryService.java          # Business logic
│   ├── controller/
│   │   ├── InventoryController.java       # API endpoints
│   │   └── ProductController.java         # API tích hợp inventory
│   └── config/
│       └── SecurityConfig.java            # Cấu hình security
├── src/main/resources/
│   └── db/migration/
│       └── V2__Add_Inventory_Table.sql    # Database migration
└── frontend-web/
    ├── src/
    │   ├── services/
    │   │   └── index.js                   # API services
    │   └── views/
    │       └── Products.vue               # UI quản lý sản phẩm
    └── test-inventory-frontend.html       # Test page
```

## 🧪 Cách Test Hệ Thống

### 1. Khởi Chạy Backend
```bash
cd web-ban-hang-gia-dinh
mvn spring-boot:run
```
Backend sẽ chạy trên: `http://localhost:8080`

### 2. Khởi Chạy Frontend
```bash
cd frontend-web
npm run dev
```
Frontend sẽ chạy trên: `http://localhost:3000`

### 3. Test APIs
Mở file `test-inventory-frontend.html` trong trình duyệt để test:
- Products with Inventory API
- Inventory Statistics
- Product Inventory Details
- Low Stock Products
- Frontend Services

### 4. Test Frontend
1. Mở `http://localhost:3000`
2. Đi đến trang "Products"
3. Kiểm tra các cột tồn kho mới
4. Click vào số tồn kho để xem chi tiết

## 📊 Các API Endpoints

### Inventory APIs
```
GET    /api/inventory                              # Tất cả inventory records
GET    /api/inventory/product/{id}/details         # Chi tiết tồn kho sản phẩm
GET    /api/inventory/product/{id}/total-stock     # Tổng tồn kho
GET    /api/inventory/product/{id}/available-stock # Tồn kho có sẵn
GET    /api/inventory/statistics                   # Thống kê tồn kho
POST   /api/inventory/stock-in                     # Nhập hàng
POST   /api/inventory/stock-out                    # Xuất hàng
POST   /api/inventory/reserve                      # Đặt trước hàng
```

### Product APIs (Updated)
```
GET    /api/products/with-inventory                # Sản phẩm kèm tồn kho
GET    /api/products/{id}/inventory                # Thông tin tồn kho sản phẩm
GET    /api/products/{id}/total-stock              # Tổng tồn kho sản phẩm
```

## 🔒 Security Configuration

API endpoints đã được cấu hình để cho phép truy cập không cần đăng nhập (cho mục đích test):
- `/api/inventory/**`
- `/api/products/**`

## 💾 Database Schema

### Bảng Inventory
```sql
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    supplier_id BIGINT,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT DEFAULT 0,
    cost_price DECIMAL(19,2),
    batch_number VARCHAR(255),
    location VARCHAR(255),
    notes TEXT,
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🎨 UI Components

### Products Table
- **Tồn kho**: Hiển thị tổng tồn kho, có thể click để xem chi tiết
- **Có sẵn**: Số lượng có sẵn để bán
- **Đã đặt**: Số lượng đã được đặt trước
- **Nhà CC**: Số lượng nhà cung cấp

### Stock Details Modal
- Thông tin tổng quan (tổng tồn kho, có sẵn, đã đặt, số nhà cung cấp)
- Bảng chi tiết theo từng nhà cung cấp
- Thông tin giá vốn và vị trí kho

## 🚀 Các Tính Năng Có Thể Mở Rộng

### 1. Inventory Management Page
- Trang quản lý tồn kho riêng biệt
- Form nhập/xuất hàng
- Lịch sử giao dịch tồn kho

### 2. Reports & Analytics
- Báo cáo tồn kho theo thời gian
- Cảnh báo hết hàng
- Phân tích xu hướng tồn kho

### 3. Advanced Features
- Batch tracking
- Expiry date management
- Location-based inventory
- Automatic reorder points

## 🔧 Troubleshooting

### Backend Issues
1. **Database Connection**: Kiểm tra application.properties
2. **Migration Failed**: Kiểm tra Flyway migration scripts
3. **API Errors**: Xem logs trong console

### Frontend Issues
1. **CORS Errors**: Kiểm tra backend CORS configuration
2. **API Calls Failed**: Kiểm tra network tab trong browser
3. **UI Not Loading**: Kiểm tra console errors

## 📝 Sample Data

Hệ thống đã được khởi tạo với sample data:
- 3 sản phẩm mẫu
- 2 nhà cung cấp
- Dữ liệu tồn kho mẫu

## 📞 Hỗ Trợ

Nếu có vấn đề gì, hãy kiểm tra:
1. Backend logs trong console
2. Frontend console trong browser
3. Network requests trong Developer Tools
4. Database connections

---

**🎉 Hệ thống quản lý tồn kho đa nguồn cung ứng đã sẵn sàng để sử dụng!**

## 🎯 Hướng Dẫn Sử Dụng Phần Mềm

### 🖱️ Các Nút Bấm Chính

#### 1. Khởi Chạy Phần Mềm
**Trong VS Code:**
- Mở Command Palette: `Ctrl+Shift+P`
- Gõ: "Tasks: Run Task"
- Chọn "Run Spring Boot App" (Backend)
- Chọn "Run Frontend Dev Server" (Frontend)

**Hoặc sử dụng Task Panel:**
- Mở Task Panel: `Ctrl+Shift+P` → "Tasks: Show Running Tasks"
- Click "Run Spring Boot App"
- Click "Run Frontend Dev Server"

#### 2. Truy Cập Phần Mềm
- **Mở trình duyệt** → `http://localhost:3000`
- **Trang chính**: Hiển thị dashboard tổng quan
- **Menu Products**: Click để quản lý sản phẩm và tồn kho

#### 3. Quản Lý Tồn Kho
- **Xem tồn kho**: Click vào cột "Tồn kho" 
- **Chi tiết tồn kho**: Click vào số tồn kho → Modal hiện ra
- **Đóng modal**: Click nút "X" hoặc click bên ngoài

### 🎨 Giao Diện Người Dùng

#### Bảng Sản Phẩm
| Cột | Mô Tả | Thao Tác |
|-----|--------|----------|
| **Sản phẩm** | Tên và mã SKU | Hiển thị thông tin |
| **Tồn kho** | Tổng số tồn kho | **Click** để xem chi tiết |
| **Có sẵn** | Số lượng có sẵn | Hiển thị số liệu |
| **Đã đặt** | Số lượng đã đặt trước | Hiển thị số liệu |
| **Nhà CC** | Số nhà cung cấp | Hiển thị số liệu |

#### Modal Chi Tiết Tồn Kho
- **Thẻ thống kê**: 4 thẻ màu hiển thị tổng quan
- **Bảng chi tiết**: Danh sách theo nhà cung cấp
- **Thông tin**: Giá vốn, vị trí kho, batch number

### 🖼️ Minh Họa Giao Diện

#### Bảng Sản Phẩm - Products Table
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           QUẢN LÝ SẢN PHẨM                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Tìm kiếm: [___________] Danh mục: [All ▼] Trạng thái: [Active ▼] [Export]     │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Sản phẩm        │ Danh mục    │ Tồn kho  │ Có sẵn  │ Đã đặt  │ Nhà CC │ Thao tác │
├─────────────────┼─────────────┼──────────┼─────────┼─────────┼────────┼──────────┤
│ 🔧 Lọc dầu      │ Động cơ     │   [50]   │   45    │    5    │   2    │ ✏️ 🗑️   │
│ SKU: OIL-001    │             │  👆click │         │         │        │          │
├─────────────────┼─────────────┼──────────┼─────────┼─────────┼────────┼──────────┤
│ 🚗 Má phanh     │ Phanh       │   [5]    │    5    │    0    │   1    │ ✏️ 🗑️   │
│ SKU: BRAKE-002  │             │  👆click │         │         │        │          │
└─────────────────┴─────────────┴──────────┴─────────┴─────────┴────────┴──────────┘
```

#### Modal Chi Tiết Tồn Kho - Stock Details Modal
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ CHI TIẾT TỒN KHO - Lọc dầu động cơ                                      [ X ]  │
├─────────────────────────────────────────────────────────────────────────────────┤
│ SKU: OIL-FILTER-001                                                            │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐               │
│ │ Tổng tồn kho│ │  Có sẵn     │ │  Đã đặt     │ │ Nhà cung cấp│               │
│ │     50      │ │     45      │ │      5      │ │      2      │               │
│ └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘               │
├─────────────────────────────────────────────────────────────────────────────────┤
│ CHI TIẾT THEO NHÀ CUNG CẤP:                                                    │
│ ┌─────────────┬─────────┬─────────┬─────────┬─────────────┬─────────────────┐   │
│ │ Nhà cung cấp│ Tồn kho │ Có sẵn  │ Đã đặt  │ Giá vốn     │ Vị trí          │   │
│ ├─────────────┼─────────┼─────────┼─────────┼─────────────┼─────────────────┤   │
│ │ ABC Corp    │   30    │   25    │    5    │ 150,000 VND │ Kho A-01        │   │
│ │ XYZ Ltd     │   20    │   20    │    0    │ 140,000 VND │ Kho B-02        │   │
│ └─────────────┴─────────┴─────────┴─────────┴─────────────┴─────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### Các Nút Bấm Chính - Main Action Buttons
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           CÁC NÚT BẤM CHÍNH                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│ 🟢 [Khởi chạy Backend]    → Ctrl+Shift+P → Tasks → Run Spring Boot App         │
│                                                                                 │
│ 🔵 [Khởi chạy Frontend]   → Ctrl+Shift+P → Tasks → Run Frontend Dev Server     │
│                                                                                 │
│ 🌐 [Mở phần mềm]         → http://localhost:3000                               │
│                                                                                 │
│ 📦 [Quản lý sản phẩm]    → Click menu "Products"                               │
│                                                                                 │
│ 📊 [Xem chi tiết tồn kho] → Click vào số tồn kho trong bảng                   │
│                                                                                 │
│ 🧪 [Test API]            → Mở file test-inventory-frontend.html                │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 🎯 Các Màu Hiển Thị

#### Trạng Thái Tồn Kho
- 🟢 **Xanh lá**: Tồn kho đủ (>20)
- 🟡 **Vàng**: Tồn kho thấp (5-20)
- 🔴 **Đỏ**: Hết hàng (<5)

#### Trạng Thái Sản Phẩm
- 🟢 **Hoạt động**: Sản phẩm đang bán
- 🟡 **Hết hàng**: Tạm ngừng do hết hàng
- ⚫ **Ngừng bán**: Không bán nữa

#### Màu Cột Thống Kê
- 🔵 **Xanh dương**: Tổng tồn kho
- 🟢 **Xanh lá**: Có sẵn
- 🟡 **Vàng**: Đã đặt trước
- 🟣 **Tím**: Số nhà cung cấp

### 🚀 Workflow Sử Dụng Hàng Ngày

#### Sáng - Kiểm Tra Tồn Kho
1. Mở phần mềm: `http://localhost:3000`
2. Vào trang Products
3. Kiểm tra cột "Tồn kho" và "Có sẵn"
4. Click vào sản phẩm sắp hết để xem chi tiết

#### Trong Ngày - Cập Nhật Tồn Kho
1. Nhập hàng: Sử dụng API `/api/inventory/stock-in`
2. Xuất hàng: Sử dụng API `/api/inventory/stock-out`
3. Đặt trước: Sử dụng API `/api/inventory/reserve`

#### Cuối Ngày - Báo Cáo
1. Xuất báo cáo tồn kho
2. Kiểm tra sản phẩm hết hàng
3. Lập danh sách cần nhập thêm

### 🔧 Troubleshooting - Xử Lý Sự Cố

#### Backend Không Chạy
1. **Kiểm tra port 8080**: Mở `http://localhost:8080`
2. **Nếu lỗi**: Restart task "Run Spring Boot App"
3. **Xem logs**: Kiểm tra Terminal Output

#### Frontend Không Chạy
1. **Kiểm tra port 3000**: Mở `http://localhost:3000`
2. **Nếu lỗi**: Restart task "Run Frontend Dev Server"
3. **Lỗi npm**: Chạy `npm install` trong thư mục frontend-web

#### Không Hiển Thị Dữ Liệu
1. **Kiểm tra Network**: F12 → Network tab
2. **Xem Console**: F12 → Console tab
3. **Test API**: Mở file `test-inventory-frontend.html`
