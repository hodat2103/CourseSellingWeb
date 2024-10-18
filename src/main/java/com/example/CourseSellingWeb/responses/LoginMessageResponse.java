package com.example.CourseSellingWeb.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginMessageResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}
