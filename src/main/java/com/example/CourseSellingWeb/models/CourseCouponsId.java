package com.example.CourseSellingWeb.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class CourseCouponsId implements Serializable {

    private int courseId;
    private int couponId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCouponsId that = (CourseCouponsId) o;
        return courseId == that.courseId && couponId == that.couponId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, couponId);
    }
}
