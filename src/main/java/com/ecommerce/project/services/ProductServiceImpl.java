package com.ecommerce.project.services;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.models.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    
    public ProductServiceImpl(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ProductDTO createProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        
        product.setCategory(category);
        Double computedSpecialPrice = product.getPrice() - (product.getPrice() * product.getDiscount()/100);
        product.setSpecialPrice(computedSpecialPrice);
        Product savedProduct = productRepository.save(product);
        
        return modelMapper.map(savedProduct, ProductDTO.class) ;
    }
    
    @Override
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return List.of();
    }
    
    @Override
    public ProductResponse getProduct(Long productId) {
        return null;
    }
}
