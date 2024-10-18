package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.responses.blog.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
//    @Query("SELECT b FROM Blog b WHERE " +
//            "(:employeeId IS NULL OR :employeeId = 0 OR b.employee.id = :employeeId) " +
//            "AND (:createAt IS NULL OR b.createdAt = :createAt) " +
//            "AND (:keyword IS NULL OR :keyword = '' OR b.title LIKE CONCAT('%', :keyword, '%'))")
//    Page<Blog> searchBlogs(
//            @Param("employeeId") Integer employeeId,
//            @Param("createAt") LocalDateTime createAt,
//            @Param("keyword") String keyword,
//            Pageable pageable
//    );
    @Query("SELECT b FROM Blog b WHERE " +
            "(:employeeId IS NULL OR :employeeId = 0 OR b.employee.id = :employeeId) " +
            "AND (:startOfDay IS NULL OR b.createdAt >= :startOfDay) " +
            "AND (:endOfDay IS NULL OR b.createdAt <= :endOfDay) " +
            "AND (:keyword IS NULL OR :keyword = '' OR b.title LIKE CONCAT('%', :keyword, '%'))")
    Page<Blog> searchBlogs(
            @Param("employeeId") Integer employeeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("keyword") String keyword,
            Pageable pageable);


}
