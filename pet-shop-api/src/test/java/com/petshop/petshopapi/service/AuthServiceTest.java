package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.UserLoginDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthService authService;

    @Test
    public void loginUser_WithValidData_ReturnsUserResponseDTO() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("johndoe@example.com", "password123");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John Doe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setRole("ROLE_CUSTOMER");

        Authentication fakeAuth = new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password());
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(fakeAuth);

        Mockito.when(userRepository.findByEmail(userLoginDTO.email()))
                .thenReturn(Optional.of(mockUser));

        UserResponseDTO response = authService.login(userLoginDTO);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("johndoe@example.com", response.email());
        assertEquals("ROLE_CUSTOMER", response.role());
    }

    @Test
    public void loginUser_WithWrongCredentials_ThrowsBadCredentialsException() {
        UserLoginDTO loginDTO = new UserLoginDTO("johndoe@example.com", "wrongpassword");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginDTO));

        Mockito.verify(userRepository, Mockito.never()).findByEmail(anyString());
    }

    @Test
    public void loginUser_UserNotFoundInDatabase_ThrowsRuntimeException() {
        UserLoginDTO loginDTO = new UserLoginDTO("johndoe@example.com", "password123");

        Authentication fakeAuth = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(fakeAuth);

        Mockito.when(userRepository.findByEmail(loginDTO.email()))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginDTO));

        assertEquals("User not found", exception.getMessage());
    }
}
