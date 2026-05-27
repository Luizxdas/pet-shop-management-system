package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.UserLoginDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        UserResponseDTO userResponseDTO = authService.login(userLoginDTO);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }
}
