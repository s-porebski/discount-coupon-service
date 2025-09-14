package com.sporebski.couponservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final String error;
    private final String message;
    private final String path;
    private final Integer status;
    private final Map<String, String> details;
    private final Long timestamp;
}
