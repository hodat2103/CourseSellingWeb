package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private int id;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("course_id")
    private int courseId;

    private BigDecimal price;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .courseId(orderDetail.getCourse().getId())
                .price(orderDetail.getPrice())
                .build();

        return orderDetailResponse;
    }
}
