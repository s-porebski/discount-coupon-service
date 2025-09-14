package com.sporebski.couponservice.coupon.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateCouponRequest implements Serializable {

    private String code;

    private Integer maxUses;

    private String country;
}
