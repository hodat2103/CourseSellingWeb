package com.example.CourseSellingWeb.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class ResourceNotFound extends RuntimeException{
    private String errorCode;
    private String errorMessage;
}
