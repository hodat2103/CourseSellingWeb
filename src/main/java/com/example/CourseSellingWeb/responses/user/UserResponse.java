package com.example.CourseSellingWeb.responses.user;

import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.User;
import com.example.CourseSellingWeb.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse extends BaseResponse {
    private int id;

    private String name;

    private String email;

    private String phone;

    private String address;

    @JsonProperty("account_id")
    private int accountId;

    public static UserResponse fromUser(User user){
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .accountId(user.getAccount().getId())
                .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }
}