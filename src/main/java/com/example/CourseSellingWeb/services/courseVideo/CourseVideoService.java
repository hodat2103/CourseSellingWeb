package com.example.CourseSellingWeb.services.courseVideo;

import com.cloudinary.EagerTransformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.CourseSellingWeb.configurations.CloudinaryConfig;
import com.example.CourseSellingWeb.dtos.CourseVideoDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.CourseVideo;
import com.example.CourseSellingWeb.respositories.CourseRepository;
import com.example.CourseSellingWeb.respositories.CourseVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseVideoService implements CourseVideoServiceImpl{
    @Autowired
    private final CourseVideoRepository courseVideoRepository;
    @Autowired
    private  final CourseRepository courseRepository;

    @Autowired
    private final CloudinaryConfig cloudinary;

    @Override
    public CourseVideo createCourseVideo(int courseId, CourseVideoDTO courseVideoDTO, MultipartFile videoFile) throws DataNotFoundException, InvalidParamException, IOException {
        Course existsCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found with id: " + courseId));
        String url = courseVideoDTO.getVideoUrl();
        if(courseVideoDTO.getVideoType().equals("cloudinary")){
            Map<String, Object> uploadResult = uploadFile(videoFile);


//            String videoUrl = (String) uploadResult.get("secure_url");
            url = (String) uploadResult.get("public_id");
        }
        CourseVideo newCourseVideo = CourseVideo.builder()
                .course(existsCourse)
                .title(courseVideoDTO.getTitle())
                .description(courseVideoDTO.getDescription())
                .videoUrl(url)
                .lessonNumber(courseVideoDTO.getLessonNumber())
                .build();
        int size = courseVideoRepository.findCourseVideosByCourseId(courseId).size();
//        if(size <= CourseVideo.MINIMUM_IMAGES_PER_PRODUCT){
//            throw new InvalidParamException("Number of video must be >= " + CourseVideo.MINIMUM_IMAGES_PER_PRODUCT);
//        }
        return courseVideoRepository.save(newCourseVideo);
    }

    @Override
    public CourseVideo update(int courseVideoId, CourseVideoDTO courseVideoDTO, MultipartFile videoFile) throws IOException, DataNotFoundException {
        CourseVideo existsCourseVideo = courseVideoRepository.findById(courseVideoId)
                .orElseThrow(() -> new DataNotFoundException("Not found course video with id: " + courseVideoId));

        Course existsCourse = courseRepository.findById(courseVideoDTO.getCourseId())
                .orElseThrow(() -> new DataNotFoundException("Not found course with id: " + courseVideoDTO.getCourseId()));
        String url = courseVideoDTO.getVideoUrl();
        if(courseVideoDTO.getVideoType().equals("cloudinary")){
            Map<String, Object> uploadResult = uploadFile(videoFile);
            url = (String) uploadResult.get("public_id");
        }
        existsCourseVideo.setCourse(existsCourse);
        existsCourseVideo.setTitle(courseVideoDTO.getTitle());
        existsCourseVideo.setVideoUrl(courseVideoDTO.getVideoUrl());
        existsCourseVideo.setVideoType(courseVideoDTO.getVideoType());
        existsCourseVideo.setDescription(courseVideoDTO.getDescription());
        existsCourseVideo.setLessonNumber(courseVideoDTO.getLessonNumber());
        return courseVideoRepository.save(existsCourseVideo);
    }

    @Override
    public void deleteCourseVideo(int courseVideoId) {
        courseVideoRepository.deleteById(courseVideoId);
    }

    @Override
    public List<CourseVideo> findCourseVideosByCourseId(int courseId) {
        return courseVideoRepository.findCourseVideosByCourseId(courseId);
    }


    public CourseVideo getCourseVideoById(int courseVideoId) throws DataNotFoundException {
        return courseVideoRepository.findById(courseVideoId)
                .orElseThrow(() -> new DataNotFoundException("Not found course video with id: " + courseVideoId));
    }

    @Override
    public int countVideosByCourseId(int courseId) {
        return courseVideoRepository.countByCourseId(courseId);

    }


    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws IOException {
        //        return cloudinary.uploader()
//                .upload(multipartFile.getBytes(),
//                        Map.of("public_id", UUID.randomUUID().toString()))
//                .get("url")
//                .toString()
//                ;
        return cloudinary.cloudinary().uploader().upload(multipartFile.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "public_id", UUID.randomUUID().toString(),
                        "eager", Arrays.asList(
                                new EagerTransformation().width(300).height(300).crop("pad").audioCodec("none"),
                                new EagerTransformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")),
                        "eager_async", true,  // Xử lý eager async
                        "eager_notification_url", "https://mysite.example.com/notify_endpoint"
                )
        );
    }
}
