package com.digitalhouse.money.accountservice.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageExceptionHandler> badRequestException(BadRequestException badRequestException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), badRequestException.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageExceptionHandler> notFoundException(ResourceNotFoundException notFoundException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), notFoundException.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MessageExceptionHandler> conflictException(ConflictException conflictException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(), conflictException.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<MessageExceptionHandler> unauthorizedException(UnauthorizedException unauthorizedException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), unauthorizedException.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<MessageExceptionHandler> insufficientFundsException(InsufficientFundsException insufficientFundsException) {
        MessageExceptionHandler error = new MessageExceptionHandler(
                LocalDateTime.now(), HttpStatus.GONE.value(), insufficientFundsException.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.GONE);
    }

}