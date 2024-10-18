package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "coupon_conditions")
@Builder
@Data
public class CouponCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    @JsonBackReference
    @JsonIgnore
    @ToString.Exclude
    private Coupon coupon;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "minimum_order_value")
    private BigDecimal minimumOrderValue;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
