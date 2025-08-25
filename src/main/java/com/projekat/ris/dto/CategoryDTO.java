package com.projekat.ris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 30, message = "Category name must be under 30 characters")
    private String name;

    @Size(max = 255, message = "Description must be under 255 characters")
    private String description;
}
