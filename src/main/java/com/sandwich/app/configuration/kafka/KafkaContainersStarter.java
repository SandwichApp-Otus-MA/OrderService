package com.sandwich.app.configuration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaContainersStarter {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Value("${billing-service.kafka.excluded-consumers}")
    private String excludedListeners;

    public void start() {
        log.info("Запускаются слушатели Kafka - сообщений...");
        kafkaListenerEndpointRegistry
            .getListenerContainers()
            .forEach(this::startContainer);
    }

    private void startContainer(MessageListenerContainer container) {
        if (excludedListeners.contains(container.getListenerId())) {
            log.warn("Kafka - слушатель id = {} добавлен в исключения и не будет запущен", container.getListenerId());
        } else {
            container.getContainerProperties().setObservationEnabled(true);
            container.start();
        }
    }
}
