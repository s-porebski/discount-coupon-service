package com.sporebski.couponservice.geolocation.service;

import com.sporebski.couponservice.geolocation.IpApiGeolocationClient;
import com.sporebski.couponservice.geolocation.dto.IpApiGeolocationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpApiGeolocationServiceTest {

    @InjectMocks
    private IpApiGeolocationService geolocationService;

    @Mock
    private IpApiGeolocationClient ipApiGeolocationClient;

    @Test
    void shouldReturnCountryCode() {
        IpApiGeolocationResponse response = new IpApiGeolocationResponse();
        response.setCountryCode("PL");
        when(ipApiGeolocationClient.fetchGeolocation("5.5.5.5")).thenReturn(response);

        String country = geolocationService.getCountryCode("5.5.5.5");

        assertEquals("PL", country);
    }
}