package com.example.CourseSellingWeb.services.courseVideo;

import com.example.CourseSellingWeb.dtos.CourseVideoDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.CourseVideo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface CourseVideoServiceImpl {
    CourseVideo createCourseVideo(int courseId, CourseVideoDTO courseVideoDTO, MultipartFile videoFile) throws DataNotFoundException, InvalidParamException, IOException;

    CourseVideo update(int courseVideoId, CourseVideoDTO courseVideoDTO, MultipartFile videoFile) throws IOException, DataNotFoundException;

    void deleteCourseVideo(int courseVideoId);

    List<CourseVideo> findCourseVideosByCourseId(int courseId);

    CourseVideo getCourseVideoById(int courseVideoId) throws DataNotFoundException;

    Map<String, Object> uploadFile(MultipartFile multipartFile) throws IOException;
}
