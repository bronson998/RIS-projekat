package com.projekat.ris.service;

import com.projekat.ris.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO createCart(CartDTO cartDTO);
    CartDTO getCartById(Long id);
    List<CartDTO> getAllCarts();
    void deleteCart(Long id);

    CartDTO addItemToCart(Long cartId, Long productId, int quantity);
    CartDTO removeItemFromCart(Long cartId, Long itemId);
}
