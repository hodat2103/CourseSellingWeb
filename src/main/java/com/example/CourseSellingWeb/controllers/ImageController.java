package com.example.CourseSellingWeb.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.example.CourseSellingWeb.configurations.CloudinaryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("${api.prefix}/image_detail")
@RequiredArgsConstructor
public class ImageController {
    private final CloudinaryConfig cloudinaryConfig;
    @GetMapping("/{image_url}")
    public Map<String, Object> getImageDetail(@PathVariable("image_url")  String publicId) throws Exception {

        return cloudinaryConfig.cloudinary().api().resource(publicId, ObjectUtils.asMap(
                "resource_type", "image"
        ));
    }
}
