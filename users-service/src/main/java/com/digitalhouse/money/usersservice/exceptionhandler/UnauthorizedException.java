package com.digitalhouse.money.usersservice.exceptionhandler;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
