package com.projekat.ris.controller;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.CartService;
import com.projekat.ris.service.ProductService;
import com.projekat.ris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @GetMapping
    public String myCart(Authentication auth, Model model) {
        Long userId = currentUserId(auth);
        CartDTO cart = ensureCart(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("products", productService.getAllProducts());
        return "cart";
    }

    @PostMapping("/items/add")
    public String addItem(Authentication auth,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        Long userId = currentUserId(auth);
        CartDTO cart = ensureCart(userId);
        cartService.addItemToCart(cart.getId(), productId, quantity);
        return "redirect:/carts";
    }

    @PostMapping("/items/{itemId}/remove")
    public String removeItem(Authentication auth,
                             @PathVariable Long itemId) {
        Long userId = currentUserId(auth);
        CartDTO cart = ensureCart(userId);
        cartService.removeItemFromCart(cart.getId(), itemId);
        return "redirect:/carts";
    }

    private Long currentUserId(Authentication auth) {
        String username = auth.getName();
        UserResponseDTO user = userService.getUserByUsername(username);
        if (user == null || user.getId() == null) {
            throw new IllegalStateException("Authenticated user not found: " + username);
        }
        return user.getId();
    }

    private CartDTO ensureCart(Long userId) {
        try {
            return cartService.getCartByUserId(userId);
        } catch (RuntimeException notFound) {
            return cartService.createCart(CartDTO.builder().userId(userId).build());
        }
    }

}
