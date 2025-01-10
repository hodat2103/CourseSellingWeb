package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.BlogDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.exceptions.PermissionDenyException;
import com.example.CourseSellingWeb.models.Blog;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.blog.BlogResponse;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.blog.BlogService;
import com.example.CourseSellingWeb.services.blog.BlogServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/blogs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {
    private final BlogServiceImpl blogService;
    private final LocalizationUtils localizationUtils;

    @PostMapping(value = "",consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseObject> create(@Valid @RequestPart("blog") BlogDTO blogDTO,
                                                 @RequestPart("file")  MultipartFile multipartFile,
                                                 BindingResult result) throws InvalidParamException, DataNotFoundException, IOException {

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
        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("File upload is required")
                    .build());
        }
        Blog blogResponse = blogService.create(blogDTO,multipartFile);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                .data(BlogResponse.fromBlog(blogResponse))
                .status(HttpStatus.OK)
                .build());
    }

//    @GetMapping("")
//    public ResponseEntity<ResponseObject> getAllBlogs(@RequestParam(defaultValue = "0", name = "employee_id") int employeeId,
//                                         @RequestParam(defaultValue = "", name = "date") String dateStr,
//                                         @RequestParam(defaultValue = "", name = "keyword") String keyword,
//                                         @RequestParam(defaultValue = "0", name = "page") int page,
//                                         @RequestParam(defaultValue = "9", name = "limit") int limit){
//
//        LocalDateTime startOfDay = null;
//        LocalDateTime endOfDay = null;
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        if (!dateStr.isEmpty()) {
//            try {
//                LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
//                // Set start of the day (00:00:00)
//                startOfDay = parsedDate.atStartOfDay();
//                // Set end of the day (23:59:59.999)
//                endOfDay = parsedDate.atTime(LocalTime.MAX);
//            } catch (DateTimeParseException e) {
//                return ResponseEntity.badRequest().body(ResponseObject.builder()
//                        .message("Invalid date format. Please use dd/MM/yyyy.")
//                        .build());
//            }
//        }
//
//        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
//
//        Page<BlogResponse> blogPage = blogService.getAllBlogs(employeeId, startOfDay, endOfDay, keyword, pageRequest);
//        List<BlogResponse> blogResponses = blogPage.getContent();
//
//        if (blogResponses.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(ResponseObject.builder()
//                        .data(blogResponses)
//                        .status(HttpStatus.OK)
//                .build());
//    }
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllBlogs(){
       List<Blog> blogs =  blogService.findAll();
       return ResponseEntity.ok(ResponseObject.builder()
                       .message("ok")
                       .data(blogs)
                       .status(HttpStatus.OK)
               .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBlogById(@PathVariable int id){
        try{
           Optional<Blog> blogOptional =  blogService.getById(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(blogOptional
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND)))
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(String.join(";", e.getMessage()))
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                    @RequestBody BlogDTO blogDTO,
                                    @ModelAttribute(value = "file", binding = false) MultipartFile multipartFile){
        try{
            Blog updateBlog = blogService.update(id, blogDTO, multipartFile);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(BlogResponse.fromBlog(updateBlog))
                    .status(HttpStatus.OK)
                    .build());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED) + String.join(";", e.getMessage()))
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable  int id) throws PermissionDenyException {
        try{
            blogService.delete(id);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                    .build());
        }catch(Exception e){
            throw new PermissionDenyException(localizationUtils
                    .getLocalizationMessage(MessageKeys.DELETE_FAILED) +" " +e.getMessage());
        }
    }
}
