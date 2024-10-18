package com.example.CourseSellingWeb.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fields")
@Builder
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @OneToMany(mappedBy = "course_field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<Course> courses;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
