package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.dtos.CourseDiscountDTO;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.responses.course.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Page<Course> findAll(Pageable pageable);//pagination
    @Query("SELECT c FROM Course c WHERE c.forFree = true")
    List<Course> findByForFreeTrue();


    @Query("SELECT c FROM Course c WHERE " +
            "(:fieldId IS NULL OR :fieldId = 0 OR c.field.id = :fieldId)" +
            "AND (:languageId IS NULL OR :languageId = 0 OR c.language.id = :languageId)" +
            "AND (:keyword IS NULL OR :keyword = '' OR c.title LIKE %:keyword%)")
    Page<Course> searchCourses(
            @Param("fieldId") Integer fieldId,
            @Param("languageId") Integer languageId,
            @Param("keyword") String keyword, Pageable pageable
    );

    @Query("SELECT new com.example.CourseSellingWeb.dtos.CourseDiscountDTO(c.id, c.title, c.price, cond.discountValue) " +
            "FROM Course c " +
            "JOIN CouponCondition cond ON cond.coupon.id = c.id " +
            "WHERE cond.expirationDate > CURRENT_DATE " +
            "AND cond.minimumOrderValue <= c.price")
    List<CourseDiscountDTO> findCoursesWithValidCoupons();



    @Query("SELECT c from Course c WHERE c.id IN :courseIds")
    List<Course> findCoursesByIds(@Param("courseIds")List<Integer> courseIds);

    Boolean existsByDemoVideoUrl(String demoVideoUrl);
}
