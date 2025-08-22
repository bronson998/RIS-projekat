package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.MessageDTO;
import com.projekat.ris.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.username", target = "receiverUsername")
    MessageDTO toDto(Message message);

    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "receiverId", target = "receiver.id")
    Message toEntity(MessageDTO dto);
}
