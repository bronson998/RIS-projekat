package com.projekat.ris.dto;

import com.projekat.ris.model.OrderStatus;
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
    private Long userId;
    private OrderStatus status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
