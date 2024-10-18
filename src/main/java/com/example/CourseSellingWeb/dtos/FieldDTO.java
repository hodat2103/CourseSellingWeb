package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FieldDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @JsonProperty("employee_id")
    private int employeeId;
}
