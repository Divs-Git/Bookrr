package com.divyansh.bookrr.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ICloudinaryImageService {
    public Map upload(MultipartFile file);
}
