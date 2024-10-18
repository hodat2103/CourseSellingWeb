package com.example.CourseSellingWeb.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountAndUserDTO {
    private AccountDTO accountDTO;
    private UserDTO userDTO;
}

