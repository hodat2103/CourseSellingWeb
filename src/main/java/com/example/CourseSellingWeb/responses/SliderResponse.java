package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.Slider;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SliderResponse extends BaseResponse {
    private Integer id;

    @JsonProperty("image_url")
    private String imageUrl;

    private String description;


    @JsonProperty("employee_id")
    private Integer employeeId;

    public static SliderResponse fromSlider(Slider slider){
        SliderResponse employeeResponse = SliderResponse.builder()
                .id(slider.getId())
                .imageUrl(slider.getImageUrl())
                .description(slider.getDescription())
                .employeeId(slider.getEmployee().getId())
                .build();
        return employeeResponse;
    }
}
