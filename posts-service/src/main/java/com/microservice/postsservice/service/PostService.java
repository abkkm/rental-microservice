package com.microservice.postsservice.service;

import com.microservice.postsservice.*;
import com.microservice.postsservice.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PostService {
    PostDto write(PostDto postDto) throws Exception;

    Optional<PostDto> readPostById(Long id);

    Iterable<PostDto> getAllPosts();

    Iterable<PostDto> getAllPostsByStatus(String status);

    Iterable<PostDto> getPostsByUserId(String userId);

    PostDto deletePost(Long postId);

    Iterable<PostDto> getPostsByKeyword(String keyword);

    Iterable<PostDto> getPostsByCategory(String category);

    void rental(Long postId);

    void rollbackPost(Long postId);
}