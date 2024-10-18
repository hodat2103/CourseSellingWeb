package com.example.CourseSellingWeb.responses.user;

import com.example.CourseSellingWeb.responses.course.CourseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponse {
    private List<UserResponse> users;
    private int totalPages;
}