package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.FieldDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Field;
import com.example.CourseSellingWeb.models.Language;
import com.example.CourseSellingWeb.responses.FieldResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.filed.FieldService;
import com.example.CourseSellingWeb.services.filed.FieldServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldServiceImpl fieldService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(
            @Valid  @RequestBody FieldDTO fieldDTO, BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(localizationUtils
                                    .getLocalizationMessage(MessageKeys.CREATE_FAILED + String.join(";",errorMessages)))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        Field fieldResponse = fieldService.create(fieldDTO);

        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                        .data(FieldResponse.fromField(fieldResponse))
                        .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllField(){
       try{
           List<Field> fields = fieldService.getAllFields();
           return ResponseEntity.ok(ResponseObject.builder()
                   .data(fields)
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
    public ResponseEntity<ResponseObject> getFieldById(
            @PathVariable Integer id
    ) throws DataNotFoundException {
        Optional<Field> fieldOptional = fieldService.getFieldById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(fieldOptional
                                .map(ResponseEntity::ok)
                                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND))))
                        .status(HttpStatus.OK)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(
            @PathVariable Integer id,
            @RequestBody FieldDTO fieldDTO
    ){
        try{
            Field fieldResponse = fieldService.update(id, fieldDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                            .data(FieldResponse.fromField(fieldResponse))
                            .status(HttpStatus.OK)
                    .build());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED + e.getMessage()))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(
            @PathVariable Integer id
    ){
        try{
            fieldService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_FAILED + e.getMessage()))
                    .build());
        }
    }

}
