package com.sandwich.app.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class KafkaApplicationEventListener {

    private final KafkaContainersStarter containersStarter;

    @EventListener(ApplicationReadyEvent.class)
    public void handleApplicationReadyEvent() {
        containersStarter.start();
    }
}
