package com.ecommerce.project.controllers;

import com.ecommerce.project.models.Category;
import com.ecommerce.project.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories ,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
        Category cagetory = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(cagetory, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(
        @PathVariable Long categoryId,
        @Valid @RequestBody Category category
    ) {
        categoryService.updateCategory(categoryId, category);
        return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
    }
}
