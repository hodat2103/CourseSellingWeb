package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.CouponCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponConditionRepository extends JpaRepository<CouponCondition, Integer> {

    @Query("SELECT cc FROM CouponCondition cc WHERE cc.coupon.id = :couponId")
    Optional<CouponCondition> getByCourseId(int couponId);
}
