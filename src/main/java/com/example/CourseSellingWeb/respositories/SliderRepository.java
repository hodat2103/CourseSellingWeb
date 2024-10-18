package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Integer> {
}
