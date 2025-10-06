package com.sandwich.app.configuration;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.saga.DeliveryStep;
import com.sandwich.app.saga.PaymentStep;
import com.sandwich.app.saga.RestaurantStep;
import com.sandwich.app.service.SagaDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SagaConfiguration {

    @Bean
    public SagaDefinition<OrderEntity> orderSagaDefinition(PaymentStep paymentStep,
                                                           RestaurantStep restaurantStep,
                                                           DeliveryStep deliveryStep) {
        return new SagaDefinition<>(List.of(paymentStep, restaurantStep, deliveryStep));
    }
}

