package com.projekat.ris.service.impl;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.dto.mappers.CartMapper;
import com.projekat.ris.model.Cart;
import com.projekat.ris.model.CartItem;
import com.projekat.ris.model.Product;
import com.projekat.ris.model.User;
import com.projekat.ris.repository.CartRepository;
import com.projekat.ris.repository.ProductRepository;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CartDTO createCart(CartDTO cartDTO) {
        if (cartDTO.getUserId() == null) {
            throw new IllegalArgumentException("userId is required to create a cart");
        }
        Cart cart = cartMapper.toEntity(cartDTO);

        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + cartDTO.getUserId()));
        cart.setUser(user);

        if (cartRepository.existsByUserId(user.getId())) {
            Cart existing = cartRepository.findByUserId(user.getId())
                    .orElseThrow();
            return cartMapper.toDto(existing);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartDTO getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
       Cart cart = cartRepository.findByUserId(userId)
               .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int productQuantity = product.getQuantity();
        if (productQuantity < quantity) {
            throw new IllegalArgumentException("Not enough stock for product " + product.getId()
                    + " (requested " + quantity + ", available " + productQuantity + ")");
        }

        product.setQuantity(productQuantity - quantity);
        productRepository.save(product);

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(item);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem toRemove = cart.getItems().stream()
                        .filter(i -> i.getId().equals(itemId)).findFirst()
                        .orElseThrow(() -> new RuntimeException("Cart item not found: " + itemId));

        Product product = toRemove.getProduct();
        int productQuantity = product.getQuantity();
        product.setQuantity(productQuantity + toRemove.getQuantity());
        productRepository.save(product);

        cart.getItems().remove(toRemove);
        return cartMapper.toDto(cartRepository.save(cart));
    }
}
