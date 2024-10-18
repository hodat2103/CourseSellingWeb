package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @OneToOne
    @JsonProperty("account_id")
    private int account;

    @JsonProperty("position")
    private String position;
}
