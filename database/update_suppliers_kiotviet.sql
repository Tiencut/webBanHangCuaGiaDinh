-- ===========================================
-- SCRIPT CẬP NHẬT SUPPLIERS TABLE - KIOTVIET COMPATIBILITY
-- ===========================================

USE `web_ban_hang_gia_dinh`;

-- Thêm các cột tương thích với KiotViet
ALTER TABLE `suppliers` 
ADD COLUMN `region` VARCHAR(100) COMMENT 'Khu vực',
ADD COLUMN `ward` VARCHAR(100) COMMENT 'Phường/Xã', 
ADD COLUMN `supplier_group` VARCHAR(100) COMMENT 'Nhóm nhà cung cấp',
ADD COLUMN `total_purchased` DECIMAL(19,2) DEFAULT 0 COMMENT 'Tổng mua',
ADD COLUMN `current_debt` DECIMAL(19,2) DEFAULT 0 COMMENT 'Nợ cần trả hiện tại';

-- Cập nhật dữ liệu mẫu với thông tin KiotViet
UPDATE `suppliers` SET 
    `region` = 'TP.HCM',
    `ward` = 'Phường Bến Nghé',
    `supplier_group` = 'Nhà cung cấp chính',
    `total_purchased` = 0,
    `current_debt` = 0
WHERE `id` = 1;


UPDATE `suppliers` SET 
    `region` = 'Hà Nội', 
    `ward` = 'Phường Hàng Bài',
    `supplier_group` = 'Nhà cung cấp chính',
    `total_purchased` = 0,
    `current_debt` = 0
WHERE `id` = 2;

UPDATE `suppliers` SET 
    `region` = 'TP.HCM',
    `ward` = 'Phường Tân Định', 
    `supplier_group` = 'Nhà cung cấp dầu nhớt',
    `total_purchased` = 0,
    `current_debt` = 0
WHERE `id` = 3;

UPDATE `suppliers` SET 
    `region` = 'Hà Nội',
    `ward` = 'Phường Cầu Giấy',
    `supplier_group` = 'Nhà cung cấp lốp xe', 
    `total_purchased` = 0,
    `current_debt` = 0
WHERE `id` = 4;

-- Tạo index cho các trường mới
CREATE INDEX idx_suppliers_region ON suppliers(region);
CREATE INDEX idx_suppliers_supplier_group ON suppliers(supplier_group);
CREATE INDEX idx_suppliers_total_purchased ON suppliers(total_purchased);
CREATE INDEX idx_suppliers_current_debt ON suppliers(current_debt);

-- Kiểm tra cấu trúc bảng sau khi cập nhật
DESCRIBE `suppliers`;

-- Kiểm tra dữ liệu
SELECT 
    name,
    code,
    region,
    ward,
    supplier_group,
    total_purchased,
    current_debt
FROM suppliers 
ORDER BY id;

SHOW INDEX FROM suppliers;
