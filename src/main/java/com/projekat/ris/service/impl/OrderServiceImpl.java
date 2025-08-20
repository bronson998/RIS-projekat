package com.projekat.ris.service.impl;

import com.projekat.ris.dto.OrderDTO;
import com.projekat.ris.dto.mappers.OrderMapper;
import com.projekat.ris.model.Cart;
import com.projekat.ris.model.Order;
import com.projekat.ris.model.OrderItem;
import com.projekat.ris.model.OrderStatus;
import com.projekat.ris.model.Product;
import com.projekat.ris.model.User;
import com.projekat.ris.repository.CartRepository;
import com.projekat.ris.repository.OrderRepository;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO checkout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        Set<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();

                    // 1. Validacija stanja proizvoda
                    if (product.getQuantity() < cartItem.getQuantity()) {
                        throw new RuntimeException("Not enough stock for product: " + product.getName());
                    }

                    // 2. Oduzmi količinu iz stanja
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());

                    // 3. Kreiraj order item
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(product.getPrice());

                    return orderItem;
                })
                .collect(Collectors.toSet());

        order.setItems(orderItems);

        double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(total);

        // Sačuvaj order i update-uj proizvode (flush)
        Order saved = orderRepository.save(order);

        // Isprazni cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
