package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouponDTO {
    @Size()
    private String code;
    private boolean active;

    private String description;

    @JsonProperty("employee_id")
    private int employeeId;

}
