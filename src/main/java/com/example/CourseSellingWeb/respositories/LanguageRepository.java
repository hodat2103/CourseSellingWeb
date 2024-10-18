package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    @Query("SELECT l FROM Language l WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR l.name LIKE %:keyword% )")
    Page<Language> findByKeyword(String keyword, Pageable pageable);
}
