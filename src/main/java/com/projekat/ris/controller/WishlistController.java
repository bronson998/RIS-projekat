package com.projekat.ris.controller;

import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.dto.WishlistDTO;
import com.projekat.ris.service.UserService;
import com.projekat.ris.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishlists")
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;

    @GetMapping
    public String getWishlist(Authentication auth, Model model) {
        Long userId = currentUserId(auth);
        WishlistDTO wishlist = wishlistService.getWishlistByUser(userId);
        model.addAttribute("wishlist", wishlist);
        return "wishlist";
    }

    @PostMapping("/add")
    public String addItemToWishlist(Authentication auth, @RequestParam Long productId) {
        Long userId = currentUserId(auth);
        wishlistService.addProductToWishlist(userId, productId);
        return "redirect:/wishlists";
    }

    @PostMapping("/remove/{productId}")
    public String remove(Authentication auth, @PathVariable Long productId) {
        Long userId = currentUserId(auth);
        wishlistService.removeProductFromWishlist(userId, productId);
        return "redirect:/wishlists";
    }

    private Long currentUserId(Authentication auth) {
        UserResponseDTO user = userService.getUserByUsername(auth.getName());
        if (user == null || user.getId() == null) {
            throw new IllegalStateException("User not found");
        }
        return user.getId();
    }
}