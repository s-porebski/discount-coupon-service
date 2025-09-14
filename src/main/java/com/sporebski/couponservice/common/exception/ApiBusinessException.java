package com.sporebski.couponservice.common.exception;

public class ApiBusinessException extends RuntimeException {

    public ApiBusinessException(String message) {
        super(message);
    }
}
