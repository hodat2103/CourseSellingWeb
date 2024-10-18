package com.example.CourseSellingWeb.services.user;

import com.example.CourseSellingWeb.dtos.UserDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.User;
import com.example.CourseSellingWeb.responses.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserServiceImpl {
    User create(UserDTO userDTO) throws DataNotFoundException;

    User update(int id, UserDTO userDTO) throws DataNotFoundException;

    Optional<User> getById(int id) throws DataNotFoundException;

    Optional<User> getByAccountId(int accountId) throws DataNotFoundException;

    Page<UserResponse> searchUsers(String keyword, Pageable pageable);

    List<User> getAll();

    void delete(int id);
}
