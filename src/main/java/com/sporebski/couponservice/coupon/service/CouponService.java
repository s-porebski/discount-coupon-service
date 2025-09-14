package com.sporebski.couponservice.coupon.service;

import com.sporebski.couponservice.coupon.dto.CouponResponse;
import com.sporebski.couponservice.coupon.dto.CreateCouponRequest;
import com.sporebski.couponservice.coupon.model.Coupon;
import com.sporebski.couponservice.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponResponse createCoupon(CreateCouponRequest couponRequest) {
        Coupon coupon = new Coupon();
        coupon.setCode(couponRequest.getCode().toUpperCase());
        coupon.setMaxUses(couponRequest.getMaxUses());
        coupon.setCurrentUses(0);
        coupon.setCountry(couponRequest.getCountry());

        return mapCouponResponse(couponRepository.save(coupon));
    }

    private CouponResponse mapCouponResponse(Coupon coupon) {
        CouponResponse couponResponse = new CouponResponse();
        couponResponse.setCode(coupon.getCode());
        couponResponse.setMaxUses(coupon.getMaxUses());
        couponResponse.setCurrentUses(coupon.getCurrentUses());
        couponResponse.setCountry(coupon.getCountry());
        couponResponse.setCreatedAt(coupon.getCreatedAt());

        return couponResponse;
    }
}