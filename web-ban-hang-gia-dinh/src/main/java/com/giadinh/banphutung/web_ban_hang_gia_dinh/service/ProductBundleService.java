package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductBundle;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductBundleRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductBundleService {
    
    private final ProductBundleRepository productBundleRepository;
    private final ProductRepository productRepository;
    
    /**
     * Tạo bundle mới
     */
    public ProductBundle createBundle(ProductBundle bundle) {
        log.info("Creating new product bundle: {} -> {}", 
                bundle.getParentProduct().getName(), bundle.getChildProduct().getName());
        
        // Validate parent product
        Product parentProduct = productRepository.findById(bundle.getParentProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent product not found"));
        
        // Validate child product
        Product childProduct = productRepository.findById(bundle.getChildProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Child product not found"));
        
        // Check if bundle already exists
        Optional<ProductBundle> existingBundle = productBundleRepository
                .findByParentProductAndChildProduct(parentProduct, childProduct);
        if (existingBundle.isPresent()) {
            throw new BusinessException("Bundle already exists for these products");
        }
        
        // Set default values
        if (bundle.getBundlePrice() == null) {
            bundle.setBundlePrice(childProduct.getActualSellingPrice());
        }
        
        bundle.setParentProduct(parentProduct);
        bundle.setChildProduct(childProduct);
        
        return productBundleRepository.save(bundle);
    }
    
    /**
     * Tìm bundle theo ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductBundle> findById(Long id) {
        return productBundleRepository.findById(id);
    }
    
    /**
     * Tìm tất cả sản phẩm con của một sản phẩm cha
     */
    @Transactional(readOnly = true)
    public List<ProductBundle> findByParentProduct(Long parentId) {
        return productBundleRepository.findByParentProductId(parentId);
    }
    
    /**
     * Tìm tất cả sản phẩm cha của một sản phẩm con
     */
    @Transactional(readOnly = true)
    public List<ProductBundle> findByChildProduct(Long childId) {
        return productBundleRepository.findByChildProductId(childId);
    }
    
    /**
     * Tìm tất cả sản phẩm combo
     */
    @Transactional(readOnly = true)
    public List<Product> findAllComboProducts() {
        return productBundleRepository.findAllComboProducts();
    }
    
    /**
     * Tìm sản phẩm có thể thay thế cho một sản phẩm con
     */
    @Transactional(readOnly = true)
    public List<Product> findSubstituteProducts(Long childId) {
        return productBundleRepository.findSubstituteProductsForChild(childId);
    }
    
    /**
     * Cập nhật bundle
     */
    public ProductBundle updateBundle(Long id, ProductBundle bundleUpdate) {
        ProductBundle existingBundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found"));
        
        // Update fields
        if (bundleUpdate.getQuantity() != null) {
            existingBundle.setQuantity(bundleUpdate.getQuantity());
        }
        if (bundleUpdate.getBundlePrice() != null) {
            existingBundle.setBundlePrice(bundleUpdate.getBundlePrice());
        }
        if (bundleUpdate.getIsSubstitutable() != null) {
            existingBundle.setIsSubstitutable(bundleUpdate.getIsSubstitutable());
        }
        if (bundleUpdate.getSortOrder() != null) {
            existingBundle.setSortOrder(bundleUpdate.getSortOrder());
        }
        if (bundleUpdate.getNotes() != null) {
            existingBundle.setNotes(bundleUpdate.getNotes());
        }
        if (bundleUpdate.getCompatibilityGroup() != null) {
            existingBundle.setCompatibilityGroup(bundleUpdate.getCompatibilityGroup());
        }
        if (bundleUpdate.getStatus() != null) {
            existingBundle.setStatus(bundleUpdate.getStatus());
        }
        
        return productBundleRepository.save(existingBundle);
    }
    
    /**
     * Xóa bundle
     */
    public void deleteBundle(Long id) {
        ProductBundle bundle = productBundleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found"));
        productBundleRepository.delete(bundle);
    }
    
    /**
     * Thêm sản phẩm thay thế vào bundle
     */
    public ProductBundle addSubstituteProduct(Long bundleId, Long substituteProductId) {
        ProductBundle bundle = productBundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found"));
        
        Product substituteProduct = productRepository.findById(substituteProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Substitute product not found"));
        
        bundle.addSubstituteProduct(substituteProduct);
        return productBundleRepository.save(bundle);
    }
    
    /**
     * Xóa sản phẩm thay thế khỏi bundle
     */
    public ProductBundle removeSubstituteProduct(Long bundleId, Long substituteProductId) {
        ProductBundle bundle = productBundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found"));
        
        Product substituteProduct = productRepository.findById(substituteProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Substitute product not found"));
        
        bundle.removeSubstituteProduct(substituteProduct);
        return productBundleRepository.save(bundle);
    }
    
    /**
     * Tính tổng giá của một combo
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateBundleTotalPrice(Long parentProductId) {
        List<ProductBundle> bundles = productBundleRepository.findByParentProductId(parentProductId);
        return bundles.stream()
                .map(ProductBundle::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Kiểm tra xem một sản phẩm có phải là combo không
     */
    @Transactional(readOnly = true)
    public boolean isComboProduct(Long productId) {
        return productBundleRepository.isComboProduct(productId);
    }
    
    /**
     * Tạo combo từ danh sách sản phẩm
     */
    public ProductBundle createComboFromProducts(Long parentProductId, List<Long> childProductIds, 
                                               List<Integer> quantities, List<BigDecimal> prices) {
        Product parentProduct = productRepository.findById(parentProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent product not found"));
        
        // Validate input lists have same size
        if (childProductIds.size() != quantities.size() || childProductIds.size() != prices.size()) {
            throw new BusinessException("Input lists must have the same size");
        }
        
        // Create bundles for each child product
        for (int i = 0; i < childProductIds.size(); i++) {
            Product childProduct = productRepository.findById(childProductIds.get(i))
                    .orElseThrow(() -> new ResourceNotFoundException("Child product not found"));
            
            ProductBundle bundle = new ProductBundle();
            bundle.setParentProduct(parentProduct);
            bundle.setChildProduct(childProduct);
            bundle.setQuantity(quantities.get(i));
            bundle.setBundlePrice(prices.get(i));
            bundle.setSortOrder(i);
            
            productBundleRepository.save(bundle);
        }
        
        return null; // Return first bundle or create a summary object
    }
} 