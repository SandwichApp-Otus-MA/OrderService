package com.sandwich.app.exception;

import org.springframework.http.HttpStatusCode;

public class ClientInvokeException extends RuntimeException {

    private final HttpStatusCode status;

    public ClientInvokeException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }
}
