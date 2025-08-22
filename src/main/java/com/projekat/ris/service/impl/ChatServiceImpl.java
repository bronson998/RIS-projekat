package com.projekat.ris.service.impl;

import com.projekat.ris.dto.MessageDTO;
import com.projekat.ris.dto.mappers.MessageMapper;
import com.projekat.ris.model.Message;
import com.projekat.ris.model.User;
import com.projekat.ris.repository.MessageRepository;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public List<MessageDTO> getConversation(String userA, String userB) {
        User u1 = userRepository.findByUsername(userA)
                .orElseThrow(() -> new RuntimeException("User not found: " + userA));
        User u2 = userRepository.findByUsername(userB)
                .orElseThrow(() -> new RuntimeException("User not found: " + userB));

        return messageRepository.findConversation(u1, u2).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MessageDTO send(String senderUsername, String receiverUsername, String content) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + senderUsername));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + receiverUsername));

        Message saved = messageRepository.save(
                Message.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .messageContent(content)
                        .build()
        );

        MessageDTO messageDTO = messageMapper.toDto(saved);
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                messageDTO
        );

        if (!sender.getUsername().equals(receiver.getUsername())) {
            messagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/messages", messageDTO);
        }

        return messageDTO;
    }
}
