package com.sporebski.couponservice.geolocation.service;

import com.sporebski.couponservice.coupon.service.GeolocationService;
import com.sporebski.couponservice.geolocation.dto.IpApiGeolocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class IpApiGeolocationService implements GeolocationService {

    private final RestClient restClient = RestClient.create();

    @Override
    public String getCountryCode(String ipAddress) {
        IpApiGeolocationResponse result = restClient.get()
                .uri("http://ip-api.com/json/{ipAddress}", ipAddress)
                .retrieve()
                .body(IpApiGeolocationResponse.class);

        return result.getCountryCode();
    }
}
