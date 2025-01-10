package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.CourseDTO;
import com.example.CourseSellingWeb.dtos.CourseDiscountDTO;
import com.example.CourseSellingWeb.dtos.CourseVideoDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.CourseVideo;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.course.CourseListResponse;
import com.example.CourseSellingWeb.responses.course.CourseResponse;
import com.example.CourseSellingWeb.responses.course.CourseVideoResponse;
import com.example.CourseSellingWeb.responses.messages.BaseMessageResponse;
import com.example.CourseSellingWeb.services.course.CourseServiceImpl;
import com.example.CourseSellingWeb.services.courseVideo.CourseVideoServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/courses")
@CrossOrigin(origins = "http://localhost:3000")

public class CourseController {
    private final CourseServiceImpl courseService;
    private final CourseVideoServiceImpl courseVideoService;
    private final LocalizationUtils localizationUtils;
    @GetMapping("/playYoutube")
    public String playYoutube(@RequestParam("id") int  courseId, Model model) {
        try {
            Course course = courseService.getCourseById(courseId);
            String url = course.getDemoVideoUrl();
            String videoId = extractYouTubeVideoId(url);
            String embedUrl = "https://www.youtube.com/embed/" + videoId;
            model.addAttribute("embedUrl", embedUrl);
            return "playVideo";
        } catch (IllegalArgumentException | DataNotFoundException e) {
            return "errorPage: "+ e.getMessage();
        }
    }
    @GetMapping("/playCloudinary")
    public String playCloudinary(@RequestParam("url") String publicId, Model model) {
        try {
            //Get information from Cloudinary based on publicId
            Map<String, Object> videoDetails = courseService.getVideoDetails(publicId);
            String videoUrl = (String) videoDetails.get("secure_url");  //Get secure URL of Video

            model.addAttribute("embedUrl", videoUrl);
            return "playVideo";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading video: " + e.getMessage());
            return "errorPage";
        }
    }

    // divide videoId from URL YouTube
    private String extractYouTubeVideoId(String url) {
        if (url.contains("v=")) {
            String videoId = url.substring(url.indexOf("v=") + 2);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            return videoId;
        }
        throw new IllegalArgumentException("Invalid YouTube URL: " + url);
    }

    // pagination
    @GetMapping("")
    public ResponseEntity<CourseListResponse> searchCourses(
            @RequestParam(defaultValue = "",name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "field_id") int fieldId,
            @RequestParam(defaultValue = "0", name = "language_id") int languageId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "15", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        Page<CourseResponse> coursePage = courseService.getAllCourses(keyword,fieldId,languageId,pageRequest);
        //get sum number of page
        int totalPages = coursePage.getTotalPages();
        List<CourseResponse> courses = coursePage.getContent();
        return ResponseEntity.ok(CourseListResponse.builder()
                        .courses(courses)
                        .totalPages(totalPages)
                .build());
    }
    // infinite scrolling with reactjs ok
    @GetMapping("/infiniteScrolling")
    public ResponseEntity<List<CourseResponse>> getCoursesByInfiniteScrolling(
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "field_id") int fieldId,
            @RequestParam(defaultValue = "0", name = "language_id") int languageId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "15", name = "limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending());
        Page<CourseResponse> coursePage = courseService.getAllCourses(keyword, fieldId, languageId, pageRequest);

        List<CourseResponse> courses = coursePage.getContent();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/discounts")
    public ResponseEntity<List<CourseDiscountDTO>> getCoursesWithDiscounts() {
        try{
            List<CourseDiscountDTO> coursesWithDiscounts = courseService.getCoursesWithDiscounts();
            if (coursesWithDiscounts.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(coursesWithDiscounts);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }
    @GetMapping("/free")
    public ResponseEntity<ResponseObject> getFreeCourses() {
        try{
            List<Course> freeCourses = courseService.getByForFreeTrue();
            if (freeCourses.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<CourseResponse> freeCourseResponses = freeCourses.stream()
                    .map(CourseResponse::fromCourse)
                    .toList();
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(freeCourseResponses)
                            .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCourseById(@PathVariable("id") int courseId){
        try {
            Course existsCourse = courseService.getCourseById(courseId);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .data(CourseResponse.fromCourse(existsCourse))
                            .status(HttpStatus.OK)
                            .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST).build());
        }
    }
    @GetMapping("/by-ids")
    public ResponseEntity<?> getCoursesByIds(@RequestParam("ids") String ids){
        try {
            List<Integer> courseIds = Arrays.stream(ids.split(","))
                    .map(Integer :: parseInt)
                    .collect(Collectors.toList());
            List<Course> courses = courseService.findCoursesByIds(courseIds);
            return ResponseEntity.ok(courses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/count/{courseId}")
    public ResponseEntity<Integer> countVideos(@PathVariable int courseId) throws DataNotFoundException {
        int count = courseVideoService.countVideosByCourseId(courseId);
        if(count <= 0){
            throw new DataNotFoundException(MessageKeys.NOT_FOUND);
        }
        return ResponseEntity.ok(count);
    }
    @PostMapping("")
    public ResponseEntity<ResponseObject> create(
            @Valid @RequestBody CourseDTO courseDTO,
            @ModelAttribute(value = "video_file", binding = false) MultipartFile videoFile,

            BindingResult result
            ) throws DataNotFoundException, IOException, InvalidParamException {

            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                        return ResponseEntity.badRequest().body(ResponseObject.builder()
                                        .message(String.join(";",errorMessages))
                                        .status(HttpStatus.BAD_REQUEST)
                                .build());

            }
            Course courseResponse = courseService.create(courseDTO, videoFile);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                    .data(CourseResponse.fromCourse(courseResponse))
                    .status(HttpStatus.OK)
                    .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable int id,
                                    @RequestBody CourseDTO courseDTO,
                                    @ModelAttribute(value = "video_file", binding = false) MultipartFile videoFile,
                                                 @ModelAttribute(value = "image_file", binding = false) MultipartFile imageFile){

        try{
            Course updateCourse = courseService.update(id, courseDTO,videoFile);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(CourseResponse.fromCourse(updateCourse))
                    .status(HttpStatus.OK)
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_FAILED))
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseMessageResponse> delete(@PathVariable int id){
        courseService.delete(id);
        return ResponseEntity.ok(BaseMessageResponse.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY))
                .build());
    }


    //VIDEO
    @GetMapping("/videos/all_video/{id}")
    public ResponseEntity<ResponseObject> getCourseVideosByCourseId(@PathVariable("id") int courseId){
        List<CourseVideo> courseVideos = courseVideoService.findCourseVideosByCourseId(courseId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(courseVideos)
                .status(HttpStatus.OK)
                .build());

    }
    @GetMapping("/videos/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable("id") int courseVideoId){

            try{
                CourseVideo existsCourseVideo = courseVideoService.getCourseVideoById(courseVideoId);
                return ResponseEntity.ok(ResponseObject.builder()
                        .data(CourseVideoResponse.fromCourse(existsCourseVideo))
                        .status(HttpStatus.OK)
                        .build());
            }catch (Exception e){
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                                .message(String.join(";",e.getMessage()))
                                .status(HttpStatus.BAD_REQUEST)
                        .build());
            }

    }

    @PostMapping("/videos/{id}")
    public ResponseEntity<ResponseObject> createVideo(
            @PathVariable("id") int courseId,
            @Valid @RequestBody CourseVideoDTO courseVideoDTO,
            @ModelAttribute(value = "file", binding = false) MultipartFile fileVideo,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return  ResponseEntity.badRequest().body(ResponseObject.builder()
                        .message(localizationUtils
                                .getLocalizationMessage(MessageKeys.CREATE_FAILED + String.join(";", errorMessages)))
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
            }
//            if(fileVideo.getSize() < 1000 * 1024 * 1024 ){
//                throw new InvalidParamException("File must smaller than 1GB");
//            }
            CourseVideo newCourseVideo = courseVideoService.createCourseVideo(courseId, courseVideoDTO,fileVideo);
            return  ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.CREATE_SUCCESSFULLY))
                    .data(CourseVideoResponse.fromCourse(newCourseVideo))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.CREATE_FAILED + String.join(";",e.getMessage())))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @PutMapping("/videos/{id}")
    public ResponseEntity<ResponseObject> updateVideo(@PathVariable int id,
                                    @RequestBody CourseVideoDTO courseVideoDTO,
                                    @ModelAttribute(value = "file", binding = false) MultipartFile videoFile){

        try{
            CourseVideo updateCourseVideo = courseVideoService.update(id, courseVideoDTO,videoFile);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                    .data(CourseVideoResponse.fromCourse(updateCourseVideo))
                    .status(HttpStatus.OK)
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizationMessage(MessageKeys.UPDATE_FAILED + String.join(";",e.getMessage())))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }


    @DeleteMapping("/videos/{id}")
    public ResponseEntity<BaseMessageResponse> deleteVideo (@PathVariable int courseVideoId){
        try{
            courseVideoService.deleteCourseVideo(courseVideoId);
            return ResponseEntity.ok(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_SUCCESSFULLY + courseVideoId))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(BaseMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.DELETE_FAILED + e.getMessage()))
                    .build());
        }
    }


}
