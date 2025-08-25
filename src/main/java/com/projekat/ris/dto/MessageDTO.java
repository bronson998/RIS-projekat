package com.projekat.ris.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;

    @NotNull(message = "Sender ID must not be null")
    private Long senderId;

    @NotBlank(message = "Sender username is required")
    private String senderUsername;

    @NotNull(message = "Receiver ID must not be null")
    private Long receiverId;

    @NotBlank(message = "Receiver username is required")
    private String receiverUsername;

    @NotBlank(message = "Message content must not be blank")
    @Size(max = 1000, message = "Message content must be under 1000 characters")
    private String messageContent;

    private LocalDateTime timestamp;
}
