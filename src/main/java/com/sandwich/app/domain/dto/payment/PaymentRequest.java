package com.sandwich.app.domain.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {
    private UUID orderId;
    private UUID userId;
    private BigDecimal amount;
    private String currency;
    private String description;
}
