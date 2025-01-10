package com.example.CourseSellingWeb.dtos;

import com.example.CourseSellingWeb.models.VideoType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    @NotBlank (message = "Title is required")
    private String title;

    @JsonProperty("mentor_id")
    private int mentorId;

    @NotBlank(message = "Description not empty")
    private String description;

    @Min(value=0,message = "Price must be greater than or equal to 0")
    private BigDecimal price;

//    @Size(min = 5, max = 200, message = "Url size must be greater 5 and smaller 200 ")
//    @JsonProperty("image_url")
//    private String imageUrl;

    @Size(min = 5, max = 200, message = "Url size must be greater 5 and smaller 200 ")
    @JsonProperty("demo_video_url")
    private String demoVideoUrl;

    @JsonProperty("video_type")
    private VideoType videoType;

    @JsonProperty("field_id")
    private int fieldId;

    @JsonProperty("language_id")
    private int languageId;

    @JsonProperty("employee_id")
    private int employeeId;

    @JsonProperty("for_free")
    private boolean forFree;
}
