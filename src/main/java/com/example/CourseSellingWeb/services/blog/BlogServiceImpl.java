package com.example.CourseSellingWeb.services.blog;

import com.example.CourseSellingWeb.dtos.BlogDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.responses.blog.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface BlogServiceImpl {
    Blog create(BlogDTO blogDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException;

    Blog update(int id, BlogDTO blogDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException;

    void delete(int id);

    Page<BlogResponse> getAllBlogs(int employeeId, LocalDateTime startOfDay, LocalDateTime endOfDay, String keyword, Pageable pageable);

    Optional<Blog> getById(int id) throws DataNotFoundException;

    Map<String, Object> getImageDetail(String publicId) throws Exception;
}
