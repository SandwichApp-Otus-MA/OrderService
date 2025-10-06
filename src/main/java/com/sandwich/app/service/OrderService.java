package com.sandwich.app.service;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.mapper.OrderMapper;
import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.models.model.order.OrderDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TransactionTemplate transactionTemplate;
    private final SagaExecutor sagaExecutor;
    private final SagaDefinition<OrderEntity> orderSagaDefinition;

    @Transactional(readOnly = true)
    public OrderDto get(UUID id) {
        return orderRepository.findById(id)
            .map(orderMapper::convert)
            .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    public UUID create(OrderDto order) {
        var newOrderEntity = Optional.ofNullable(transactionTemplate.execute(status -> {
            Optional.ofNullable(order.getId())
                .flatMap(id -> orderRepository.findById(order.getId())).ifPresent(o -> {
                    throw new IllegalStateException("Заказ c id: %s уже существует!".formatted(order.getId()));
                });

            var newOrder = orderMapper.convert(new OrderEntity(), order);
            return orderRepository.save(newOrder);
        })).orElseThrow(() -> new IllegalStateException("Не удалось создать заказ!"));

        log.info("Starting order processing saga for order: {}", order.getId());
        var result = sagaExecutor.executeSaga(orderSagaDefinition, newOrderEntity);

        if (result.isSuccess()) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(newOrderEntity);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        log.debug("Saga execution details: {}", result.getMessage());
        result.getStepExecutions().forEach(step ->
            log.debug("Step {}: {} (compensation: {})", step.getStepName(), step.getResult(), step.isCompensation()));

        return newOrderEntity.getId();
    }
}
