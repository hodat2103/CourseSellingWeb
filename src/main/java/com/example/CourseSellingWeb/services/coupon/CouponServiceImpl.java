package com.example.CourseSellingWeb.services.coupon;

import com.example.CourseSellingWeb.dtos.CouponDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Coupon;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface CouponServiceImpl {

    Coupon create(CouponDTO couponDTO) throws DataNotFoundException;

    String generateCoupon();

    Coupon update(int id, CouponDTO couponDTO) throws DataNotFoundException;

    Optional<Coupon> getById(int id) throws DataNotFoundException;

    List<Coupon> getAllCoupon();

    List<Coupon> searchCouponsNative(String keyword, BigDecimal minimumOrderValue, Date expiration_date);

    void delete(int id);
}
