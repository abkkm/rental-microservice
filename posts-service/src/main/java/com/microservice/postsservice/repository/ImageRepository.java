package com.microservice.postsservice.repository;
import com.microservice.postsservice.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Iterable<ImageEntity> findByPostId(String postId);
}