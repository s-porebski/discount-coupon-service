package com.sporebski.couponservice.common.exception.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;
import java.util.Set;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    @Override
    public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
        return country != null && ISO_COUNTRIES.contains(country);
    }
}
