package com.sandwich.app.domain.entity;


import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.models.model.order.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends DomainObject {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "checksum")
    private String checksum;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "restaurant_comment")
    private String restaurantComment;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_comment")
    private String deliveryComment;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "position_ids")
    private List<UUID> positionIds = new ArrayList<>();

    @Transient
    private List<OrderItem> positions = new ArrayList<>();
}
