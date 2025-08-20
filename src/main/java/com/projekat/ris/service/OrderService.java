package com.projekat.ris.service;

import com.projekat.ris.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getOrdersByUser(Long userId);
    OrderDTO checkout(Long userId);
    void deleteOrder(Long id);
}
