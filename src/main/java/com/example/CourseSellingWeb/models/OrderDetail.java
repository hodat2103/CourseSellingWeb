package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_details")
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonManagedReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "price")
    private BigDecimal price;
}
