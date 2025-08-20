package com.projekat.ris.service;

import com.projekat.ris.dto.WishlistDTO;

public interface WishlistService {
    WishlistDTO getWishlistByUser(Long userId);
    WishlistDTO addProductToWishlist(Long userId, Long productId);
    WishlistDTO removeProductFromWishlist(Long userId, Long productId);
}
