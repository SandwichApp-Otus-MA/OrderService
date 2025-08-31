package com.sandwich.app.configuration.rest;

import com.sandwich.app.configuration.rest.client.BillingClient;
import com.sandwich.app.configuration.rest.properties.RestClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
@EnableConfigurationProperties({RestClientProperties.class})
public class RestClientConfig {

    @Bean
    public RestClientFactory restClientFactory(RestClientProperties properties,
                                               RestClient.Builder builder,
                                               List<ClientHttpRequestInterceptor> interceptors) {
        return new RestClientFactory(properties, builder, interceptors);
    }

    @Bean
    public BillingClient billingClient(RestClientProperties properties, RestClientFactory restClientFactory) {
        return new BillingClient(properties.getBilling(), restClientFactory);
    }
}
