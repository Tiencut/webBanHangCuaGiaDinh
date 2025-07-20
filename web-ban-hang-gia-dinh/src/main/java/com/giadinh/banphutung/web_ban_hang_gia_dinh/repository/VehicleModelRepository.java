package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel.ProductionStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel.VehicleType;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    
    // Tìm theo mã xe
    Optional<VehicleModel> findByCode(String code);
    
    // Tìm theo tên xe (không phân biệt hoa thường)
    List<VehicleModel> findByNameContainingIgnoreCase(String name);
    
    // Tìm theo thương hiệu
    List<VehicleModel> findByBrandIgnoreCase(String brand);
    
    // Tìm theo loại xe
    List<VehicleModel> findByVehicleType(VehicleType vehicleType);
    
    // Tìm xe active
    List<VehicleModel> findByIsActiveTrueOrderByBrandAscNameAsc();
    
    // Tìm xe đang sản xuất
    List<VehicleModel> findByProductionStatus(ProductionStatus status);
    
    // Tìm xe theo năm sản xuất
    @Query("SELECT v FROM VehicleModel v WHERE v.yearFrom <= :year AND (v.yearTo IS NULL OR v.yearTo >= :year)")
    List<VehicleModel> findByProductionYear(@Param("year") int year);
    
    // Tìm xe theo tải trọng
    List<VehicleModel> findByTonnageBetween(Double minTonnage, Double maxTonnage);
    
    // Tìm xe theo động cơ
    @Query("SELECT v FROM VehicleModel v WHERE LOWER(v.engineModel) LIKE LOWER(CONCAT('%', :engine, '%'))")
    List<VehicleModel> findByEngineContaining(@Param("engine") String engine);
    
    /**
     * Tìm kiếm vehicle model theo keyword
     */
    @Query("SELECT vm FROM VehicleModel vm WHERE vm.isActive = true AND (LOWER(vm.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(vm.model) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(vm.year) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(vm.engine) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<VehicleModel> searchVehicleModels(@Param("keyword") String keyword);
    
    // Kiểm tra code đã tồn tại chưa
    boolean existsByCode(String code);
    
    // Đếm xe theo brand
    Long countByBrand(String brand);
    
    // Đếm xe theo vehicle type
    Long countByVehicleType(VehicleType vehicleType);
    
    // === Các method bổ sung cho VehicleModelService ===
    
    /**
     * Tìm vehicle model theo brand và active
     */
    List<VehicleModel> findByBrandContainingIgnoreCaseAndIsActiveTrue(String brand);
    
    /**
     * Tìm vehicle model theo year và active
     */
    List<VehicleModel> findByYearAndIsActiveTrue(String year);
    
    /**
     * Tìm vehicle model theo engine và active
     */
    List<VehicleModel> findByEngineContainingIgnoreCaseAndIsActiveTrue(String engine);
    
    /**
     * Tìm vehicle model theo fuel type và active
     */
    List<VehicleModel> findByFuelTypeAndIsActiveTrue(String fuelType);
    
    /**
     * Tìm vehicle model theo body type và active
     */
    List<VehicleModel> findByBodyTypeAndIsActiveTrue(String bodyType);
    
    /**
     * Lấy tất cả brand
     */
    @Query("SELECT DISTINCT vm.brand FROM VehicleModel vm WHERE vm.isActive = true ORDER BY vm.brand")
    List<String> findAllBrands();
    
    /**
     * Lấy tất cả year
     */
    @Query("SELECT DISTINCT vm.year FROM VehicleModel vm WHERE vm.isActive = true ORDER BY vm.year DESC")
    List<String> findAllYears();
    
    /**
     * Lấy tất cả fuel type
     */
    @Query("SELECT DISTINCT vm.fuelType FROM VehicleModel vm WHERE vm.isActive = true ORDER BY vm.fuelType")
    List<String> findAllFuelTypes();
    
    /**
     * Lấy tất cả body type
     */
    @Query("SELECT DISTINCT vm.bodyType FROM VehicleModel vm WHERE vm.isActive = true ORDER BY vm.bodyType")
    List<String> findAllBodyTypes();
    
    // Tìm tất cả xe đang active
    List<VehicleModel> findByIsActiveTrue();
    
    // Tìm xe đang active với phân trang
    Page<VehicleModel> findByIsActiveTrue(Pageable pageable);
    
    // Tìm xe theo id và active
    Optional<VehicleModel> findByIdAndIsActiveTrue(Long id);
    
    // Tìm xe theo code và active
    Optional<VehicleModel> findByCodeAndIsActiveTrue(String code);
    
    // Tìm xe đang active theo sort order
    List<VehicleModel> findByIsActiveTrueOrderBySortOrderAsc();
}
