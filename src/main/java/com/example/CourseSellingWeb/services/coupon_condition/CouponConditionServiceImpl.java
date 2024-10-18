package com.example.CourseSellingWeb.services.coupon_condition;

import com.example.CourseSellingWeb.dtos.CouponConditionDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.CouponCondition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CouponConditionServiceImpl {

    CouponCondition create(CouponConditionDTO couponConditionDTO) throws DataNotFoundException;

    CouponCondition update(int id, CouponConditionDTO couponConditionDTO) throws DataNotFoundException;

    Optional<CouponCondition> getById(int id) throws DataNotFoundException;

    Optional<CouponCondition> getByCouponId(int couponId) throws DataNotFoundException;

    void delete(int id);
}
