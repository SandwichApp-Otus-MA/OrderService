package com.sandwich.app.exception;

import com.sandwich.app.models.exception.AppExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultErrorHandler {

    @ExceptionHandler
    public AppExceptionResponse defaultHandler(Throwable exception) {
        return new AppExceptionResponse()
            .setStatus(HttpStatus.BAD_REQUEST)
            .setErrorMessage(exception.getMessage());
    }
}
