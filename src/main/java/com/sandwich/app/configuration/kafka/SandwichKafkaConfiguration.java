package com.sandwich.app.configuration.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sandwich")
@RequiredArgsConstructor
public class SandwichKafkaConfiguration {

    @NestedConfigurationProperty
    private KafkaProperties kafka = new KafkaProperties();

    @Bean
    public KafkaAdmin sandwichKafkaAdmin(ObjectProvider<SslBundles> sslBundles) {
        var adminProperties = this.kafka.buildAdminProperties(sslBundles.getIfAvailable());
        return new KafkaAdmin(adminProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> orderListenerContainerFactory(ObjectProvider<SslBundles> sslBundles,
                                                                                                 RecordMessageConverter recordMessageConverter,
                                                                                                 KafkaAdmin sandwichKafkaAdmin,
                                                                                                 CommonLoggingErrorHandler kafkaContainerErrorHandler) {
        var consumerProperties = kafka.buildConsumerProperties(sslBundles.getIfAvailable());
        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProperties);
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        containerFactory.setConsumerFactory(consumerFactory);
        containerFactory.setContainerCustomizer(container -> container.setKafkaAdmin(sandwichKafkaAdmin));
        containerFactory.setRecordMessageConverter(recordMessageConverter);
        containerFactory.setCommonErrorHandler(kafkaContainerErrorHandler);
        return containerFactory;
    }

}
