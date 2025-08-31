package com.sandwich.app.configuration.rest.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties("rest.clients")
public class RestClientProperties {

    @NestedConfigurationProperty
    private TimeoutProperties timeout;

    @NestedConfigurationProperty
    private BillingProperties billing;
}
