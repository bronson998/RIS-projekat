package com.projekat.ris.service;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserRegistrationDTO userUpdateDTO);
    void deleteUser(Long id);
}
