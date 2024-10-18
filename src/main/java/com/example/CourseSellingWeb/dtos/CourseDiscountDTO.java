package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDiscountDTO {
    @JsonProperty("course_id")
    private int courseId;

    @JsonProperty("course_title")
    private String courseTitle;

    @JsonProperty("original_price")
    private BigDecimal originalPrice;

    @JsonProperty("discount_value")
    private BigDecimal discountValue;

}
