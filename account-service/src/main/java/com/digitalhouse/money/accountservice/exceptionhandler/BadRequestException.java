package com.digitalhouse.money.accountservice.exceptionhandler;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
