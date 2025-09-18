package com.sporebski.couponservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode {

    COUPON_CODE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Coupon code already exists."),
    COUPON_USAGE_LIMIT_EXCEEDED(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon usage limit exceeded."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon code does not exist."),
    COUPON_COUNTRY_NOT_ALLOWED(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon is not valid in your country.");

    private final HttpStatus status;

    private final String message;
}
