package com.sporebski.couponservice.geolocation.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class IpApiGeolocationClientConfig {

    @Bean
    @Qualifier("ipApiClient")
    public RestClient ipApiClient() {
        return RestClient
                .builder()
                .baseUrl("http://ip-api.com")
                .build();
    }
}
