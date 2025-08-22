package com.projekat.ris.controller;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.dto.OrderDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.CartService;
import com.projekat.ris.service.OrderService;
import com.projekat.ris.service.ProductService;
import com.projekat.ris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/checkout")
    public String checkout(Authentication auth, Model model) {
        Long userId = currentUserId(auth);
        var cart = cartService.getCartByUserId(userId);
        if (cart.getItems() == null || cart.getItems().isEmpty()) return "redirect:/carts";

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", total);
        return "order-summary";
    }

    @PostMapping("/confirm")
    public String confirm(Authentication auth) {
        Long userId = currentUserId(auth);
        OrderDTO order = orderService.checkout(userId);
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping
    public String getOrders(Authentication auth, Model model) {
        Long userId = currentUserId(auth);
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Authentication auth, Model model) {
        Long userId = currentUserId(auth);
        OrderDTO order = orderService.getOrderById(id);
        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Not allowed to view this order");
        }
        model.addAttribute("order", order);
        return "order-detail";
    }

    private Long currentUserId(Authentication auth) {
        UserResponseDTO user = userService.getUserByUsername(auth.getName());
        if (user == null || user.getId() == null){
            throw new IllegalArgumentException("User not found");
        }

        return user.getId();
    }
}
