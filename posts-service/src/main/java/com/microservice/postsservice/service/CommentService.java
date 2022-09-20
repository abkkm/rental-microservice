package com.microservice.postsservice.service;

import com.microservice.postsservice.dto.CommentDto;
import com.microservice.postsservice.entity.CommentEntity;
import com.microservice.postsservice.helper.DateFormat;
import com.microservice.postsservice.repository.CommentRepository;
import com.microservice.postsservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Long addComment(CommentDto commentDto) {
        CommentEntity commentEntity = CommentEntity.builder()
                .writer(commentDto.getWriter())
                .comment(commentDto.getComment())
                .post(postRepository.findById(commentDto.getPostId()).get())
                .createdDate(DateFormat.now())
                .build();

        return commentRepository.save(commentEntity).getId();
    }

    @Transactional
    public Boolean deleteComment(Long id) {
        try {
            Optional<CommentEntity> commentEntity = commentRepository.findById(id);
            if (commentEntity.isPresent()) {
                commentRepository.delete(commentEntity.get());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
