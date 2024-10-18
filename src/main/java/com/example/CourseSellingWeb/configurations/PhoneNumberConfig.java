package com.example.CourseSellingWeb.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PhoneNumberConfig {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^0\\d{9}$";
        return phoneNumber.matches(regex);
    }
}
