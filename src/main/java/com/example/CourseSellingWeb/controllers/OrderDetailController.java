package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.OrderDetailDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.OrderDetail;
import com.example.CourseSellingWeb.responses.OrderDetailResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.order_detail.OrderDetailServiceImpl;
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
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailServiceImpl orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    private ResponseEntity<ResponseObject> create(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                                  BindingResult result) throws DataNotFoundException {
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());}
        OrderDetail orderDetailResponse = orderDetailService.create(orderDetailDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(OrderDetailResponse.fromOrderDetail(orderDetailResponse))
                .status(HttpStatus.OK)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable int id){
        try{
            Optional<OrderDetail> orderDetailOptional = orderDetailService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(orderDetailOptional.map(ResponseEntity :: ok)
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND))))
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<ResponseObject> getByOrderId(@PathVariable("order_id") int orderId){
        try{
            List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(orderDetails)
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                                 @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try{
            OrderDetail orderDetail = orderDetailService.update(id, orderDetailDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(orderDetail)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED) + String.join(";",e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(@PathVariable int id){
        try{
            orderDetailService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .build());
        }catch (Exception e){
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_FAILED) + String.join(";",e.getMessage()))
                    .build());
        }
    }
}
