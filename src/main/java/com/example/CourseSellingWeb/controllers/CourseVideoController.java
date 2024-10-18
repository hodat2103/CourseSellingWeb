package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.services.courseVideo.CourseVideoServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/course_videos")
public class CourseVideoController {
    private final CourseVideoServiceImpl courseVideoService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/uploadVideo")
    public String uploadFile(@RequestParam("video") MultipartFile multipartFile, Model model) {
        try {
            Map<String, Object> uploadResult = courseVideoService.uploadFile(multipartFile);

            model.addAttribute("uploadResult", uploadResult);

            return localizationUtils.getLocalizationMessage(MessageKeys.UPLOAD_VIDEO_SUCCESSFULLY);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", localizationUtils.getLocalizationMessage(MessageKeys.UPLOAD_VIDEO_FAILED) + e.getMessage());
            return localizationUtils.getLocalizationMessage(MessageKeys.UPLOAD_VIDEO_FAILED);
        }
    }
}
