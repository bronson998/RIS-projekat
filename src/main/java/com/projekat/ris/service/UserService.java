package com.projekat.ris.service;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByUsername(String username);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserResponseDTO userUpdateDTO);
    UserResponseDTO updatePassword(Long id, String newPassword);
    void deleteUser(Long id);
}
