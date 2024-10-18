package com.example.CourseSellingWeb.responses;

import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageResponse extends BaseResponse {
    private Integer id;

    private String name;

    private String description;


    @JsonProperty("employee_id")
    private Integer employeeId;

    public static LanguageResponse fromLanguage(Language language){
        LanguageResponse languageResponse = LanguageResponse.builder()
                .id(language.getId())
                .name(language.getName())
                .description(language.getDescription())
                .employeeId(language.getEmployee().getId())
                .build();
        return languageResponse;
    }
}
