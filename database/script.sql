-- ===========================================
-- SCRIPT TẠO DATABASE CHO HỆ THỐNG BÁN HÀNG
-- ===========================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS `web_ban_hang_gia_dinh` 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `web_ban_hang_gia_dinh`;

-- ===========================================
-- 1. BẢNG USERS (Người dùng)
-- ===========================================
CREATE TABLE `users` (
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
CREATE TABLE `categories` (
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
CREATE TABLE `suppliers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `contact_person` VARCHAR(100),
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `address` TEXT,
    `tax_code` VARCHAR(20),
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- 4. BẢNG PRODUCTS (Sản phẩm)
-- ===========================================
CREATE TABLE `products` (
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
CREATE TABLE `inventory` (
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
CREATE TABLE `customers` (
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
CREATE TABLE `orders` (
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
CREATE TABLE `order_items` (
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
CREATE TABLE `purchase_orders` (
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
CREATE TABLE `purchase_order_items` (
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
CREATE TABLE `inventory_transactions` (
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
CREATE TABLE `delivery_partners` (
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
CREATE TABLE `vehicle_types` (
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
-- INDEXES để tối ưu performance
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