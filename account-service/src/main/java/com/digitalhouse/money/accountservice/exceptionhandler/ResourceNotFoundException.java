package com.digitalhouse.money.accountservice.exceptionhandler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
