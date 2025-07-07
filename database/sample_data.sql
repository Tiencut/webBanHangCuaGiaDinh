-- ===========================================
-- SCRIPT DỮ LIỆU MẪU CHO HỆ THỐNG BÁN HÀNG
-- ===========================================

USE `web_ban_hang_gia_dinh`;

-- ===========================================
-- 1. DỮ LIỆU MẪU CHO USERS
-- ===========================================
INSERT INTO `users` (`username`, `password`, `email`, `full_name`, `phone`, `role`, `is_active`) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@giadinh.com', 'Quản trị viên', '0901234567', 'ADMIN', TRUE),
('employee1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'employee1@giadinh.com', 'Nhân viên 1', '0901234568', 'EMPLOYEE', TRUE),
('customer1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'customer1@gmail.com', 'Khách hàng 1', '0901234569', 'CUSTOMER', TRUE);

-- ===========================================
-- 2. DỮ LIỆU MẪU CHO CATEGORIES
-- ===========================================
INSERT INTO `categories` (`name`, `description`, `parent_id`, `is_active`) VALUES
('Phụ tùng xe máy', 'Các phụ tùng dành cho xe máy', NULL, TRUE),
('Dầu nhớt', 'Dầu nhớt các loại', NULL, TRUE),
('Lốp xe', 'Lốp xe máy, xe hơi', NULL, TRUE),
('Phanh xe', 'Má phanh, dầu phanh', 1, TRUE),
('Lọc dầu', 'Lọc dầu engine', 1, TRUE),
('Dầu nhớt xe máy', 'Dầu nhớt cho xe máy', 2, TRUE),
('Dầu nhớt ô tô', 'Dầu nhớt cho ô tô', 2, TRUE);

-- ===========================================
-- 3. DỮ LIỆU MẪU CHO SUPPLIERS (bao gồm các trường KiotViet)
-- ===========================================
INSERT INTO `suppliers` (
    `name`, `code`, `contact_person`, `phone`, `email`, `address`, `city`, `district`, `ward`, `region`,
    `tax_code`, `bank_account`, `bank_name`, `credit_limit`, `delivery_time_days`, `rating`, 
    `notes`, `supplier_group`, `total_purchased`, `current_debt`, `last_transaction_date`,
    `payment_terms`, `status`, `is_active`
) VALUES
(
    'Công ty TNHH Phụ tùng ABC', 'SUP001', 'Nguyễn Văn A', '0901111111', 
    'contact@phutungabc.com', '123 Nguyễn Huệ', 'TP.HCM', 'Quận 1', 'Phường Bến Nghé', 'Miền Nam',
    '0123456789', '123456789012', 'Vietcombank', 50000000, 3, 4.5, 
    'Nhà cung cấp chính, chất lượng tốt', 'Nhà cung cấp chính', 15000000, 500000, '2024-12-15',
    'NET_15', 'ACTIVE', TRUE
),
(
    'Công ty Cổ phần Dầu nhớt XYZ', 'SUP002', 'Trần Thị B', '0902222222', 
    'info@dauxyz.com', '456 Lê Lợi', 'Hà Nội', 'Quận Hai Bà Trưng', 'Phường Hàng Bài', 'Miền Bắc',
    '0987654321', '987654321098', 'Techcombank', 30000000, 2, 4.2, 
    'Chuyên cung cấp dầu nhớt', 'Nhà cung cấp chính', 22000000, 1200000, '2024-12-20',
    'NET_30', 'ACTIVE', TRUE
),
(
    'Cửa hàng Lốp xe DEF', 'SUP003', 'Lê Văn C', '0903333333', 
    'sales@lopxedef.com', '789 Cách Mạng Tháng 8', 'TP.HCM', 'Quận 3', 'Phường Tân Định', 'Miền Nam',
    '0147258369', '147258369014', 'ACB', 20000000, 1, 4.0, 
    'Chuyên lốp xe các loại', 'Nhà cung cấp lốp xe', 8500000, 0, '2024-12-10',
    'CASH', 'ACTIVE', TRUE
),
(
    'Nhà phân phối Phụ tùng GHI', 'SUP004', 'Phạm Thị D', '0904444444', 
    'order@phutungghi.com', '321 Cầu Giấy', 'Hà Nội', 'Quận Cầu Giấy', 'Phường Cầu Giấy', 'Miền Bắc',
    '0369147258', '369147258036', 'VietinBank', 40000000, 5, 3.8, 
    'Phụ tùng đa dạng', 'Nhà cung cấp phụ tùng', 12500000, 750000, '2024-12-05',
    'NET_45', 'ACTIVE', TRUE
),
(
    'Công ty Phụ tùng Miền Trung', 'SUP005', 'Võ Văn E', '0905555555',
    'info@phutungmientrung.com', '654 Hùng Vương', 'Đà Nẵng', 'Quận Hải Châu', 'Phường Hải Châu 1', 'Miền Trung',
    '0258147963', '258147963025', 'Sacombank', 25000000, 4, 4.1,
    'Phụ tùng xe máy, ô tô', 'Nhà cung cấp miền Trung', 18000000, 300000, '2024-12-18',
    'NET_30', 'ACTIVE', TRUE
);

-- ===========================================
-- 4. DỮ LIỆU MẪU CHO PRODUCTS
-- ===========================================
INSERT INTO `products` (
    `name`, `description`, `sku`, `barcode`, `category_id`, `unit`, 
    `cost_price`, `selling_price`, `min_stock`, `max_stock`, `weight`, 
    `dimensions`, `is_active`
) VALUES
('Má phanh trước Honda Wave', 'Má phanh chính hãng cho Honda Wave', 'BPF-HW-001', '1234567890123', 4, 'cặp', 45000, 65000, 5, 50, 0.3, '10x8x2 cm', TRUE),
('Lọc dầu Yamaha Jupiter', 'Lọc dầu chính hãng cho Yamaha Jupiter', 'OF-YJ-001', '1234567890124', 5, 'cái', 25000, 38000, 10, 100, 0.2, '8x8x6 cm', TRUE),
('Dầu nhớt Castrol 4T 10W-40', 'Dầu nhớt cao cấp cho xe máy 4 thì', 'OIL-CAS-4T-1L', '1234567890125', 6, 'chai', 85000, 120000, 20, 200, 1.0, '25x8x8 cm', TRUE),
('Lốp Michelin City Grip 80/90-14', 'Lốp xe tay ga chính hãng', 'TIRE-MCH-CG-80', '1234567890126', 3, 'cái', 450000, 650000, 2, 20, 3.5, '80x80x14 cm', TRUE),
('Dầu phanh DOT 4 500ml', 'Dầu phanh chất lượng cao', 'BF-DOT4-500', '1234567890127', 4, 'chai', 35000, 50000, 15, 150, 0.5, '20x6x6 cm', TRUE);

-- ===========================================
-- 5. DỮ LIỆU MẪU CHO INVENTORY
-- ===========================================
INSERT INTO `inventory` (
    `product_id`, `supplier_id`, `quantity`, `reserved_quantity`, 
    `cost_price`, `location`, `batch_number`, `expiry_date`, `status`
) VALUES
(1, 1, 25, 0, 45000, 'KHO-A-01', 'BATCH001', '2025-12-31', 'AVAILABLE'),
(2, 1, 80, 5, 25000, 'KHO-A-02', 'BATCH002', '2025-12-31', 'AVAILABLE'),
(3, 2, 150, 10, 85000, 'KHO-B-01', 'BATCH003', '2025-06-30', 'AVAILABLE'),
(4, 3, 8, 0, 450000, 'KHO-C-01', 'BATCH004', '2026-12-31', 'AVAILABLE'),
(5, 4, 120, 5, 35000, 'KHO-A-03', 'BATCH005', '2025-12-31', 'AVAILABLE');

-- ===========================================
-- 6. DỮ LIỆU MẪU CHO CUSTOMERS
-- ===========================================
INSERT INTO `customers` (
    `name`, `code`, `phone`, `email`, `address`, `city`, `district`, 
    `customer_type`, `credit_limit`, `discount_rate`, `is_active`
) VALUES
('Nguyễn Văn Khách', 'KH001', '0911111111', 'khach1@gmail.com', '123 Lê Văn Việt, Quận 9', 'TP.HCM', 'Quận 9', 'RETAIL', 5000000, 0.05, TRUE),
('Garage Minh Tuấn', 'KH002', '0922222222', 'garage.minhtuan@gmail.com', '456 Quang Trung, Quận Gò Vấp', 'TP.HCM', 'Quận Gò Vấp', 'WHOLESALE', 20000000, 0.10, TRUE),
('Cửa hàng Phụ tùng Hùng', 'KH003', '0933333333', 'phutung.hung@gmail.com', '789 Hoàng Văn Thụ, Quận Tân Bình', 'TP.HCM', 'Quận Tân Bình', 'DEALER', 50000000, 0.15, TRUE);
