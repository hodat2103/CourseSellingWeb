package com.example.CourseSellingWeb.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sliders")
@Builder
@Data
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
