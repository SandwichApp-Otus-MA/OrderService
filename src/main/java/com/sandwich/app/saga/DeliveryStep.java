package com.sandwich.app.saga;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.repository.OrderRepository;
import com.sandwich.app.model.SagaStep;
import com.sandwich.app.model.SagaStepResult;
import com.sandwich.app.models.model.delivery.DeliveryDto;
import com.sandwich.app.models.model.enums.OrderStatus;
import com.sandwich.app.restservices.service.DeliveryRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryStep implements SagaStep<OrderEntity> {

    private final DeliveryRestService deliveryService;
    private final OrderRepository orderRepository;

    @Override
    public String getName() {
        return "DELIVERY_STEP";
    }

    @Override
    public SagaStepResult execute(OrderEntity order) {
        try {
            var deliveryId = deliveryService.create(new DeliveryDto()
                .setOrderId(order.getId())
                .setRestaurantId(order.getRestaurantId())
                .setAddress(order.getDeliveryAddress())
                .setComment(order.getDeliveryComment()));
            order.setDeliveryId(deliveryId);
            order.setStatus(OrderStatus.DELIVERY_ASSIGNED);
            log.info("Delivery assigned successfully for order: {}", order.getId());
            return SagaStepResult.SUCCESS;
        } catch (Exception e) {
            log.error("Delivery assignment failed for order: {}", order.getId(), e);
            order.setStatus(OrderStatus.DELIVERY_FAILED);
            return SagaStepResult.FAILURE;
        } finally {
            orderRepository.save(order);
        }
    }

    @Override
    public SagaStepResult compensate(OrderEntity order) {
        try {
            if (order.getDeliveryId() != null) {
                deliveryService.cancel(order.getDeliveryId(), order.getId());
                log.info("Delivery cancelled successfully for order: {}", order.getId());
            }
            return SagaStepResult.SUCCESS;
        } catch (Exception e) {
            log.error("Delivery cancellation failed for order: {}", order.getId(), e);
            return SagaStepResult.COMPENSATION_FAILED;
        }
    }
}
