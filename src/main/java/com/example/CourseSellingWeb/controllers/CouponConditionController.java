package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.CouponConditionDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.CouponCondition;
import com.example.CourseSellingWeb.responses.CouponConditionResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.services.coupon_condition.CouponConditionServiceImpl;
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
@RequestMapping("${api.prefix}/coupon_conditions")
@RequiredArgsConstructor
public class CouponConditionController {
    private final CouponConditionServiceImpl couponConditionService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/{id}")
    public ResponseEntity<ResponseObject> create(
                                                 @Valid @RequestBody CouponConditionDTO couponConditionDTO,
                                                 BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        CouponCondition responseCouponCondition = couponConditionService.create(couponConditionDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(CouponConditionResponse.fromCouponCondition(responseCouponCondition))
                .status(HttpStatus.OK)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable int id) {
        try {
            Optional<CouponCondition> couponConditionResponse = couponConditionService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(couponConditionResponse
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils
                                    .getLocalizationMessage(MessageKeys.NOT_FOUND))))
                    .status(HttpStatus.OK)
                    .build());


        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                                 @Valid @RequestBody CouponConditionDTO couponConditionDTO){
        try{
            CouponCondition updateCouponCondition = couponConditionService.update(id,couponConditionDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(CouponConditionResponse.fromCouponCondition(updateCouponCondition))
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return  ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.UPDATE_FAILED + String.join(";", e.getMessage())))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable int id){
        try {
            couponConditionService.delete(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.DELETE_FAILED + String.join(";", e.getMessage())))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
}
