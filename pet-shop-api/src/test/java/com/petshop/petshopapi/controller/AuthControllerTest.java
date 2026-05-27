package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.UserLoginDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testLoginUser_Returns200Ok() throws Exception {
        String loginJson = """
                {
                    "email": "johndoe@example.com",
                    "password": "password123"
                }
                """;

        UserResponseDTO falseReponse = new UserResponseDTO(2L, "John Doe", "johndoe@example.com", "ROLE_CUSTOMER");
        Mockito.when(authService.login(any(UserLoginDTO.class))).thenReturn(falseReponse);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));
    }

    @Test
    public void testLoginUser_WithInvalidEmail_Returns400BadRequest() throws Exception {
        String loginJson = """
                {
                    "email": "johndoeexample.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser_WithWrongCredentials_Returns401Unauthorized() throws Exception {
        String loginJson = """
                {
                    "email": "johndoe@example.com",
                    "password": "wrongpassword"
                }
                """;

        Mockito.when(authService.login(any(UserLoginDTO.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }
}
