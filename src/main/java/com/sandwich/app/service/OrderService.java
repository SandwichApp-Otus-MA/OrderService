package com.sandwich.app.service;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.mapper.OrderMapper;
import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.models.model.order.OrderDto;
import com.sandwich.app.models.utils.HashUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final TransactionTemplate transactionTemplate;
    private final SagaExecutor sagaExecutor;
    private final SagaDefinition<OrderEntity> orderSagaDefinition;

    @Transactional(readOnly = true)
    public OrderDto get(UUID id) {
        return repository.findById(id)
            .map(mapper::convert)
            .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    public UUID create(OrderDto order) {
        var newOrderEntity = Optional.ofNullable(transactionTemplate.execute(status -> {
            Optional.ofNullable(order.getId())
                .flatMap(id -> repository.findById(order.getId())).ifPresent(o -> {
                    throw new IllegalStateException("Заказ c id: %s уже существует!".formatted(order.getId()));
                });

            var newOrder = mapper.convert(new OrderEntity(), order);
            var checksum = HashUtils.calculateChecksum(newOrder, Set.of("createdAt"));

            repository.findByChecksum(checksum, Instant.now().minusSeconds(15 * 60)).ifPresent(existOrder -> {
                throw new IllegalStateException("Такой заказ уже существует! id: %s".formatted(existOrder.getId()));
            });

            newOrder.setChecksum(checksum);

            return repository.save(newOrder);
        })).orElseThrow(() -> new IllegalStateException("Не удалось создать заказ!"));

        log.debug("Starting order processing saga for order: {}", newOrderEntity.getId());
        var result = sagaExecutor.executeSaga(orderSagaDefinition, newOrderEntity);

        if (result.isSuccess()) {
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        repository.save(newOrderEntity);
        log.debug("Saga execution details: {}", result.getMessage());
        result.getStepExecutions().forEach(step ->
            log.debug("Step {}: {} (compensation: {})", step.getStepName(), step.getResult(), step.isCompensation()));

        return newOrderEntity.getId();
    }

    @Transactional
    public void changeStatus(UUID orderId, OrderStatus status) {
        var delivery = repository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order with id: %s not found!".formatted(orderId)));

        delivery.setStatus(status);
    }
}
