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
                    var product = cartItem.getProduct();

                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(product.getPrice());
                    return item;
                })
                .collect(Collectors.toSet());

        order.setItems(orderItems);

        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(saved);
    }
}
