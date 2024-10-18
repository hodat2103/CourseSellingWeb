package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlogDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;


    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Url size must be greater 5 and smaller 200 ")
    private String imageUrl;


    @JsonProperty("employee_id")
    private int employeeId;
}
