package com.projekat.ris.repository;

import com.projekat.ris.model.Order;
import com.projekat.ris.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
