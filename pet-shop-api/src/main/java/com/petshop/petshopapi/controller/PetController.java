package com.petshop.petshopapi.controller;

import com.petshop.petshopapi.dto.pet.PetRegistrationDTO;
import com.petshop.petshopapi.dto.pet.PetResponseDTO;
import com.petshop.petshopapi.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/pets")
public class PetController {

    PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> register(@Valid @RequestBody PetRegistrationDTO dto, Authentication authentication) {
        String loggedInEmail = authentication.getName();

        PetResponseDTO petResponseDTO = petService.registerPet(dto, loggedInEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(petResponseDTO);
    }

}
