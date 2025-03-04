package com.ecommerce.project.controllers;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.models.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(AppConstants.BASE_URL)
public class ProductController {
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping("/admin/{categoryId}/products")
    public ResponseEntity<ProductDTO> createProduct(
        @PathVariable Long categoryId,
        @Valid @RequestBody Product product
    ) {
        ProductDTO createdProduct = productService.createProduct(categoryId, product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    
    
}
