package com.petshop.petshopapi.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PetRegistrationDTO(

        @NotBlank(message = "Pet name cannot be blank")
        @Size(max = 100, message = "Pet name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Pet species is required")
        @Size(max = 50, message = "Pet species must not exceed 50 characters")
        String species
){
}