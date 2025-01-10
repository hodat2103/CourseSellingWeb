package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.CouponDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Coupon;
import com.example.CourseSellingWeb.responses.CouponResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.services.coupon.CouponServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class CouponController {
    private final CouponServiceImpl couponService;
    private final LocalizationUtils localizationUtils;
    @GetMapping("/generate_code")
    public String generateCode() {
        return couponService.generateCoupon();
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody CouponDTO couponDTO,
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

        Coupon responseCoupon = couponService.create(couponDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(CouponResponse.fromCoupon(responseCoupon))
                .status(HttpStatus.OK)
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchCouponsNative( @RequestParam(value = "keyword",required = false) String keyword,
                                                               @RequestParam(value = "minimum_order_value",required = false) BigDecimal minimumOrderValue,
                                                               @RequestParam(value = "expiration_date",required = false)
                                                                   @DateTimeFormat(pattern = "dd/MM/yyyy") Date expirationDate){
       try{
           List<Coupon> coupons = couponService.searchCouponsNative(keyword,minimumOrderValue,expirationDate);

           if (coupons.isEmpty()) {
               return ResponseEntity.noContent().build();
           }

           return ResponseEntity.ok(ResponseObject.builder()
                   .data(coupons)
                   .status(HttpStatus.OK)
                   .build());
       }catch (Exception e){
           return ResponseEntity.ok(ResponseObject.builder()
                   .message(String.join(";", e.getMessage()))
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .build());
       }
    }
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllCoupons() {
        try {
            List<Coupon> coupons = couponService.getAllCoupon();
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(coupons)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCouponById(@PathVariable int id) {
        try {
            Optional<Coupon> couponOptional = couponService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(couponOptional
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND + id))))
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
                                                 @Valid @RequestBody CouponDTO couponDTO){
        try{
            Coupon updateCoupon = couponService.update(id,couponDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(CouponResponse.fromCoupon(updateCoupon))
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
            couponService.delete(id);
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
