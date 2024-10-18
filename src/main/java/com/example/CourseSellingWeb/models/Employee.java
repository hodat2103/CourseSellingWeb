package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employees")
@Builder
public class Employee extends BaseEntity implements ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;

    @Column(name = "position")
    private String position;


    @Override
    public Object[] toExcelRow() {
        return new Object[]{id, name, email, phone, position};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Tên nhân viên","Email","SĐT","Chức vụ"};
    }
}
