package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();
    Category getCategory(Long categoryId);
    Category createCategory(Category category);
    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
