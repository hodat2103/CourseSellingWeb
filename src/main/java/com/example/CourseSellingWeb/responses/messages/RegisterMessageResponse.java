package com.example.CourseSellingWeb.responses.messages;

import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.AccountAndUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMessageResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("account")
    private AccountAndUser accountAndUser;
}
