package com.sandwich.app.saga;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.model.SagaStep;
import com.sandwich.app.model.SagaStepResult;
import com.sandwich.app.models.model.billing.payment.PaymentRequest;
import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.restservices.service.BillingRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStep implements SagaStep<OrderEntity> {

    private final BillingRestService billingService;
    private final OrderRepository orderRepository;

    @Override
    public String getName() {
        return "PAYMENT_STEP";
    }

    @Override
    public SagaStepResult execute(OrderEntity order) {
        try {
            var response = billingService.createPayment(new PaymentRequest()
                .setOrderId(order.getId())
                .setUserId(order.getUserId())
                .setAmount(order.getAmount())
                .setDescription("Оплата заказа")
                .setCurrency("RUB"));

            order.setPaymentId(response.getId());
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
            log.info("Payment processed successfully for order: {}", order.getId());
            orderRepository.save(order);
            return SagaStepResult.SUCCESS;
        } catch (Exception e) {
            log.error("Payment failed for order: {}", order.getId(), e);
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            return SagaStepResult.FAILURE;
        }
    }

    @Override
    public SagaStepResult compensate(OrderEntity order) {
        try {
            if (order.getPaymentId() != null) {
                billingService.refund(order.getUserId(), order.getPaymentId());
                log.info("Payment refunded successfully for order: {}", order.getId());
            }
            return SagaStepResult.SUCCESS;
        } catch (Exception e) {
            log.error("Payment refund failed for order: {}", order.getId(), e);
            return SagaStepResult.COMPENSATION_FAILED;
        }
    }
}
