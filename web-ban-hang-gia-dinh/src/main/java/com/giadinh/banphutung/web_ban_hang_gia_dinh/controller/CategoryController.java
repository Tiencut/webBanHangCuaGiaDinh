package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.CategoryService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    // GET /api/categories - Lấy tất cả category active
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Getting all active categories");
        List<Category> categories = categoryService.findAllActiveCategories();
        return ResponseEntity.ok(categories);
    }
    
    // GET /api/categories/root - Lấy category gốc (level 0)
    @GetMapping("/root")
    public ResponseEntity<List<Category>> getRootCategories() {
        log.info("Getting root categories");
        List<Category> rootCategories = categoryService.findRootCategories();
        return ResponseEntity.ok(rootCategories);
    }
    
    // GET /api/categories/{id} - Lấy category theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        log.info("Getting category with id: {}", id);
        return categoryService.findById(id)
            .map(category -> ResponseEntity.ok(category))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/categories/{id}/children - Lấy category con
    @GetMapping("/{id}/children")
    public ResponseEntity<List<Category>> getCategoryChildren(@PathVariable Long id) {
        log.info("Getting children of category id: {}", id);
        List<Category> children = categoryService.findChildrenByParentId(id);
        return ResponseEntity.ok(children);
    }
    
    // POST /api/categories - Tạo category mới
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        log.info("Creating new category: {}", category.getName());
        try {
            Category savedCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (BusinessException e) {
            log.error("Business error creating category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found creating category: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT /api/categories/{id} - Cập nhật category
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody Category category) {
        log.info("Updating category with id: {}", id);
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (BusinessException e) {
            log.error("Business error updating category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found updating category: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT /api/categories/{id}/move - Di chuyển category
    @PutMapping("/{id}/move")
    public ResponseEntity<Category> moveCategory(
            @PathVariable Long id,
            @RequestParam(required = false) Long newParentId) {
        log.info("Moving category {} to parent {}", id, newParentId);
        try {
            Category movedCategory = categoryService.moveCategory(id, newParentId);
            return ResponseEntity.ok(movedCategory);
        } catch (BusinessException e) {
            log.error("Business error moving category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found moving category: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE /api/categories/{id} - Xóa category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category with id: {}", id);
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            log.error("Business error deleting category: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found deleting category: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/categories/search - Tìm kiếm category
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String name) {
        log.info("Searching categories with name: {}", name);
        List<Category> categories = categoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }
    
    // GET /api/categories/{id}/breadcrumb - Lấy breadcrumb
    @GetMapping("/{id}/breadcrumb")
    public ResponseEntity<String> getCategoryBreadcrumb(@PathVariable Long id) {
        log.info("Getting breadcrumb for category id: {}", id);
        return categoryService.findById(id)
            .map(category -> {
                String breadcrumb = categoryService.getCategoryBreadcrumb(category);
                return ResponseEntity.ok(breadcrumb);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
