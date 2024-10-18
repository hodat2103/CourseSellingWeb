package com.example.CourseSellingWeb.dtos;

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
public class UserDTO {
    @NotBlank(message = "Name can't blank")
    private String name;

    @NotBlank(message = "Email can't blank")
    private String email;

    @NotBlank(message = "Phone can't blank")
    private String phone;

    private String address;

    @NotBlank(message = "Account ID is required")
    @JsonProperty("account_id")
    private int accountId;

}
