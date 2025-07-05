-- Migration to add inventory table
-- V2__Add_Inventory_Table.sql

CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    supplier_id BIGINT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    cost_price DECIMAL(19,2) NULL,
    batch_number VARCHAR(100) NULL,
    location VARCHAR(255) NULL,
    notes TEXT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    last_updated DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    
    INDEX idx_inventory_product (product_id),
    INDEX idx_inventory_supplier (supplier_id),
    INDEX idx_inventory_status (status),
    INDEX idx_inventory_batch (batch_number),
    INDEX idx_inventory_location (location)
);

-- Insert some sample inventory data for testing
INSERT INTO inventory (product_id, supplier_id, quantity, cost_price, location, status, created_at, updated_at, is_active) 
SELECT 
    p.id, 
    s.id, 
    FLOOR(RAND() * 100) + 10 as quantity,
    p.cost_price,
    CONCAT('Kho ', CHAR(65 + (s.id % 3))) as location,
    'AVAILABLE',
    NOW(),
    NOW(),
    TRUE
FROM products p
CROSS JOIN suppliers s 
WHERE p.is_active = TRUE AND s.is_active = TRUE
LIMIT 20;
