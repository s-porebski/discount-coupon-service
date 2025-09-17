package com.sporebski.couponservice.coupon.service;

import com.sporebski.couponservice.common.exception.ApiBusinessException;
import com.sporebski.couponservice.common.exception.NotFoundException;
import com.sporebski.couponservice.coupon.dto.CouponResponse;
import com.sporebski.couponservice.coupon.dto.CreateCouponRequest;
import com.sporebski.couponservice.coupon.dto.UseCouponRequest;
import com.sporebski.couponservice.coupon.model.Coupon;
import com.sporebski.couponservice.coupon.repository.CouponRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private final GeolocationService geolocationService;

    public CouponResponse createCoupon(CreateCouponRequest couponRequest) {
        String normalizedCode = couponRequest.getCode().toUpperCase();

        if (couponRepository.existsByCode(normalizedCode)) {
            throw new ApiBusinessException("Coupon code already exists");
        }
        Coupon coupon = new Coupon();
        coupon.setCode(normalizedCode);
        coupon.setMaxUses(couponRequest.getMaxUses());
        coupon.setCurrentUses(0);
        coupon.setCountryCode(couponRequest.getCountryCode());

        return mapCouponResponse(couponRepository.save(coupon));
    }

    public void useCoupon(UseCouponRequest couponRequest, HttpServletRequest httpRequest) {
        String normalizedCode = couponRequest.getCode().toUpperCase();
        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new NotFoundException("Coupon code does not exist"));

        if (!isUserCountryValidForCoupon(httpRequest, coupon)) {
            throw new ApiBusinessException("Coupon not valid in your country");
        }
        int updatedRows = couponRepository.incrementUsageIfNotExceeded(normalizedCode);

        if (updatedRows == 0) {
            throw new ApiBusinessException("Coupon usage limit exceeded");
        }
    }

    private boolean isUserCountryValidForCoupon(HttpServletRequest httpRequest, Coupon coupon) {
        return coupon.getCountryCode().equals(geolocationService.getCountryCode(httpRequest.getRemoteAddr()));
    }

    private CouponResponse mapCouponResponse(Coupon coupon) {
        CouponResponse couponResponse = new CouponResponse();
        couponResponse.setCode(coupon.getCode());
        couponResponse.setMaxUses(coupon.getMaxUses());
        couponResponse.setCurrentUses(coupon.getCurrentUses());
        couponResponse.setCountry(coupon.getCountryCode());
        couponResponse.setCreatedAt(coupon.getCreatedAt());

        return couponResponse;
    }
}