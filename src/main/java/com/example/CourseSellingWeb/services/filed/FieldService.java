package com.example.CourseSellingWeb.services.filed;

import com.example.CourseSellingWeb.dtos.FieldDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.respositories.FieldRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FieldService implements FieldServiceImpl{
    @Autowired
    private final FieldRepository fieldRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public Field create(FieldDTO fieldDTO) throws DataNotFoundException {
        Employee existsEmployee = employeeRepository.findById(fieldDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException("Not found employee with: " + fieldDTO.getEmployeeId()));

        Field newField = Field.builder()
                .name(fieldDTO.getName())
                .description(fieldDTO.getDescription())
                .employee(existsEmployee)
                .build();

        return fieldRepository.save(newField);
    }

    @Override
    public Optional<Field> getFieldById(Integer id) {
        Field field =  fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        return Optional.ofNullable(field);

    }

    @Override
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    @Override
    public Field update(Integer id, FieldDTO fieldDTO) throws DataNotFoundException {
        Field existsField = fieldRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        existsField.setName(fieldDTO.getName());
        existsField.setDescription(fieldDTO.getDescription());
        fieldRepository.save(existsField);

        return existsField;
    }

    @Override
    public void delete(Integer id) {
        fieldRepository.deleteById(id);
    }
}
