package com.example.CourseSellingWeb.responses.messages;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseMessageResponse {
    private String message;
}
