package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.config.AbstractIntegrationTest;
import com.petshop.petshopapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterUser_Integration_Returns201() throws Exception {
        String newUserJson = """
                {
                    "name": "Integration Test User",
                    "email": "integration@example.com",
                    "password": "securepassword123",
                    "role": "ROLE_CUSTOMER"
                }
                """;

        mockMvc.perform(post("/api/users/register").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated());

        boolean userExists = userRepository.existsByEmail("integration@example.com");
        assertThat(userExists).isTrue();
    }
}