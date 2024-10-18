package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);



    @Query(value = "SELECT c.* FROM coupons c " +
            "JOIN coupon_conditions cc ON c.id = cc.coupon_id " +
            "WHERE (:keyword IS NULL OR c.description LIKE %:keyword%) " +
            "AND (:minimumOrderValue IS NULL OR cc.minimum_order_value >= :minimumOrderValue) " +
            "AND (cc.expiration_date >= CURDATE() AND (:expirationEndDate IS NULL OR cc.expiration_date <= :expirationEndDate))",
            nativeQuery = true)
    List<Coupon> searchCouponsNative(
            @Param("keyword") String keyword,
            @Param("minimumOrderValue") BigDecimal minimumOrderValue,
            @Param("expirationEndDate") Date expirationEndDate
    );


}
