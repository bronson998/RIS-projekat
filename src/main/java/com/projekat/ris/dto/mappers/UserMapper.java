package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegistrationDTO dto);
    UserResponseDTO toResponseDTO(User user);
}
