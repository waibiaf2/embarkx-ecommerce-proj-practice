package com.ecommerce.project.services;

import com.ecommerce.project.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();
    Category getCategory(Long categoryId);
    Category createCategory(Category category);
    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
