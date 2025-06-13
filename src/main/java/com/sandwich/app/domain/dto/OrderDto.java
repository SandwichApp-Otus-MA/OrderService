package com.sandwich.app.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sandwich.app.domain.entity.OrderEntity}
 */
@Data
@Accessors(chain = true)
public class OrderDto {
    private UUID id;
    @NotNull
    private UUID userId;
    @Size(min = 1)
    private List<UUID> positionIds;
}