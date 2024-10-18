package com.example.CourseSellingWeb.responses.course;

import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import com.example.CourseSellingWeb.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse extends BaseResponse {
    private Integer id;
    private String title;

    @JsonProperty("mentor_id")
    private int mentorId;

    private String description;

    private BigDecimal price;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("demo_video_url")
    private String demoVideoUrl;

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("language_id")
    private Integer languageId;

    @JsonProperty("employee_id")
    private Integer employeeId;

    @JsonProperty("for_free")
    private boolean forFree;

    public static CourseResponse fromCourse(Course course){
        CourseResponse courseResponse = CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .mentorId(course.getMentor().getId())
                .description(course.getDescription())
                .price(course.getPrice())
                .imageUrl(course.getImageUrl())
                .demoVideoUrl(course.getDemoVideoUrl())
                .fieldId(course.getField().getId())
                .languageId(course.getLanguage().getId())
                .employeeId(course.getEmployee().getId())
                .forFree(course.isForFree())
                .build();
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }
}
