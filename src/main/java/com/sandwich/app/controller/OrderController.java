package com.sandwich.app.controller;

import com.sandwich.app.domain.dto.OrderDto;
import com.sandwich.app.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@Valid @RequestBody OrderDto order) {
        return ResponseEntity.ok(orderService.create(order));
    }
}
