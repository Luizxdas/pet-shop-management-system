package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.UserRegistrationDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.exception.EmailAlreadyExistsException;
import com.petshop.petshopapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    public void registerUser_EmailAlreadyExists_ThrowsException() {
        UserRegistrationDTO dto = new UserRegistrationDTO("John Doe", "john@example.com", "password123", "ROLE_CUSTOMER");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void registerUser_ValidData_SavesUserAndReturnsDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO("John Doe", "john@example.com", "password123", "ROLE_CUSTOMER");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        when(passwordEncoder.encode(dto.password())).thenReturn("hashed_password_123");

        UserResponseDTO response = userService.registerUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals("John Doe", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals("ROLE_CUSTOMER", capturedUser.getRole());
        assertEquals("hashed_password_123", capturedUser.getPassword());

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        assertEquals("ROLE_CUSTOMER", response.role());
    }
}
