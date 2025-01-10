package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserCourseDTO {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("course_id")
    private int courseId;



}
