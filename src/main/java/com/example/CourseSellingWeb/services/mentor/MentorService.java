package com.example.CourseSellingWeb.services.mentor;

import com.example.CourseSellingWeb.dtos.MentorDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Mentor;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.respositories.MentorRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentorService implements MentorServiceImpl {
    @Autowired
    private final MentorRepository mentorRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Override
    public Mentor create(MentorDTO mentorDTO) throws InvalidParamException, DataNotFoundException {
        boolean existsMentor = mentorRepository.existsMentorByEmail(mentorDTO.getEmail());
        if(existsMentor){
            throw new InvalidParamException(("Email existed"));
        }
        Employee existsEmployee = employeeRepository.findById(mentorDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + mentorDTO.getEmployeeId()));
        Mentor newMentor = new Mentor();

        newMentor.setName(mentorDTO.getName());
        newMentor.setEmail(mentorDTO.getEmail());
        newMentor.setMajor(mentorDTO.getMajor());
        newMentor.setExperience(mentorDTO.getExperience());
        newMentor.setEmployee(existsEmployee);

        return mentorRepository.save(newMentor);
    }

    @Override
    public Mentor update(int id, MentorDTO mentorDTO) throws DataNotFoundException {
        Mentor existsMentor = mentorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        Employee existsEmployee = employeeRepository.findById(mentorDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + mentorDTO.getEmployeeId()));
        if (existsMentor != null && existsEmployee != null) {
            existsMentor.setName(mentorDTO.getName());
            existsMentor.setEmail(mentorDTO.getEmail());
            existsMentor.setMajor(mentorDTO.getMajor());
            existsMentor.setExperience(mentorDTO.getExperience());
            existsMentor.setEmployee(existsEmployee);
        }
        mentorRepository.save(existsMentor);
        return existsMentor;
    }

    @Override
    @Transactional
    public void delete(int mentorId) throws DataNotFoundException {
        Optional<Mentor> optionalMentor = mentorRepository.findById(mentorId);
        optionalMentor.ifPresent(mentorRepository :: delete);
    }

    @Override
    public List<Mentor> searchMentors(String keyword) {
        List<Mentor> mentors = mentorRepository.searchMentors(keyword);
        return mentors;
    }

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Override
    public Optional<Mentor> getById(int id) throws DataNotFoundException {
        Mentor existsMentor = mentorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        return Optional.ofNullable(existsMentor);
    }
}
