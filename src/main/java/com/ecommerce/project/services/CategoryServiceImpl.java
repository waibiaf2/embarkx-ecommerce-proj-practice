package com.ecommerce.project.services;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortAndOrderBy = sortOrder.equalsIgnoreCase("asc") ?
                                  Sort.by(sortBy).ascending() :
                                  Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortAndOrderBy);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        
        if (categories.isEmpty())
            throw new APIException("No Categories have been created yet!");
        
        List<CategoryDTO> categoryDTOs
            = categories.stream()
                  .map(category -> modelMapper.map(category, CategoryDTO.class))
                  .toList();
        
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        
        return categoryResponse;
    }
    
    @Override
    public CategoryDTO getCategory(Long categoryId) {
        Category category
            = categoryRepository.findById(categoryId)
                  .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        
        return modelMapper.map(category, CategoryDTO.class);
    }
    
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        System.out.println(category.toString());
        
        Category exitCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (exitCategory != null)
            throw new APIException(
                String.format("Category %s already exists!", category.getCategoryName())
            );
        
        
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }
    
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("Category", "CategoryId", categoryId)
        );
        
        categoryRepository.delete(category);
        
        return modelMapper.map(category, CategoryDTO.class);
    }
    
    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        
        Category categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("Category", "categoryId", categoryId)
        );
        
        
        categoryToUpdate.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryToUpdate);
        
        return modelMapper.map(categoryToUpdate, CategoryDTO.class);
    }
}
