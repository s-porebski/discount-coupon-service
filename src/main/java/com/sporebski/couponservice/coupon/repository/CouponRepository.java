package com.sporebski.couponservice.coupon.repository;

import com.sporebski.couponservice.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCode(String code);

}