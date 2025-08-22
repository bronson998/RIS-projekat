package com.projekat.ris.repository;

import com.projekat.ris.model.Message;
import com.projekat.ris.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
            select m from Message m
            where (m.sender = :u1 and m.receiver= :u2)
               or (m.sender = :u2 and m.receiver= :u1) 
            order by m.timestamp desc
            """)
    List<Message> findConversation(User u1, User u2);

}
