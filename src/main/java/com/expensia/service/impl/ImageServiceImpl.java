package com.expensia.service.impl;

import com.expensia.entity.Image;
import com.expensia.entity.User;
import com.expensia.exception.ResourceNotFoundException;
import com.expensia.repository.ImageRepository;
import com.expensia.repository.UserRepository;
import com.expensia.service.ImageService;
import com.expensia.utils.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;
    private UserRepository userRepository;

    @Override
    public String uploadImage(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        Image imageData = null;
        try {
            if(imageExistsByUserId(user.getId())){
                Optional<Image> byUser = imageRepository.findByUser(user);
                imageRepository.delete(byUser.get());
                imageData = imageRepository.save(Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .user(user)
                        .build());
            }
            else{
                imageData = imageRepository.save(Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .user(user)
                        .build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    @Override
    public byte[] downloadImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        Optional<Image> dbImageData = imageRepository.findByUser(user);
        byte[] image = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return image;
    }

    @Override
    public boolean imageExistsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return imageRepository.existsByUser(user);
        }
        return false;
    }
}
