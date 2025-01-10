package com.example.CourseSellingWeb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseId implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "course_id")
    private int courseId;


    // Override equals and hashCode to ensure proper behavior in composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCourseId that = (UserCourseId) o;
        return userId == that.userId && courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }
}
