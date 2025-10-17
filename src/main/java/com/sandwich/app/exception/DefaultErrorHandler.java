package com.sandwich.app.exception;

import com.sandwich.app.models.exception.AppExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultErrorHandler {

    @ExceptionHandler
    public AppExceptionResponse defaultHandler(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return new AppExceptionResponse()
            .setStatus(HttpStatus.BAD_REQUEST)
            .setErrorMessage(exception.getMessage());
    }
}
