package com.projekat.ris.service.impl;

import com.projekat.ris.dto.WishlistDTO;
import com.projekat.ris.dto.mappers.WishlistMapper;
import com.projekat.ris.model.Product;
import com.projekat.ris.model.User;
import com.projekat.ris.model.Wishlist;
import com.projekat.ris.model.WishlistItem;
import com.projekat.ris.repository.ProductRepository;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.repository.WishlistRepository;
import com.projekat.ris.service.WishlistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    public WishlistDTO getWishlistByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        return wishlistMapper.toDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO addProductToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        boolean exists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));

        if (!exists) {
            WishlistItem item = new WishlistItem();
            item.setWishlist(wishlist);
            item.setProduct(product);
            wishlist.getItems().add(item);
        }

        Wishlist saved = wishlistRepository.save(wishlist);
        return wishlistMapper.toDto(saved);
    }

    @Override
    @Transactional
    public WishlistDTO removeProductFromWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlist.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        Wishlist saved = wishlistRepository.save(wishlist);
        return wishlistMapper.toDto(saved);
    }
}
