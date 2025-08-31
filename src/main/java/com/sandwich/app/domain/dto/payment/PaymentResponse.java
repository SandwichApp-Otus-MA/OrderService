package com.sandwich.app.domain.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sandwich.app.domain.dto.enums.PaymentStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private UUID id;
    private PaymentStatus status;
    private String errorMessage;
}
