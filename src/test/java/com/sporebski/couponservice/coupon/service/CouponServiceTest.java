package com.sporebski.couponservice.coupon.service;

import com.sporebski.couponservice.common.exception.ApiBusinessException;
import com.sporebski.couponservice.common.exception.CouponErrorCode;
import com.sporebski.couponservice.coupon.dto.CouponResponse;
import com.sporebski.couponservice.coupon.dto.CreateCouponRequest;
import com.sporebski.couponservice.coupon.dto.UseCouponRequest;
import com.sporebski.couponservice.coupon.model.Coupon;
import com.sporebski.couponservice.coupon.repository.CouponRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private GeolocationService geolocationService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Nested
    class CreateCoupon {

        @Test
        void shouldCreateCouponWithZeroCurrentUses() {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("DISCOUNT10");
            request.setMaxUses(100);
            request.setCountryCode("US");
            when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

            CouponResponse coupon = couponService.createCoupon(request);

            assertEquals(0, coupon.getCurrentUses());
        }

        @Test
        void shouldNormalizeCouponCodeToUpperCaseString() {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("disCount10");
            request.setMaxUses(100);
            request.setCountryCode("US");
            when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

            CouponResponse coupon = couponService.createCoupon(request);

            assertEquals("DISCOUNT10", coupon.getCode());
        }

        @Test
        void shouldThrowExceptionWhenCouponCodeExists() {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("DISCOUNT10");
            request.setMaxUses(100);
            request.setCountryCode("US");
            when(couponRepository.existsByCode("DISCOUNT10")).thenReturn(true);

            assertThrows(ApiBusinessException.class, () -> couponService.createCoupon(request));
        }

        @Test
        void shouldThrowExceptionWhenCouponCodeExistsAfterNormalization() {
            CreateCouponRequest request = new CreateCouponRequest();
            request.setCode("disCount10");
            request.setMaxUses(100);
            request.setCountryCode("US");
            when(couponRepository.existsByCode("DISCOUNT10")).thenReturn(true);

            assertThrows(ApiBusinessException.class, () -> couponService.createCoupon(request));
        }
    }

    @Nested
    class UseCoupon {

        @Test
        void shouldUseCouponWhenCouponCodeIsValidButInLowerCase() {
            UseCouponRequest request = new UseCouponRequest();
            request.setCode("discount10");
            Coupon coupon = new Coupon();
            coupon.setCountryCode("PL");
            when(couponRepository.findByCode("DISCOUNT10")).thenReturn(Optional.of(coupon));
            when(geolocationService.getCountryCode(httpServletRequest.getRemoteAddr())).thenReturn("PL");
            when(couponRepository.incrementUsageIfNotExceeded("DISCOUNT10")).thenReturn(1);

            couponService.useCoupon(request, httpServletRequest);
        }

        @Test
        void shouldThrowExceptionWhenCouponIsUsedUp() {
            UseCouponRequest request = new UseCouponRequest();
            request.setCode("DISCOUNT10");
            Coupon coupon = new Coupon();
            coupon.setCountryCode("PL");
            when(couponRepository.findByCode("DISCOUNT10")).thenReturn(Optional.of(coupon));
            when(geolocationService.getCountryCode(httpServletRequest.getRemoteAddr())).thenReturn("PL");
            when(couponRepository.incrementUsageIfNotExceeded("DISCOUNT10")).thenReturn(0);

            assertThrows(ApiBusinessException.class, () -> couponService.useCoupon(request, httpServletRequest));
        }

        @Test
        void shouldThrowExceptionWhenUserCountryIsDifferentThanCouponCountry() {
            UseCouponRequest request = new UseCouponRequest();
            request.setCode("DISCOUNT10");
            Coupon coupon = new Coupon();
            coupon.setCountryCode("PL");
            when(couponRepository.findByCode("DISCOUNT10")).thenReturn(Optional.of(coupon));
            when(geolocationService.getCountryCode(httpServletRequest.getRemoteAddr())).thenReturn("US");

            assertThrows(ApiBusinessException.class, () -> couponService.useCoupon(request, httpServletRequest));
        }

        @Test
        void shouldThrowExceptionWhenCouponDoesNotExist() {
            UseCouponRequest request = new UseCouponRequest();
            request.setCode("DISCOUNT10");
            when(couponRepository.findByCode("DISCOUNT10")).thenReturn(Optional.empty());

            ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class, () -> couponService.useCoupon(request, httpServletRequest));

            assertEquals(CouponErrorCode.COUPON_NOT_FOUND, apiBusinessException.getErrorCode());
        }

    }
}