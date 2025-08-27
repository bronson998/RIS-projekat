package com.projekat.ris.service.impl;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.dto.mappers.UserMapper;
import com.projekat.ris.model.Role;
import com.projekat.ris.model.User;
import com.projekat.ris.repository.RoleRepository;
import com.projekat.ris.repository.UserRepository;
import com.projekat.ris.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        userRepository.findByUsername(userRegistrationDTO.getUsername())
                .ifPresent(username -> { throw new IllegalArgumentException("Username already taken"); });
        userRepository.findByEmail(userRegistrationDTO.getEmail())
                .ifPresent(username -> { throw new IllegalArgumentException("Email already taken"); });

        User user = userMapper.toEntity(userRegistrationDTO);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setFirstName(userRegistrationDTO.getFirstname());
        user.setLastName(userRegistrationDTO.getLastname());

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in DB"));
            user.setRoles(new HashSet<>(List.of(roleUser)));
        }

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
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
    public UserResponseDTO updateUser(Long id, UserResponseDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional.ofNullable(userUpdateDTO.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(userUpdateDTO.getFirstname()).ifPresent(user::setFirstName);
        Optional.ofNullable(userUpdateDTO.getLastname()).ifPresent(user::setLastName);

        User updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }

    @Override
    public UserResponseDTO updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newPassword != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
