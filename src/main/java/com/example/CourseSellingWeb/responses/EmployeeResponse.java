package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.BaseEntity;
import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.responses.blog.BlogResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse extends BaseResponse {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String position;

    @JsonProperty("account_id")
    private Integer accountId;

    public static EmployeeResponse fromEmployee(Employee employee){
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .position(employee.getPosition())
                .accountId(employee.getAccount().getId())
                .build();
        employeeResponse.setCreatedAt(employee.getCreatedAt());
        employeeResponse.setUpdatedAt(employee.getUpdatedAt());

        return employeeResponse;
    }
}
