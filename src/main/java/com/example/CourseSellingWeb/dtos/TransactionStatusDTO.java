package com.example.CourseSellingWeb.dtos;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusDTO {
    private String status;
    private String message;
    private String data;
}
