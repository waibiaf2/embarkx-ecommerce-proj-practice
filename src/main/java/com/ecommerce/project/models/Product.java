package com.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Currency;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    
    @NotBlank(message = "Product name cannot be blank, its required!")
    @Size(min=3, message = "Product name must be at least 3 characters long.")
    private String productName;
    
    @NotBlank(message = "Description cannot be blank, its required!")
    @Size(min=6, message = "Description must be at least 3 characters long.")
    private String description;
    @Min(value = 0, message = "Discount must be greater than or equal to 0.")
    private Double discount = 0.0;
    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private Double price;
    private Double specialPrice;
    @Min(value = 0, message = "Quantity must be greater than or equal to 0.")
    private Integer quantity;
    private String image;
    
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
