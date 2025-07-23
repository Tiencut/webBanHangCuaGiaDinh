package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CategoryDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.CategoryMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CategorySearchRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categoryMapper.toDtoList(categories);
    }

    public CategoryDto getCategoryById(Long id) {
        log.info("Fetching category by id: {}", id);
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    public CategoryDto getCategoryByCode(String code) {
        log.info("Fetching category by code: {}", code);
        Category category = categoryRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with code: " + code));
        return categoryMapper.toDto(category);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Creating new category: {}", categoryDto.getName());
        
        // Check if category code already exists
        if (categoryDto.getCode() != null && !categoryDto.getCode().trim().isEmpty()) {
            Optional<Category> existingCategory = categoryRepository.findByCodeAndIsActiveTrue(categoryDto.getCode());
            if (existingCategory.isPresent()) {
                throw new BusinessException("Category code already exists: " + categoryDto.getCode());
            }
        }

        Category category = new Category();
        categoryMapper.updateEntityFromDto(categoryDto, category);
        category.setIsActive(true);

        // Set parent if provided
        if (categoryDto.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndIsActiveTrue(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDto.getParentId()));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", savedCategory.getId());
        return categoryMapper.toDto(savedCategory);
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        log.info("Updating category with id: {}", id);
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Check if new code conflicts with existing category
        if (categoryDto.getCode() != null && !categoryDto.getCode().trim().isEmpty() && 
            !categoryDto.getCode().equals(category.getCode())) {
            Optional<Category> existingCategory = categoryRepository.findByCodeAndIsActiveTrue(categoryDto.getCode());
            if (existingCategory.isPresent()) {
                throw new BusinessException("Category code already exists: " + categoryDto.getCode());
            }
        }

        categoryMapper.updateEntityFromDto(categoryDto, category);
        
        // Update parent if provided
        if (categoryDto.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndIsActiveTrue(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDto.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", updatedCategory.getId());
        return categoryMapper.toDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        Category category = categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Check if category has children
        List<Category> children = categoryRepository.findByParentIdAndIsActiveTrue(id);
        if (!children.isEmpty()) {
            throw new BusinessException("Cannot delete category with children. Please delete children first.");
        }
        
        category.setIsActive(false);
        categoryRepository.save(category);
        log.info("Category deleted successfully with id: {}", id);
    }

    @Transactional
    public void updateCategoryParent(Long categoryId, Long newParentId) {
        log.info("Moving category {} to new parent {}", categoryId, newParentId);

        Category categoryToMove = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category to move not found with id: " + categoryId));

        // Prevent moving a category into itself
        if (categoryId.equals(newParentId)) {
            throw new BusinessException("Cannot move a category into itself.");
        }
        
        // Prevent moving a category into one of its own descendants
        if (newParentId != null) {
            Category current = categoryRepository.findById(newParentId).orElse(null);
            while (current != null) {
                if (current.getId().equals(categoryId)) {
                    throw new BusinessException("Cannot move a category into its own descendant.");
                }
                current = current.getParent();
            }
        }
        
        Category newParent = null;
        if (newParentId != null) {
            newParent = categoryRepository.findById(newParentId)
                .orElseThrow(() -> new ResourceNotFoundException("New parent category not found with id: " + newParentId));
        }

        categoryToMove.setParent(newParent);
        categoryRepository.save(categoryToMove);

        // Update levels for the moved category and all its descendants
        updateLevelRecursively(categoryToMove, newParent != null ? newParent.getLevel() + 1 : 0);
    }

    private void updateLevelRecursively(Category category, int newLevel) {
        category.setLevel(newLevel);
        categoryRepository.save(category);
        
        List<Category> children = categoryRepository.findByParentIdAndIsActiveTrue(category.getId());
        for (Category child : children) {
            updateLevelRecursively(child, newLevel + 1);
        }
    }

    public List<CategoryDto> getRootCategories() {
        log.info("Fetching root categories");
        List<Category> categories = categoryRepository.findByParentIdIsNullAndIsActiveTrue();
        return categoryMapper.toDtoList(categories);
    }

    public List<CategoryDto> getChildCategories(Long parentId) {
        log.info("Fetching child categories for parent id: {}", parentId);
        List<Category> children = categoryRepository.findByParentIdAndIsActiveTrue(parentId);
        return categoryMapper.toDtoList(children);
    }

    public CategoryDto getParentCategory(Long childId) {
        log.info("Fetching parent category for child id: {}", childId);
        Category child = categoryRepository.findByIdAndIsActiveTrue(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + childId));
        
        if (child.getParent() == null) {
            throw new ResourceNotFoundException("Category has no parent");
        }
        
        return categoryMapper.toDto(child.getParent());
    }

    public List<CategoryDto> getCategoriesByLevel(Integer level) {
        log.info("Fetching categories by level: {}", level);
        List<Category> categories = categoryRepository.findByLevelAndIsActiveTrue(level);
        return categoryMapper.toDtoList(categories);
    }

    public List<CategoryDto> searchCategories(String keyword) {
        log.info("Searching categories with keyword: {}", keyword);
        List<Category> categories = categoryRepository.searchCategories(keyword);
        return categoryMapper.toDtoList(categories);
    }

    public List<CategoryDto> advancedSearch(CategorySearchRequest request) {
        log.info("Advanced search categories with request: {}", request);
        List<Category> categories = categoryRepository.advancedSearch(
            request.getKeyword(),
            request.getParentId(),
            request.getLevel(),
            request.getIsActive(),
            request.getProductCountRange(),
            request.getSortBy(),
            request.getSortDirection()
        );
        return categoryMapper.toDtoList(categories);
    }

    public List<CategoryDto> getCategoriesBySortOrder() {
        log.info("Fetching categories ordered by sort order");
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return categoryMapper.toDtoList(categories);
    }

    public void updateCategorySortOrder(Long categoryId, Integer newSortOrder) {
        log.info("Updating sort order for category id: {} to {}", categoryId, newSortOrder);
        Category category = categoryRepository.findByIdAndIsActiveTrue(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        
        category.setSortOrder(newSortOrder);
        categoryRepository.save(category);
        log.info("Category sort order updated successfully");
    }
}
