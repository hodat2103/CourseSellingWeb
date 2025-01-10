package com.example.CourseSellingWeb.services.user_course;

import com.example.CourseSellingWeb.dtos.UserCourseDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.UserCourse;

import java.util.List;
import java.util.Optional;

public interface UserCourseServiceImpl {
    UserCourse addUserCourse(UserCourseDTO userCourseDTO) throws InvalidParamException;

    List<UserCourse> getUsersByCourseId(int courseId);

    List<UserCourse> getCoursesByUserId(int userId);

    Optional<UserCourse> getUserCourse(int userId, int courseId) throws DataNotFoundException;
}
