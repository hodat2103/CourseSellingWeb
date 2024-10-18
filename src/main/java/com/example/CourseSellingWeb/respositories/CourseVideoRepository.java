package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVideoRepository extends JpaRepository<CourseVideo, Integer> {

    List<CourseVideo> findCourseVideosByCourseId(int courseId);
}
