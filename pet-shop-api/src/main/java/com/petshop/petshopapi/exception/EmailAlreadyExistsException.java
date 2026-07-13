package com.petshop.petshopapi.exception;

import com.petshop.petshopapi.entity.enums.ErrorCode;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public ErrorCode getErrorCode() {
        return ErrorCode.EMAIL_ALREADY_EXISTS;
    }
}
