package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_videos")
@Builder
public class CourseVideo extends BaseEntity{
    public static  final  int MINIMUM_IMAGES_PER_PRODUCT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    @Column(name = "video_url")
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_type")
    private VideoType videoType;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "lesson_number")
    private int lessonNumber;



}
