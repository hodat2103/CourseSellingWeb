package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.OrderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Order;
import com.example.CourseSellingWeb.responses.order.OrderListResponse;
import com.example.CourseSellingWeb.responses.order.OrderResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.order.OrderServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody OrderDTO orderDTO,
                                                 BindingResult result) throws DataNotFoundException, InvalidParamException {
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());}
        Order orderResponse = orderService.create(orderDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(OrderResponse.fromOrder(orderResponse))
                .status(HttpStatus.OK)
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable int id){
        try{
            Optional<Order> orderOptional = orderService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(orderOptional.map(ResponseEntity :: ok)
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

    @GetMapping("/revenue/monthly_yearly")
    public ResponseEntity<ResponseObject> getRevenueByMonthAndYear(@RequestParam(defaultValue = "2024") int year,
                                                            @RequestParam(defaultValue = "10") int month){
        try{
            List<Object[]> responseRevenue = orderService.calculateRevenueForQuarters(year);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(responseRevenue)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @GetMapping("/revenue/quarterly")
    public ResponseEntity<ResponseObject> getQuarterlyRevenue(@RequestParam(defaultValue = "2024") int year){
        try{
            List<Object[]> responseRevenue = orderService.calculateRevenueForQuarters(year);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(responseRevenue)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) Integer fieldId,
            @RequestParam(required = false) Integer languageId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        try {
            if (startDate != null && !startDate.isEmpty()) {
                LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
                startDateTime = parsedStartDate.atStartOfDay(); // 00:00:00
            }

            if (endDate != null && !endDate.isEmpty()) {
                LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
                endDateTime = parsedEndDate.atTime(LocalTime.MAX); // 23:59:59.999
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.LIST_EMPTY) + String.join(";", e.getMessage()))
                            .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        Pageable pageable = PageRequest.of(page,
                                            size,
                                            Sort.by(Sort
                                                    .Order
                                                    .desc("orderDate")));

        Page<Order> orders = orderService.searchOrders(keyword, employeeId, fieldId, languageId, startDateTime, endDateTime, pageable);
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(orders)
                        .status(HttpStatus.OK)
                    .build());
    }

@PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                                 @RequestBody OrderDTO orderDTO){
        try{
            Order orderResponse = orderService.update(id, orderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(orderResponse)
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
           orderService.delete(id);
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
