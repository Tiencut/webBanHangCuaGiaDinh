-- ===========================================
-- SCRIPT KHỞI TẠO & DỮ LIỆU MẪU HỆ THỐNG BÁN HÀNG
-- Gồm: Tạo bảng, cập nhật cấu trúc, insert dữ liệu mẫu
-- ===========================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS `web_ban_hang_gia_dinh` 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `web_ban_hang_gia_dinh`;

-- ===========================================
-- 1. TẠO CÁC BẢNG CHÍNH
-- ===========================================

-- USERS
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `full_name` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20),
    `role` ENUM('ADMIN', 'EMPLOYEE', 'CUSTOMER') DEFAULT 'CUSTOMER',
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 2. BẢNG CATEGORIES (Danh mục)
-- ===========================================
CREATE TABLE IF NOT EXISTS `categories` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `parent_id` BIGINT,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`parent_id`) REFERENCES `categories`(`id`)
);

-- ===========================================
-- 3. BẢNG SUPPLIERS (Nhà cung cấp)
-- ===========================================
CREATE TABLE IF NOT EXISTS `suppliers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(200) NOT NULL,
    `code` VARCHAR(50) UNIQUE,
    `contact_person` VARCHAR(100),
    `phone` VARCHAR(15),
    `email` VARCHAR(100),
    `address` VARCHAR(500),
    `city` VARCHAR(100) COMMENT 'Thành phố/Tỉnh',
    `district` VARCHAR(100) COMMENT 'Quận/Huyện',
    `ward` VARCHAR(100) COMMENT 'Phường/Xã',
    `region` VARCHAR(100) COMMENT 'Khu vực/Vùng miền',
    `tax_code` VARCHAR(20),
    `bank_account` VARCHAR(50),
    `bank_name` VARCHAR(100),
    `credit_limit` DECIMAL(19,2) DEFAULT 0,
    `delivery_time_days` INTEGER DEFAULT 0,
    `rating` DECIMAL(3,2) DEFAULT 0.0,
    `notes` TEXT,
    `supplier_group` VARCHAR(100) COMMENT 'Nhóm nhà cung cấp',
    `total_purchased` DECIMAL(19,2) DEFAULT 0 COMMENT 'Tổng tiền đã mua',
    `current_debt` DECIMAL(19,2) DEFAULT 0 COMMENT 'Công nợ hiện tại',
    `last_transaction_date` DATE COMMENT 'Ngày giao dịch cuối cùng',
    `payment_terms` ENUM('CASH', 'NET_7', 'NET_15', 'NET_30', 'NET_45', 'NET_60') DEFAULT 'CASH',
    `status` ENUM('ACTIVE', 'INACTIVE', 'BLACKLISTED') DEFAULT 'ACTIVE',
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 4. BẢNG PRODUCTS (Sản phẩm)
-- ===========================================
CREATE TABLE IF NOT EXISTS `products` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `sku` VARCHAR(50) UNIQUE,
    `barcode` VARCHAR(50),
    `category_id` BIGINT,
    `unit` VARCHAR(20) DEFAULT 'cái',
    `cost_price` DECIMAL(12,2) DEFAULT 0,
    `selling_price` DECIMAL(12,2) DEFAULT 0,
    `min_stock` INTEGER DEFAULT 0,
    `max_stock` INTEGER DEFAULT 0,
    `weight` DECIMAL(8,2),
    `dimensions` VARCHAR(50),
    `image_url` VARCHAR(255),
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`)
);

-- ===========================================
-- 5. BẢNG INVENTORY (Tồn kho)
-- ===========================================
CREATE TABLE IF NOT EXISTS `inventory` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `supplier_id` BIGINT,
    `quantity` INTEGER NOT NULL DEFAULT 0,
    `reserved_quantity` INTEGER DEFAULT 0,
    `cost_price` DECIMAL(12,2),
    `batch_number` VARCHAR(50),
    `expiry_date` DATE,
    `location` VARCHAR(100),
    `status` ENUM('AVAILABLE', 'RESERVED', 'DAMAGED', 'EXPIRED', 'RETURNED') DEFAULT 'AVAILABLE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`),
    FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`)
);

-- ===========================================
-- 6. BẢNG CUSTOMERS (Khách hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS `customers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `address` TEXT,
    `tax_code` VARCHAR(20),
    `customer_type` ENUM('INDIVIDUAL', 'BUSINESS') DEFAULT 'INDIVIDUAL',
    `credit_limit` DECIMAL(12,2) DEFAULT 0,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 7. BẢNG ORDERS (Đơn hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_number` VARCHAR(50) NOT NULL UNIQUE,
    `customer_id` BIGINT,
    `user_id` BIGINT,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `status` ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    `total_amount` DECIMAL(12,2) NOT NULL,
    `discount_amount` DECIMAL(12,2) DEFAULT 0,
    `tax_amount` DECIMAL(12,2) DEFAULT 0,
    `shipping_amount` DECIMAL(12,2) DEFAULT 0,
    `payment_method` ENUM('CASH', 'CARD', 'TRANSFER', 'CREDIT') DEFAULT 'CASH',
    `payment_status` ENUM('UNPAID', 'PARTIAL', 'PAID', 'REFUNDED') DEFAULT 'UNPAID',
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`customer_id`) REFERENCES `customers`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

-- ===========================================
-- 8. BẢNG ORDER_ITEMS (Chi tiết đơn hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INTEGER NOT NULL,
    `unit_price` DECIMAL(12,2) NOT NULL,
    `total_price` DECIMAL(12,2) NOT NULL,
    `discount_amount` DECIMAL(12,2) DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)
);

-- ===========================================
-- 9. BẢNG PURCHASE_ORDERS (Đơn nhập hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS `purchase_orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `po_number` VARCHAR(50) NOT NULL UNIQUE,
    `supplier_id` BIGINT NOT NULL,
    `user_id` BIGINT,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `expected_date` DATE,
    `received_date` DATE,
    `status` ENUM('PENDING', 'CONFIRMED', 'PARTIAL_RECEIVED', 'RECEIVED', 'CANCELLED') DEFAULT 'PENDING',
    `total_amount` DECIMAL(12,2) NOT NULL,
    `discount_amount` DECIMAL(12,2) DEFAULT 0,
    `tax_amount` DECIMAL(12,2) DEFAULT 0,
    `shipping_amount` DECIMAL(12,2) DEFAULT 0,
    `payment_status` ENUM('UNPAID', 'PARTIAL', 'PAID') DEFAULT 'UNPAID',
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

-- ===========================================
-- 10. BẢNG PURCHASE_ORDER_ITEMS (Chi tiết đơn nhập)
-- ===========================================
CREATE TABLE IF NOT EXISTS `purchase_order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `purchase_order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity_ordered` INTEGER NOT NULL,
    `quantity_received` INTEGER DEFAULT 0,
    `unit_cost` DECIMAL(12,2) NOT NULL,
    `total_cost` DECIMAL(12,2) NOT NULL,
    `discount_amount` DECIMAL(12,2) DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)
);

-- ===========================================
-- 11. BẢNG INVENTORY_TRANSACTIONS (Giao dịch kho)
-- ===========================================
CREATE TABLE IF NOT EXISTS `inventory_transactions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `transaction_type` ENUM('IN', 'OUT', 'ADJUSTMENT', 'TRANSFER') NOT NULL,
    `quantity` INTEGER NOT NULL,
    `unit_cost` DECIMAL(12,2),
    `reference_type` ENUM('PURCHASE', 'SALE', 'ADJUSTMENT', 'RETURN', 'TRANSFER') NOT NULL,
    `reference_id` BIGINT,
    `location_from` VARCHAR(100),
    `location_to` VARCHAR(100),
    `batch_number` VARCHAR(50),
    `expiry_date` DATE,
    `user_id` BIGINT,
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

-- ===========================================
-- 12. BẢNG DELIVERY_PARTNERS (Đối tác giao hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS `delivery_partners` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `contact_person` VARCHAR(100),
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `address` TEXT,
    `service_area` TEXT,
    `pricing_model` ENUM('FLAT_RATE', 'DISTANCE_BASED', 'WEIGHT_BASED', 'CUSTOM') DEFAULT 'FLAT_RATE',
    `base_rate` DECIMAL(10,2) DEFAULT 0,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 13. BẢNG VEHICLE_TYPES (Loại xe)
-- ===========================================
CREATE TABLE IF NOT EXISTS `vehicle_types` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `max_weight` DECIMAL(8,2),
    `max_volume` DECIMAL(8,2),
    `base_rate` DECIMAL(10,2) DEFAULT 0,
    `rate_per_km` DECIMAL(10,2) DEFAULT 0,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 2. INDEXES
-- ===========================================

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_inventory_product ON inventory(product_id);
CREATE INDEX idx_inventory_supplier ON inventory(supplier_id);
CREATE INDEX idx_inventory_status ON inventory(status);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_purchase_orders_supplier ON purchase_orders(supplier_id);
CREATE INDEX idx_purchase_orders_status ON purchase_orders(status);
CREATE INDEX idx_inventory_transactions_product ON inventory_transactions(product_id);
CREATE INDEX idx_inventory_transactions_type ON inventory_transactions(transaction_type);
CREATE INDEX idx_inventory_transactions_date ON inventory_transactions(created_at);
CREATE INDEX idx_suppliers_code ON suppliers(code);
CREATE INDEX idx_suppliers_phone ON suppliers(phone);
CREATE INDEX idx_suppliers_email ON suppliers(email);
CREATE INDEX idx_suppliers_city ON suppliers(city);
CREATE INDEX idx_suppliers_district ON suppliers(district);
CREATE INDEX idx_suppliers_status ON suppliers(status);
CREATE INDEX idx_suppliers_region ON suppliers(region);
CREATE INDEX idx_suppliers_supplier_group ON suppliers(supplier_group);
CREATE INDEX idx_suppliers_active ON suppliers(is_active);
CREATE INDEX idx_suppliers_last_transaction ON suppliers(last_transaction_date);
CREATE INDEX idx_suppliers_total_purchased ON suppliers(total_purchased);
CREATE INDEX idx_suppliers_current_debt ON suppliers(current_debt);

-- ===========================================
-- 3. DỮ LIỆU MẪU
-- ===========================================


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
('Công ty TNHH Phụ tùng ABC', 'SUP001', 'Nguyễn Văn A', '0901111111', 
    'contact@phutungabc.com', '123 Nguyễn Huệ', 'TP.HCM', 'Quận 1', 'Phường Bến Nghé', 'Miền Nam',
    '0123456789', '123456789012', 'Vietcombank', 50000000, 3, 4.5, 
    'Nhà cung cấp chính, chất lượng tốt', 'Nhà cung cấp chính', 15000000, 500000, '2024-12-15',
    'NET_15', 'ACTIVE', TRUE),
('Công ty Cổ phần Dầu nhớt XYZ', 'SUP002', 'Trần Thị B', '0902222222', 
    'info@dauxyz.com', '456 Lê Lợi', 'Hà Nội', 'Quận Hai Bà Trưng', 'Phường Hàng Bài', 'Miền Bắc',
    '0987654321', '987654321098', 'Techcombank', 30000000, 2, 4.2, 
    'Chuyên cung cấp dầu nhớt', 'Nhà cung cấp chính', 22000000, 1200000, '2024-12-20',
    'NET_30', 'ACTIVE', TRUE),
('Cửa hàng Lốp xe DEF', 'SUP003', 'Lê Văn C', '0903333333', 
    'sales@lopxedef.com', '789 Cách Mạng Tháng 8', 'TP.HCM', 'Quận 3', 'Phường Tân Định', 'Miền Nam',
    '0147258369', '147258369014', 'ACB', 20000000, 1, 4.0, 
    'Chuyên lốp xe các loại', 'Nhà cung cấp lốp xe', 8500000, 0, '2024-12-10',
    'CASH', 'ACTIVE', TRUE),
('Nhà phân phối Phụ tùng GHI', 'SUP004', 'Phạm Thị D', '0904444444', 
    'order@phutungghi.com', '321 Cầu Giấy', 'Hà Nội', 'Quận Cầu Giấy', 'Phường Cầu Giấy', 'Miền Bắc',
    '0369147258', '369147258036', 'VietinBank', 40000000, 5, 3.8, 
    'Phụ tùng đa dạng', 'Nhà cung cấp phụ tùng', 12500000, 750000, '2024-12-05',
    'NET_45', 'ACTIVE', TRUE),
('Công ty Phụ tùng Miền Trung', 'SUP005', 'Võ Văn E', '0905555555',
    'info@phutungmientrung.com', '654 Hùng Vương', 'Đà Nẵng', 'Quận Hải Châu', 'Phường Hải Châu 1', 'Miền Trung',
    '0258147963', '258147963025', 'Sacombank', 25000000, 4, 4.1,
    'Phụ tùng xe máy, ô tô', 'Nhà cung cấp miền Trung', 18000000, 300000, '2024-12-18',
    'NET_30', 'ACTIVE', TRUE);

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

