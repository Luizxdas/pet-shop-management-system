package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.UserLoginDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthenticationManager authenticationManager;
    public UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public UserResponseDTO login(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password()));

        User user = userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
