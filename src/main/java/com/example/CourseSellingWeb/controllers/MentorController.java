package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.MentorDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Mentor;
import com.example.CourseSellingWeb.responses.MentorResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.mentor.MentorService;
import com.example.CourseSellingWeb.services.mentor.MentorServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/mentors")
@RequiredArgsConstructor
public class MentorController {
    private final MentorServiceImpl mentorService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<ResponseObject> create (@Valid @RequestBody MentorDTO mentorDTO,
                                                  BindingResult result) throws InvalidParamException, DataNotFoundException {
        if(result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        Mentor mentorResponse = mentorService.create(mentorDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(MentorResponse.fromMentor(mentorResponse))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMentor(){
        try {
           List<Mentor> mentors =  mentorService.getAllMentors();
           return ResponseEntity.ok(ResponseObject.builder()
                   .data(mentors)
                   .status(HttpStatus.OK)
                   .build());
        }catch (Exception e){
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMentorById(@PathVariable int id){
        try {
            Optional<Mentor> mentorOptional = mentorService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(mentorOptional
                                    .map(ResponseEntity::ok)
                                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND))))
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(String.join(";", e.getMessage()))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> searchMentor(
            @RequestParam(defaultValue = "0") String keyword) {

        try {
            List<Mentor> mentors = mentorService.searchMentors(keyword);
            if (mentors.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(mentors)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                    @RequestBody MentorDTO mentorDTO){
        try{
            Mentor mentor = mentorService.update(id, mentorDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                            .data(MentorResponse.fromMentor(mentor))
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.UPDATE_FAILED + String.join(";", e.getMessage())))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(@PathVariable int id){
        try{
            mentorService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .build());
        }catch (Exception e) {
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED))
                    .build());
        }
    }
}
