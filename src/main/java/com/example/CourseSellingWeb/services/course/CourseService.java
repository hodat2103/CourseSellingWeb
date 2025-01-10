package com.example.CourseSellingWeb.services.course;

import com.cloudinary.utils.ObjectUtils;
import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.configurations.CloudinaryConfig;
import com.example.CourseSellingWeb.configurations.ImageConfig;
import com.example.CourseSellingWeb.dtos.CourseDTO;
import com.example.CourseSellingWeb.dtos.CourseDiscountDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.*;
import com.example.CourseSellingWeb.responses.course.CourseResponse;
import com.example.CourseSellingWeb.respositories.*;
import com.example.CourseSellingWeb.services.courseVideo.CourseVideoService;
import com.example.CourseSellingWeb.utils.ConstantKeys;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseService implements CourseServiceImpl{
    private final CourseRepository courseRepository;
    private final FieldRepository fieldRepository;
    private final LanguageRepository languageRepository;
    private final EmployeeRepository employeeRepository;
    private final MentorRepository mentorRepository;
    private final CloudinaryConfig cloudinaryConfig;
    private final ImageConfig imageConfig;
    private final CourseVideoService courseVideoService;
    private final LocalizationUtils localizationUtils;

    @Override
    public Course create(CourseDTO courseDTO, MultipartFile videoFile) throws DataNotFoundException, IOException, InvalidParamException {
        Field existsField = fieldRepository.findById(courseDTO.getFieldId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find field with id: " + courseDTO.getFieldId()));
        Language existsLanguage = languageRepository.findById(courseDTO.getLanguageId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find language with id: " + courseDTO.getLanguageId()));
        Employee existsEmployee = employeeRepository.findById(courseDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException("Not found employee with: " + courseDTO.getEmployeeId()));
        Mentor existsMentor = mentorRepository.findById(courseDTO.getMentorId())
                .orElseThrow(() -> new DataNotFoundException("Not found mentor with id: " + courseDTO.getMentorId()));
//        String uniqueFileName = ConstantKeys.UNIQUE_FILE_NAME + courseDTO.getImageUrl();

        boolean existsUrl = courseRepository.existsByDemoVideoUrl(courseDTO.getDemoVideoUrl());
//        Map result = imageConfig.uploadImageFile(imageFile,uniqueFileName);
//        if(result.isEmpty()){
//            throw new InvalidParamException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_IMAGE));
//        }
        String url = courseDTO.getDemoVideoUrl();
        if(courseDTO.getVideoType().equals("cloudinary")){
            Map<String, Object> uploadResult = courseVideoService.uploadFile(videoFile);
//            String videoUrl = (String) uploadResult.get("secure_url");
            url = (String) uploadResult.get("public_id");
        }
        if(existsUrl){
            new InvalidParamException("Demo Video Url be existed");
        }
        Course newCourse = Course.builder()
                .title(courseDTO.getTitle())
                .mentor(existsMentor)
                .description(courseDTO.getDescription())
                .price(courseDTO.getPrice())
//                .imageUrl(uniqueFileName)
                .demoVideoUrl(url)
                .videoType(courseDTO.getVideoType())
                .field(existsField)
                .language(existsLanguage)
                .employee(existsEmployee)
                .build();

        return courseRepository.save(newCourse);
    }

    @Override
    public Course getCourseById(Integer id) throws DataNotFoundException {
        return courseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find course with id: "+ id));
    }

    @Override
    public Course update(Integer id, CourseDTO courseDTO, MultipartFile videoFile) throws DataNotFoundException, IOException, InvalidParamException {
        Course existsCourse = getCourseById(id);
        if(existsCourse != null){
            Field existsField = fieldRepository.findById(courseDTO.getFieldId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Cannot find field with id: " + courseDTO.getFieldId()));
            Language existsLanguage = languageRepository.findById(courseDTO.getLanguageId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Cannot find language with id: " + courseDTO.getLanguageId()));
            Mentor existsMentor = mentorRepository.findById(courseDTO.getMentorId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find mentor with id: " + courseDTO.getMentorId()));
//            String uniqueFileName = ConstantKeys.UNIQUE_FILE_NAME + courseDTO.getImageUrl();
//            Map result = imageConfig.uploadImageFile(imageFile,uniqueFileName);
//            if(result.isEmpty()){
//                throw new InvalidParamException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_IMAGE));
//            }
            String url = courseDTO.getDemoVideoUrl();

            if(courseDTO.getVideoType().equals("cloudinary")){
                Map<String, Object> uploadResult = courseVideoService.uploadFile(videoFile);
                url = (String) uploadResult.get("public_id");
            }

            existsCourse.setTitle(courseDTO.getTitle());
            existsCourse.setMentor(existsMentor);
            existsCourse.setDescription(courseDTO.getDescription());
//            existsCourse.setImageUrl(uniqueFileName);
            existsCourse.setDemoVideoUrl(url);
            existsCourse.setVideoType(courseDTO.getVideoType());
            existsCourse.setPrice(courseDTO.getPrice());
            existsCourse.setField(existsField);
            existsCourse.setLanguage(existsLanguage);


            return courseRepository.save(existsCourse);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        optionalCourse.ifPresent(courseRepository :: delete);
    }

    @Override
    public Page<CourseResponse> getAllCourses(String keyword, Integer fieldId, Integer languageId, PageRequest pageRequest) {
        Page<Course> coursePage;
        coursePage = courseRepository.searchCourses(fieldId,languageId,keyword,pageRequest);
        return coursePage.map(CourseResponse :: fromCourse);
    }

    @Override
    public List<Course> findCoursesByIds(List<Integer> courseIds) {
        return courseRepository.findCoursesByIds(courseIds);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getByForFreeTrue() {
        return courseRepository.findByForFreeTrue();
    }

    @Override
    public List<CourseDiscountDTO> getCoursesWithDiscounts() {
        return courseRepository.findCoursesWithValidCoupons();
    }

    @Override
    public InputStream getResource(String url) throws IOException {
        URL videoUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) videoUrl.openConnection();
        connection.setRequestMethod("GET");
        return connection.getInputStream(); // return thread InputStream from URL
    }
    public Map<String, Object> getVideoDetails(String publicId) throws Exception {

        return cloudinaryConfig.cloudinary().api().resource(publicId, ObjectUtils.asMap(
                "resource_type", "video"
        ));
    }
}
