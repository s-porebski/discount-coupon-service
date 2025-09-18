package com.sporebski.couponservice.geolocation.service;

import com.sporebski.couponservice.coupon.service.GeolocationService;
import com.sporebski.couponservice.geolocation.IpApiGeolocationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpApiGeolocationService implements GeolocationService {

    private final IpApiGeolocationClient ipApiGeolocationClient;

    @Override
    public String getCountryCode(String ipAddress) {
        return ipApiGeolocationClient.fetchGeolocation(ipAddress)
                .getCountryCode();
    }
}
