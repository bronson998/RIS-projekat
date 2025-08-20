package com.projekat.ris.repository;

import com.projekat.ris.model.Message;
import com.projekat.ris.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findBySender(User sender);
    Optional<Message> findByReceiver(User receiver);
}
