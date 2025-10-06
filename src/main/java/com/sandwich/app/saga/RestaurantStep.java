package com.sandwich.app.saga;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.mapper.OrderMapper;
import com.sandwich.app.model.SagaStep;
import com.sandwich.app.model.SagaStepResult;
import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.models.model.enums.RestaurantStatus;
import com.sandwich.app.models.model.restaurant.restaurant.RestaurantOrderRequest;
import com.sandwich.app.restservices.service.RestaurantRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantStep implements SagaStep<OrderEntity> {

    private final RestaurantRestService restaurantService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public String getName() {
        return "RESTAURANT_STEP";
    }

    @Override
    public SagaStepResult execute(OrderEntity order) {
        try {
            var response = restaurantService.create(new RestaurantOrderRequest()
                .setOrderId(order.getId())
                .setPositions(orderMapper.convert(order.getPositions()))
                .setDeliveryId(order.getDeliveryId())
                .setComment(order.getRestaurantComment()));

            if (response.getStatus() == RestaurantStatus.ACCEPTED) {
                order.setStatus(OrderStatus.RESTAURANT_ACCEPTED);
                log.info("Restaurant accepted order: {}", order.getId());
                orderRepository.save(order);
                return SagaStepResult.SUCCESS;
            } else {
                order.setStatus(OrderStatus.RESTAURANT_REJECTED);
                log.warn("Restaurant rejected order: {}", order.getId());
                return SagaStepResult.FAILURE;
            }
        } catch (Exception e) {
            log.error("Restaurant notification failed for order: {}", order.getId(), e);
            return SagaStepResult.FAILURE;
        }
    }

    @Override
    public SagaStepResult compensate(OrderEntity order) {
        try {
            restaurantService.cancel(order.getRestaurantId(), order.getId());
            log.info("Restaurant order cancelled successfully: {}", order.getId());
            return SagaStepResult.SUCCESS;
        } catch (Exception e) {
            log.error("Restaurant order cancellation failed: {}", order.getId(), e);
            return SagaStepResult.COMPENSATION_FAILED;
        }
    }
}
