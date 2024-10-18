package com.example.CourseSellingWeb.services.slider;

import com.example.CourseSellingWeb.dtos.SliderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Slider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface SliderServiceImpl {

    Slider create(SliderDTO sliderDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException;

    Slider update(int id, SliderDTO sliderDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException;

    List<Slider> getAllSliders();

    Slider getById(int id) throws DataNotFoundException;

    void delete(int id);
}
