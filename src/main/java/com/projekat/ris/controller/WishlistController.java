package com.projekat.ris.controller;

import com.projekat.ris.dto.WishlistDTO;
import com.projekat.ris.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
@AllArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/user/{userId}")
    public WishlistDTO getWishlistByUser(@PathVariable Long userId) {
        return wishlistService.getWishlistByUser(userId);
    }

    @PostMapping("/user/{userId}/add/{productId}")
    public WishlistDTO addProductToWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        return wishlistService.addProductToWishlist(userId, productId);
    }

    @DeleteMapping("/user/{userId}/remove/{productId}")
    public WishlistDTO removeProductFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        return wishlistService.removeProductFromWishlist(userId, productId);
    }
}