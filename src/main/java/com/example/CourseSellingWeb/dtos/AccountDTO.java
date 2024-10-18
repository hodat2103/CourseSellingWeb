package com.example.CourseSellingWeb.dtos;

import com.example.CourseSellingWeb.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password can't blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @NotBlank(message = "Email can't blank")
    private String email;

    @JsonProperty("role_id")
    private int roleId;

}
