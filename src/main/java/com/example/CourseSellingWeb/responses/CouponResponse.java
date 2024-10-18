package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.Coupon;
import com.example.CourseSellingWeb.models.CouponCondition;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse extends BaseResponse {
    private Integer id;

    private String code;

    private boolean active;

    private String description;


    @JsonProperty("employee_id")
    private Integer employeeId;

    CouponCondition couponConditions;

    public static CouponResponse fromCoupon(Coupon coupon){
        CouponResponse couponResponse = CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .active(coupon.isActive())
                .description(coupon.getDescription())
                .employeeId(coupon.getEmployee().getId())
                .couponConditions(coupon.getCouponCondition())
                .build();
        couponResponse.setCreatedAt(coupon.getCreatedAt());
        couponResponse.setUpdatedAt(coupon.getUpdatedAt());

        return couponResponse;
    }
}
