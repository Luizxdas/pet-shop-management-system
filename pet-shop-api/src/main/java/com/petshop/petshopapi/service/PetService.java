package com.petshop.petshopapi.service;

import com.petshop.petshopapi.dto.pet.PetRegistrationDTO;
import com.petshop.petshopapi.dto.pet.PetResponseDTO;
import com.petshop.petshopapi.entity.Pet;
import com.petshop.petshopapi.entity.User;
import com.petshop.petshopapi.repository.PetRepository;
import com.petshop.petshopapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public PetResponseDTO registerPet (PetRegistrationDTO petRegistrationDTO, String loggedInEmail) {
        User owner = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loggedInEmail));

        Pet pet = new Pet();
        pet.setName(petRegistrationDTO.name());
        pet.setSpecies(petRegistrationDTO.species());
        pet.setOwner(owner);

        petRepository.save(pet);
        
        return new PetResponseDTO(pet.getId(), owner.getId(), pet.getName(), pet.getSpecies());
    }
}
