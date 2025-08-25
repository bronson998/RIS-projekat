package com.projekat.ris.controller;

import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.ChatService;
import com.projekat.ris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @GetMapping
    public String chatPage(@RequestParam(value = "user", required = false) String peerUsername,
                           Authentication auth,
                           Model model) {

        UserResponseDTO me = userService.getUserByUsername(auth.getName());
        model.addAttribute("me", me);

        if (peerUsername == null || peerUsername.isBlank()) {
            List<UserResponseDTO> users = userService.getAllUsers().stream()
                    .filter(u -> !u.getUsername().equals(me.getUsername()))
                    .sorted(Comparator.comparing(UserResponseDTO::getUsername))
                    .toList();
            model.addAttribute("users", users);
            model.addAttribute("peer", null);
            model.addAttribute("messages", List.of());
            return "chat";
        }

        UserResponseDTO peer = userService.getUserByUsername(peerUsername);
        model.addAttribute("peer", peer);
        model.addAttribute("users", null);
        model.addAttribute("messages",
                chatService.getConversation(me.getUsername(), peer.getUsername())); // returns List<MessageDTO>
        return "chat";
    }

    // STOMP payload
    public record ChatPayload(String receiver, String content) {}

    @MessageMapping("/chat.send")
    public void handlePrivate(@Payload @Validated ChatPayload payload, Authentication auth) {
        chatService.send(auth.getName(), payload.receiver(), payload.content());
    }
}
