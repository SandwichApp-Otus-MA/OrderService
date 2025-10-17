package com.sandwich.app.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandwich.app.models.model.event.OrderEvent;
import com.sandwich.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        containerFactory = "orderListenerContainerFactory",
        topics = "${sandwich.kafka.consumer.topic.order}",
        groupId = "${sandwich.kafka.consumer.group-id}",
        errorHandler = "kafkaListenerErrorHandler",
        autoStartup = "false")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        try {
            var orderEvent = objectMapper.readValue(consumerRecord.value(), OrderEvent.class);
            orderService.changeStatus(orderEvent.getId(), orderEvent.getStatus());
        } catch (Exception ex) {
            log.error("Произошла ошибка при обработке сообщения", ex);
        }
    }
}
