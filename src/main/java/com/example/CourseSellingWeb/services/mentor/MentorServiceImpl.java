package com.example.CourseSellingWeb.services.mentor;

import com.example.CourseSellingWeb.dtos.MentorDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Mentor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MentorServiceImpl {
    Mentor create(MentorDTO mentorDTO) throws InvalidParamException, DataNotFoundException;

    Mentor update(int id, MentorDTO mentorDTO) throws DataNotFoundException;

    void delete(int mentorId) throws DataNotFoundException;

    List<Mentor> searchMentors(String keyword);

    List<Mentor> getAllMentors();

    Optional<Mentor> getById(int id) throws DataNotFoundException;
}
