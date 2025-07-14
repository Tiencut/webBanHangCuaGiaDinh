package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * CustomerVehicle - Entity quản lý thông tin xe của khách hàng
 * 
 * Mỗi khách hàng có thể có nhiều xe khác nhau
 * Thông tin xe được sử dụng để:
 * - Gợi ý phụ tùng phù hợp
 * - Theo dõi lịch sử thay thế
 * - Dự báo nhu cầu bảo dưỡng
 * 
 * Ví dụ:
 * - Khách A có 3 xe:
 *   + Biển số 51A-12345: Hino 300 (2019)
 *   + Biển số 51B-67890: Hyundai HD72 (2017)
 *   + Biển số 51C-11111: Thaco Ollin 500 (2020)
 */
@Entity
@Table(name = "customer_vehicles")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerVehicle extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    /**
     * Biển số xe
     */
    @NotBlank
    @Column(name = "license_plate", unique = true)
    private String licensePlate;
    
    /**
     * Mẫu xe (liên kết với VehicleModel)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_model_id")
    private VehicleModel vehicleModel;
    
    /**
     * Năm sản xuất xe
     */
    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;
    
    /**
     * Mục đích sử dụng xe
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type")
    private VehicleUsageType usageType = VehicleUsageType.CITY_DELIVERY;
    
    /**
     * Số km đã chạy
     */
    @Column(name = "mileage")
    private Integer mileage = 0;
    
    /**
     * Ngày mua xe
     */
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    /**
     * Ngày đăng ký xe
     */
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    /**
     * Ngày hết hạn đăng kiểm
     */
    @Column(name = "inspection_expiry_date")
    private LocalDate inspectionExpiryDate;
    
    /**
     * Ngày hết hạn bảo hiểm
     */
    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;
    
    /**
     * Màu xe
     */
    @Column(name = "color")
    private String color;
    
    /**
     * Số khung xe (VIN)
     */
    @Column(name = "vin_number")
    private String vinNumber;
    
    /**
     * Số máy xe
     */
    @Column(name = "engine_number")
    private String engineNumber;
    
    /**
     * Tình trạng xe
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private VehicleCondition condition = VehicleCondition.GOOD;
    
    /**
     * Ghi chú về xe
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Có đang sử dụng không
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    /**
     * Ngày bảo dưỡng cuối cùng
     */
    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;
    
    /**
     * Số km bảo dưỡng tiếp theo
     */
    @Column(name = "next_maintenance_mileage")
    private Integer nextMaintenanceMileage;
    
    /**
     * Tần suất bảo dưỡng (km)
     */
    @Column(name = "maintenance_interval")
    private Integer maintenanceInterval = 10000;
    
    /**
     * Số km bảo dưỡng cuối cùng
     */
    @Column(name = "last_maintenance_mileage")
    private Integer lastMaintenanceMileage;
    
    /**
     * Tuổi xe (tính toán)
     */
    @Transient
    private Integer vehicleAge;
    
    /**
     * Enum mục đích sử dụng xe
     */
    public enum VehicleUsageType {
        CITY_DELIVERY("Chạy thành phố"),
        LONG_DISTANCE("Chạy đường dài"),
        MOUNTAIN_ROAD("Chạy núi đồi"),
        CONSTRUCTION("Công trình"),
        AGRICULTURE("Nông nghiệp"),
        WASTE_COLLECTION("Thu gom rác"),
        CONCRETE_MIXER("Trộn bê tông"),
        CRANE("Cần cẩu"),
        OTHER("Khác");

        private final String displayName;

        VehicleUsageType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum tình trạng xe
     */
    public enum VehicleCondition {
        EXCELLENT("Xuất sắc"),
        GOOD("Tốt"),
        FAIR("Khá"),
        POOR("Kém"),
        NEEDS_REPAIR("Cần sửa chữa");

        private final String displayName;

        VehicleCondition(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Tính tuổi xe
     */
    public Integer getVehicleAge() {
        if (manufacturingYear == null) {
            return null;
        }
        return LocalDate.now().getYear() - manufacturingYear;
    }
    
    /**
     * Kiểm tra có cần bảo dưỡng không
     */
    public boolean needsMaintenance() {
        return mileage >= nextMaintenanceMileage;
    }
    
    /**
     * Tính số km còn lại đến bảo dưỡng
     */
    public Integer getMileageUntilMaintenance() {
        if (nextMaintenanceMileage == null) {
            return null;
        }
        return Math.max(0, nextMaintenanceMileage - mileage);
    }
    
    /**
     * Kiểm tra đăng kiểm có hết hạn không
     */
    public boolean isInspectionExpired() {
        return inspectionExpiryDate != null && 
               inspectionExpiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * Kiểm tra bảo hiểm có hết hạn không
     */
    public boolean isInsuranceExpired() {
        return insuranceExpiryDate != null && 
               insuranceExpiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * Cập nhật km sau khi bảo dưỡng
     */
    public void updateAfterMaintenance(Integer newMileage) {
        this.mileage = newMileage;
        this.lastMaintenanceDate = LocalDateTime.now();
        this.lastMaintenanceMileage = newMileage;
        this.nextMaintenanceMileage = newMileage + maintenanceInterval;
    }
    
    /**
     * Set tuổi xe
     */
    public void setVehicleAge(Integer vehicleAge) {
        this.vehicleAge = vehicleAge;
    }
    
    /**
     * Set số km bảo dưỡng cuối cùng
     */
    public void setLastMaintenanceMileage(Integer lastMaintenanceMileage) {
        this.lastMaintenanceMileage = lastMaintenanceMileage;
    }
} 