package com.projekat.ris.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be under 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be under 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Quantity must be 0 or greater")
    private int quantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
