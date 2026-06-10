package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.pet.PetRegistrationDTO;
import com.petshop.petshopapi.dto.pet.PetResponseDTO;
import com.petshop.petshopapi.entity.Pet;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.repository.PetRepository;
import com.petshop.petshopapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    @Test
    public void registerPet_ValidData_SavesAndReturnsDTO() {
        String loggedInEmail = "cliente@email.com";
        User fakeOwner = new User(1L, "João", loggedInEmail, "senha123", "ROLE_CUSTOMER");
        PetRegistrationDTO dto = new PetRegistrationDTO("Marley", "Dog");

        when(userRepository.findByEmail(loggedInEmail)).thenReturn(Optional.of(fakeOwner));

        PetResponseDTO  response = petService.registerPet(dto, loggedInEmail);

        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository).save(petCaptor.capture());

        Pet capturedPet =  petCaptor.getValue();

        assertEquals(fakeOwner, capturedPet.getOwner());
        assertEquals("Marley", capturedPet.getName());
        assertEquals("Dog", capturedPet.getSpecies());

        assertNotNull(response);

        assertEquals("Marley", response.name());
        assertEquals("Dog", response.species());
    }
}
