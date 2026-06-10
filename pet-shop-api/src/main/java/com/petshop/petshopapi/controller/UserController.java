package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.user.UserRegistrationDTO;
import com.petshop.petshopapi.dto.user.UserResponseDTO;
import com.petshop.petshopapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserResponseDTO userResponseDTO = userService.registerUser(userRegistrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
}
