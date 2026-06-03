package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.pet.PetRegistrationDTO;
import com.petshop.petshopapi.dto.pet.PetResponseDTO;
import com.petshop.petshopapi.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @Autowired
    public PetControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void registerPet_Return201Create() throws Exception {
        String loggedInEmail = "cliente@email.com";
        String newPetJson = """
                    {
                        "name": "Poppy",
                        "species": "Dog"
                    }
                    """;

        Authentication authMock = Mockito.mock(Authentication.class);
        Mockito.when(authMock.getName()).thenReturn(loggedInEmail);

        PetResponseDTO fakeResponse = new PetResponseDTO(1L, 1L, "Poppy", "Dog");

        Mockito.when(petService.registerPet(any(PetRegistrationDTO.class), eq(loggedInEmail)))
                .thenReturn(fakeResponse);

        mockMvc.perform(post("/api/pets/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPetJson)
                        .principal(authMock))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Poppy"))
                .andExpect(jsonPath("$.species").value("Dog"));
    }
}
