package com.example.CourseSellingWeb.services.filed;

import com.example.CourseSellingWeb.dtos.FieldDTO;
import com.example.CourseSellingWeb.dtos.LanguageDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FieldServiceImpl {
    Field create (FieldDTO fieldDTO) throws DataNotFoundException;

    Optional<Field> getFieldById(Integer id);

    List<Field> getAllFields();

    Field update(Integer id, FieldDTO fieldDTO) throws DataNotFoundException;

    void delete(Integer id);
}
