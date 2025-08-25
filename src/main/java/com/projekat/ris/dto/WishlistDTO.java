package com.projekat.ris.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDTO {
    private Long id;

    @NotNull(message = "User ID must not be null")
    private Long userId;
    private List<WishlistItemDTO> items;
}
