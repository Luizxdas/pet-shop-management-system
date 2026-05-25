package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.UserRegistrationDTO;
import com.petshop.petshopapi.dto.UserResponseDTO;
import com.petshop.petshopapi.exception.EmailAlreadyExistsException;
import com.petshop.petshopapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testRegisterUser_Returns201Create() throws Exception {
        String newUserJson = """
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "password": "securepassword123",
                        "role": "ROLE_CUSTOMER"
                    }
                    """;

        UserResponseDTO fakeResponse = new UserResponseDTO(1L, "John Doe", "john@example.com", "ROLE_CUSTOMER");

        Mockito.when(userService.registerUser(any(UserRegistrationDTO.class)))
                .thenReturn(fakeResponse);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterUser_WithInvalidEmail_Returns400() throws Exception {
        String newUserJson = """
                    {
                        "name": "John Doe",
                        "email": "johnexample.com",
                        "password": "securepassword123",
                        "role": "ROLE_CUSTOMER"
                    }
                    """;

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists_Returns400() throws Exception {
        String newUserJson = """
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "password": "securepassword123",
                        "role": "ROLE_CUSTOMER"
                    }
                    """;

        Mockito.when(userService.registerUser(any(UserRegistrationDTO.class)))
                .thenThrow(new EmailAlreadyExistsException("Email is already in use"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }

    @Test
    public void testRegisterUser_WithMissingFields_Returns400() throws Exception {
        String newUserJson = """
                    {
                        "name": "",
                        "email": "",
                        "password": "",
                        "role": ""
                    }
                    """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isBadRequest());
    }
}
