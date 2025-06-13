package com.sandwich.app.service;

import com.sandwich.app.domain.dto.OrderDto;
import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderDto get(UUID id) {
        return orderRepository.findById(id)
            .map(orderMapper::convert)
            .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    @Transactional
    public void create(OrderDto order) {
        Optional.ofNullable(order.getId())
            .flatMap(id -> orderRepository.findById(order.getId())).ifPresent(o -> {
                throw new IllegalStateException("Заказ c id: %s уже существует!".formatted(order.getId()));
            });

        var newOrder = orderMapper.convert(new OrderEntity(), order);
        orderRepository.save(newOrder);
    }
}
