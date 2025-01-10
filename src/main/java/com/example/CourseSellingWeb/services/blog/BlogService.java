package com.example.CourseSellingWeb.services.blog;

import com.example.CourseSellingWeb.configurations.ImageConfig;
import com.example.CourseSellingWeb.dtos.BlogDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.responses.blog.BlogResponse;
import com.example.CourseSellingWeb.respositories.BlogRepository;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.utils.ConstantKeys;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BlogService implements BlogServiceImpl{
    @Autowired
    private final BlogRepository blogRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final ImageConfig imageConfig;

    @Override
    public Blog create(BlogDTO blogDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException {
        Employee existsEmployee = employeeRepository.findById(blogDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + blogDTO.getEmployeeId()));
        String uniqueFileName = ConstantKeys.UNIQUE_FILE_NAME + blogDTO.getImageUrl();

        Blog newBlog = Blog.builder()
                .title(blogDTO.getTitle())
                .content(blogDTO.getContent())
                .imageUrl(uniqueFileName)
                .employee(existsEmployee)
                .build();
        Map result = imageConfig.uploadImageFile(multipartFile,uniqueFileName);
        if(result.isEmpty()){
            throw new InvalidParamException("Cannot upload image file");
        }
        return blogRepository.save(newBlog);
    }

    @Override
    public Blog update(int id, BlogDTO blogDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException {
        Blog existsBlog = blogRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        String uniqueFileName = ConstantKeys.UNIQUE_FILE_NAME + blogDTO.getImageUrl();
        boolean imageType = imageConfig.isImageFile(multipartFile);
        if(!imageType){
            throw new InvalidParamException("Failed image type");
        }

        existsBlog.setTitle(blogDTO.getTitle());
        existsBlog.setImageUrl(uniqueFileName);
        existsBlog.setContent(blogDTO.getContent());

        Map result = imageConfig.uploadImageFile(multipartFile, uniqueFileName);
        if(result.isEmpty()){
            throw new InvalidParamException("Cannot upload image file");
        }
        blogRepository.save(existsBlog);

        return existsBlog;
    }

    @Override
    public void delete(int id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        optionalBlog.ifPresent(blogRepository :: delete);
    }

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Page<BlogResponse> getAllBlogs(int employeeId, LocalDateTime startOfDay, LocalDateTime endOfDay, String keyword, Pageable pageable) {
        Page<Blog> blogPage;
        blogPage= blogRepository.searchBlogs(employeeId, startOfDay,endOfDay, keyword, pageable);
        return blogPage.map(BlogResponse::fromBlog);
    }



    @Override
    public Optional<Blog> getById(int id) throws DataNotFoundException {
        Blog existsBlog = blogRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND  + id));
        return Optional.ofNullable(existsBlog);
    }

    @Override
    public Map<String, Object> getImageDetail(String publicId) throws Exception {
        return imageConfig.getImageDetail(publicId);
    }
}
