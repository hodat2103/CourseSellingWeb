package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldResponse extends BaseResponse {
    private Integer id;

    private String name;

    private String description;


    @JsonProperty("employee_id")
    private Integer employeeId;

    public static FieldResponse fromField(Field field){
        FieldResponse fieldResponse = FieldResponse.builder()
                .id(field.getId())
                .name(field.getName())
                .description(field.getDescription())
                .employeeId(field.getEmployee().getId())
                .build();
        return fieldResponse;
    }
}