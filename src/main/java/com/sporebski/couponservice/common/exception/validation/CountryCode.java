package com.sporebski.couponservice.common.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface CountryCode {

    String message() default "Value must be a valid ISO 3166-1 alpha-2 code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
