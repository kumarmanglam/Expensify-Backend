package com.expensia.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file);
    byte[] downloadImage();

    boolean imageExistsByUserId(Long userId);
}
