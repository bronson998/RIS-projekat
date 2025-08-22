package com.projekat.ris.service;

import com.projekat.ris.dto.MessageDTO;

import java.util.List;

public interface ChatService {
    List<MessageDTO> getConversation(String userA, String userB);
    MessageDTO send(String senderUsername, String receiverUsername, String content);
}
