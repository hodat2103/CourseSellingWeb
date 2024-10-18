package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SliderDTO {
    @NotBlank(message = "Url is required")
    @JsonProperty("image_url")
    private String imageUrl;

    private String description;

    @JsonProperty("employee_id")
    private int employeeId;
}
