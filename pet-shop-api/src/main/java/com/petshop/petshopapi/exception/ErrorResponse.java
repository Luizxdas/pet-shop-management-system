package com.petshop.petshopapi.exception;

public record ErrorResponse(Integer status, String error, String message) {
}
