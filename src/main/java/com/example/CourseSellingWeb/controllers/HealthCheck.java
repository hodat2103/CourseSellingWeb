package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/health_check")
@RequiredArgsConstructor
public class HealthCheck {
//    @CrossOrigin(origins = "http://localhost:8080")
    private final LocalizationUtils localizationUtils;
    @GetMapping
    public ResponseEntity<ResponseObject> healthCheck() {
        return ResponseEntity.ok(ResponseObject.builder()
                        .message(localizationUtils.getLocalizationMessage(MessageKeys.HEALTH_CHECK_RUNNING))
                        .status(HttpStatus.ACCEPTED)
                .build());
    }

}
