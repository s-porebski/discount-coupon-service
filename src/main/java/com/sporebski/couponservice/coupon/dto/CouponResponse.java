package com.sporebski.couponservice.coupon.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CouponResponse {

    private String code;

    private Integer maxUses;

    private Integer currentUses;

    private String country;

    private Instant createdAt;
}
