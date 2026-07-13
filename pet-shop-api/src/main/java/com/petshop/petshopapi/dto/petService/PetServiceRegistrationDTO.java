package com.petshop.petshopapi.dto.petService;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PetServiceRegistrationDTO(
        @NotBlank
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotNull
        @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
        @Digits(integer = 8, fraction = 2, message = "Price must have 2 digits after the decimal point")
        BigDecimal price) {
}
