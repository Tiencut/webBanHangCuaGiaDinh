package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    // Tạo category mới
    public Category createCategory(Category category) {
        log.info("Creating new category: {}", category.getName());
        
        // Kiểm tra code đã tồn tại
        if (category.getCode() != null && categoryRepository.existsByCode(category.getCode())) {
            throw new BusinessException("Category code đã tồn tại: " + category.getCode());
        }
        
        // Tự động tạo code nếu chưa có
        if (category.getCode() == null || category.getCode().trim().isEmpty()) {
            category.setCode(generateCategoryCode(category.getName()));
        }
        
        // Set level và path nếu có parent
        if (category.getParent() != null) {
            Category parent = categoryRepository.findById(category.getParent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            
            category.setLevel(parent.getLevel() + 1);
            category.setParent(parent);
        } else {
            category.setLevel(0);
        }
        
        Category savedCategory = categoryRepository.save(category);
        
        // Cập nhật path sau khi save (vì cần ID)
        updateCategoryPath(savedCategory);
        
        return savedCategory;
    }
    
    // Tìm category theo ID
    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    // Tìm tất cả category root
    @Transactional(readOnly = true)
    public List<Category> findRootCategories() {
        return categoryRepository.findByParentIsNull();
    }
    
    // Tìm category con theo parent ID
    @Transactional(readOnly = true)
    public List<Category> findChildrenByParentId(Long parentId) {
        return categoryRepository.findByParentIdOrderBySortOrderAsc(parentId);
    }
    
    // Tìm tất cả category active
    @Transactional(readOnly = true)
    public List<Category> findAllActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderBySortOrderAsc();
    }
    
    // Cập nhật category
    public Category updateCategory(Long id, Category categoryUpdate) {
        log.info("Updating category with id: {}", id);
        
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingCategory.setName(categoryUpdate.getName());
        existingCategory.setDescription(categoryUpdate.getDescription());
        existingCategory.setSortOrder(categoryUpdate.getSortOrder());
        
        // Cập nhật code nếu khác
        if (!existingCategory.getCode().equals(categoryUpdate.getCode())) {
            if (categoryRepository.existsByCode(categoryUpdate.getCode())) {
                throw new BusinessException("Category code đã tồn tại: " + categoryUpdate.getCode());
            }
            existingCategory.setCode(categoryUpdate.getCode());
        }
        
        return categoryRepository.save(existingCategory);
    }
    
    // Di chuyển category (thay đổi parent)
    public Category moveCategory(Long categoryId, Long newParentId) {
        log.info("Moving category {} to parent {}", categoryId, newParentId);
        
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        // Kiểm tra không thể di chuyển thành con của chính nó
        if (categoryId.equals(newParentId)) {
            throw new BusinessException("Không thể di chuyển category thành con của chính nó");
        }
        
        Category newParent = null;
        if (newParentId != null) {
            newParent = categoryRepository.findById(newParentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            
            // Kiểm tra không tạo vòng lặp (newParent không được là con của category)
            if (isDescendant(newParent, category)) {
                throw new BusinessException("Không thể di chuyển category thành cha của category cha");
            }
        }
        
        category.setParent(newParent);
        updateCategoryHierarchy(category);
        
        return categoryRepository.save(category);
    }
    
    // Xóa category (soft delete)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        // Kiểm tra có category con không
        Long childrenCount = categoryRepository.countByParentId(id);
        if (childrenCount > 0) {
            throw new BusinessException("Không thể xóa category có category con");
        }
        
        // Kiểm tra có sản phẩm không (sẽ implement sau)
        // Long productCount = productRepository.countByCategory(category);
        // if (productCount > 0) {
        //     throw new BusinessException("Không thể xóa category có sản phẩm");
        // }
        
        category.setIsActive(false);
        categoryRepository.save(category);
        
        log.info("Category {} has been deactivated", category.getName());
    }
    
    // Tìm kiếm category theo tên
    @Transactional(readOnly = true)
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Lấy full path của category (breadcrumb)
    @Transactional(readOnly = true)
    public String getCategoryBreadcrumb(Category category) {
        StringBuilder breadcrumb = new StringBuilder();
        buildBreadcrumb(category, breadcrumb);
        return breadcrumb.toString();
    }
    
    // Helper methods
    private String generateCategoryCode(String name) {
        // Tạo code từ tên (bỏ dấu, thay space bằng underscore)
        String code = name.toLowerCase()
                         .replaceAll("\\s+", "_")
                         .replaceAll("[^a-z0-9_]", "");
        
        // Đảm bảo unique
        int counter = 1;
        String originalCode = code;
        while (categoryRepository.existsByCode(code)) {
            code = originalCode + "_" + counter++;
        }
        
        return code;
    }
    
    private void updateCategoryPath(Category category) {
        if (category.getParent() == null) {
            category.setPath("/" + category.getId());
        } else {
            category.setPath(category.getParent().getPath() + "/" + category.getId());
        }
        categoryRepository.save(category);
        
        // Cập nhật path cho tất cả children
        List<Category> children = categoryRepository.findByParentIdOrderBySortOrderAsc(category.getId());
        children.forEach(this::updateCategoryPath);
    }
    
    private void updateCategoryHierarchy(Category category) {
        if (category.getParent() == null) {
            category.setLevel(0);
        } else {
            category.setLevel(category.getParent().getLevel() + 1);
        }
        
        updateCategoryPath(category);
    }
    
    private boolean isDescendant(Category potentialDescendant, Category ancestor) {
        Category current = potentialDescendant.getParent();
        while (current != null) {
            if (current.getId().equals(ancestor.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
    
    private void buildBreadcrumb(Category category, StringBuilder breadcrumb) {
        if (category.getParent() != null) {
            buildBreadcrumb(category.getParent(), breadcrumb);
            breadcrumb.append(" > ");
        }
        breadcrumb.append(category.getName());
    }
}
