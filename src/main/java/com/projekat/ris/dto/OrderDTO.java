package com.projekat.ris.dto;

import com.projekat.ris.model.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;

    @NotNull(message = "User ID can't be null")
    private Long userId;

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be non-negative")
    private double totalPrice;

    private LocalDateTime createdAt;

    @NotNull(message = "Order must have items")
    @Size(min = 1, message = "Order must contain at least one item")
    private List<OrderItemDTO> items;
}
