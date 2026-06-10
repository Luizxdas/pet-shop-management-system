package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.config.AbstractIntegrationTest;
import com.petshop.petshopapi.entity.Pet;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.repository.PetRepository;
import com.petshop.petshopapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class PetControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        petRepository.deleteAll();
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setName("Integration User");
        testUser.setEmail("integration@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole("ROLE_CUSTOMER");

        userRepository.saveAndFlush(testUser);
    }

    @Test
    public void testRegisterPet_Integration_Returns201() throws Exception {
        String newPetJson = """
                {
                    "name": "Bob",
                    "species": "Cat"
                }
                """;


        mockMvc.perform(post("/api/v1/pets")
                .with(user("integration@example.com").roles("CUSTOMER"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPetJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.species").value("Cat"));

        assertEquals(1, petRepository.count());

        Pet savedPet = petRepository.findAll().getFirst();
        assertEquals("Bob", savedPet.getName());
        assertEquals("integration@example.com", savedPet.getOwner().getEmail());
    }
}
