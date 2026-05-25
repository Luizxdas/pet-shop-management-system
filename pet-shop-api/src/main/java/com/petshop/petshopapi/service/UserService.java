package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.UserRegistrationDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.exception.EmailAlreadyExistsException;
import com.petshop.petshopapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.email())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setName(userRegistrationDTO.name());
        user.setEmail(userRegistrationDTO.email());
        user.setRole(userRegistrationDTO.role());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
