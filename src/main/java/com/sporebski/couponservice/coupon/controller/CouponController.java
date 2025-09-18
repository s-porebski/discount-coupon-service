package com.sporebski.couponservice.coupon.controller;

import com.sporebski.couponservice.coupon.dto.CouponResponse;
import com.sporebski.couponservice.coupon.dto.CreateCouponRequest;
import com.sporebski.couponservice.coupon.dto.UseCouponRequest;
import com.sporebski.couponservice.coupon.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @PostMapping("/usage")
    public ResponseEntity<Void> useCoupon(@Valid @RequestBody UseCouponRequest useCouponRequest, HttpServletRequest httpServletRequest) {
        couponService.useCoupon(useCouponRequest, httpServletRequest);

        return ResponseEntity.ok().build();
    }
}
