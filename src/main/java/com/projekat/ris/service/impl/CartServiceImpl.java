package com.projekat.ris.service.impl;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.dto.mappers.CartMapper;
import com.projekat.ris.model.Cart;
import com.projekat.ris.model.CartItem;
import com.projekat.ris.model.Product;
import com.projekat.ris.repository.CartRepository;
import com.projekat.ris.repository.ProductRepository;
import com.projekat.ris.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = cartMapper.toEntity(cartDTO);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartDTO getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(cartMapper::toDto)
                .toList();
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public CartDTO addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();

        cart.getItems().add(item);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartDTO removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return cartMapper.toDto(cartRepository.save(cart));
    }
}
