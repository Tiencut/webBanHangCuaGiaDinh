# Phần mềm Bán hàng Phụ tùng Xe tải - webBanHangCuaGiaDinh

## 1. TỔNG QUAN Dự ÁN

### 1.1 Thông tin cơ bản
- **Tên dự án**: webBanHangCuaGiaDinh
- **Công nghệ**: Spring Boot (Backend), Flutter (Mobile App)
- **Đối tượng**: Hộ kinh doanh phụ tùng xe tải, xe ben
- **Mục tiêu**: Quản lý bán hàng đa nguồn cung ứng

### 1.2 Đặc điểm kinh doanh
- **Lĩnh vực**: Phụ tùng xe tải, xe ben, xe chuyên dụng
- **Mô hình**: Một sản phẩm có thể nhập từ nhiều nhà cung cấp khác nhau
- **Giá cả**: Mỗi nguồn cung có giá nhập và giá bán riêng biệt
- **Khách hàng**: Garage sửa chữa, đại lý phụ tùng, chủ xe

## 2. PHÂN TÍCH NGHIỆP VỤ

### 2.1 Quy trình kinh doanh chính
1. **Quản lý nhà cung cấp**
   - Đăng ký thông tin nhà cung cấp
   - Quản lý điều khoản thanh toán
   - Theo dõi công nợ

2. **Quản lý sản phẩm đa nguồn**
   - Một sản phẩm = nhiều nguồn cung
   - Mỗi nguồn có: giá nhập, giá bán, tồn kho riêng
   - **Hiển thị tồn kho**: Vừa hiển thị tồn kho tổng, vừa hiển thị chi tiết theo từng nhà cung cấp
   - **Quy trình bán hàng**: Hỗ trợ 2 mode:
     * **Mode 1**: Bán không phân biệt nguồn (tự động chọn nguồn tối ưu)
     * **Mode 2**: Bán có chỉ định nguồn cụ thể (theo yêu cầu đặc biệt)
   - Thông tin kỹ thuật: mã phụ tùng, xe áp dụng, năm sản xuất

3. **Quản lý bán hàng**
   - **Hiển thị tồn kho thông minh**:
     * Tồn kho tổng (tất cả nguồn cộng lại)
     * Chi tiết tồn kho theo từng nhà cung cấp
     * Trạng thái: "Còn hàng", "Sắp hết", "Hết hàng"
   - **Báo giá linh hoạt**:
     * Báo giá theo tồn kho tổng (giá tốt nhất)
     * Báo giá theo nguồn cụ thể (nếu khách yêu cầu)
   - **Quy trình bán hàng đa mode**:
     * **Mode Tự động**: Hệ thống tự chọn nguồn tối ưu (giá + tồn kho)
     * **Mode Thủ công**: Nhân viên chọn nguồn cụ thể
   - **Chính sách xuất kho**:
     * FIFO: Xuất hàng nhập trước tiên
     * Tối ưu lợi nhuận: Xuất từ nguồn có lợi nhuận cao nhất
     * Cân bằng nhà cung cấp: Xuất đều các nguồn
   - Xuất hóa đơn và giao hàng

4. **Quản lý tồn kho**
   - Nhập hàng từ nhiều nguồn
   - Xuất kho theo FIFO/LIFO
   - Kiểm kê định kỳ

### 2.2 Actors (Người dùng hệ thống)
- **Chủ cửa hàng**: Quản lý tổng thể
- **Nhân viên bán hàng**: Tạo đơn hàng, báo giá
- **Thủ kho**: Quản lý nhập/xuất kho
- **Shipper/Giao hàng**: Nhận đơn, cập nhật trạng thái giao hàng
- **Kế toán**: Theo dõi công nợ, doanh thu
- **Khách hàng** (Mobile): Xem sản phẩm, đặt hàng, theo dõi giao hàng

### 2.3 Phân tích quy trình tồn kho và bán hàng

#### 2.3.1 Vấn đề nghiệp vụ
**Câu hỏi**: Khi bán hàng, có cần biết đang bán sản phẩm của nhà cung cấp nào không?

#### 2.3.2 Giải pháp đề xuất: Hệ thống Dual-Mode

##### **Mode 1: Bán hàng đơn giản (Recommended)**
- **Hiển thị**: Chỉ hiển thị tồn kho tổng cho khách hàng
- **Quy trình bán**:
  1. Khách hàng thấy: "Còn 50 cái" (tổng tất cả nguồn)
  2. Khi đặt hàng, hệ thống TỰ ĐỘNG chọn nguồn tối ưu
  3. Tiêu chí tự động: Giá nhập thấp nhất → Lợi nhuận cao nhất
- **Ưu điểm**: 
  - Đơn giản cho khách hàng
  - Tối ưu lợi nhuận tự động
  - Nhanh chóng trong bán hàng

##### **Mode 2: Bán hàng có kiểm soát nguồn**
- **Hiển thị**: Tồn kho tổng + chi tiết theo nhà cung cấp
- **Quy trình bán**:
  1. Nhân viên thấy: "Tổng: 50 cái (NCC A: 20, NCC B: 30)"
  2. Có thể chọn nguồn cụ thể khi bán
  3. Lý do chọn nguồn: Chất lượng, thời gian giao, mối quan hệ NCC
- **Ưu điểm**:
  - Kiểm soát được nguồn bán ra
  - Cân bằng đơn hàng với các NCC
  - Đáp ứng yêu cầu đặc biệt của khách

#### 2.3.3 Khuyến nghị triển khai
1. **Bắt đầu với Mode 1** (đơn giản, hiệu quả)
2. **Có tùy chọn chuyển sang Mode 2** khi cần thiết
3. **Cấu hình linh hoạt** theo từng sản phẩm hoặc nhân viên

## 2.4 Đặc điểm quy trình nhập hàng thực tế

### 2.4.1 Quy trình nhập hàng với NCC
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Quy trình thực tế**:
   - Hộ kinh doanh tạo **Danh sách hàng cần nhập** (chỉ có tên sản phẩm + số lượng mong muốn)
   - Gửi cho NCC qua Zalo/Email/Phone
   - NCC chỉ trả lời **CÓ/KHÔNG** có hàng (không báo giá, không báo số lượng chính xác)
   - Khi nhận hàng mới biết giá thực tế và số lượng thực tế

2. **Vấn đề phát sinh**:
   - **Giá bất ngờ**: Giá có thể tăng so với lần nhập trước
   - **Số lượng khác**: NCC có thể giao ít hơn hoặc nhiều hơn yêu cầu
   - **So sánh khó khăn**: Khó so sánh giá giữa các NCC trước khi đặt hàng

3. **Giải pháp hệ thống cần hỗ trợ**:
   - **Tạo đơn dự kiến**: Danh sách hàng cần nhập (chưa có giá)
   - **Gửi inquiry**: Gửi danh sách cho nhiều NCC cùng lúc
   - **Theo dõi phản hồi**: NCC nào có/không có hàng
   - **Nhập hàng thực tế**: Cập nhật giá và số lượng thực khi nhận hàng
   - **Cảnh báo giá**: So sánh với giá lịch sử, cảnh báo nếu tăng quá nhiều

### 2.4.2 Tính năng cần thiết cho quy trình này
- **Module Inquiry Management**: Quản lý danh sách inquiry gửi NCC
- **Price History Tracking**: Theo dõi lịch sử giá để cảnh báo
- **Supplier Response Tracking**: Theo dõi phản hồi của từng NCC
- **Flexible Purchase Order**: Đơn nhập linh hoạt, cho phép cập nhật sau

## 2.5 Đặc điểm bán hàng cho khách hàng đặc biệt

### 2.5.1 Quy trình bán hàng cho chủ garage
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Quy trình thực tế**:
   - **Khách hàng đặc biệt**: Chủ garage, thợ sửa chữa có uy tín
   - **Cách đặt hàng**: Gọi điện trực tiếp, yêu cầu giao hàng ngay
   - **Đặc điểm giá**: Yêu cầu **làm giá cao hơn** giá thực tế
   - **Mục đích**: Để họ báo giá cao với chủ xe và kiếm lời từ chênh lệch

2. **Lý do kinh doanh**:
   - **Mối quan hệ lâu dài**: Khách hàng VIP, đặt hàng thường xuyên
   - **Uy tín nghề nghiệp**: Chủ garage cần duy trì hình ảnh chuyên nghiệp
   - **Win-win**: Cả hai bên đều có lợi nhuận

3. **Yêu cầu hệ thống**:
   - **Chính sách giá linh hoạt**: Cho phép điều chỉnh giá theo khách hàng
   - **Múi giá khác nhau**: Giá thường, giá VIP, giá đặc biệt
   - **Ghi chú đặc biệt**: Lưu lý do điều chỉnh giá
   - **Bảo mật thông tin**: Không để lộ giá gốc cho khách

### 2.5.2 Tính năng cần thiết
- **Customer Price Tier**: Phân cấp giá theo loại khách hàng
- **Dynamic Pricing**: Điều chỉnh giá linh hoạt khi bán
- **Price Markup**: Tự động cộng thêm % markup theo quy định
- **Confidential Pricing**: Ẩn giá gốc với một số loại khách hàng

## 2.6 Yêu cầu bảo mật cho Mobile App

### 2.6.1 Đăng nhập bằng khuôn mặt
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE MOBILE:**

1. **Face Recognition Login**:
   - **Setup lần đầu**: Đăng ký khuôn mặt khi cài đặt app
   - **Login nhanh**: Sử dụng camera để nhận diện khuôn mặt
   - **Fallback**: Vẫn hỗ trợ PIN/Password backup
   - **Security**: Lưu biometric data local, không gửi lên server

2. **Multi-layer Security**:
   - **Primary**: Face Recognition hoặc Fingerprint
   - **Secondary**: PIN/Password
   - **Emergency**: OTP qua SMS (nếu quên tất cả)

3. **Yêu cầu kỹ thuật**:
   - **iOS**: Face ID API hoặc Touch ID
   - **Android**: BiometricPrompt API
   - **Flutter packages**: local_auth, flutter_secure_storage
   - **Offline support**: Hoạt động ngay cả khi mất mạng

### 2.6.2 Tính năng bảo mật khác
- **Auto-lock**: Tự động khóa app sau X phút không sử dụng
- **Screenshot protection**: Chặn chụp màn hình với thông tin nhạy cảm
- **Session timeout**: Hết phiên làm việc sau thời gian nhất định

## 2.7 Hệ thống Ghi chú và Quản lý Kiến thức

### 2.7.1 Nhu cầu ghi chú trong kinh doanh
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Vấn đề thực tế**:
   - **Ghi chú kinh doanh**: Cần lưu các vấn đề, ý tưởng, nhắc nhở
   - **Thông tin khách hàng**: Ghi chú về sở thích, yêu cầu đặc biệt
   - **Vấn đề với NCC**: Lưu các vấn đề giao hàng, chất lượng
   - **Ý tưởng cải tiến**: Các ý tưởng phát triển kinh doanh

2. **Yêu cầu tích hợp**:
   - **Thay thế Notion/GG Keep**: Không cần mở app khác
   - **Liên kết với dữ liệu**: Ghi chú có thể liên kết với khách hàng, sản phẩm, đơn hàng
   - **Tìm kiếm nhanh**: Dễ dàng tìm lại thông tin
   - **Đồng bộ**: Sync giữa web và mobile

3. **Phân loại ghi chú**:
   - **Ghi chú cá nhân**: Nhắc nhở, to-do list
   - **Ghi chú khách hàng**: Thông tin bổ sung về khách hàng
   - **Ghi chú sản phẩm**: Đặc điểm, lưu ý khi bán
   - **Ghi chú NCC**: Vấn đề, đánh giá, thỏa thuận
   - **Ghi chú đơn hàng**: Yêu cầu đặc biệt, vấn đề giao hàng

### 2.7.2 Tính năng cần thiết
- **Rich Text Editor**: Hỗ trợ format text, hình ảnh, checklist
- **Tag System**: Gắn tag để phân loại và tìm kiếm
- **Quick Note**: Ghi chú nhanh từ bất kỳ màn hình nào
- **Voice Note**: Ghi chú bằng giọng nói (mobile)
- **Reminder**: Nhắc nhở theo thời gian
- **Export/Import**: Xuất ra file hoặc chia sẻ

## 2.8 Hệ thống Quản lý Giao hàng và Vận chuyển

### 2.8.1 Đặc điểm giao hàng phụ tùng xe tải
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Phương thức giao hàng thực tế**:
   - **Xe ôm**: Giao hàng nhanh, đơn hàng nhỏ, trong thành phố
   - **Xe khách**: Giao đi tỉnh xa, hàng cồng kềnh, tiết kiệm chi phí
   - **Xe tải riêng**: Đơn hàng lớn, khách VIP
   - **Khách tự đến lấy**: Khách hàng đến cửa hàng

2. **Đặc điểm từng phương thức**:
   - **Xe ôm**: 
     * Phí cao nhưng nhanh (1-2 tiếng)
     * Giới hạn kích thước/trọng lượng
     * Thường dùng cho khách gấp
   - **Xe khách**:
     * Phí rẻ nhưng chậm (1-2 ngày)
     * Có thể gửi hàng cồng kềnh
     * Cần đóng gói cẩn thận
   - **Tự giao**:
     * Kiểm soát được chất lượng
     * Chi phí xăng + nhân công
     * Thường cho khách VIP

3. **Vấn đề cần quản lý**:
   - **Chọn phương thức**: Dựa vào kích thước, trọng lượng, địa chỉ
   - **Tính phí vận chuyển**: Theo từng phương thức
   - **Theo dõi đơn hàng**: Trạng thái giao hàng
   - **Quản lý đối tác**: Thông tin xe ôm, xe khách thường dùng

### 2.8.2 Tính năng cần thiết
- **Delivery Method Selection**: Chọn phương thức giao hàng thông minh
- **Delivery Partner Management**: Quản lý danh bạ xe ôm, xe khách
- **Shipping Cost Calculator**: Tính phí vận chuyển tự động
- **Delivery Tracking**: Theo dõi trạng thái giao hàng
- **Route Optimization**: Tối ưu tuyến đường giao hàng

## 2.9 Hệ thống Sản phẩm Combo và Thay thế linh kiện

### 2.9.1 Đặc điểm sản phẩm combo trong phụ tùng xe tải
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Mô hình sản phẩm combo thực tế**:
   - **Sản phẩm chính (Parent)**: Bộ phanh, bộ động cơ, bộ hộp số
   - **Sản phẩm con (Child)**: Các linh kiện thành phần
   - **Ví dụ**: Bộ phanh xe Hino = má phanh + đĩa phanh + dầu phanh + ống dẫn

2. **Nhu cầu thay thế linh kiện**:
   - **Thay thế theo chất lượng**: Khách muốn linh kiện tốt hơn/rẻ hơn
   - **Thay thế theo hãng**: Đổi từ hàng zin sang hàng Trung Quốc hoặc ngược lại
   - **Thay thế theo tình trạng**: Một số linh kiện khách đã có sẵn
   - **Thay thế theo giá**: Điều chỉnh để phù hợp ngân sách

3. **Quy trình thay thế thực tế**:
   - Khách hàng chọn combo ban đầu
   - Xem chi tiết các linh kiện trong combo
   - Yêu cầu thay đổi một số linh kiện cụ thể
   - Hệ thống tính lại giá tổng
   - Kiểm tra tương thích giữa các linh kiện

4. **Vấn đề cần giải quyết**:
   - **Tương thích**: Đảm bảo linh kiện thay thế tương thích
   - **Giá cả**: Tính toán lại giá khi có thay đổi
   - **Tồn kho**: Kiểm tra tồn kho của linh kiện thay thế
   - **Chiết khấu**: Áp dụng chiết khấu combo có còn hợp lý không

### 2.9.2 Tính năng cần thiết
- **Product Bundle Management**: Quản lý sản phẩm combo
- **Component Substitution**: Thay thế linh kiện linh hoạt
- **Compatibility Check**: Kiểm tra tương thích tự động
- **Dynamic Pricing**: Tính giá động khi có thay đổi
- **Stock Validation**: Kiểm tra tồn kho cho tất cả linh kiện

## 2.10 Hệ thống Phân loại Sản phẩm Đa cấp

### 2.10.1 Cấu trúc phân loại theo cây (Tree Structure)
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Mô hình phân loại thực tế**:
   ```
   Phụ tùng xe tải (Root)
   ├── Hệ thống động cơ (Level 1)
   │   ├── Động cơ Hino (Level 2)
   │   │   ├── Động cơ J05E (Level 3)
   │   │   │   ├── Piston J05E (Level 4)
   │   │   │   ├── Xupap J05E (Level 4)
   │   │   │   └── Gasket J05E (Level 4)
   │   │   └── Động cơ J08E (Level 3)
   │   ├── Động cơ Hyundai (Level 2)
   │   └── Động cơ Thaco (Level 2)
   ├── Hệ thống phanh (Level 1)
   │   ├── Phanh Hino (Level 2)
   │   │   ├── Má phanh (Level 3)
   │   │   ├── Đĩa phanh (Level 3)
   │   │   └── Dầu phanh (Level 3)
   │   └── Phanh Hyundai (Level 2)
   └── Hệ thống điện (Level 1)
       ├── Bình ắc quy (Level 2)
       ├── Đèn chiếu sáng (Level 2)
       └── Hệ thống dây điện (Level 2)
   ```

2. **Đặc điểm của hệ thống**:
   - **Không giới hạn cấp độ**: Có thể tạo bao nhiêu cấp tùy ý
   - **Phân loại theo nhiều tiêu chí**:
     * Theo chức năng: Động cơ, phanh, điện, khung gầm
     * Theo hãng xe: Hino, Hyundai, Thaco, Dongfeng
     * Theo loại xe: Xe tải nhẹ, xe tải nặng, xe ben
     * Theo năm sản xuất: 2010-2015, 2015-2020, 2020+
   - **Kế thừa thuộc tính**: Nhóm con kế thừa thuộc tính từ nhóm cha
   - **Quản lý dễ dàng**: Drag & drop để sắp xếp lại cây

3. **Ví dụ phân loại chi tiết**:
   ```
   Phụ tùng xe Hino
   ├── Hino 300 Series (Xe tải nhẹ)
   │   ├── Động cơ N04C
   │   │   ├── Phần đầu máy
   │   │   │   ├── Nắp quy lát
   │   │   │   ├── Gasket nắp quy lát
   │   │   │   └── Ốc vít nắp quy lát
   │   │   ├── Block máy
   │   │   └── Phần phụ kiện
   │   ├── Hộp số M038OD
   │   └── Cầu sau
   ├── Hino 500 Series (Xe tải nặng)
   │   ├── Động cơ J05E
   │   ├── Động cơ J08E
   │   └── Hộp số Pro Shift
   └── Hino 700 Series (Đầu kéo)
       ├── Động cơ E13C
       └── Động cơ A09C
   ```

### 2.10.2 Yêu cầu chức năng hệ thống
1. **Quản lý cây danh mục**:
   - **CRUD Category**: Tạo, sửa, xóa, di chuyển danh mục
   - **Drag & Drop**: Kéo thả để sắp xếp lại cấu trúc
   - **Bulk operations**: Thao tác hàng loạt (di chuyển nhiều sản phẩm)
   - **Import/Export**: Nhập/xuất cây danh mục từ Excel

2. **Hiển thị và điều hướng**:
   - **Tree view**: Hiển thị dạng cây có thể mở/đóng
   - **Breadcrumb**: Đường dẫn phân cấp (Home > Động cơ > Hino > J05E)
   - **Search in category**: Tìm kiếm trong danh mục cụ thể
   - **Category filter**: Lọc sản phẩm theo danh mục

3. **Kế thừa thuộc tính**:
   - **Inherited properties**: Nhóm con kế thừa thuộc tính từ cha
   - **Override properties**: Có thể ghi đè thuộc tính nếu cần
   - **Template inheritance**: Kế thừa template mô tả, hình ảnh

### 2.10.3 Thiết kế database cho Tree Structure
1. **Adjacency List Model** (Đơn giản, dễ implement):
   ```sql
   categories (
     id INT PRIMARY KEY,
     name VARCHAR(255),
     parent_id INT REFERENCES categories(id),
     level INT,
     sort_order INT,
     path VARCHAR(1000), -- "/1/5/12/" cho fast query
     properties JSON -- Thuộc tính kế thừa
   )
   ```

2. **Nested Set Model** (Hiệu quả cho read operations):
   ```sql
   categories (
     id INT PRIMARY KEY,
     name VARCHAR(255),
     lft INT, -- Left boundary
     rgt INT, -- Right boundary
     level INT
   )
   ```

### 2.10.4 Tính năng nâng cao
1. **Smart categorization**:
   - **Auto-suggest**: Đề xuất danh mục dựa trên tên sản phẩm
   - **Duplicate detection**: Phát hiện sản phẩm trùng nhóm
   - **Category analytics**: Thống kê sản phẩm theo nhóm

2. **Bulk management**:
   - **Mass category change**: Đổi nhóm hàng loạt
   - **Category merge**: Gộp 2 nhóm thành 1
   - **Category split**: Tách 1 nhóm thành nhiều nhóm

3. **Advanced features**:
   - **Category SEO**: URL friendly cho từng nhóm
   - **Category images**: Hình ảnh đại diện cho nhóm
   - **Category permissions**: Phân quyền xem/chỉnh sửa theo nhóm
   - **Category templates**: Mẫu thuộc tính cho từng nhóm

### 2.10.5 API Design cho Category Management

1. **Category CRUD APIs**:
   ```javascript
   // Lấy toàn bộ cây danh mục
   GET /api/categories/tree
   Response: {
     "data": [
       {
         "id": 1,
         "name": "Phụ tùng xe tải",
         "level": 0,
         "children": [
           {
             "id": 2,
             "name": "Hệ thống động cơ",
             "level": 1,
             "parent_id": 1,
             "children": [...]
           }
         ]
       }
     ]
   }

   // Tạo danh mục mới
   POST /api/categories
   Body: {
     "name": "Động cơ J05E",
     "parent_id": 5,
     "sort_order": 1,
     "properties": {...}
   }

   // Di chuyển danh mục (drag & drop)
   PUT /api/categories/{id}/move
   Body: {
     "new_parent_id": 10,
     "new_sort_order": 3
   }

   // Lấy breadcrumb
   GET /api/categories/{id}/breadcrumb
   Response: {
     "breadcrumb": [
       {"id": 1, "name": "Phụ tùng xe tải"},
       {"id": 2, "name": "Hệ thống động cơ"},
       {"id": 5, "name": "Động cơ Hino"},
       {"id": 12, "name": "Động cơ J05E"}
     ]
   }
   ```

2. **Product-Category APIs**:
   ```javascript
   // Lấy sản phẩm theo danh mục
   GET /api/categories/{id}/products?include_children=true
   
   // Đổi danh mục hàng loạt
   PUT /api/products/batch-category
   Body: {
     "product_ids": [1,2,3],
     "category_id": 15
   }

   // Tìm kiếm sản phẩm trong danh mục
   GET /api/categories/{id}/products/search?q=gasket&deep=true
   ```

### 2.10.6 Implementation notes khi code

1. **Performance optimization**:
   ```java
   // Sử dụng materialized path cho fast query
   @Entity
   public class Category {
     @Id
     private Long id;
     
     private String name;
     
     @Column(name = "parent_id")
     private Long parentId;
     
     @Column(name = "path") // "/1/5/12/"
     private String path;
     
     @Column(name = "level")
     private Integer level;
     
     // Index cho path để query nhanh
     @Index(columnList = "path")
     
     // Lazy load children
     @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
     private List<Category> children;
   }
   ```

2. **Tree operations service**:
   ```java
   @Service
   public class CategoryTreeService {
     
     // Di chuyển node và update path cho tất cả children
     public void moveCategory(Long categoryId, Long newParentId) {
       Category category = findById(categoryId);
       String oldPath = category.getPath();
       
       // Update parent
       category.setParentId(newParentId);
       category.setPath(buildNewPath(newParentId, categoryId));
       
       // Update all descendants
       updateDescendantPaths(oldPath, category.getPath());
     }
     
     // Lấy toàn bộ cây với optimization
     @Cacheable("category-tree")
     public List<CategoryTreeNode> getCategoryTree() {
       // Implementation với recursive CTE hoặc nested query
     }
   }
   ```

3. **Frontend tree component**:
   - Sử dụng React Tree component hoặc Vue Tree
   - Lazy loading cho cây lớn
   - Virtual scrolling nếu có quá nhiều node
   - Drag & drop với sortable.js

### 2.10.7 Vấn đề thực tế cần lưu ý

1. **Data migration từ KiotViet**:
   - KiotViet chỉ hỗ trợ 1-2 cấp danh mục
   - Cần mapping và tái cấu trúc dữ liệu hiện có
   - Backup before migration

2. **User experience**:
   - Không nên tạo cây quá sâu (> 6 levels)
   - Cung cấp shortcut cho danh mục thường dùng
   - Search global và search trong category

3. **Business rules**:
   - Không được xóa danh mục có sản phẩm
   - Không được tạo cycle trong cây
   - Validate tên danh mục unique trong cùng level

4. **Mobile considerations**:
   - Tree view responsive
   - Touch-friendly drag & drop
   - Collapse/expand animation smooth
## 3. CHỨC NĂNG CHI TIẾT

### 3.1 Module Sản phẩm
- **Quản lý danh mục sản phẩm**
  - Phân loại theo xe (Hino, Hyundai, Thaco...)
  - Phân loại theo bộ phận (động cơ, hộp số, phanh...)
- **Quản lý thông tin sản phẩm**
  - Mã sản phẩm, tên sản phẩm
  - Mô tả kỹ thuật, hình ảnh
  - Xe áp dụng, năm sản xuất
  - **Loại sản phẩm**:
    * Sản phẩm đơn: Linh kiện riêng lẻ
    * Sản phẩm combo: Bộ sản phẩm gồm nhiều linh kiện
  - **Quản lý combo sản phẩm**:
    * Định nghĩa sản phẩm cha và con
    * Thiết lập linh kiện mặc định
    * Cấu hình linh kiện có thể thay thế
    * Quy tắc tương thích giữa linh kiện
  - **Hiển thị tồn kho thông minh**:
    * Tồn kho tổng: Tổng hợp tất cả nguồn cung
    * Tồn kho chi tiết: Từng nhà cung cấp riêng biệt
    * Cảnh báo: Hết hàng, sắp hết, cần nhập thêm
  - **Ghi chú sản phẩm**:
    * Đặc điểm kỹ thuật bổ sung
    * Lưu ý khi bán hàng
    * Kinh nghiệm từ khách hàng
- **Quản lý đa nguồn cung**
  - Liên kết sản phẩm với nhiều nhà cung cấp
  - Giá nhập/bán theo từng nguồn
  - Thời gian giao hàng của từng nguồn
  - **Chính sách bán hàng**:
    * Auto-select: Tự động chọn nguồn tối ưu
    * Manual-select: Nhân viên chọn nguồn cụ thể
    * Priority rules: Ưu tiên theo giá, chất lượng, hoặc mối quan hệ

### 3.2 Module Nhà cung cấp
- **Thông tin cơ bản**: Tên, địa chỉ, liên hệ
- **Điều khoản thương mại**: Thời hạn thanh toán, chiết khấu
- **Đánh giá nhà cung cấp**: Chất lượng, thời gian giao hàng
- **Ghi chú NCC**: 
  * Vấn đề giao hàng, chất lượng
  * Thỏa thuận đặc biệt
  * Đánh giá và nhận xét
- **Theo dõi công nợ**: Phải trả, đã trả, quá hạn

### 3.3 Module Bán hàng
- **Quản lý khách hàng**
  - Thông tin cá nhân/doanh nghiệp
  - **Phân loại khách hàng**: 
    * Khách lẻ (giá niêm yết)
    * Khách VIP (chiết khấu)
    * Khách đặc biệt - Chủ garage (giá markup)
  - **Chính sách giá riêng**: 
    * Giá cố định theo từng khách
    * % markup tự động
    * Giá linh hoạt theo đơn hàng
  - **Ghi chú khách hàng**: 
    * Sở thích, yêu cầu đặc biệt
    * Lịch sử vấn đề, khiếu nại
    * Ghi chú về mối quan hệ
  - Lịch sử mua hàng
- **Tạo báo giá**
  - So sánh giá từ nhiều nguồn cung
  - **Báo giá sản phẩm combo**:
    * Hiển thị giá combo tổng
    * Chi tiết giá từng linh kiện
    * Tùy chọn thay thế linh kiện với giá chênh lệch
    * So sánh giá combo vs mua lẻ
  - **Tính giá theo khách hàng**:
    * Giá gốc + markup % (cho chủ garage)
    * Giá chiết khấu (cho khách VIP)
    * Giá niêm yết (cho khách lẻ)
  - **Thương lượng giá linh hoạt**:
    * Real-time discount validation
    * Profit margin protection
    * Manager approval workflow cho discount lớn
    * Lưu lại lý do và lịch sử thương lượng
  - **Bảo mật giá**: Chỉ hiển thị giá phù hợp với từng loại khách
  - Tính toán lợi nhuận
  - Gửi báo giá qua email/Zalo
- **Quản lý đơn hàng**
  - Tạo đơn hàng từ báo giá
  - **Voice-to-Order**: Tạo đơn hàng bằng giọng nói (mobile + web)
  - **Quick Order**: Tạo đơn nhanh cho khách quen
  - **Price Negotiation**: Thương lượng và điều chỉnh giá realtime
  - **Discount Management**: Áp dụng giảm giá với approval workflow
  - Theo dõi trạng thái đơn hàng
  - **Quản lý giao hàng**:
    * Chọn phương thức: Xe ôm, xe khách, tự giao
    * Tính phí vận chuyển tự động
    * Assign đối tác giao hàng
    * Theo dõi trạng thái giao hàng
  - **Ghi chú đơn hàng**: Yêu cầu đặc biệt, vấn đề giao hàng
  - Xuất hóa đơn điện tử

### 3.4 Module Kho
- **Quản lý nhập kho**
  - **Tạo đơn inquiry**: Danh sách hàng cần nhập gửi NCC
  - **Theo dõi phản hồi NCC**: Có/không có hàng
  - **Nhập hàng thực tế**: Cập nhật giá và số lượng khi nhận hàng
  - **Cảnh báo giá**: So sánh với lịch sử, cảnh báo tăng giá
  - Kiểm tra chất lượng
- **Quản lý xuất kho**
  - Xuất theo đơn hàng
  - **Chính sách xuất kho thông minh**:
    * FIFO: Xuất hàng nhập trước (mặc định)
    * LIFO: Xuất hàng nhập sau
    * Profit-First: Ưu tiên lợi nhuận cao
    * Balance: Cân bằng giữa các nhà cung cấp
  - **Truy xuất nguồn gốc**: Ghi nhận sản phẩm xuất từ nhà cung cấp nào
  - Cập nhật tồn kho realtime
- **Báo cáo tồn kho**
  - Tồn kho theo sản phẩm
  - Tồn kho theo nhà cung cấp
  - Hàng sắp hết, hàng ế

### 3.5 Module Báo cáo
- **Báo cáo bán hàng**
  - Doanh thu theo ngày/tháng/quý
  - Top sản phẩm bán chạy
  - Hiệu quả nhân viên bán hàng
  - **Báo cáo giao hàng**: Chi phí, thời gian, tỷ lệ thành công
- **Báo cáo lợi nhuận**
  - Lợi nhuận theo sản phẩm
  - Lợi nhuận theo nhà cung cấp
  - Lợi nhuận theo khách hàng
- **Báo cáo tồn kho**
  - Vòng quay kho
  - Hàng tồn kho chết
  - Dự báo nhập hàng

### 3.6 Module Ghi chú và Quản lý Kiến thức
- **Quản lý ghi chú**
  - **Tạo ghi chú nhanh**: Quick note từ bất kỳ màn hình nào
  - **Rich text editor**: Format text, chèn hình ảnh, tạo checklist
  - **Voice note**: Ghi chú bằng giọng nói (mobile)
  - **Phân loại ghi chú**:
    * Ghi chú cá nhân (to-do, nhắc nhở)
    * Ghi chú khách hàng (sở thích, yêu cầu đặc biệt)
    * Ghi chú sản phẩm (đặc điểm, lưu ý bán hàng)
    * Ghi chú NCC (vấn đề, đánh giá)
    * Ghi chú đơn hàng (yêu cầu đặc biệt)

- **Hệ thống Tag và Tìm kiếm**
  - **Tag system**: Gắn tag tự do cho ghi chú
  - **Tìm kiếm full-text**: Tìm trong nội dung ghi chú
  - **Filter nâng cao**: Lọc theo loại, tag, ngày tạo
  - **Liên kết thông minh**: Tự động liên kết với khách hàng/sản phẩm

- **Tính năng nâng cao**
  - **Reminder**: Nhắc nhở theo thời gian
  - **Share note**: Chia sẻ ghi chú với đồng nghiệp
  - **Template**: Mẫu ghi chú có sẵn
  - **Export**: Xuất ra PDF, Word, hoặc text file
  - **Offline support**: Hoạt động khi mất mạng (mobile)

### 3.7 Module Giao hàng và Vận chuyển
- **Quản lý phương thức giao hàng**
  - **Xe ôm**: 
    * Danh bạ xe ôm thường dùng (SĐT, đánh giá)
    * Tính phí theo km hoặc cố định
    * Giới hạn kích thước/trọng lượng
    * Thời gian giao hàng: 1-2 tiếng
  - **Xe khách**:
    * Danh sách nhà xe, tuyến đường
    * Phí vận chuyển theo tuyến
    * Yêu cầu đóng gói đặc biệt
    * Thời gian: 1-2 ngày
  - **Tự giao**:
    * Lên kế hoạch tuyến đường
    * Tính chi phí xăng + nhân công
    * Khách hàng VIP ưu tiên

- **Hệ thống chọn phương thức thông minh**
  - **Auto-suggest**: Đề xuất phương thức dựa trên:
    * Kích thước/trọng lượng sản phẩm
    * Địa chỉ giao hàng (trong/ngoài thành phố)
    * Độ gấp của đơn hàng
    * Loại khách hàng
  - **Manual override**: Cho phép nhân viên chọn thủ công

- **Quản lý đối tác vận chuyển**
  - **Danh bạ xe ôm**: SĐT, đánh giá, khu vực hoạt động
  - **Thông tin xe khách**: Tên nhà xe, tuyến đường, giá cước
  - **Đánh giá dịch vụ**: Rating, ghi chú về chất lượng

- **Theo dõi và báo cáo**
  - **Tracking đơn hàng**: Trạng thái giao hàng realtime
  - **Chi phí vận chuyển**: Thống kê theo phương thức
  - **Hiệu quả giao hàng**: Thời gian, tỷ lệ thành công
  - **Tối ưu tuyến đường**: Ghép đơn hàng cùng tuyến

### 3.8 Module Quản lý Sản phẩm Combo
- **Tạo và quản lý combo**
  - **Tạo combo mới**: 
    * Chọn sản phẩm cha (ví dụ: Bộ phanh xe Hino)
    * Thêm các sản phẩm con (má phanh, đĩa phanh, dầu phanh)
    * Thiết lập số lượng mỗi linh kiện
    * Đặt giá combo (có thể chiết khấu so với mua lẻ)
  
  - **Cấu hình thay thế linh kiện**:
    * Định nghĩa linh kiện có thể thay thế
    * Thiết lập nhóm tương thích
    * Cấu hình giá chênh lệch khi thay thế
    * Quy tắc kiểm tra tương thích

- **Quy trình bán combo có tùy chỉnh**
  - **Hiển thị combo**: Khách thấy combo với giá tổng và chi tiết linh kiện
  - **Tùy chỉnh linh kiện**:
    * Khách click "Thay đổi" trên linh kiện cụ thể
    * Hiển thị danh sách linh kiện thay thế tương thích
    * Cho phép chọn linh kiện mới
    * Tự động tính lại giá combo
  - **Validation**:
    * Kiểm tra tương thích giữa các linh kiện
    * Kiểm tra tồn kho của tất cả linh kiện
    * Cảnh báo nếu có vấn đề

- **Tính năng nâng cao**
  - **Template combo**: Lưu các combo phổ biến
  - **Combo suggestion**: Đề xuất combo dựa trên xe khách hàng
  - **Bulk discount**: Chiết khấu khi mua nhiều combo
  - **Combo history**: Lưu lịch sử combo đã mua của khách
- **Smart substitution**: Gợi ý thay thế dựa trên lịch sử và xe cụ thể
  - **Performance tracking**: Theo dõi hiệu quả của combo đã bán

### 3.9 Module Quản lý Thông tin Xe và Lịch sử Thay thế
- **Quản lý thông tin xe khách hàng**
  - **Đăng ký xe**: Biển số, model, năm sản xuất, mục đích sử dụng
  - **Phân loại theo usage**: Xe chạy thành phố, đường dài, núi đồi
  - **Tracking maintenance**: Lịch sử bảo dưỡng, thay thế linh kiện

- **Hệ thống gợi ý thông minh**
  - **Personal history**: "Xe này đã từng thay má phanh Bendix và đánh giá 5⭐"
  - **Similar vehicle insights**: "Xe tương tự thường thay linh kiện này"
  - **Usage-based recommendations**: "Xe chạy núi nên dùng phanh cao cấp"
  - **Cost-benefit analysis**: "Thay linh kiện này sẽ tiết kiệm 20% chi phí"

- **Feedback và đánh giá**
  - **Rating system**: Khách hàng đánh giá linh kiện đã thay
  - **Performance tracking**: Theo dõi tuổi thọ thực tế
  - **Issue reporting**: Báo cáo vấn đề nếu có
  - **Knowledge base**: Tích lũy kiến thức để tư vấn tốt hơn
## 4. YÊU CẦU KỸ THUẬT

### 4.1 Yêu cầu chức năng
- **Đa người dùng**: Phân quyền theo vai trò
- **Đa chi nhánh**: Quản lý nhiều cửa hàng
- **Tích hợp**: API với nhà cung cấp, hóa đơn điện tử
- **Mobile**: 
  * App Flutter cho nhân viên và khách hàng
  * Face Recognition login cho bảo mật
  * Offline capability cho nhân viên bán hàng
- **Báo cáo**: Xuất Excel, PDF
- **Backup**: Sao lưu dữ liệu tự động

### 4.2 Yêu cầu phi chức năng
- **Hiệu năng**: Xử lý 1000 đơn hàng/ngày
- **Bảo mật**: 
  * Mã hóa dữ liệu, xác thực 2FA
  * Face Recognition cho mobile app
  * Encrypted local storage
- **Sẵn sàng**: 99.5% uptime
- **Khả năng mở rộng**: Hỗ trợ thêm chi nhánh
- **Giao diện**: Responsive, thân thiện người dùng

## 5. CÔNG NGHỆ SỬ DỤNG

### 5.1 Backend
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (hỗ trợ JSON, full-text search, recursive CTE cho tree structure)
- **ORM**: Spring Data JPA + Hibernate
- **Security**: Spring Security + JWT
- **API Documentation**: Swagger/OpenAPI
- **File Storage**: MinIO/AWS S3
- **Search Engine**: Elasticsearch (cho tìm kiếm sản phẩm nâng cao, category search)
- **Cache**: Redis (session, cart, frequent data, category tree cache)
- **Message Queue**: RabbitMQ (async processing, notification)
- **Tree Structure**: PostgreSQL recursive CTE + materialized path cho category hierarchy

### 5.2 Frontend Web
- **Framework**: React.js hoặc Angular
- **UI Library**: Material-UI hoặc Ant Design
- **State Management**: Redux hoặc NgRx
- **Tree Components**: React Tree Select, Ant Design Tree

### 5.3 Mobile App
- **Framework**: Flutter
- **State Management**: Bloc/Provider
- **Local Storage**: SQLite/Hive
- **Authentication**: 
  * Face ID/Face Recognition (iOS/Android)
  * Fingerprint/Touch ID
  * Traditional login (username/password)
- **Voice Technology**:
  * Speech-to-Text: Google Cloud Speech API, FPT.AI
  * Text-to-Speech: Flutter TTS
  * Voice Recognition: speech_to_text package
  * Offline Voice: on-device speech recognition
- **Security**: Biometric authentication với encrypted storage

### 5.4 Infrastructure
- **Deployment**: Docker + Kubernetes
- **CI/CD**: GitHub Actions
- **Monitoring**: Prometheus + Grafana
- **Backup**: Automated DB backup

## 6. KẾ HOẠCH PHÁT TRIỂN

### Phase 1 (2-3 tháng): Core Features
- **Category Management**: Hệ thống phân loại sản phẩm đa cấp (tree structure)
- Quản lý sản phẩm cơ bản với category assignment
- **Product Combo System**: Tạo combo, thay thế linh kiện cơ bản
- **Vehicle Management**: Đăng ký thông tin xe khách hàng
- **Price Negotiation**: Thương lượng giá với validation và approval workflow
- Quản lý nhà cung cấp
- **Inquiry Management**: Tạo danh sách inquiry, theo dõi phản hồi NCC
- **Note System**: Hệ thống ghi chú cơ bản (personal notes, quick notes)
- **Delivery Management**: Quản lý giao hàng cơ bản (xe ôm, xe khách)
- Bán hàng đơn giản
- Quản lý kho cơ bản (nhập thực tế với giá flexibility)

### Phase 2 (2 tháng): Advanced Features
- **Advanced Category Features**: Drag & drop, bulk operations, category analytics
- **Substitution History**: Lưu trữ và theo dõi lịch sử thay thế linh kiện
- **Smart Recommendations**: Gợi ý thông minh dựa trên lịch sử khách hàng và xe
- **Voice-to-Order**: Tạo đơn hàng bằng giọng nói (mobile app)
- **Advanced Discount Analytics**: Phân tích hiệu quả discount, customer behavior
- Đa nguồn cung cho sản phẩm
- **Advanced Notes**: Rich text editor, voice notes, tags, reminder
- Báo cáo chi tiết theo danh mục
- Mobile app cơ bản với category tree navigation
- Tích hợp thanh toán

### Phase 3 (1-2 tháng): Integration & AI/ML Features
- **AI-powered Recommendations**: Machine learning cho gợi ý sản phẩm
- **Predictive Analytics**: Dự báo nhu cầu thay thế dựa trên usage pattern
- **Performance Analytics**: Phân tích hiệu quả của các loại thay thế
- Tích hợp hóa đơn điện tử
- API với nhà cung cấp
- Tối ưu hiệu năng
- Testing & deployment

## 7. RISK & MITIGATION

### 7.1 Rủi ro kỹ thuật
- **Phức tạp đa nguồn cung**: Thiết kế database carefully
- **Tree structure performance**: Implement caching, materialized path
- **Substitution data complexity**: Chuẩn bị schema linh hoạt
- **Hiệu năng với big data**: Implement caching, pagination
- **Data consistency**: Transaction management

### 7.2 Rủi ro nghiệp vụ
- **Data privacy**: Bảo mật thông tin xe và lịch sử khách hàng
- **Recommendation accuracy**: Cần thời gian để tích lũy đủ data
- **User adoption**: Training và support cho tính năng phức tạp
- **Data migration**: Careful planning và testing

## 2.12 Hệ thống Tạo đơn hàng bằng Giọng nói (Voice-to-Order)

### 2.12.1 Nhu cầu sử dụng giọng nói trong bán hàng
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Tình huống thực tế cần voice input**:
   - **Nhân viên bận tay**: Đang kiểm tra hàng trong kho
   - **Khách hàng gọi điện**: Đặt hàng qua phone, cần ghi nhanh
   - **Di động nhiều**: Nhân viên đi giao hàng, khách gọi đặt thêm
   - **Tốc độ**: Nói nhanh hơn gõ, đặc biệt với tên sản phẩm phức tạp

2. **Workflow thực tế**:
   ```
   Khách gọi: "Anh ơi, em cần má phanh Hino J05E, 2 cái, 
             và dầu phanh Shell 1 thùng, giao cho garage Minh Tâm"
   
   Nhân viên: [Bấm nút mic] "Tạo đơn hàng cho garage Minh Tâm,
             má phanh Hino J05E 2 cái, dầu phanh Shell 1 thùng"
   
   Hệ thống: ✅ Đã tạo đơn hàng #12345
             📍 Khách hàng: Garage Minh Tâm
             📦 2x Má phanh Hino J05E
             📦 1x Dầu phanh Shell 1L
             💰 Tổng tiền: 1,250,000đ
   ```

3. **Thách thức kỹ thuật**:
   - **Tên sản phẩm phức tạp**: "Gasket nắp quy lát Hino J05E-TK"
   - **Số lượng và đơn vị**: "2 cái", "1 thùng", "3 bộ"
   - **Accent và giọng địa phương**: Nhân viên miền Nam, Bắc, Trung
   - **Nhiễu môi trường**: Tiếng ồn kho hàng, xe cộ

### 2.12.2 Thiết kế Voice Recognition System

1. **Speech-to-Text Engine**:
   ```javascript
   // Sử dụng multiple engines cho accuracy cao
   const voiceEngines = [
     'Google Cloud Speech-to-Text', // Primary - tốt cho tiếng Việt
     'FPT.AI Speech', // Backup - hiểu accent Việt Nam tốt
     'Azure Speech Services' // Fallback
   ];
   
   // Custom vocabulary cho ngành phụ tùng
   const customVocabulary = [
     'Hino J05E', 'Hyundai HD120', 'Thaco Ollin',
     'má phanh', 'đĩa phanh', 'gasket', 'piston',
     'garage', 'thùng', 'bộ', 'cái', 'chiếc'
   ];
   ```

2. **Intent Recognition và NLP**:
   ```javascript
   // Phân tích ý định từ speech
   const intentPatterns = {
     createOrder: [
       'tạo đơn hàng', 'đặt hàng', 'order mới',
       'khách đặt', 'cần giao'
     ],
     addProduct: [
       'thêm', 'cần thêm', 'và', 'cộng thêm'
     ],
     setQuantity: [
       /(\d+)\s*(cái|chiếc|bộ|thùng|lít)/g
     ],
     setCustomer: [
       'cho khách', 'garage', 'anh', 'chị', 'công ty'
     ]
   };
   
   // Example parsing
   function parseVoiceCommand(transcript) {
     const result = {
       action: 'create_order',
       customer: extractCustomer(transcript),
       items: extractItems(transcript),
       delivery: extractDeliveryInfo(transcript)
     };
     return result;
   }
   ```

### 2.12.3 Workflow Voice-to-Order

1. **Step-by-step Process**:
   ```javascript
   // 1. Voice Input
   "Tạo đơn hàng cho garage Minh Tâm, má phanh Hino 2 cái"
   
   // 2. Speech Recognition
   {
     transcript: "tạo đơn hàng cho garage minh tâm má phanh hino j05e hai cái",
     confidence: 0.92
   }
   
   // 3. Intent & Entity Extraction
   {
     intent: "create_order",
     entities: {
       customer: "garage Minh Tâm",
       products: [
         {
           name: "má phanh Hino J05E",
           quantity: 2,
           unit: "cái"
         }
       ]
     }
   }
   
   // 4. Product Matching & Validation
   {
     matched_products: [
       {
         id: 1234,
         name: "Má phanh trước Hino J05E",
         price: 450000,
         stock: 10
       }
     ],
     ambiguous: [], // Cần clarify
     not_found: [] // Không tìm thấy
   }
   
   // 5. Order Creation & Confirmation
   ```

2. **Voice Feedback System**:
   ```javascript
   // Text-to-Speech responses
   const voiceResponses = {
     orderCreated: "Đã tạo đơn hàng số {orderNumber} cho {customer}",
     productAdded: "Đã thêm {quantity} {product} vào đơn hàng",
     needClarification: "Không tìm thấy sản phẩm {product}. Bạn có thể nói rõ hơn không?",
     priceConfirm: "Tổng đơn hàng {total} đồng. Xác nhận tạo đơn không?"
   };
   ```

### 2.12.4 Mobile App Voice Interface

1. **Voice Command UI**:
   ```dart
   // Flutter Voice Interface
   class VoiceOrderScreen extends StatefulWidget {
     @override
     Widget build(BuildContext context) {
       return Scaffold(
         body: Column(
           children: [
             // Voice visualizer
             VoiceWaveformWidget(),
             
             // Quick commands
             VoiceCommandChips([
               "Tạo đơn hàng mới",
               "Thêm sản phẩm", 
               "Gọi khách hàng",
               "Kiểm tra tồn kho"
             ]),
             
             // Order preview
             VoiceOrderPreview(),
             
             // Voice controls
             VoiceControlButtons()
           ]
         )
       );
     }
   }
   ```

2. **Gesture & Voice Combination**:
   ```dart
   // Combine voice với gestures
   GestureDetector(
     onLongPress: () => startVoiceRecording(),
     onTap: () => stopAndProcess(),
     child: FloatingActionButton(
       child: Icon(_isRecording ? Icons.mic : Icons.mic_none),
       backgroundColor: _isRecording ? Colors.red : Colors.blue,
     )
   )
   ```

### 2.12.5 Advanced Voice Features

1. **Contextual Understanding**:
   ```javascript
   // Hiểu context conversation
   const conversationContext = {
     currentOrder: orderId,
     lastCustomer: "Garage Minh Tâm",
     recentProducts: ["má phanh Hino", "dầu phanh"],
     
     // Smart defaults
     applyContext: (command) => {
       if (command.includes("thêm") && !command.includes("cho")) {
         // "thêm 2 cái" -> thêm vào đơn hiện tại
         return {
           action: "add_to_current_order",
           orderId: this.currentOrder
         };
       }
     }
   };
   ```

2. **Multi-language Support**:
   ```javascript
   // Hỗ trợ đa ngôn ngữ trong cùng 1 command
   const mixedLanguagePatterns = [
     'má phanh brake pad Hino', // Việt + English
     'dầu engine oil Shell',    // Brand names
     'gasket nắp quy lát'       // Technical terms
   ];
   ```

3. **Voice Shortcuts**:
   ```javascript
   // Shortcuts cho commands thường dùng
   const voicePermissions = {
     sales_staff: ['create_order', 'check_stock', 'call_customer'],
     manager: ['create_order', 'check_stock', 'view_reports', 'manage_pricing'],
     warehouse: ['check_stock', 'update_inventory']
   };
   ```

### 2.12.6 Error Handling & Recovery

1. **Confidence Threshold**:
   ```javascript
   if (recognition.confidence < 0.7) {
     // Low confidence - ask for repeat
     speakResponse("Xin lỗi, tôi không nghe rõ. Bạn có thể nói lại không?");
     return;
   }
   
   if (recognition.confidence < 0.85) {
     // Medium confidence - confirm
     speakResponse(`Bạn muốn ${interpretation}. Đúng không?`);
     waitForConfirmation();
   }
   ```

2. **Fallback Mechanisms**:
   ```javascript
   // Nếu voice fail, fallback to text
   const errorHandlers = {
     noMicrophone: () => showTextInput(),
     noiseEnvironment: () => switchToTypingMode(),
     networkError: () => useOfflineRecognition(),
     ambiguousProduct: () => showProductSelector()
   };
   ```

### 2.12.7 Privacy & Security

1. **Voice Data Protection**:
   - **Local processing**: Speech recognition on-device khi có thể
   - **No storage**: Không lưu voice recordings
   - **Encrypted transmission**: Mã hóa khi gửi lên server
   - **Consent**: Xin phép trước khi sử dụng microphone

2. **Access Control**:
   ```javascript
   // Phân quyền voice commands
   const voicePermissions = {
     sales_staff: ['create_order', 'check_stock', 'call_customer'],
     manager: ['create_order', 'check_stock', 'view_reports', 'manage_pricing'],
     warehouse: ['check_stock', 'update_inventory']
   };
   ```

## 2.13 Hệ thống Thương lượng Giá và Giảm giá Linh hoạt

### 2.13.1 Nhu cầu thương lượng giá thực tế
**⚠️ LƯU Ý QUAN TRỌNG KHI CODE:**

1. **Tình huống thực tế**:
   - **Sản phẩm có giá niêm yết**: 1,600,000đ
   - **Khách hàng cò kèo**: "Anh bán 1,550,000đ được không?"
   - **Nhân viên cần quyết định**: Có nên giảm giá để bán không?
   - **Lưu lại lý do**: Tại sao giảm giá và giảm bao nhiêu

2. **Các loại discount thực tế**:
   - **Negotiation discount**: Thương lượng với khách
   - **Volume discount**: Mua nhiều được giảm giá
   - **Loyalty discount**: Khách VIP, khách quen
   - **Clearance discount**: Thanh lý hàng tồn kho
   - **Damage discount**: Hàng có khuyết tật nhỏ
   - **Competition discount**: Cạnh tranh với đối thủ

3. **Business rules cần kiểm soát**:
   - **Discount limit**: Nhân viên chỉ được giảm tối đa bao nhiêu %
   - **Minimum profit**: Không được bán dưới giá vốn + margin tối thiểu
   - **Approval workflow**: Discount lớn cần approval từ manager
   - **Tracking**: Lưu lại lịch sử discount để phân tích

### 2.13.2 Thiết kế Discount Management System

1. **Database Schema cho Discount**:
   ```sql
   discount_rules (
     id BIGINT PRIMARY KEY,
     name VARCHAR(100), -- "Thương lượng khách hàng", "Khuyến mãi VIP"
     type VARCHAR(50), -- 'NEGOTIATION', 'VOLUME', 'LOYALTY', 'CLEARANCE'
     discount_type VARCHAR(20), -- 'PERCENTAGE', 'FIXED_AMOUNT'
     max_discount_percent DECIMAL(5,2), -- Tối đa 15%
     max_discount_amount DECIMAL(15,2), -- Tối đa 500,000đ
     min_profit_margin DECIMAL(5,2), -- Ít nhất 10% lợi nhuận
     requires_approval BOOLEAN, -- Cần approval không
     applicable_roles JSON, -- ['sales_staff', 'manager']
     is_active BOOLEAN DEFAULT TRUE,
     created_at TIMESTAMP
   );
   
   order_discounts (
     id BIGINT PRIMARY KEY,
     order_id BIGINT REFERENCES orders(id),
     product_id BIGINT REFERENCES products(id),
     discount_rule_id BIGINT REFERENCES discount_rules(id),
     
     -- Thông tin discount
     original_price DECIMAL(15,2), -- 1,600,000
     discounted_price DECIMAL(15,2), -- 1,550,000
     discount_amount DECIMAL(15,2), -- 50,000
     discount_percent DECIMAL(5,2), -- 3.13%
     
     -- Lý do và approval
     reason TEXT, -- "Khách hàng cò kèo, cạnh tranh với shop khác"
     applied_by BIGINT REFERENCES users(id),
     approved_by BIGINT REFERENCES users(id),
     approval_status VARCHAR(20), -- 'PENDING', 'APPROVED', 'REJECTED'
     
     -- Profit analysis
     cost_price DECIMAL(15,2), -- Giá vốn
     profit_amount DECIMAL(15,2), -- Lợi nhuận sau discount
     profit_margin DECIMAL(5,2), -- % lợi nhuận
     
     created_at TIMESTAMP,
     approved_at TIMESTAMP
   );
   ```

2. **Discount Validation Logic**:
   ```java
   @Service
   public class DiscountValidationService {
     
     public DiscountValidationResult validateDiscount(
         Long productId, 
         BigDecimal originalPrice, 
         BigDecimal requestedPrice,
         String reason,
         User user
     ) {
       Product product = productService.findById(productId);
       BigDecimal costPrice = product.getCostPrice();
       BigDecimal discountAmount = originalPrice.subtract(requestedPrice);
       BigDecimal discountPercent = discountAmount.divide(originalPrice).multiply(BigDecimal.valueOf(100));
       
       // 1. Kiểm tra profit margin
       BigDecimal profitAmount = requestedPrice.subtract(costPrice);
       BigDecimal profitMargin = profitAmount.divide(requestedPrice).multiply(BigDecimal.valueOf(100));
       
       if (profitMargin.compareTo(MIN_PROFIT_MARGIN) < 0) {
         return DiscountValidationResult.error("Không thể bán dưới mức lợi nhuận tối thiểu " + MIN_PROFIT_MARGIN + "%");
       }
       
       // 2. Kiểm tra quyền hạn của user
       DiscountRule rule = getApplicableDiscountRule(user.getRole(), "NEGOTIATION");
       if (discountPercent.compareTo(rule.getMaxDiscountPercent()) > 0) {
         return DiscountValidationResult.requiresApproval("Discount " + discountPercent + "% vượt quá quyền hạn. Cần approval từ manager.");
       }
       
       return DiscountValidationResult.success();
     }
   }
   ```

### 3.11 Module Quản lý Giảm giá và Thương lượng
- **Discount Rules Management**
  - **Thiết lập quy tắc giảm giá**: Phần trăm tối đa, số tiền tối đa
  - **Role-based permissions**: Nhân viên vs Manager discount limits
  - **Profit protection**: Không cho phép bán dưới mức lợi nhuận tối thiểu
  - **Product-specific rules**: Quy tắc riêng cho từng loại sản phẩm

- **Real-time Price Negotiation**
  - **Price validation**: Kiểm tra giá đề nghị có hợp lệ không
  - **Profit calculation**: Tính toán lợi nhuận còn lại sau discount
  - **Smart suggestions**: Gợi ý mức giá phù hợp dựa trên lịch sử
  - **Competitor pricing**: So sánh với giá thị trường

- **Approval Workflow**
  - **Manager approval**: Request approval cho discount lớn
  - **Multi-level approval**: Approval hierarchy cho discount value cao
  - **Mobile notifications**: Thông báo approval request cho manager
  - **Quick approve**: Manager có thể approve nhanh trên mobile

- **Discount Analytics & Insights**
  - **Discount effectiveness**: Phân tích hiệu quả của discount
  - **Customer behavior**: Pattern thương lượng của từng khách hàng
  - **Profit impact analysis**: Impact của discount lên profit margin
  - **Sales conversion**: Tỷ lệ thành công khi có discount

- **Voice-enabled Negotiation**
  - **Voice discount commands**: "Giảm giá còn 1 triệu 5"
  - **Voice approval requests**: "Xin phép manager giảm giá"
  - **Voice feedback**: Hệ thống đọc lại thông tin discount
  - **Smart voice parsing**: Hiểu các cách nói về giá và discount khác nhau