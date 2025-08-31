package com.sandwich.app.configuration.rest.client;

import com.sandwich.app.configuration.rest.AbstractClient;
import com.sandwich.app.configuration.rest.RestClientFactory;
import com.sandwich.app.configuration.rest.properties.BillingProperties;
import com.sandwich.app.domain.dto.payment.PaymentRequest;
import com.sandwich.app.domain.dto.payment.PaymentResponse;
import org.springframework.http.HttpStatusCode;

import java.math.BigDecimal;
import java.util.UUID;

public class BillingClient extends AbstractClient<BillingProperties> {

    public BillingClient(BillingProperties properties, RestClientFactory restClientFactory) {
        super(properties, restClientFactory);
    }

    public void deposit(UUID userId, BigDecimal amount) {
        restClient
            .post()
            .uri(properties.getEndpoints().getDeposit(), uriBuilder -> uriBuilder
                .queryParam("amount", amount)
                .build(userId))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .toBodilessEntity();
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        return restClient
            .post()
            .uri(properties.getEndpoints().getCreatePayment())
            .body(request)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .body(PaymentResponse.class);
    }

    public PaymentResponse checkStatus(UUID paymentId) {
        return restClient
            .post()
            .uri(properties.getEndpoints().getCheckStatus(), paymentId)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .body(PaymentResponse.class);
    }
}
