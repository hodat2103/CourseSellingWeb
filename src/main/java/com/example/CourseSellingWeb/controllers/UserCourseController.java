package com.example.CourseSellingWeb.controllers;


import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.UserCourseDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.UserCourse;
import com.example.CourseSellingWeb.models.UserCourseId;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.services.user_course.UserCourseServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/user_course")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserCourseController {


    private final UserCourseServiceImpl userCourseService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> addUserCourse(@Valid  @RequestBody UserCourseDTO userCourseDTO) {
        try {
            UserCourse userCourse = userCourseService.addUserCourse(userCourseDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                            .data(userCourse)
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED) + String.join(e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getCoursesByUserId(@PathVariable int userId) {
        List<UserCourse> userCourses = userCourseService.getCoursesByUserId(userId);

        List<Integer> courseIds = userCourses.stream()
                .map(userCourse -> userCourse.getId().getCourseId())
                .collect(Collectors.toList());
        if(courseIds.isEmpty()){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.LIST_EMPTY))
                            .data(courseIds)
                    .build());
        }
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(courseIds)
                        .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<UserCourse>> getUsersByCourseId(@PathVariable int courseId) {
        List<UserCourse> userCourses = userCourseService.getUsersByCourseId(courseId);
        return ResponseEntity.ok(userCourses);
    }

    // Endpoint to get specific user-course relationship
    @GetMapping("/{userId}/{courseId}")
    public ResponseEntity<UserCourse> getUserCourse(
            @PathVariable int userId,
            @PathVariable int courseId
    ) throws DataNotFoundException {
        Optional<UserCourse> userCourse = userCourseService.getUserCourse(userId, courseId);
        return userCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

