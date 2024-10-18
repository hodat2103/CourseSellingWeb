package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mentors")
@Builder
public class Mentor extends BaseEntity implements ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "major")
    private String major;

    @Column(name = "experience")
    private String experience;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @Override
    public Object[] toExcelRow() {
        return new Object[]{id, name, email, major, experience,employee.getName()};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Tên người hướng dẫn","Email","Chuyên ngành","Kinh nghiệm","Nhân viên tạo"};
    }
}
