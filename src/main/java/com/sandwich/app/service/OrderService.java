package com.sandwich.app.service;

import com.sandwich.app.domain.dto.enums.PaymentStatus;
import com.sandwich.app.domain.dto.order.OrderDto;
import com.sandwich.app.domain.dto.payment.PaymentRequest;
import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.integration.BillingService;
import com.sandwich.app.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final TransactionTemplate transactionTemplate;
    private final BillingService billingService;

    @Transactional(readOnly = true)
    public OrderDto get(UUID id) {
        return orderRepository.findById(id)
            .map(orderMapper::convert)
            .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    public UUID create(OrderDto order) {
        AtomicReference<PaymentStatus> paymentStatus = new AtomicReference<>();

        try {
            return Optional.ofNullable(transactionTemplate.execute(status -> {
                    Optional.ofNullable(order.getId())
                        .flatMap(id -> orderRepository.findById(order.getId())).ifPresent(o -> {
                            throw new IllegalStateException("Заказ c id: %s уже существует!".formatted(order.getId()));
                        });

                    var newOrder = orderMapper.convert(new OrderEntity(), order);
                    return orderRepository.save(newOrder);
                })).map(newOrder -> {
                    var response = billingService.createPayment(new PaymentRequest()
                        .setOrderId(newOrder.getId())
                        .setUserId(order.getUserId())
                        .setAmount(order.getPrice())
                        .setDescription("Оплата заказа")
                        //todo: в целевом виде нужен справочник валют
                        .setCurrency("RUB"));

                    // todo: лучше слушать ответ billing-service по кафке
                    var currentStatus = billingService.checkPaymentStatus(response.getId()).getStatus();

                    if (currentStatus == PaymentStatus.FAILED) {
                        orderRepository.delete(newOrder);
                        return null;
                    }

                    paymentStatus.set(currentStatus);

                    return newOrder.getId();
                })
                .orElseThrow(() -> new IllegalStateException("Не удалось создать заказ!"));
        } catch (Exception ex) {
            Optional.of(paymentStatus)
                .filter(s -> s.get() == PaymentStatus.SUCCEEDED)
                .ifPresent(p -> billingService.deposit(order.getUserId(), order.getPrice()));
            throw ex;
        }
    }
}
