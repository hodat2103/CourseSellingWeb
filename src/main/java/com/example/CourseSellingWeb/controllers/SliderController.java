package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.SliderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Slider;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.SliderResponse;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.slider.SliderServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SliderController {
    private final SliderServiceImpl sliderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody SliderDTO sliderDTO,
                                                 @ModelAttribute(name = "file", binding = true) MultipartFile multipartFile,
                                                 BindingResult result) throws InvalidParamException, DataNotFoundException, IOException {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        Slider sliderResponse = sliderService.create(sliderDTO, multipartFile);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(SliderResponse.fromSlider(sliderResponse))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllSliders() {
        try {
            List<Slider> sliders = sliderService.getAllSliders();
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(sliders)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getSliderById(@PathVariable int id) {
        try {
            Slider slider = sliderService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(SliderResponse.fromSlider(slider))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                                 @RequestBody SliderDTO sliderDTO,
                                                 @ModelAttribute(name = "file", binding = false) MultipartFile multipartFile) {
        try {
            Slider updateSlider = sliderService.update(id, sliderDTO, multipartFile);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(SliderResponse.fromSlider(updateSlider))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(@PathVariable int id) {
        try {
            sliderService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_FAILED + e.getMessage()))
                    .build());
        }
    }
}
