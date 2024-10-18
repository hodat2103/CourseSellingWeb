package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Field;
import org.hibernate.validator.constraints.ru.INN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    @Query("SELECT f FROM Field f WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR f.name LIKE %:keyword% )")
    Page<Field> findByKeyword(String keyword, Pageable pageable);
}
