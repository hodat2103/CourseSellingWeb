package com.example.CourseSellingWeb.responses;


import com.example.CourseSellingWeb.models.Coupon;
import com.example.CourseSellingWeb.models.CouponCondition;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponConditionResponse {
    private Integer id;

    @JsonProperty("coupon_id")
    private int couponId;
    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @JsonProperty("expiration_date")
    private Date expirationDate;


    @JsonProperty("minimum_order_value")
    private BigDecimal minimumOrderValue;


    public static CouponConditionResponse fromCouponCondition(CouponCondition couponCondition) {
        CouponConditionResponse couponConditionResponse = CouponConditionResponse.builder()
                .id(couponCondition.getId())
                .couponId(couponCondition.getCoupon().getId())
                .discountValue(couponCondition.getDiscountValue())
                .expirationDate(couponCondition.getExpirationDate())
                .minimumOrderValue(couponCondition.getMinimumOrderValue())
                .build();

        return couponConditionResponse;
    }
}
