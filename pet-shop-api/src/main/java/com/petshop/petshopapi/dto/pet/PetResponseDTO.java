package com.petshop.petshopapi.dto.pet;

public record PetResponseDTO(

        Long id,

        Long ownerId,

        String name,

        String species
){
}