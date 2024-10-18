package com.example.CourseSellingWeb.configurations;

import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ImageConfig {

    private final CloudinaryConfig cloudinaryConfig;
    public Map<String, Object> uploadImageFile(MultipartFile file, String uniqueFileName) throws IOException {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        byte[] bytes = file.getBytes();

        Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(bytes,
                ObjectUtils.asMap(
                        "public_id",uniqueFileName,
                                "resource_type","image"
                ));

        return uploadResult;
    }

    public Map<String, Object> getImageDetail(String publicId) throws Exception {

        return cloudinaryConfig.cloudinary().api().resource(publicId, ObjectUtils.asMap(
                "resource_type", "image"
        ));
    }
    public boolean isImageFile(MultipartFile file) throws IOException {
            String contentType = file.getContentType();
            return contentType != null ;
//            && contentType.startsWith("image/");
        }

}
