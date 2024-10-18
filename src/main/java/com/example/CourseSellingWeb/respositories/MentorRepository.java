package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    @Query("SELECT m FROM Mentor m WHERE " +
            "(:keyword IS NULL OR m.name LIKE %:keyword%) " +
            "OR (:keyword IS NULL OR m.major LIKE %:keyword%) " +
            "OR (:keyword IS NULL OR m.experience LIKE %:keyword%)")
    List<Mentor> searchMentors(@Param("keyword") String keyword);

    boolean existsMentorByEmail(String email);
}
