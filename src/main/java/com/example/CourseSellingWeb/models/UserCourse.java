package com.example.CourseSellingWeb.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_course")
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;



}
