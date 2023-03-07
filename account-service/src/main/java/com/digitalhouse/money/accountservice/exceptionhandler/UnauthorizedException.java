package com.digitalhouse.money.accountservice.exceptionhandler;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
