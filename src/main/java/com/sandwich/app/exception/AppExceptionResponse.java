package com.sandwich.app.exception;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class AppExceptionResponse {
    private HttpStatus status;
    private String errorMessage;
}