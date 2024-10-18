package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User extends BaseEntity implements ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    @JsonManagedReference
    @JsonIgnore
    private Account account;


    @Override
    public Object[] toExcelRow() {
        return new Object[]{id, name,email, phone,address};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Tên học viên","Email","SĐT","Địa chỉ"};
    }
}
