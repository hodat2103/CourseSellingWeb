package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVideoRepository extends JpaRepository<CourseVideo, Integer> {
    @Query("SELECT COUNT(cv) FROM CourseVideo cv WHERE cv.course.id = :courseId")
    int countByCourseId(int courseId);
    List<CourseVideo> findCourseVideosByCourseId(int courseId);
}
