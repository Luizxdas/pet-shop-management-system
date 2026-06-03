package com.petshop.petshopapi.dto.pet;

import jakarta.validation.constraints.NotBlank;

public record PetRegistrationDTO(

        @NotBlank(message = "Pet name cannot be blank")
        String name,

        @NotBlank(message = "Pet species is required")
        String species
){
}