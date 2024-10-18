package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.UserDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.User;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.user.UserListResponse;
import com.example.CourseSellingWeb.responses.user.UserResponse;
import com.example.CourseSellingWeb.services.user.UserServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody UserDTO userDTO,
                                                 BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.
                            getLocalizationMessage(MessageKeys.CREATE_FAILED) + String
                            .join(";" + errorMessages))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        User userResponse = userService.create(userDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(userResponse)
                .status(HttpStatus.OK)
                .build());
    }



    @GetMapping("")
    public ResponseEntity<UserListResponse> searchUsers(@RequestParam(defaultValue = "0") String keyword,
                                                  @RequestParam(defaultValue = "0", name = "page") int page,
                                                  @RequestParam(defaultValue = "10", name = "limit") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        Page<UserResponse> userResponse = userService.searchUsers(keyword, pageRequest);
        //get sum number of page
        int totalPages = userResponse.getTotalPages();
        List<UserResponse> users = userResponse.getContent();
        return ResponseEntity.ok(UserListResponse.builder()
                .users(users)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable int id){
        try{
            Optional<User> userResponse = userService.getById(id);
            if(userResponse.isPresent()){
                throw new DataNotFoundException(MessageKeys.NOT_FOUND + id);
            }
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(UserResponse.fromUser(userResponse.get()))
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
}