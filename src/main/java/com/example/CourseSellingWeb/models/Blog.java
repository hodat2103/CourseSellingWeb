package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "blogs")
@Builder
@Data
public class Blog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content",  columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

//    @Temporal(TemporalType.DATE)
    private LocalDateTime createAt;

}
