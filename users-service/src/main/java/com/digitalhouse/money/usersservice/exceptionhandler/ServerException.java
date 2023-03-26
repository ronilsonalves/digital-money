package com.digitalhouse.money.usersservice.exceptionhandler;

public class ServerException extends RuntimeException {
    public ServerException (String message) {
        super(message);
    }
}
