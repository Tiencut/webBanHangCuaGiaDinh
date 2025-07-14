package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.CustomerVehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * CustomerVehicleRepository - Repository cho quản lý xe khách hàng
 * 
 * Cung cấp các query methods để:
 * - Tìm xe của khách hàng
 * - Tìm xe theo mẫu, hãng, năm sản xuất
 * - Quản lý bảo dưỡng và đăng kiểm
 * - Gợi ý phụ tùng phù hợp
 */
@Repository
public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicle, Long> {
    
    /**
     * Tìm tất cả xe của một khách hàng
     */
    List<CustomerVehicle> findByCustomerIdOrderByManufacturingYearDesc(Long customerId);
    
    /**
     * Tìm xe đang hoạt động của một khách hàng
     */
    List<CustomerVehicle> findByCustomerIdAndIsActiveTrueOrderByManufacturingYearDesc(Long customerId);
    
    /**
     * Tìm xe theo biển số
     */
    Optional<CustomerVehicle> findByLicensePlate(String licensePlate);
    
    /**
     * Tìm xe theo biển số (ignore case)
     */
    Optional<CustomerVehicle> findByLicensePlateIgnoreCase(String licensePlate);
    
    /**
     * Tìm xe theo biển số (fuzzy search)
     */
    List<CustomerVehicle> findByLicensePlateContainingIgnoreCase(String licensePlate);
    
    /**
     * Tìm xe theo mẫu xe
     */
    List<CustomerVehicle> findByVehicleModelId(Long vehicleModelId);
    
    /**
     * Tìm xe theo hãng xe
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.brand = :brand " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByBrand(@Param("brand") String brand);
    
    /**
     * Tìm xe theo năm sản xuất
     */
    List<CustomerVehicle> findByManufacturingYear(Integer manufacturingYear);
    
    /**
     * Tìm xe theo khoảng năm sản xuất
     */
    List<CustomerVehicle> findByManufacturingYearBetween(Integer fromYear, Integer toYear);
    
    /**
     * Tìm xe theo loại sử dụng
     */
    List<CustomerVehicle> findByUsageType(CustomerVehicle.VehicleUsageType usageType);
    
    /**
     * Tìm xe theo tình trạng
     */
    List<CustomerVehicle> findByCondition(CustomerVehicle.VehicleCondition condition);
    
    /**
     * Tìm xe cần bảo dưỡng (mileage >= next_maintenance_mileage)
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.mileage >= cv.nextMaintenanceMileage " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findVehiclesNeedingMaintenance();
    
    /**
     * Tìm xe có đăng kiểm sắp hết hạn
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.inspectionExpiryDate <= :expiryDate " +
           "AND cv.isActive = true ORDER BY cv.inspectionExpiryDate ASC")
    List<CustomerVehicle> findVehiclesWithExpiringInspection(@Param("expiryDate") LocalDate expiryDate);
    
    /**
     * Tìm xe có bảo hiểm sắp hết hạn
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.insuranceExpiryDate <= :expiryDate " +
           "AND cv.isActive = true ORDER BY cv.insuranceExpiryDate ASC")
    List<CustomerVehicle> findVehiclesWithExpiringInsurance(@Param("expiryDate") LocalDate expiryDate);
    
    /**
     * Tìm xe có đăng kiểm đã hết hạn
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.inspectionExpiryDate < CURRENT_DATE " +
           "AND cv.isActive = true ORDER BY cv.inspectionExpiryDate ASC")
    List<CustomerVehicle> findVehiclesWithExpiredInspection();
    
    /**
     * Tìm xe có bảo hiểm đã hết hạn
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.insuranceExpiryDate < CURRENT_DATE " +
           "AND cv.isActive = true ORDER BY cv.insuranceExpiryDate ASC")
    List<CustomerVehicle> findVehiclesWithExpiredInsurance();
    
    /**
     * Tìm xe theo số km đã chạy
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.mileage >= :minMileage " +
           "AND cv.isActive = true ORDER BY cv.mileage DESC")
    List<CustomerVehicle> findByMileageGreaterThan(@Param("minMileage") Integer minMileage);
    
    /**
     * Tìm xe theo khoảng km
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.mileage BETWEEN :minMileage AND :maxMileage " +
           "AND cv.isActive = true ORDER BY cv.mileage ASC")
    List<CustomerVehicle> findByMileageBetween(@Param("minMileage") Integer minMileage, 
                                              @Param("maxMileage") Integer maxMileage);
    
    /**
     * Tìm xe theo tuổi xe
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE (YEAR(CURRENT_DATE) - cv.manufacturingYear) >= :minAge " +
           "AND cv.isActive = true ORDER BY cv.manufacturingYear ASC")
    List<CustomerVehicle> findByVehicleAgeGreaterThan(@Param("minAge") Integer minAge);
    
    /**
     * Tìm xe theo tên khách hàng (fuzzy search)
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE LOWER(cv.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByCustomerNameContainingIgnoreCase(@Param("customerName") String customerName);
    
    /**
     * Tìm xe theo tên mẫu xe (fuzzy search)
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE LOWER(cv.vehicleModel.name) LIKE LOWER(CONCAT('%', :modelName, '%')) " +
           "AND cv.isActive = true ORDER BY cv.vehicleModel.name, cv.licensePlate")
    List<CustomerVehicle> findByModelNameContainingIgnoreCase(@Param("modelName") String modelName);
    
    /**
     * Tìm xe theo VIN number
     */
    Optional<CustomerVehicle> findByVinNumber(String vinNumber);
    
    /**
     * Tìm xe theo engine number
     */
    Optional<CustomerVehicle> findByEngineNumber(String engineNumber);
    
    /**
     * Tìm xe theo màu
     */
    List<CustomerVehicle> findByColorIgnoreCase(String color);
    
    /**
     * Tìm xe theo tải trọng
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.tonnage >= :minTonnage " +
           "AND cv.isActive = true ORDER BY cv.vehicleModel.tonnage DESC")
    List<CustomerVehicle> findByTonnageGreaterThan(@Param("minTonnage") Double minTonnage);
    
    /**
     * Tìm xe theo động cơ
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.engineModel = :engineModel " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByEngineModel(@Param("engineModel") String engineModel);
    
    /**
     * Tìm xe theo hộp số
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.transmission = :transmission " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByTransmission(@Param("transmission") String transmission);
    
    /**
     * Tìm xe theo xuất xứ
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.origin = :origin " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByOrigin(@Param("origin") String origin);
    
    /**
     * Tìm xe theo loại xe
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.vehicleType = :vehicleType " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByVehicleType(@Param("vehicleType") String vehicleType);
    
    /**
     * Tìm xe theo trạng thái sản xuất
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.productionStatus = :productionStatus " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByProductionStatus(@Param("productionStatus") String productionStatus);
    
    /**
     * Tìm xe có bảo dưỡng gần đây
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.lastMaintenanceDate IS NOT NULL " +
           "ORDER BY cv.lastMaintenanceDate DESC")
    Page<CustomerVehicle> findRecentlyMaintainedVehicles(Pageable pageable);
    
    /**
     * Tìm xe chưa bao giờ bảo dưỡng
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.lastMaintenanceDate IS NULL " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findVehiclesNeverMaintained();
    
    /**
     * Tìm xe theo tần suất bảo dưỡng
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.maintenanceInterval = :interval " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByMaintenanceInterval(@Param("interval") Integer interval);
    
    /**
     * Tìm xe có km cao nhất
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.mileage > 0 " +
           "ORDER BY cv.mileage DESC")
    Page<CustomerVehicle> findHighestMileageVehicles(Pageable pageable);
    
    /**
     * Tìm xe có tuổi cao nhất
     */
    @Query("SELECT cv FROM CustomerVehicle cv ORDER BY cv.manufacturingYear ASC")
    Page<CustomerVehicle> findOldestVehicles(Pageable pageable);
    
    /**
     * Tìm xe theo customer type
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.customer.customerType = :customerType " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByCustomerType(@Param("customerType") String customerType);
    
    /**
     * Đếm số xe của một khách hàng
     */
    long countByCustomerIdAndIsActiveTrue(Long customerId);
    
    /**
     * Đếm số xe theo mẫu
     */
    long countByVehicleModelIdAndIsActiveTrue(Long vehicleModelId);
    
    /**
     * Đếm số xe theo hãng
     */
    @Query("SELECT COUNT(cv) FROM CustomerVehicle cv WHERE cv.vehicleModel.brand = :brand " +
           "AND cv.isActive = true")
    long countByBrand(@Param("brand") String brand);
    
    /**
     * Đếm số xe cần bảo dưỡng
     */
    @Query("SELECT COUNT(cv) FROM CustomerVehicle cv WHERE cv.mileage >= cv.nextMaintenanceMileage " +
           "AND cv.isActive = true")
    long countVehiclesNeedingMaintenance();
    
    /**
     * Đếm số xe có đăng kiểm sắp hết hạn
     */
    @Query("SELECT COUNT(cv) FROM CustomerVehicle cv WHERE cv.inspectionExpiryDate <= :expiryDate " +
           "AND cv.isActive = true")
    long countVehiclesWithExpiringInspection(@Param("expiryDate") LocalDate expiryDate);
    
    /**
     * Tìm xe theo alternative names
     */
    @Query("SELECT cv FROM CustomerVehicle cv WHERE cv.vehicleModel.alternativeNames LIKE CONCAT('%', :name, '%') " +
           "AND cv.isActive = true ORDER BY cv.customer.name, cv.licensePlate")
    List<CustomerVehicle> findByAlternativeName(@Param("name") String name);
} 