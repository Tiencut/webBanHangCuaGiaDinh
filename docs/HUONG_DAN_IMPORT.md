# 📥 HƯỚNG DẪN SỬ DỤNG TÍNH NĂNG IMPORT DỮ LIỆU TỪ EXCEL

## 🎯 Tổng Quan

Tính năng Import dữ liệu từ Excel giúp bạn nhanh chóng nhập liệu vào hệ thống bán hàng phụ tùng xe tải. Hệ thống hỗ trợ import:

- ✅ **Danh mục sản phẩm** (Categories)
- ✅ **Nhà cung cấp** (Suppliers) 
- ✅ **Mẫu xe** (Vehicle Models)
- ✅ **Sản phẩm** (Products)
- ✅ **Khách hàng** (Customers)
- ✅ **Nội dung đào tạo** (Training Content)

---

## 📋 Yêu Cầu File Excel

### Định dạng file
- **Loại file**: .xlsx (Excel 2007 trở lên)
- **Kích thước tối đa**: 10MB
- **Encoding**: UTF-8

### Cấu trúc file
File Excel phải chứa các sheet với tên chính xác sau:

#### 1. **Sheet "Categories"** - Danh mục sản phẩm
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `name` | | Tên danh mục (VD: "Phụ tùng động cơ") |
| | `description` | Mô tả danh mục |
| | `parentCategory` | Danh mục cha (để tạo cây danh mục) |

#### 2. **Sheet "Suppliers"** - Nhà cung cấp
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `name` | | Tên nhà cung cấp |
| | `contactInfo` | Thông tin liên hệ |
| | `address` | Địa chỉ |
| | `email` | Email |
| | `phone` | Số điện thoại |

#### 3. **Sheet "VehicleModels"** - Mẫu xe
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `brand` | | Hãng xe (VD: "Hyundai") |
| `model` | | Mẫu xe (VD: "HD120SL") |
| | `year` | Năm sản xuất |
| | `engineType` | Loại động cơ |
| | `description` | Mô tả |

#### 4. **Sheet "Products"** - Sản phẩm
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `name` | | Tên sản phẩm |
| `sku` | | Mã sản phẩm (duy nhất) |
| `price` | | Giá bán (số) |
| | `category` | Danh mục (phải tồn tại) |
| | `description` | Mô tả sản phẩm |
| | `compatibleVehicles` | Xe tương thích |
| | `supplier` | Nhà cung cấp |

#### 5. **Sheet "Customers"** - Khách hàng
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `name` | | Tên khách hàng |
| | `phone` | Số điện thoại |
| | `email` | Email |
| | `address` | Địa chỉ |
| | `customerType` | Loại KH: INDIVIDUAL/BUSINESS |
| | `taxCode` | Mã số thuế |

#### 6. **Sheet "TrainingContent"** - Nội dung đào tạo
| Cột (bắt buộc) | Cột (tùy chọn) | Mô tả |
|----------------|---------------|-------|
| `title` | | Tiêu đề |
| `content` | | Nội dung đào tạo |
| | `type` | Loại: IDENTIFICATION/INSPECTION/USAGE |
| | `tags` | Tags (phân cách bằng dấu phẩy) |
| | `difficulty` | Độ khó: BASIC/INTERMEDIATE/ADVANCED |
| | `productSku` | Mã sản phẩm liên quan |

---

## 🚀 Cách Sử Dụng

### Bước 1: Chuẩn bị file Excel
1. **Tải template mẫu**: Vào trang Import → Click "📥 Tải Template"
2. **Chỉnh sửa dữ liệu**: Mở file template, thêm/sửa dữ liệu theo format
3. **Kiểm tra**: Đảm bảo các cột bắt buộc không để trống

### Bước 2: Import vào hệ thống
1. **Vào trang Import**: Menu sidebar → "Import dữ liệu"
2. **Chọn file**: Kéo thả hoặc click chọn file .xlsx
3. **Kiểm tra**: Xem thông tin file đã chọn
4. **Import**: Click "🚀 Bắt Đầu Import"

### Bước 3: Xem kết quả
- **Thành công**: Số lượng record đã import thành công
- **Cảnh báo**: Các vấn đề nhỏ (vẫn import được)
- **Lỗi**: Các lỗi nghiêm trọng (không import được)

---

## ⚠️ Lưu Ý Quan Trọng

### 📝 Quy tắc dữ liệu
- **Dòng đầu tiên** của mỗi sheet phải là tiêu đề cột
- **Không được** để trống các cột bắt buộc
- **Giá sản phẩm** phải là số dương
- **Email và phone** phải đúng định dạng
- **SKU sản phẩm** phải duy nhất

### 🔄 Xử lý trùng lặp
- **Sản phẩm**: Trùng SKU → ghi đè dữ liệu cũ
- **Khách hàng**: Trùng phone → ghi đè dữ liệu cũ  
- **Nhà cung cấp**: Trùng tên → ghi đè dữ liệu cũ
- **Danh mục**: Trùng tên → ghi đè dữ liệu cũ

### 🏗️ Thứ tự import
Hệ thống tự động import theo thứ tự:
1. Categories (danh mục)
2. Suppliers (nhà cung cấp)
3. VehicleModels (mẫu xe)
4. Products (sản phẩm)
5. Customers (khách hàng)
6. TrainingContent (nội dung đào tạo)

---

## 🔧 Xử Lý Lỗi Thường Gặp

### ❌ "File không đúng định dạng"
- **Nguyên nhân**: File không phải .xlsx
- **Giải pháp**: Lưu lại file dưới định dạng Excel (.xlsx)

### ❌ "Sheet không tồn tại"
- **Nguyên nhân**: Tên sheet không chính xác
- **Giải pháp**: Đặt tên sheet chính xác: Categories, Suppliers, VehicleModels, Products, Customers, TrainingContent

### ❌ "Thiếu dữ liệu bắt buộc"
- **Nguyên nhân**: Để trống cột bắt buộc
- **Giải pháp**: Điền đầy đủ các cột có đánh dấu bắt buộc

### ❌ "Giá sản phẩm không hợp lệ"
- **Nguyên nhân**: Giá không phải số hoặc là số âm
- **Giải pháp**: Nhập giá là số dương (VD: 1500000)

### ❌ "Email không đúng định dạng"
- **Nguyên nhân**: Email sai format
- **Giải pháp**: Sử dụng format: name@domain.com

---

## 📊 Ví Dụ Dữ Liệu Mẫu

### Categories
```
name                | description                           | parentCategory
Phụ tùng động cơ   | Các phụ tùng liên quan đến động cơ   |
Piston             | Piston động cơ các loại              | Phụ tùng động cơ
```

### Products  
```
name              | sku          | price   | category | description
Piston Hyundai    | PST-HD120-001| 1500000 | Piston   | Piston động cơ D4DB
```

### Customers
```
name              | phone      | email                | customerType
Công ty Vận Tải   | 0912345678 | info@vantai.com     | BUSINESS
Anh Nguyễn Văn A  | 0987654321 | nguyenvana@email.com| INDIVIDUAL
```

---

## 🆘 Hỗ Trợ

- **Xem hướng dẫn**: Click nút "📖 Hướng Dẫn" trên trang Import
- **Tải template**: Click nút "📥 Tải Template" để có file mẫu
- **Xem log chi tiết**: Sau import, click "📥 Tải Log" để xem chi tiết

---

## ⭐ Tips & Tricks

1. **Bắt đầu với ít dữ liệu** để test trước khi import hàng loạt
2. **Backup dữ liệu** trước khi import
3. **Kiểm tra kết quả** sau mỗi lần import
4. **Sử dụng template** để đảm bảo format đúng
5. **Import theo thứ tự**: Danh mục → Nhà cung cấp → Xe → Sản phẩm → Khách hàng → Đào tạo

---

*💡 Tip: Hệ thống sẽ hiển thị chi tiết lỗi và cảnh báo sau mỗi lần import. Hãy đọc kỹ để biết cách khắc phục.*
