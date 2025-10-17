package com.sandwich.app.configuration.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class KafkaConfiguration {

    // Обрабатывает ошибки consumer-а на уровне слушателя
    @Bean
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        return new KafkaListenerErrorHandler();
    }

    // Обрабатывает ошибки consumer-а на уровне контейнера
    @Bean
    public CommonLoggingErrorHandler kafkaContainerErrorHandler() {
        return new CommonLoggingErrorHandler();
    }

    // Преобразует входящие сообщения consumer-а string -> json
    @Bean
    public RecordMessageConverter recordMessageConverter(ObjectMapper objectMapper) {
        return new StringJsonMessageConverter(objectMapper);
    }

    public static @NotNull Function<ProducerRecord<?, ?>, Map<String, String>> getMicrometerTagsProvider() {
        return producerRecord -> Map.of("topic", producerRecord.topic());
    }
}
