package com.sandwich.app.configuration.rest;

import com.sandwich.app.configuration.rest.properties.ClientProperties;
import com.sandwich.app.exception.ClientInvokeException;
import lombok.SneakyThrows;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

public abstract class AbstractClient<T extends ClientProperties> {

    protected final T properties;
    protected final RestClient restClient;

    protected AbstractClient(T properties, RestClientFactory restClientFactory) {
        this.properties = properties;
        this.restClient = restClientFactory.createRestClient(properties.getUrl());
    }

    protected void handleError(HttpRequest request, ClientHttpResponse response) {
        var statusCode = getStatusCode(response);
        var body = getBody(response);
        throw new ClientInvokeException(statusCode, body);
    }

    @SneakyThrows
    private static HttpStatusCode getStatusCode(ClientHttpResponse response) {
        try {
            return response.getStatusCode();
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @SneakyThrows
    private static String getBody(ClientHttpResponse response) {
        return StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
    }
}
