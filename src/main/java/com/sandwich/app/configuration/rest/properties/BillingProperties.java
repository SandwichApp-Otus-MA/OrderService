package com.sandwich.app.configuration.rest.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingProperties implements ClientProperties {

    private String url;

    private Endpoints endpoints;

    @Getter
    @Setter
    public static class Endpoints {
        private String deposit = "/v1/user-account/deposit/{id}";
        private String createPayment = "/v1/payment/create";
        private String checkStatus = "/v1/payment/check-status/{id}";
    }
}
