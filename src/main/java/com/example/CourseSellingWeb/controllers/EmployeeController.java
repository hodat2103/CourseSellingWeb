package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.EmployeeDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.responses.EmployeeResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.employee.EmployeeServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class EmployeeController {
    private final EmployeeServiceImpl employeeService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody EmployeeDTO employeeDTO,
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
        Employee employeeResponse = employeeService.create(employeeDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                        .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                        .data(EmployeeResponse.fromEmployee(employeeResponse))
                        .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable int id) throws DataNotFoundException {
        try {
            Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(employeeOptional
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND))))
                            .status(HttpStatus.OK)
                    .build());


        }catch (Exception e){
            return  ResponseEntity.ok(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject>  getAllEmployees(){
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(employees)
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return  ResponseEntity.ok(ResponseObject.builder()
                    .message(String.join(";", e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());        }
    }
    @GetMapping("/account/{id}")
    public ResponseEntity<ResponseObject> getByAccountId(@PathVariable int id){
        try{
            Optional<Employee> employeeResponse = employeeService.getByAccountId(id);
            if(!employeeResponse.isPresent()){
                throw new DataNotFoundException(MessageKeys.NOT_FOUND + id);
            }
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(EmployeeResponse.fromEmployee(employeeResponse.get()))
                    .status(HttpStatus.OK)
                    .build());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.
                            getLocalizationMessage(String.join(";")+ e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @GetMapping("/by_role_id/{role_id}")
    public ResponseEntity<?> getEmployeesByRoleId(@PathVariable("role_id")  int id){
        try {
            List<Employee> employees = employeeService.getEmployeeByRoleId(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(employees)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return  ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.LIST_EMPTY + String.join(";", e.getMessage())))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,@RequestBody EmployeeDTO employeeDTO){
        try{
            Employee updateEmployee = employeeService.update(id,employeeDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(EmployeeResponse.fromEmployee(updateEmployee))
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
    public ResponseEntity<?> delete(@PathVariable int id){
        try {
            employeeService.delete(id);
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
