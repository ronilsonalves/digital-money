package com.digitalhouse.money.accountservice.exceptionhandler;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
