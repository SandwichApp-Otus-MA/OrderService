package com.sandwich.app.domain.repository;

import com.sandwich.app.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o where o.checksum = :checksum and o.createdAt > :createdAt")
    Optional<OrderEntity> findByChecksum(String checksum, Instant createdAt);
}