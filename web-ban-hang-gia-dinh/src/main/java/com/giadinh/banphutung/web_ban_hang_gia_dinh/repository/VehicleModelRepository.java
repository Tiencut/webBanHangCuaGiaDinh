package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

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
    
    // Tìm kiếm tổng hợp (tên, thương hiệu, tên gọi khác)
    @Query("SELECT v FROM VehicleModel v WHERE " +
           "LOWER(v.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.alternativeNames) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<VehicleModel> searchVehicles(@Param("keyword") String keyword);
    
    // Tìm xe có sản phẩm tương thích
    @Query("SELECT DISTINCT v FROM VehicleModel v JOIN v.compatibleProducts p WHERE p.id = :productId")
    List<VehicleModel> findCompatibleWithProduct(@Param("productId") Long productId);
    
    // Tìm xe phổ biến (có nhiều sản phẩm tương thích)
    @Query("SELECT v, COUNT(p) FROM VehicleModel v LEFT JOIN v.compatibleProducts p " +
           "WHERE v.isActive = true GROUP BY v ORDER BY COUNT(p) DESC")
    List<Object[]> findPopularVehicles();
    
    // Tìm xe mới nhất
    @Query("SELECT v FROM VehicleModel v WHERE v.isActive = true ORDER BY v.yearFrom DESC")
    List<VehicleModel> findLatestVehicles();
    
    // Đếm xe theo thương hiệu
    @Query("SELECT v.brand, COUNT(v) FROM VehicleModel v WHERE v.isActive = true GROUP BY v.brand")
    List<Object[]> countVehiclesByBrand();
    
    // Kiểm tra mã xe đã tồn tại
    boolean existsByCode(String code);
    
    // Kiểm tra tên xe đã tồn tại
    boolean existsByNameIgnoreCase(String name);
}
