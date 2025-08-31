package com.sandwich.app.configuration.rest;

import com.sandwich.app.configuration.rest.properties.RestClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
public class RestClientFactory {

    private final RestClient.Builder builder;

    public RestClientFactory(RestClientProperties properties, RestClient.Builder builder, List<ClientHttpRequestInterceptor> interceptors) {
        this.builder = builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .requestFactory(createRequestFactory(properties))
            .requestInterceptors(requestInterceptors -> requestInterceptors.addAll(interceptors));
    }

    private ClientHttpRequestFactory createRequestFactory(RestClientProperties properties) {
        var timeout = properties.getTimeout();
        var connectTimeout = timeout.getConnectTimeout();
        var readTimeout = timeout.getReadTimeout();
        var settings = ClientHttpRequestFactorySettings.defaults()
            .withConnectTimeout(connectTimeout)
            .withReadTimeout(readTimeout);
        return ClientHttpRequestFactoryBuilder.jdk().build(settings);
    }

    public RestClient createRestClient(String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
