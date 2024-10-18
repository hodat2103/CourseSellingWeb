package com.example.CourseSellingWeb.services.course;

import com.example.CourseSellingWeb.dtos.CourseDTO;
import com.example.CourseSellingWeb.dtos.CourseDiscountDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.responses.course.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public interface CourseServiceImpl {
    Course create(CourseDTO courseDTO, MultipartFile fileVideo) throws DataNotFoundException, IOException;

    Course getCourseById(Integer id) throws DataNotFoundException;

    Course update(Integer id, CourseDTO courseDTO, MultipartFile videoFile) throws DataNotFoundException, IOException;

    void delete(Integer id);

    Page<CourseResponse> getAllCourses(String keyword, Integer fieldId,Integer languageId , PageRequest pageRequest) ;

    List<Course> findCoursesByIds(List<Integer> courseIds);

    List<Course> findAll();

    List<Course> getByForFreeTrue();

    List<CourseDiscountDTO> getCoursesWithDiscounts();

    InputStream getResource(String demoVideoUrl) throws IOException;
     Map<String, Object> getVideoDetails(String publicId) throws Exception;
}
