package com.sandwich.app.integration;

import com.sandwich.app.configuration.rest.client.BillingClient;
import com.sandwich.app.domain.dto.payment.PaymentRequest;
import com.sandwich.app.domain.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingClient billingClient;

    public void deposit(UUID userId, BigDecimal amount) {
        log.info("Попытка вернуть денеги за заказ для userId: {}", userId);
        billingClient.deposit(userId, amount);
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Попытка провести оплату для userId: {}", request.getUserId());
        return billingClient.createPayment(request);
    }

    public PaymentResponse checkPaymentStatus(UUID paymentId) {
        log.info("Попытка проверить статус платежа: {}", paymentId);
        return billingClient.checkStatus(paymentId);
    }
}
