package com.example.CourseSellingWeb.dtos;

import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.VideoType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CourseVideoDTO {

    @JsonProperty("course_id")
    private int courseId;

    @Size(min = 5, max = 200, message = "Url size must be greater 5 and smaller 200")
    @JsonProperty("video_url")
    private String videoUrl;

    @JsonProperty("video_type")
    private VideoType videoType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("lesson_number")
    private int lessonNumber;
}
