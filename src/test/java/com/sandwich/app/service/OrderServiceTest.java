package com.sandwich.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sandwich.app.domain.dto.OrderDto;
import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final UUID TEST_ID = UUID.randomUUID();

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderService orderService;

    private OrderEntity testOrderEntity;
    private OrderDto testOrderDto;

    @BeforeEach
    void setUp() {
        testOrderEntity = new OrderEntity();
        testOrderEntity.setId(TEST_ID);
        testOrderDto = new OrderDto();
        testOrderDto.setId(TEST_ID);
    }

    @Test
    void get_shouldReturnOrder_whenOrderExists() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(testOrderEntity));
        when(orderMapper.convert(testOrderEntity)).thenReturn(testOrderDto);

        var result = orderService.get(TEST_ID);

        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        verify(orderRepository).findById(TEST_ID);
        verify(orderMapper).convert(testOrderEntity);
    }

    @Test
    void get_shouldThrowEntityNotFoundException_whenOrderDoesNotExist() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> orderService.get(TEST_ID));
        assertEquals("Order Not Found", exception.getMessage());
        verify(orderRepository).findById(TEST_ID);
        verify(orderMapper, never()).convert(any());
    }

    @Test
    void create_shouldSaveNewOrder_whenOrderDoesNotExist() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        when(orderMapper.convert(any(OrderEntity.class), eq(testOrderDto))).thenReturn(testOrderEntity);

        orderService.create(testOrderDto);

        verify(orderRepository).findById(TEST_ID);
        verify(orderMapper).convert(any(OrderEntity.class), eq(testOrderDto));
        verify(orderRepository).save(testOrderEntity);
    }

    @Test
    void create_shouldThrowIllegalStateException_whenOrderAlreadyExists() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(testOrderEntity));

        var exception = assertThrows(IllegalStateException.class, () -> orderService.create(testOrderDto));
        assertEquals("Заказ c id: " + TEST_ID + " уже существует!", exception.getMessage());
        verify(orderRepository).findById(TEST_ID);
        verify(orderMapper, never()).convert(any(), any());
        verify(orderRepository, never()).save(any());
    }
}