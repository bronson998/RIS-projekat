package com.projekat.ris.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private Long id;
    @NotNull(message = "User ID must not be null")
    private Long userId;
    private LocalDateTime createdAt;
    private List<CartItemDTO> items;
}
