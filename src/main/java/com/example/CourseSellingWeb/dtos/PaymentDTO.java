package com.example.CourseSellingWeb.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;
    @JsonProperty("url")
    private String url;
}
