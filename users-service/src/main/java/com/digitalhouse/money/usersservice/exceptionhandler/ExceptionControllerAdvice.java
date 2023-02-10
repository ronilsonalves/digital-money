package com.digitalhouse.money.usersservice.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.digitalhouse.money.usersservice.api.controller")
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageExceptionHandler> badRequestException(BadRequestException badRequestException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), badRequestException.getMessage()
        );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
