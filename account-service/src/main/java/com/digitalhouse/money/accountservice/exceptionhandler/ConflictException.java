package com.digitalhouse.money.accountservice.exceptionhandler;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}