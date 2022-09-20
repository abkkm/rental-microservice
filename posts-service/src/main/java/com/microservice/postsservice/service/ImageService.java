package com.microservice.postsservice.service;
import com.microservice.postsservice.entity.ImageEntity;
import com.microservice.postsservice.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public void saveImage(ImageEntity imageEntity) {
        imageRepository.save(imageEntity);
    }

    @Transactional
    public Iterable<ImageEntity> getImages(String postId) {
        return imageRepository.findByPostId(postId);
    }

    @Transactional
    public Iterable<ImageEntity> getAllImages() {
        return imageRepository.findAll();
    }
}
