package com.digitalhouse.money.usersservice.exceptionhandler;

public class InvalidCredentialsException extends RuntimeException {
    private String message;
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
