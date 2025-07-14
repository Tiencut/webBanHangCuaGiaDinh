package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * VehicleModel - Entity đại diện cho mẫu xe
 * 
 * Đây là entity quan trọng nhất trong hệ thống phụ tùng xe tải.
 * Mỗi mẫu xe sẽ có những thông số cố định và danh sách phụ tùng tương thích.
 * 
 * Ví dụ:
 * - Thaco Ollin 500 (2018-2020) -> Động cơ Isuzu 4JB1, hộp số 5 cấp
 * - Hyundai HD72 (2015-2019) -> Động cơ D4BB, hộp số 6 cấp
 * - Hino 300 (2020-2023) -> Động cơ N04C, hộp số Allison
 * 
 * Khi khách hàng nói tên xe, hệ thống sẽ gợi ý ngay các phụ tùng phù hợp.
 */
@Entity
@Table(name = "vehicle_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleModel extends BaseEntity {

    /**
     * Mã mẫu xe (unique)
     * Ví dụ: OLLIN500, HD72, HINO300
     */
    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "Mã mẫu xe không được để trống")
    private String code;

    /**
     * Tên mẫu xe đầy đủ
     * Ví dụ: "Thaco Ollin 500", "Hyundai HD72", "Hino 300"
     */
    @Column(nullable = false, length = 200)
    @NotBlank(message = "Tên mẫu xe không được để trống")
    private String name;

    /**
     * Model xe (alias cho name)
     */
    @Column(length = 200)
    private String model;

    /**
     * Thương hiệu xe
     * Ví dụ: THACO, HYUNDAI, HINO, ISUZU, DONGFENG
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Thương hiệu không được để trống")
    private String brand;

    /**
     * Năm bắt đầu sản xuất
     */
    @NotNull(message = "Năm bắt đầu sản xuất không được để trống")
    private Integer yearFrom;

    /**
     * Năm kết thúc sản xuất (null nếu vẫn còn sản xuất)
     */
    private Integer yearTo;

    /**
     * Năm sản xuất (alias cho yearFrom)
     */
    @Column(name = "production_year")
    private Integer productionYear;

    /**
     * Loại xe
     */
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    /**
     * Loại động cơ
     */
    @Column(length = 100)
    private String engineType;

    /**
     * Loại hộp số
     */
    @Column(length = 100)
    private String transmissionType;

    /**
     * Loại nhiên liệu
     */
    @Column(length = 50)
    private String fuelType;

    /**
     * Mô tả chi tiết
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Tải trọng (tấn)
     * Ví dụ: 2.5, 5.0, 8.0
     */
    private Double tonnage;

    /**
     * Động cơ chính
     * Ví dụ: "Isuzu 4JB1", "Hyundai D4BB", "Hino N04C"
     */
    @Column(length = 200)
    private String engineModel;

    /**
     * Phân khối động cơ (cc)
     */
    private Integer engineDisplacement;

    /**
     * Công suất động cơ (HP)
     */
    private Integer enginePower;

    /**
     * Hộp số
     * Ví dụ: "5 cấp số", "6 cấp số", "Tự động Allison"
     */
    @Column(length = 100)
    private String transmission;

    /**
     * Kích thước bánh xe
     * Ví dụ: "7.00-16", "8.25-20", "10.00-20"
     */
    @Column(length = 50)
    private String tireSize;

    /**
     * Chiều dài thùng xe (mm)
     */
    private Integer cargoLength;

    /**
     * Chiều rộng thùng xe (mm)
     */
    private Integer cargoWidth;

    /**
     * Chiều cao thùng xe (mm)
     */
    private Integer cargoHeight;

    /**
     * Xuất xứ
     * Ví dụ: "Lắp ráp trong nước", "Nhập khẩu nguyên chiếc"
     */
    @Column(length = 100)
    private String origin;

    /**
     * Ghi chú kỹ thuật
     */
    @Column(columnDefinition = "TEXT")
    private String technicalNotes;

    /**
     * Các tên gọi khác (để tìm kiếm)
     * Ví dụ: "Ollin 500, Ollin5, OL500"
     */
    @Column(columnDefinition = "TEXT")
    private String alternativeNames;

    /**
     * Trạng thái sản xuất
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductionStatus productionStatus = ProductionStatus.IN_PRODUCTION;

    /**
     * Có đang kinh doanh phụ tùng không
     */
    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * Danh sách phụ tùng tương thích với mẫu xe này
     * Đây là quan hệ Many-to-Many với Product
     */
    @ManyToMany(mappedBy = "compatibleVehicles", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "compatibleVehicles"})
    private List<Product> compatibleProducts = new ArrayList<>();

    /**
     * Enum loại xe
     */
    public enum VehicleType {
        LIGHT_TRUCK("Xe tải nhẹ"),           // Dưới 3.5 tấn
        MEDIUM_TRUCK("Xe tải trung"),        // 3.5 - 10 tấn  
        HEAVY_TRUCK("Xe tải nặng"),          // Trên 10 tấn
        PICKUP("Xe bán tải"),                // Pick-up
        VAN("Xe van"),                       // Xe van chở hàng
        BUS("Xe khách"),                     // Xe buýt, xe khách
        SPECIALIZED("Xe chuyên dụng");       // Xe cứu hỏa, xe rác, xe trộn bêtông

        private final String displayName;

        VehicleType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Enum trạng thái sản xuất
     */
    public enum ProductionStatus {
        IN_PRODUCTION("Đang sản xuất"),
        DISCONTINUED("Ngừng sản xuất"),
        PLANNED("Dự kiến sản xuất");

        private final String displayName;

        ProductionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Utility methods
     */
    
    /**
     * Lấy năm sản xuất dưới dạng chuỗi
     * Ví dụ: "2018-2020", "2020-nay"
     */
    public String getProductionYearRange() {
        if (yearTo == null) {
            return yearFrom + "-nay";
        }
        return yearFrom + "-" + yearTo;
    }

    /**
     * Kiểm tra xe có còn sản xuất không
     */
    public boolean isCurrentlyInProduction() {
        return productionStatus == ProductionStatus.IN_PRODUCTION && 
               (yearTo == null || yearTo >= java.time.Year.now().getValue());
    }

    /**
     * Lấy tên đầy đủ kèm năm sản xuất
     * Ví dụ: "Thaco Ollin 500 (2018-2020)"
     */
    public String getFullDisplayName() {
        return name + " (" + getProductionYearRange() + ")";
    }
}
