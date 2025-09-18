package com.sporebski.couponservice.geolocation;

import com.sporebski.couponservice.geolocation.dto.IpApiGeolocationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IpApiGeolocationClient {

    private final RestClient restClient;

    public IpApiGeolocationClient(@Qualifier("ipApiClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public IpApiGeolocationResponse fetchGeolocation(String ipAddress) {
        return restClient.get()
                .uri("/json/{ipAddress}", ipAddress)
                .retrieve()
                .body(IpApiGeolocationResponse.class);
    }
}
