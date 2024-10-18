package com.example.CourseSellingWeb.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_coupons")
@Builder
@Data
public class CourseCoupons {

    @EmbeddedId
    private CourseCouponsId id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
