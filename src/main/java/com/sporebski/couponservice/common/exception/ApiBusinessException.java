package com.sporebski.couponservice.common.exception;

import lombok.Getter;

@Getter
public class ApiBusinessException extends RuntimeException {

    private final CouponErrorCode errorCode;

    public ApiBusinessException(CouponErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
