package com.example.CourseSellingWeb.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
@Builder
@Data
public class Role extends BaseEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name",  unique = true, nullable = false)
    private String name;


    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
    public static String SALES_PERSON = "SALES PERSON";
    public static String TECHNICAL_STAFF = "TECHNICAL STAFF";


}
