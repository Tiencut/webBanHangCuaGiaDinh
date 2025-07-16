-- ===========================================
-- SCRIPT KHỞI TẠO & DỮ LIỆU MẪU HỆ THỐNG BÁN HÀNG
-- Tương thích với PostgreSQL/Neon
-- ===========================================

-- ===========================================
-- 1. TẠO CÁC BẢNG CHÍNH
-- ===========================================

-- USERS
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- 2. BẢNG CATEGORIES (Danh mục)
-- ===========================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- ===========================================
-- 3. BẢNG SUPPLIERS (Nhà cung cấp)
-- ===========================================
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) UNIQUE,
    contact_person VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address VARCHAR(500),
    city VARCHAR(100),
    district VARCHAR(100),
    ward VARCHAR(100),
    region VARCHAR(100),
    tax_code VARCHAR(20),
    bank_account VARCHAR(50),
    bank_name VARCHAR(100),
    payment_terms VARCHAR(20) DEFAULT 'CASH',
    credit_limit DECIMAL(19,2) DEFAULT 0,
    delivery_time_days INTEGER DEFAULT 0,
    vehicle_brands VARCHAR(255),
    rating DOUBLE PRECISION DEFAULT 0.0,
    notes TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    supplier_group VARCHAR(100),
    total_purchased DECIMAL(19,2) DEFAULT 0,
    current_debt DECIMAL(19,2) DEFAULT 0,
    last_transaction_date DATE
);

-- ===========================================
-- 4. BẢNG PRODUCTS (Sản phẩm)
-- ===========================================
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description VARCHAR(1000),
    category_id BIGINT,
    base_price DECIMAL(19,2) DEFAULT 0,
    selling_price DECIMAL(19,2) DEFAULT 0,
    cost_price DECIMAL(19,2) DEFAULT 0,
    weight DOUBLE PRECISION,
    dimensions VARCHAR(50),
    brand VARCHAR(100),
    model VARCHAR(100),
    vehicle_type VARCHAR(100),
    part_number VARCHAR(100),
    oem_number VARCHAR(100),
    warranty_period INTEGER,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    min_stock_level INTEGER DEFAULT 0,
    reorder_point INTEGER DEFAULT 10,
    is_combo BOOLEAN DEFAULT FALSE,
    is_substitutable BOOLEAN DEFAULT FALSE,
    image_url VARCHAR(255),
    unit VARCHAR(20) DEFAULT 'cái',
    sku VARCHAR(50) UNIQUE,
    barcode VARCHAR(50),
    min_stock INTEGER DEFAULT 0,
    max_stock INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- ===========================================
-- 5. BẢNG INVENTORY (Tồn kho)
-- ===========================================
CREATE TABLE IF NOT EXISTS inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    supplier_id BIGINT,
    quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    cost_price DECIMAL(12,2),
    batch_number VARCHAR(50),
    expiry_date DATE,
    location VARCHAR(100),
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- ===========================================
-- 6. BẢNG CUSTOMERS (Khách hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) UNIQUE,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(100),
    district VARCHAR(100),
    tax_code VARCHAR(20),
    company_name VARCHAR(200),
    contact_person VARCHAR(100),
    customer_type VARCHAR(20) DEFAULT 'RETAIL',
    credit_limit DECIMAL(19,2) DEFAULT 0,
    current_debt DECIMAL(19,2) DEFAULT 0,
    discount_percentage DOUBLE PRECISION DEFAULT 0.0,
    loyalty_points INTEGER DEFAULT 0,
    first_purchase_date DATE,
    last_purchase_date DATE,
    total_orders INTEGER DEFAULT 0,
    total_spent DECIMAL(19,2) DEFAULT 0,
    preferred_vehicle_brands VARCHAR(255),
    notes TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- 7. BẢNG ORDERS (Đơn hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT,
    user_id BIGINT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    shipping_amount DECIMAL(12,2) DEFAULT 0,
    payment_method VARCHAR(20) DEFAULT 'CASH',
    payment_status VARCHAR(20) DEFAULT 'UNPAID',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- 8. BẢNG ORDER_ITEMS (Chi tiết đơn hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ===========================================
-- 9. BẢNG PURCHASE_ORDERS (Đơn nhập hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGSERIAL PRIMARY KEY,
    po_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id BIGINT NOT NULL,
    user_id BIGINT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expected_date DATE,
    received_date DATE,
    status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    shipping_amount DECIMAL(12,2) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'UNPAID',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- 10. BẢNG PURCHASE_ORDER_ITEMS (Chi tiết đơn nhập)
-- ===========================================
CREATE TABLE IF NOT EXISTS purchase_order_items (
    id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_ordered INTEGER NOT NULL,
    quantity_received INTEGER DEFAULT 0,
    unit_cost DECIMAL(12,2) NOT NULL,
    total_cost DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ===========================================
-- 11. BẢNG INVENTORY_TRANSACTIONS (Giao dịch kho)
-- ===========================================
CREATE TABLE IF NOT EXISTS inventory_transactions (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_cost DECIMAL(12,2),
    reference_type VARCHAR(20) NOT NULL,
    reference_id BIGINT,
    location_from VARCHAR(100),
    location_to VARCHAR(100),
    batch_number VARCHAR(50),
    expiry_date DATE,
    user_id BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- 12. BẢNG DELIVERY_PARTNERS (Đối tác giao hàng)
-- ===========================================
CREATE TABLE IF NOT EXISTS delivery_partners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    service_area TEXT,
    pricing_model VARCHAR(20) DEFAULT 'FLAT_RATE',
    base_rate DECIMAL(10,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- 13. BẢNG VEHICLE_TYPES (Loại xe)
-- ===========================================
CREATE TABLE IF NOT EXISTS vehicle_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    max_weight DECIMAL(8,2),
    max_volume DECIMAL(8,2),
    base_rate DECIMAL(10,2) DEFAULT 0,
    rate_per_km DECIMAL(10,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);