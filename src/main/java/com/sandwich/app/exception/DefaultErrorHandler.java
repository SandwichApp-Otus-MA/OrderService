package com.sandwich.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultErrorHandler {

    @ExceptionHandler
    public ResponseEntity<AppExceptionResponse> defaultHandler(Throwable exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new AppExceptionResponse()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setErrorMessage(exception.getMessage()));
    }
}
