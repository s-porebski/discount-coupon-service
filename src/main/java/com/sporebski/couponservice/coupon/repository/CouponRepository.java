package com.sporebski.couponservice.coupon.repository;

import com.sporebski.couponservice.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);

    @Query("UPDATE Coupon c SET c.currentUses = c.currentUses + 1 WHERE c.code = :code AND c.currentUses < c.maxUses")
    @Modifying
    @Transactional
    int incrementUsageIfNotExceeded(@Param("code") String code);

}