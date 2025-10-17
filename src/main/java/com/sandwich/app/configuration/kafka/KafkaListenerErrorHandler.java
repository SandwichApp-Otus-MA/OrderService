package com.sandwich.app.configuration.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

import java.util.Optional;

@Slf4j
public class KafkaListenerErrorHandler implements ConsumerAwareListenerErrorHandler {

    @NotNull
    @Override
    public Object handleError(@NotNull Message<?> message,
                              @NotNull ListenerExecutionFailedException exception,
                              @NotNull Consumer<?, ?> consumer) {
        log.debug("Ошибка работы обработчика сообщения из Kafka: {}", message, exception);
        log.error("Ошибка обработчика сообщения из Kafka: {}", Optional.ofNullable(exception.getCause()).orElse(exception).getMessage());
        return message;
    }
}
