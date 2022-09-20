package com.microservice.postsservice.dto;

import com.microservice.postsservice.entity.CommentEntity;
import com.microservice.postsservice.entity.ImageEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String userId;
    private String postType;
    private Long rentalPrice;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String createdDate;
    private String writer;
    private String status;
    private List<MultipartFile> multipartFiles;
    private List<CommentEntity> comments;
    private List<ImageEntity> images;


    @Builder
    public PostDto(
            Long id,
            String userId,
            String postType,
            Long rentalPrice,
            String title,
            String content,
            String startDate,
            String endDate,
            String createdDate,
            String writer,
            String status,
            List<MultipartFile> multipartFiles,
            List<CommentEntity> comments,
            List<ImageEntity> images
    ) {
        this.id = id;
        this.userId = userId;
        this.postType = postType;
        this.rentalPrice = rentalPrice;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.writer = writer;
        this.status = status;
        this.multipartFiles = multipartFiles;
        this.images = images;
        this.comments = comments;
    }
}
