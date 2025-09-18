package com.sporebski.couponservice.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporebski.couponservice.common.exception.CouponErrorCode;
import com.sporebski.couponservice.coupon.dto.CreateCouponRequest;
import com.sporebski.couponservice.coupon.dto.UseCouponRequest;
import com.sporebski.couponservice.coupon.model.Coupon;
import com.sporebski.couponservice.coupon.repository.CouponRepository;
import com.sporebski.couponservice.coupon.service.GeolocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GeolocationService geolocationService;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void clearDatabase() {
        couponRepository.deleteAll();
    }

    @Nested
    class CreateCoupon {

        @Test
        void shouldReturn200WhenRequestIsValid() throws Exception {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("WIOSNA2025");
            request.setMaxUses(100);
            request.setCountryCode("PL");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("WIOSNA2025"))
                    .andExpect(jsonPath("$.maxUses").value(100));
        }

        @Test
        void shouldReturn400WhenCouponCodeIsBlank() throws Exception {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("");
            request.setMaxUses(100);
            request.setCountryCode("PL");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid request data."))
                    .andExpect(jsonPath("$.details.code").value("Code must not be blank"));
        }

        @Test
        void shouldReturn400WhenCountryCodeIsNotValidIsoCountryCode() throws Exception {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("WIOSNA2025");
            request.setMaxUses(100);
            request.setCountryCode("AY");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid request data."))
                    .andExpect(jsonPath("$.details.countryCode").value("Value must be a valid ISO 3166-1 alpha-2 code"));
        }
    }

    @Nested
    class UseCoupon {

        @Test
        void shouldReturn200WhenCouponIsUsedSuccessfully() throws Exception {
            Coupon existingCoupon = new Coupon();
            existingCoupon.setCode("WIOSNA2025");
            existingCoupon.setMaxUses(5);
            existingCoupon.setCurrentUses(0);
            existingCoupon.setCountryCode("PL");
            couponRepository.save(existingCoupon);
            UseCouponRequest useRequest = new UseCouponRequest();
            useRequest.setCode("WIOSNA2025");
            when(geolocationService.getCountryCode(anyString())).thenReturn("PL");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/usage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(useRequest)))
                    .andExpect(status().isOk());

            Coupon updatedCoupon = couponRepository.findByCode("WIOSNA2025").orElseThrow();
            assertEquals(1, updatedCoupon.getCurrentUses());
        }

        @Test
        void shouldReturn422WhenCouponUsageIsExceeded() throws Exception {
            Coupon existingCoupon = new Coupon();
            existingCoupon.setCode("WIOSNA2025");
            existingCoupon.setMaxUses(5);
            existingCoupon.setCurrentUses(5);
            existingCoupon.setCountryCode("PL");
            couponRepository.save(existingCoupon);
            UseCouponRequest useRequest = new UseCouponRequest();
            useRequest.setCode("WIOSNA2025");
            when(geolocationService.getCountryCode(anyString())).thenReturn("PL");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/usage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(useRequest)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value(CouponErrorCode.COUPON_USAGE_LIMIT_EXCEEDED.getMessage()));
        }

        @Test
        void shouldReturn404WhenCouponIsNotFound() throws Exception {
            UseCouponRequest useRequest = new UseCouponRequest();
            useRequest.setCode("WIOSNA2025");
            when(geolocationService.getCountryCode(anyString())).thenReturn("PL");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/usage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(useRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(CouponErrorCode.COUPON_NOT_FOUND.getMessage()));
        }

        @Test
        void shouldReturn422WhenUserCountryIsDifferentThanCouponCountry() throws Exception {
            Coupon existingCoupon = new Coupon();
            existingCoupon.setCode("WIOSNA2025");
            existingCoupon.setMaxUses(5);
            existingCoupon.setCurrentUses(5);
            existingCoupon.setCountryCode("PL");
            couponRepository.save(existingCoupon);
            UseCouponRequest useRequest = new UseCouponRequest();
            useRequest.setCode("WIOSNA2025");
            when(geolocationService.getCountryCode(anyString())).thenReturn("US");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/usage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(useRequest)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value(CouponErrorCode.COUPON_COUNTRY_NOT_ALLOWED.getMessage()));
        }
    }
}