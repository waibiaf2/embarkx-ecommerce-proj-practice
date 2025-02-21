package com.ecommerce.project.services;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        
        if(categories.isEmpty())
            throw new APIException("No Categories have been created yet!");
            
        return categories;
    }
    
    @Override
    public Category getCategory(Long categoryId) {
        Category category
            = categoryRepository.findById(categoryId)
                  .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        
        return category;
    }
    
    @Override
    public Category createCategory(Category category) {
        
        return categoryRepository.save(category);
    }
    
    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("Category", "CategoryId", categoryId)
        );
        
        categoryRepository.delete(category);
        
        return "Category deleted successfully";
    }
    
    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("Category", "categoryId", categoryId)
        );
        categoryToUpdate.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryToUpdate);
        
        return categoryToUpdate;
    }
}
