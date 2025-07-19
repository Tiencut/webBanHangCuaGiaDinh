package com.giadinh.banphutung.web_banphutung.web_ban_hang_gia_dinh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductBundle;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long> {
    
    // Tìm tất cả sản phẩm con của một sản phẩm cha
    List<ProductBundle> findByParentProductOrderBySortOrderAsc(Product parentProduct);
    
    // Tìm tất cả sản phẩm con của một sản phẩm cha theo ID
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.parentProduct.id = :parentId ORDER BY pb.sortOrder ASC")
    List<ProductBundle> findByParentProductId(@Param("parentId") Long parentId);
    
    // Tìm tất cả sản phẩm cha của một sản phẩm con
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.childProduct.id = :childId")
    List<ProductBundle> findByChildProductId(@Param("childId") Long childId);
    
    // Tìm bundle theo sản phẩm cha và con
    Optional<ProductBundle> findByParentProductAndChildProduct(Product parentProduct, Product childProduct);
    
    // Tìm bundle theo sản phẩm cha và con (theo ID)
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.parentProduct.id = :parentId AND pb.childProduct.id = :childId")
    Optional<ProductBundle> findByParentProductIdAndChildProductId(@Param("parentId") Long parentId, @Param("childId") Long childId);
    
    // Tìm tất cả sản phẩm combo (có sản phẩm con)
    @Query("SELECT DISTINCT pb.parentProduct FROM ProductBundle pb WHERE pb.status = 'ACTIVE'")
    List<Product> findAllComboProducts();
    
    // Tìm sản phẩm con có thể thay thế
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.isSubstitutable = true AND pb.status = 'ACTIVE'")
    List<ProductBundle> findSubstitutableBundles();
    
    // Tìm sản phẩm con theo nhóm tương thích
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.compatibilityGroup = :group AND pb.status = 'ACTIVE'")
    List<ProductBundle> findByCompatibilityGroup(@Param("group") String group);
    
    // Tìm sản phẩm con có thể thay thế cho một sản phẩm cụ thể
    @Query("SELECT pb FROM ProductBundle pb WHERE pb.childProduct.id = :childId AND pb.isSubstitutable = true AND pb.status = 'ACTIVE'")
    List<ProductBundle> findSubstitutesForProduct(@Param("childId") Long childId);
    
    // Đếm số lượng sản phẩm con trong một combo
    @Query("SELECT COUNT(pb) FROM ProductBundle pb WHERE pb.parentProduct.id = :parentId AND pb.status = 'ACTIVE'")
    Long countByParentProductId(@Param("parentId") Long parentId);
    
    // Kiểm tra xem một sản phẩm có phải là combo không
    @Query("SELECT CASE WHEN COUNT(pb) > 0 THEN true ELSE false END FROM ProductBundle pb WHERE pb.parentProduct.id = :productId")
    boolean isComboProduct(@Param("productId") Long productId);
    
    // Tìm tất cả sản phẩm con có thể thay thế cho một sản phẩm cụ thể
    @Query("SELECT p FROM Product p WHERE p.id IN " +
           "(SELECT DISTINCT ps.substituteProduct.id FROM ProductBundle pb " +
           "JOIN pb.substituteProducts ps WHERE pb.childProduct.id = :childId)")
    List<Product> findSubstituteProductsForChild(@Param("childId") Long childId);
} 