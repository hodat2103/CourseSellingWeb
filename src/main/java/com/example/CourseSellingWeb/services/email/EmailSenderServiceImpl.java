package com.example.CourseSellingWeb.services.email;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderServiceImpl {
    void senderEmail(String toEmail,
                     String subject,
                     String body);
}
