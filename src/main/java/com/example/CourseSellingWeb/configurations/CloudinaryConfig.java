package com.example.CourseSellingWeb.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME = "dln3udzvm";
    private final String API_KEY = "413691979578788";
    private final String API_SECRET = "Dg6X56CPDjlU9pNRXvty1tNWBLU";


    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name",CLOUD_NAME,
                "api_key",API_KEY,
                "api_secret",API_SECRET,
                "secure", true

        ));
        return cloudinary;
    }
}
