package com.example.CourseSellingWeb.services.language;

import com.example.CourseSellingWeb.dtos.LanguageDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Language;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LanguageServiceImpl {
    Language create (LanguageDTO languageDTO) throws DataNotFoundException;

    Language getLanguageById(Integer id) throws DataNotFoundException;

    List<Language> getAllLanguages();

    Language update(Integer id, LanguageDTO languageDTO);

    void delete(Integer id);
}
