package com.sporebski.couponservice.coupon.dto;

import com.sporebski.couponservice.common.exception.validation.CountryCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCouponRequest {

    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotNull(message = "Max uses must not be null")
    @Min(value = 1, message = "Max uses must be at least 1")
    private Integer maxUses;

    @CountryCode
    private String countryCode;
}
