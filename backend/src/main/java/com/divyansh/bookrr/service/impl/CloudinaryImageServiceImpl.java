package com.divyansh.bookrr.service.impl;

import com.cloudinary.Cloudinary;
import com.divyansh.bookrr.service.interfaces.ICloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageServiceImpl implements ICloudinaryImageService {

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public Map upload(MultipartFile file) {
        try {
            Map uploaded = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return uploaded;
        } catch (IOException e) {
            throw new RuntimeException("Image upload error");
        }
    }
}
