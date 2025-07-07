-- Dữ liệu mẫu cho H2 Database (tự động load khi khởi động)
-- File này sẽ được Spring Boot tự động chạy

-- Insert sample users
INSERT INTO users (username, password, email, full_name, phone, role, is_active, created_at, updated_at) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@giadinh.com', 'Quản trị viên', '0901234567', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('employee1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'employee1@giadinh.com', 'Nhân viên 1', '0901234568', 'EMPLOYEE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample categories
INSERT INTO categories (name, description, parent_id, is_active, created_at, updated_at) VALUES
('Phụ tùng xe máy', 'Các phụ tùng dành cho xe máy', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dầu nhớt', 'Dầu nhớt các loại', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lốp xe', 'Lốp xe máy, xe hơi', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample suppliers (với đầy đủ trường KiotViet)
INSERT INTO suppliers (
    name, code, contact_person, phone, email, address, city, district, ward, region,
    tax_code, bank_account, bank_name, credit_limit, delivery_time_days, rating, 
    notes, supplier_group, total_purchased, current_debt, last_transaction_date,
    payment_terms, status, is_active, created_at, updated_at
) VALUES
(
    'Công ty TNHH Phụ tùng ABC', 'SUP001', 'Nguyễn Văn A', '0901111111', 
    'contact@phutungabc.com', '123 Nguyễn Huệ', 'TP.HCM', 'Quận 1', 'Phường Bến Nghé', 'Miền Nam',
    '0123456789', '123456789012', 'Vietcombank', 50000000, 3, 4.5, 
    'Nhà cung cấp chính, chất lượng tốt', 'Nhà cung cấp chính', 15000000, 500000, '2024-12-15',
    'NET_15', 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'Công ty Cổ phần Dầu nhớt XYZ', 'SUP002', 'Trần Thị B', '0902222222', 
    'info@dauxyz.com', '456 Lê Lợi', 'Hà Nội', 'Quận Hai Bà Trưng', 'Phường Hàng Bài', 'Miền Bắc',
    '0987654321', '987654321098', 'Techcombank', 30000000, 2, 4.2, 
    'Chuyên cung cấp dầu nhớt', 'Nhà cung cấp chính', 22000000, 1200000, '2024-12-20',
    'NET_30', 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'Cửa hàng Lốp xe DEF', 'SUP003', 'Lê Văn C', '0903333333', 
    'sales@lopxedef.com', '789 Cách Mạng Tháng 8', 'TP.HCM', 'Quận 3', 'Phường Tân Định', 'Miền Nam',
    '0147258369', '147258369014', 'ACB', 20000000, 1, 4.0, 
    'Chuyên lốp xe các loại', 'Nhà cung cấp lốp xe', 8500000, 0, '2024-12-10',
    'CASH', 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Insert sample products
INSERT INTO products (
    name, description, sku, barcode, category_id, unit, 
    cost_price, selling_price, min_stock, max_stock, weight, 
    dimensions, is_active, created_at, updated_at
) VALUES
('Má phanh trước Honda Wave', 'Má phanh chính hãng cho Honda Wave', 'BPF-HW-001', '1234567890123', 1, 'cặp', 45000, 65000, 5, 50, 0.3, '10x8x2 cm', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lọc dầu Yamaha Jupiter', 'Lọc dầu chính hãng cho Yamaha Jupiter', 'OF-YJ-001', '1234567890124', 1, 'cái', 25000, 38000, 10, 100, 0.2, '8x8x6 cm', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dầu nhớt Castrol 4T 10W-40', 'Dầu nhớt cao cấp cho xe máy 4 thì', 'OIL-CAS-4T-1L', '1234567890125', 2, 'chai', 85000, 120000, 20, 200, 1.0, '25x8x8 cm', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
