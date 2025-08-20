package com.projekat.ris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
}
