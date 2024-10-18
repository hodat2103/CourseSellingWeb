package com.example.CourseSellingWeb.responses.order;

import com.example.CourseSellingWeb.models.Order;
import com.example.CourseSellingWeb.models.OrderDetail;
import com.example.CourseSellingWeb.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse extends BaseResponse {
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("final_price")
    private BigDecimal finalPrice;

    private String status;

    @JsonProperty("coupon_id")
    private Integer couponId;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private boolean active;

    @JsonProperty("employee_id")
    private Integer employeeId;

    @JsonProperty("course_id")
    private int courseId;

    @JsonProperty("course_price")
    private BigDecimal coursePrice;

    @JsonProperty("order_detail")
    private OrderDetail orderDetail;

    public static OrderResponse fromOrder(Order order) {
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .discountAmount(order.getDiscountAmount())
                .finalPrice(order.getFinalPrice())
                .status(order.getStatus())
                .couponId(order.getCoupon() != null ? order.getCoupon().getId() : null)
                .paymentMethod(order.getPaymentMethod())
                .active(order.isActive())
                .employeeId(order.getEmployee() != null ? order.getEmployee().getId() : null)
                .courseId(order.getOrderDetail().getCourse().getId())
                .coursePrice(order.getOrderDetail().getPrice())
                .orderDetail(order.getOrderDetail())
                .build();

        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());

        return orderResponse;
    }
}
