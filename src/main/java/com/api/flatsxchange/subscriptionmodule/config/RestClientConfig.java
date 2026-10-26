package com.api.flatsxchange.subscriptionmodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;



@Configuration
public class RestClientConfig {

    @Bean
    public RestClient subscriptionRestClient(
            @Value("${subscription.api.base-url}") String baseUrl,
            @Value("${subscription.api.product-key}") String productKey) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-product-key", productKey)
                .build();
    }
}
