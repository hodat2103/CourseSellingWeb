package com.example.CourseSellingWeb.responses.course;

import com.example.CourseSellingWeb.models.BaseEntity;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.CourseVideo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseVideoResponse extends BaseEntity {
    private Integer id;

    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("video_url")
    private String videoUrl;

    @JsonProperty("video_type")
    private String videoType;

    private String title;

    private String description;

    @JsonProperty("lesson_number")
    private Integer lessonNumber;


    public static CourseVideoResponse fromCourse(CourseVideo courseVideo){
        CourseVideoResponse courseVideoResponse = CourseVideoResponse.builder()
                .id(courseVideo.getId())
                .courseId(courseVideo.getCourse().getId())
                .title(courseVideo.getTitle())
                .videoUrl(courseVideo.getVideoUrl())
                .description(courseVideo.getDescription())
                .videoType(courseVideo.getVideoUrl())
                .lessonNumber(courseVideo.getLessonNumber())
                .build();
        courseVideoResponse.setCreatedAt(courseVideo.getCreatedAt());
        courseVideoResponse.setUpdatedAt(courseVideo.getUpdatedAt());

        return courseVideoResponse;
    }
}
