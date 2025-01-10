package com.example.CourseSellingWeb.services.user_course;

import com.example.CourseSellingWeb.dtos.UserCourseDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.UserCourse;
import com.example.CourseSellingWeb.models.UserCourseId;
import com.example.CourseSellingWeb.respositories.UserCourseRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserCourseService implements UserCourseServiceImpl{

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Override
    public UserCourse addUserCourse(UserCourseDTO userCourseDTO) throws InvalidParamException {
        if(userCourseRepository.existsByUserId(userCourseDTO.getCourseId())){
            throw new InvalidParamException("You bought this course");
        }
        UserCourseId userCourseId = new UserCourseId(userCourseDTO.getUserId(), userCourseDTO.getCourseId());
        UserCourse userCourse = new UserCourse();
        userCourse.setId(userCourseId);
        return userCourseRepository.save(userCourse);
    }

    @Override
    public List<UserCourse> getCoursesByUserId(int userId) {
        return userCourseRepository.findByIdUserId(userId);
    }

    @Override
    public List<UserCourse> getUsersByCourseId(int courseId) {
        return userCourseRepository.findByIdCourseId(courseId);
    }
    @Override
    public Optional<UserCourse> getUserCourse(int userId, int courseId) throws DataNotFoundException {
        UserCourseId userCourseId = new UserCourseId(userId, courseId);
        Optional<UserCourse> optionalUserCourse = userCourseRepository.findById(userCourseId);
        if(optionalUserCourse.isEmpty()){
            throw new DataNotFoundException(MessageKeys.NOT_FOUND);
        }
        return optionalUserCourse;
    }
}
