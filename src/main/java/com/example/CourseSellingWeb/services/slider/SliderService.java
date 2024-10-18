package com.example.CourseSellingWeb.services.slider;

import com.example.CourseSellingWeb.configurations.ImageConfig;
import com.example.CourseSellingWeb.dtos.SliderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Slider;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.respositories.SliderRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SliderService implements SliderServiceImpl{
    private final SliderRepository sliderRepository;
    private final EmployeeRepository employeeRepository;
    private final ImageConfig imageConfig;
    private final String UNIQUE_FILE_NAME = UUID.randomUUID().toString();

    @Override
    public Slider create(SliderDTO sliderDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException {
        Employee existsEmployee = employeeRepository.findById(sliderDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + sliderDTO.getEmployeeId()));
        String uniqueFileName = UNIQUE_FILE_NAME + "_" + sliderDTO.getImageUrl();
        Slider newSlider = Slider.builder()
                .imageUrl(uniqueFileName)
                .description(sliderDTO.getDescription())
                .employee(existsEmployee)
                .build();
        Map result = imageConfig.uploadImageFile(multipartFile,uniqueFileName);
        if(result.isEmpty()){
            throw new InvalidParamException("Cannot upload image file");
        }
        return sliderRepository.save(newSlider);
    }

    @Override
    public Slider update(int id, SliderDTO sliderDTO, MultipartFile multipartFile) throws DataNotFoundException, IOException, InvalidParamException {
        Slider existsSlider = sliderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        Employee existsEmployee = employeeRepository.findById(sliderDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + sliderDTO.getEmployeeId()));
        String uniqueFileName = UNIQUE_FILE_NAME + "_" + sliderDTO.getImageUrl();

        boolean imageType = imageConfig.isImageFile(multipartFile);
        if(!imageType){
            throw new InvalidParamException("Failed image type");
        }

        existsSlider.setImageUrl(uniqueFileName);
        existsSlider.setDescription(sliderDTO.getDescription());

        sliderRepository.save(existsSlider);

        Map result = imageConfig.uploadImageFile(multipartFile,uniqueFileName);
        if(result.isEmpty()){
            throw new InvalidParamException("Cannot upload image file");
        }
        return existsSlider;
    }

    @Override
    public List<Slider> getAllSliders() {
        return sliderRepository.findAll();
    }

    @Override
    public Slider getById(int id) throws DataNotFoundException {
        Slider existsSlider = sliderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));

        return existsSlider;
    }

    @Override
    public void delete(int id) {
        Optional<Slider> optionalSlider = sliderRepository.findById(id);
        optionalSlider.ifPresent(sliderRepository :: delete);
    }
}
