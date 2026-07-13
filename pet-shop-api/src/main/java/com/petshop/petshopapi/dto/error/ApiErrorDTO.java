package com.petshop.petshopapi.dto.error;

import com.petshop.petshopapi.entity.enums.ErrorCode;

import java.util.Map;

public record ApiErrorDTO(ErrorCode code, String message, Map<String, String> details) {
}
