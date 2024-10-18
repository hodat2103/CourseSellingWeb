package com.example.CourseSellingWeb.responses.blog;

import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import com.example.CourseSellingWeb.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse extends BaseResponse {

    private Integer id;

    private String title;

    @JsonProperty("image_url")
    private String imageUrl;

    private String content;

    @JsonProperty("employee_id")
    private Integer employeeId;

    public static BlogResponse fromBlog(Blog blog){
        BlogResponse blogResponse = BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .imageUrl(blog.getImageUrl())
                .employeeId(blog.getEmployee().getId())
                .build();
        blogResponse.setCreatedAt(blog.getCreatedAt());
        blogResponse.setUpdatedAt(blog.getUpdatedAt());

        return blogResponse;
    }

}

