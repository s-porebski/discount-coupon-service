package com.sporebski.couponservice.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UseCouponRequest {

    @NotBlank(message = "Code must not be blank")
    private String code;

}
