package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDTO {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @JsonProperty("total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @JsonProperty("final_price")
    private BigDecimal finalPrice;

    private String status;

    @JsonProperty("coupon_id")
    private Integer couponId;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("employee_id")
    private int employeeId;

    @JsonProperty("order_detail")
    private OrderDetailDTO orderDetail;

}
