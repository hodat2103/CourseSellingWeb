package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.UserCourse;
import com.example.CourseSellingWeb.models.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    List<UserCourse> findByIdUserId(int userId);

    List<UserCourse> findByIdCourseId(int courseId);

    @Query("SELECT COUNT(uc) > 0 FROM UserCourse uc WHERE uc.id.userId = :userId")
    boolean existsByUserId(@Param("userId") int userId);
}