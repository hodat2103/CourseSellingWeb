package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.FieldDTO;
import com.example.CourseSellingWeb.dtos.LanguageDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import com.example.CourseSellingWeb.responses.LanguageResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.filed.FieldServiceImpl;
import com.example.CourseSellingWeb.services.language.LanguageServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/languages")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class LanguageController {
    private final LanguageServiceImpl languageService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(
            @Valid @RequestBody LanguageDTO languageDTO, BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        Language languageResponse = languageService.create(languageDTO);

        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                        .data(LanguageResponse.fromLanguage(languageResponse))
                        .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLanguages(){
        try{
            List<Language> languages = languageService.getAllLanguages();
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(languages)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";",e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getLanguageById(
            @PathVariable Integer id
    ) throws DataNotFoundException {
        Language language = languageService.getLanguageById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(LanguageResponse.fromLanguage(language))
                        .status(HttpStatus.OK)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(
            @PathVariable Integer id,
            @RequestBody LanguageDTO languageDTO
    ){
        try{
            Language updateLanguage = languageService.update(id, languageDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                            .data(LanguageResponse.fromLanguage(updateLanguage))
                            .status(HttpStatus.OK)
                    .build());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED+ e.getMessage()))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(
            @PathVariable Integer id
    ){
        try{
            languageService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_FAILED+ e.getMessage()))
                    .build());
        }
    }
}
