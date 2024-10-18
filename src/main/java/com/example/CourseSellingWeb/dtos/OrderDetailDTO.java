package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("course_id")
    private int courseId;

    private BigDecimal price;

}
