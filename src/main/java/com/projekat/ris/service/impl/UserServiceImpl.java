package com.projekat.ris.service.impl;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.dto.mappers.UserMapper;
import com.projekat.ris.model.User;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        User user = userMapper.toEntity(userRegistrationDTO);
        user.setPassword(userRegistrationDTO.getPassword());
        user.setFirstName(userRegistrationDTO.getFirstname());
        user.setLastName(userRegistrationDTO.getLastname());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRegistrationDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found"));

        // i ovde treba odraditi verifikaciju
        user.setEmail(userUpdateDTO.getEmail());
        user.setPassword(userUpdateDTO.getPassword());
        user.setFirstName(userUpdateDTO.getFirstname());
        user.setLastName(userUpdateDTO.getLastname());

        User updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
