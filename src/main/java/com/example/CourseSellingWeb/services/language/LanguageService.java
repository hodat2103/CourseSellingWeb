package com.example.CourseSellingWeb.services.language;

import com.example.CourseSellingWeb.dtos.LanguageDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Language;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.respositories.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LanguageService implements LanguageServiceImpl{
    @Autowired
    private final LanguageRepository languageRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Override
    public Language create(LanguageDTO languageDTO) throws DataNotFoundException {
        Employee existsEmployee = employeeRepository.findById(languageDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException("Not found employee with: " + languageDTO.getEmployeeId()));
        Language newLanguage = Language.builder()
                .name(languageDTO.getName())
                .description(languageDTO.getDescription())
                .employee(existsEmployee)
                .build();

        return languageRepository.save(newLanguage);
    }

    @Override
    public Language getLanguageById(Integer id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));
    }

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public Language update(Integer id, LanguageDTO languageDTO) {
        Language existsLanguage = getLanguageById(id);
        existsLanguage.setName(languageDTO.getName());
        existsLanguage.setDescription(languageDTO.getDescription());

        languageRepository.save(existsLanguage);
        return existsLanguage;
    }

    @Override
    public void delete(Integer id) {
        languageRepository.deleteById(id);
    }
}
