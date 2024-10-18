package com.example.CourseSellingWeb.dtos;

import com.example.CourseSellingWeb.models.Coupon;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CouponConditionDTO {
    @JsonProperty("coupon_id")
    private int couponId;

    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("expiration_date")
    private Date expirationDate;

    @JsonProperty("minimum_order_value")
    private BigDecimal minimumOrderValue;

}
