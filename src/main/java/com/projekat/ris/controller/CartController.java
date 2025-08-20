package com.projekat.ris.controller;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    // TODO: refaktorisi da bude kao svuda drugde
    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartService.createCart(cartDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDTO> addItem(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, productId, quantity));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDTO> removeItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

}
